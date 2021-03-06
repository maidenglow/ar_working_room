package com.eluon.pim.snmp.value.server;

import java.util.Date;

public class PimServerStatVO {
	private int no;
	private int serverId;
	private int statType;
	private Date statTime;
	private String cpuUsageAvg;
	private float memUsageAvg;
	private float hddUsageAvg;
	private String cpuUsagePeak;
	private float memUsagePeak;
	private float hddUsagePeak;
	private String nicUsage;

	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getStatType() {
		return statType;
	}
	public void setStatType(int statType) {
		this.statType = statType;
	}
	public Date getStatTime() {
		return statTime;
	}
	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}
	public String getCpuUsageAvg() {
		return cpuUsageAvg;
	}
	public void setCpuUsageAvg(String cpuUsageAvg) {
		this.cpuUsageAvg = cpuUsageAvg;
	}
	public float getMemUsageAvg() {
		return memUsageAvg;
	}
	public void setMemUsageAvg(float memUsageAvg) {
		this.memUsageAvg = memUsageAvg;
	}
	public float getHddUsageAvg() {
		return hddUsageAvg;
	}
	public void setHddUsageAvg(float hddUsageAvg) {
		this.hddUsageAvg = hddUsageAvg;
	}
	public String getCpuUsagePeak() {
		return cpuUsagePeak;
	}
	public void setCpuUsagePeak(String cpuUsagePeak) {
		this.cpuUsagePeak = cpuUsagePeak;
	}
	public float getMemUsagePeak() {
		return memUsagePeak;
	}
	public void setMemUsagePeak(float memUsagePeak) {
		this.memUsagePeak = memUsagePeak;
	}
	public float getHddUsagePeak() {
		return hddUsagePeak;
	}
	public void setHddUsagePeak(float hddUsagePeak) {
		this.hddUsagePeak = hddUsagePeak;
	}
	public String getNicUsage() {
		return nicUsage;
	}
	public void setNicUsage(String nicUsage) {
		this.nicUsage = nicUsage;
	}
	@Override
	public String toString() {
		return "PimServerStatVO [no=" + no + ", serverId=" + serverId + ", statType=" + statType + ", statTime="
				+ statTime + ", cpuUsageAvg=" + cpuUsageAvg + ", memUsageAvg=" + memUsageAvg + ", hddUsageAvg="
				+ hddUsageAvg + ", cpuUsagePeak=" + cpuUsagePeak + ", memUsagePeak=" + memUsagePeak + ", hddUsagePeak="
				+ hddUsagePeak + ", nicUsage=" + nicUsage + "]";
	}
	
	
}
