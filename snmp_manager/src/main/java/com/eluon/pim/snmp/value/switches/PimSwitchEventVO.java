package com.eluon.pim.snmp.value.switches;

public class PimSwitchEventVO {

	private int switchId;
	private int targetNo;
	private String eventTime;
	private int profileNo;
	private String Desc;
	
	public int getSwitchId() {
		return switchId;
	}
	public void setSwitchId(int switchId) {
		this.switchId = switchId;
	}
	public int getTargetNo() {
		return targetNo;
	}
	public void setTargetNo(int targetNo) {
		this.targetNo = targetNo;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public int getProfileNo() {
		return profileNo;
	}
	public void setProfileNo(int profileNo) {
		this.profileNo = profileNo;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}

	@Override
	public String toString() {
		return "PimSwitchEventVO [switchId=" + switchId + ", targetNo=" + targetNo + ", eventTime=" + eventTime
				+ ", profileNo=" + profileNo + ", Desc=" + Desc + "]";
	}
}
