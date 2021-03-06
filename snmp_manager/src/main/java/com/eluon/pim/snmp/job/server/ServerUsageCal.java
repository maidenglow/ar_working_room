package com.eluon.pim.snmp.job.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.eluon.pim.snmp.value.server.PimServerSecVO;
import com.eluon.pim.snmp.value.server.PimServerStatVO;
import com.google.gson.Gson;


@SuppressWarnings("unchecked")
public class ServerUsageCal {

	public PimServerStatVO getStatMinData(List<PimServerSecVO> list){
		
		if(list == null || list.size() == 0){
			return null;
		}
		
		PimServerStatVO result = new PimServerStatVO();
		float totalMemUsage = 0;
		float memPeak = 0;
		float totalHddUsage = 0;
		float hddPeak = 0;
		Map<String, Double> cpuAvg = new HashMap<>();
		Map<String, Double> cpuPeak = new HashMap<>();
		
		for(PimServerSecVO temp : list){
			// Hdd peak / usage total 연산
			totalHddUsage += temp.getHddUsage();
			hddPeak = hddPeak < temp.getHddUsage() ? temp.getHddUsage() : hddPeak;
			
			// Memory peak / usage total 연산
			memPeak = memPeak < temp.getMemUsage() ? temp.getMemUsage() : memPeak;
			totalMemUsage += temp.getMemUsage();
			
			// CPU usage total 연산
			Map<String, Double>tempCpu =  new Gson().fromJson(temp.getCpuUsage(), Map.class);
			
			for(String key : tempCpu.keySet()){
				if(cpuAvg.containsKey(key)){
					Double sum =  cpuAvg.get(key) + tempCpu.get(key);
					cpuAvg.put(key, sum);
					
					// Peak 연산
					if (cpuPeak.get(key) < tempCpu.get(key)){
						cpuPeak.put(key,tempCpu.get(key));
					}
				}else{
					cpuAvg.put(key,tempCpu.get(key));
					cpuPeak.put(key,tempCpu.get(key));
				}
			}
		}
		
		// CPU AVG 연산
		for(String key : cpuAvg.keySet()){
			double avg =   Math.round( (cpuAvg.get(key) / list.size()) * 100d ) / 100d; 
			cpuAvg.put(key, avg);
		}
		
		result.setCpuUsageAvg( new Gson().toJson(cpuAvg) );
		result.setCpuUsagePeak( new Gson().toJson(cpuPeak) );
		result.setHddUsageAvg(totalHddUsage / list.size() );
		result.setHddUsagePeak(hddPeak);
		result.setMemUsageAvg(totalMemUsage / list.size() );
		result.setMemUsagePeak(memPeak);
		
		
		PimServerSecVO nicFirst = list.get(0);
		PimServerSecVO nicLast = list.get( list.size()-1 );
		result.setNicUsage( getNicUsage(nicFirst.getNicUsage(), nicLast.getNicUsage()) );
		
		return result;
	}
	
	public PimServerStatVO getStatData(List<PimServerStatVO> list){
		
		if(list == null || list.size() == 0){
			return null;
		}
		
		PimServerStatVO result = new PimServerStatVO();
		float totalMemUsage = 0;
		float memPeak = 0;
		float totalHddUsage = 0;
		float hddPeak = 0;
		Map<String, Double> cpuAvg = new HashMap<>();
		Map<String, Double> cpuPeak = new HashMap<>();

		for(PimServerStatVO temp : list){
			// Hdd peak / usage total 연산
			totalHddUsage += temp.getHddUsageAvg();
			hddPeak = hddPeak < temp.getHddUsagePeak() ? temp.getHddUsagePeak() : hddPeak;

			// Memory peak / usage total 연산
			memPeak = memPeak< temp.getMemUsagePeak() ? temp.getMemUsagePeak() : memPeak;
			totalMemUsage += temp.getMemUsageAvg();

			// CPU usage total 연산
			Map<String, Double>tempCpuAvg =  new Gson().fromJson(temp.getCpuUsageAvg(), Map.class);

			for(String key : tempCpuAvg.keySet()){
				if(cpuAvg.containsKey(key)){
					Double sum =  cpuAvg.get(key) + tempCpuAvg.get(key);
					cpuAvg.put(key, sum);
				}else{
					cpuAvg.put(key,tempCpuAvg.get(key));
				}
			}

			// CPU Peak 연산
			Map<String, Double>tempCpuPeak =  new Gson().fromJson(temp.getCpuUsagePeak(), Map.class);

			for(String key : tempCpuPeak.keySet()){
				// Peak 연산
				// cpuPeak map에 device id가 없거나 이전 데이터가 현재 데이터 보다 작은 경우 
				if(!cpuPeak.containsKey(key) || cpuPeak.get(key) < tempCpuPeak.get(key)){
					cpuPeak.put(key,tempCpuPeak.get(key));
				}
			}
		}

		// CPU AVG 연산
		for(String key : cpuAvg.keySet()){
			double avg =   Math.round( (cpuAvg.get(key) / list.size()) * 100d ) / 100d; 
			cpuAvg.put(key, avg);
		}

		
		result.setCpuUsageAvg( new Gson().toJson(cpuAvg) );
		result.setCpuUsagePeak( new Gson().toJson(cpuPeak) );
		result.setHddUsageAvg( totalHddUsage / list.size() );
		result.setHddUsagePeak(hddPeak);
		result.setMemUsageAvg( totalMemUsage / list.size() );
		result.setMemUsagePeak(memPeak);
		
		result.setNicUsage( getNicUsage(list) );
		
		return result;
	}
	
	
	private String getNicUsage(List<PimServerStatVO> list){
		Map<String, String> nicUsage = new HashMap<>();
		
		for(PimServerStatVO temp : list){
			Map<String, String> tempMap = new Gson().fromJson(temp.getNicUsage(), Map.class);
			
			for(String key : tempMap.keySet()){
				
				String nicValue = tempMap.get(key);
				long rx =0;
				long tx =0;
				if(StringUtils.isNotEmpty(nicValue) && StringUtils.isNotEmpty(nicValue)){
					rx = parseLong(nicValue.split("/")[0]);
					tx = parseLong(nicValue.split("/")[1]);
				}
				
				if(nicUsage.containsKey(key)){
					String prev = nicUsage.get(key);
					long prevRx = parseLong(prev.split("/")[0]);
					long prevTx = parseLong(prev.split("/")[1]);
					
					rx += prevRx;
					tx += prevTx;
				}
				nicUsage.put(key, rx +"/"+tx);
			}
		}
		
		return new Gson().toJson(nicUsage);
	}


	private String getNicUsage(String first, String last){
		Map<String, String> nicFirst = new Gson().fromJson( first, Map.class);
		Map<String, String> nicLast = new Gson().fromJson( last, Map.class);

		Map<String, String> nicUsage = new HashMap<>();

		for(String key : nicFirst.keySet()){

			String fnic = nicFirst.get(key);
			String lnic = nicLast.get(key);
			long fnicRx = 0;
			long fnicTx = 0;
			long lnicRx = 0;
			long lnicTx = 0;
			if(StringUtils.isNotEmpty(fnic) && StringUtils.isNotEmpty(lnic)){
				fnicRx = parseLong(fnic.split("/")[0]) ;
				fnicTx = parseLong(fnic.split("/")[1]) ;
				lnicRx = parseLong(lnic.split("/")[0]) ;
				lnicTx = parseLong(lnic.split("/")[1]) ;
			}
			String tempUsage = (lnicRx - fnicRx) +"/"+ (lnicTx - fnicTx); 
			nicUsage.put(key, tempUsage);
		}
		
		return new Gson().toJson(nicUsage);
	}
	
	// Parse Method 통합 필요 
	public static long parseLong(String num){
		long result = 0;
		try{
			result = Long.parseLong(num);
		}catch(Exception e){}
		return result;
	}
}
