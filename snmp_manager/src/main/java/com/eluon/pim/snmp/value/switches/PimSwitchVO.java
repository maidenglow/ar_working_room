package com.eluon.pim.snmp.value.switches;

public class PimSwitchVO {

	private int switchId;
	private String name;
	private String ip;
	private int status;
	private String statusTime;
	private String regTime;
	private String nicInfo;
	private String vlanInfo;

	public int getSwitchId() {
		return switchId;
	}
	public void setSwitchId(int switchId) {
		this.switchId = switchId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getNicInfo() {
		return nicInfo;
	}
	public void setNicInfo(String nicInfo) {
		this.nicInfo = nicInfo;
	}
	public String getVlanInfo() {
		return vlanInfo;
	}
	public void setVlanInfo(String vlanInfo) {
		this.vlanInfo = vlanInfo;
	}

	@Override
	public String toString() {
		return "PimSwitchVO [switchId=" + switchId + ", name=" + name + ", ip=" + ip + ", status=" + status
				+ ", statusTime=" + statusTime + ", regTime=" + regTime + ", nicInfo=" + nicInfo + ", vlanInfo="
				+ vlanInfo + "]";
	}
}
