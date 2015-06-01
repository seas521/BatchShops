package com.if2c.harald.Enums;

public enum SellerTypeEnum {

	ZHIGONG(1,"国外品牌"),
	ZHICAISELLER(2,"国外代理"),
	PROXY(3,"国内代理"),
	ZHICAIIZP(4,"代购"),
	;
	private int key;
	private String desc;
	private SellerTypeEnum(int key,String desc){
		this.key=key;
		this.desc=desc;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
