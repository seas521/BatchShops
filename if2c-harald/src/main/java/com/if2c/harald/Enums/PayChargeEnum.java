package com.if2c.harald.Enums;

public enum PayChargeEnum {
	INTBOUND(0.006f, 1, "境内"), 
	OUTBOUND(0.025f, 2, "境外"), 
	;
	private float point;
	private int key;
	private String desc;

	private PayChargeEnum(float point, int key, String desc) {
		this.point = point;
		this.key = key;
		this.desc = desc;
	}

	public float getPoint() {
		return point;
	}

	public void setPoint(float point) {
		this.point = point;
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
	
	public static PayChargeEnum getPayChargeEnum(int key){
		for(PayChargeEnum v:PayChargeEnum.values()){
			if(v.getKey()==key){
				return v;
			}
		}
		return null;
	}

}
