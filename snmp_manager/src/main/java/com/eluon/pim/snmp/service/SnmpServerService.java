package com.eluon.pim.snmp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.snmp4j.smi.OID;

/**
 * 해당 Target Server의 상태 정보를 조회 하는 클래스
 * @author moonsj
 * 
 */
public class SnmpServerService extends SnmpCommonService{

	//cpu device
	private String oidDeviceList;
	//cpu type
	private String oidTypelist;
	//cpu info
	private String oidCpuInfo;
	//cpu usage
	private String oidCpuUsage;
	//cpu type
	private String oidCpuType;

	//storage 
	private String oidStorageType;
	//storage unit
	private String oidStorageUnit;
	//storage total size
	private String oidStorageSize;
	//storage usage size
	private String oidStorageUsage;
	//storage memory type (hrStorageRam)
	private String oidMemType;
	//storage HDD type (hrStorageFixedDisk)
	private String oidHddType;

	//Nic
	//	private String oidNicCount;
	private String oidNicDesc;
	private String oidNicStatus;
	private String oidNicRx;
	private String oidNicTx;


	public SnmpServerService(String ipAddress) throws Exception{

		super(ipAddress);

		// Property 추출
		Configuration config = new PropertiesConfiguration("property/snmp.properties");
		// OID 설정
		// CPU 관련
		oidDeviceList = config.getString("oid.server.cpu.device");
		oidTypelist = config.getString("oid.server.cpu.type");
		oidCpuInfo = config.getString("oid.server.cpu.info");
		oidCpuUsage = config.getString("oid.server.cpu.usage");
		oidCpuType = config.getString("oid.server.cpu.type.num");

		// Storage 관련
		oidStorageType = config.getString("oid.server.storage.type");
		oidStorageUnit = config.getString("oid.server.storage.unit");
		oidStorageSize = config.getString("oid.server.storage.size");
		oidStorageUsage = config.getString("oid.server.storage.usage");

		//HOST-RESOURCES-TYPES::hrStorageRam
		oidMemType = config.getString("oid.server.storage.type.mem");
		//HOST-RESOURCES-TYPES:: hrStorageFixedDisk
		oidHddType = config.getString("oid.server.storage.type.hdd");

		//NIC 관련
		oidNicDesc = config.getString("oid.server.nic.desc");
		oidNicStatus = config.getString("oid.server.nic.status");
		oidNicRx = config.getString("oid.server.nic.rx");
		oidNicTx = config.getString("oid.server.nic.tx");

		getSnmpInfo();
	}

	protected OID[] getOids() throws Exception{
		OID[] oids = new OID[]{
				new OID(oidDeviceList),
				new OID(oidTypelist),
				new OID(oidCpuInfo),
				new OID(oidCpuUsage),
				new OID(oidCpuType),

				new OID(oidStorageType),
				new OID(oidStorageUnit),
				new OID(oidStorageSize),
				new OID(oidStorageUsage),

				new OID(oidMemType),
				new OID(oidHddType),

				new OID(oidNicDesc),
				new OID(oidNicStatus),
				new OID(oidNicRx),
				new OID(oidNicTx),	
		};

		return oids;		
	}

	/**
	 * NIC 관련 SNMP OID를 사용하여 NIC 정보를 조회
	 * @param param
	 * 
	 */
	public Map<String, Object> getNicInfo()throws Exception{
		Map<String, String> desc =  getResultMap(oidNicDesc);
		Map<String, String> status =  getResultMap(oidNicStatus);
		Map<String, String> rx = getResultMap(oidNicRx);
		Map<String, String> tx = getResultMap(oidNicTx);
		Map<String, String> nicInfo = new HashMap<>();
		Map<String, String> nicUsage = new HashMap<>();
		Map<String, Object> result = new HashMap<>();

		// Desc 기준으로 Nic Status 정보 추출
		for(String key : desc.keySet()){
			String statusStr = status.get(key).equals("1") ? "on" : "off";
			nicInfo.put(desc.get(key), statusStr);
			nicUsage.put(desc.get(key), rx.get(key)+"/"+tx.get(key));
		}

		result.put("nicInfo", nicInfo);
		result.put("nicUsage", nicUsage);

		return result;
	}

	/**
	 * Storage 관련 SNMP OID를 사용하여 Memory / HDD 정보를 조회
	 * @param param
	 */
	public Map<String, Float> getStorageInfo()throws Exception{
		// Storage type 조회
		Map<String, String> type =  getResultMap(oidStorageType);
		// Storage Unit 조회
		Map<String, String> unit =  getResultMap(oidStorageUnit);
		// Storage 전체 크기 조회
		Map<String, String> size =  getResultMap(oidStorageSize);
		// Storage 사용량 조회
		Map<String, String> usage =  getResultMap(oidStorageUsage);

		//Memory type device id  목록
		List<String> memIdList = new ArrayList<>();
		//HDD type device id  목록
		List<String> hddIdList = new ArrayList<>();

		// 최종 결과
		float memTotal = 0;
		float memUsage = 0;

		float hddTotal = 0;
		float hddUsage = 0;

		Map<String, Float> result = new HashMap<>();

		// Storage 목록 중 memory 타입과 hdd 타입을 구별
		for(String key : type.keySet()){
			String oidType = type.get(key);
			if(StringUtils.substring(oidType, oidType.length()-1).equals(oidMemType)){
				memIdList.add(key);
			}
			if(StringUtils.substring(oidType, oidType.length()-1).equals(oidHddType)){
				hddIdList.add(key);
			}
		}

		//Memory 전체 크기 / 사용량 계산을 위한 Loop
		for(String key : memIdList){
			long tempUnit = parseLong(unit.get(key));
			long tempSize = parseLong(size.get(key));
			long tempUsage = parseLong(usage.get(key));


			memTotal += (tempUnit * tempSize) /  1073741824.0;
			memUsage += (tempUnit * tempUsage) /  1073741824.0;
		}


		//HDD 전체 크기 / 사용량 계산을 위한 Loop
		for(String key : hddIdList){

			long tempUnit = parseLong(unit.get(key));
			long tempSize = parseLong(size.get(key));
			long tempUsage = parseLong(usage.get(key));

			hddTotal += (tempUnit * tempSize) /  1073741824.0;
			hddUsage += (tempUnit * tempUsage) /  1073741824.0;
		}

		result.put("memTotal", memTotal);
		result.put("memUsage", memUsage);
		result.put("hddTotal", hddTotal);
		result.put("hddUsage", hddUsage);

		return result;
	}

	/**
	 * CPU 관련 SNMP OID를 사용하여 CPU 정보를 조회
	 * @param param
	 */
	public Map<String,Object> getCpuInfo()throws Exception{
		// snmp device 목록 조회
		Map<String, String> device =  getResultMap(oidDeviceList);
		// snmp device type 조회
		Map<String, String> type =  getResultMap(oidTypelist);
		// snmp device 정보 조회
		Map<String, String> info =  getResultMap(oidCpuInfo);
		// snmp cpu 시용량 정보 조회
		Map<String, String> usage =  getResultMap(oidCpuUsage);
		// cpu core device id 목록
		List<String> coreList = new ArrayList<>();
		// cpu 정보 || key : cpu info , value core count
		Map<String, Integer> coreInfo = new HashMap<>();
		Map<String,Object> result = new HashMap<>();
		String cpuInfo = "";
		Map<String, Integer> cpuUsage = new HashMap<>();

		// Device List를 기준으로 Type이 3인 목록 추출 loop
		for(String key : device.keySet()){
			String oidType = type.get(key);
			if(StringUtils.isNotEmpty(oidType) && StringUtils.substring(oidType, oidType.length()-1).equals(oidCpuType)){
				coreList.add(key);
			}
		}

		// 추출한 core 정보 추출 (Core 계수 / Core Info)
		for(int i=0; i<coreList.size(); i++){

			String key = coreList.get(i);
			// 코어 정보 추출
			String tempCore = info.get(key);
			if(coreInfo.containsKey(tempCore)){
				coreInfo.put(tempCore, coreInfo.get(tempCore)+1);
			}else{
				coreInfo.put(tempCore, 1);
			}
			// 사용량 정보 추출
			cpuUsage.put(key, parseInt( usage.get(key)) );
		}

		for(String key : coreInfo.keySet()){
			cpuInfo += key + " | " + coreInfo.get(key);
		}

		result.put("cpuInfo",cpuInfo);
		result.put("cpuUsage",cpuUsage);

		return result;
	}

}
