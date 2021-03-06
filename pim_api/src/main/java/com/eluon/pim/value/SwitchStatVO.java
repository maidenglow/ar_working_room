package com.eluon.pim.value;

public class SwitchStatVO {
	private int no;
	private int switchInfoNo;
	private int statType;
	private String statTime;
	private String nicUsage;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getSwitchInfoNo() {
		return switchInfoNo;
	}
	public void setSwitchInfoNo(int switchInfoNo) {
		this.switchInfoNo = switchInfoNo;
	}
	public int getStatType() {
		return statType;
	}
	public void setStatType(int statType) {
		this.statType = statType;
	}
	public String getStatTime() {
		return statTime;
	}
	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}
	public String getNicUsage() {
		return nicUsage;
	}
	public void setNicUsage(String nicUsage) {
		this.nicUsage = nicUsage;
	}

	@Override
	public String toString() {
		return "SwitchStatVO [no=" + no + ", switchInfoNo=" + switchInfoNo + ", statType=" + statType + ", statTime="
				+ statTime + ", nicUsage=" + nicUsage + "]";
	}

}
