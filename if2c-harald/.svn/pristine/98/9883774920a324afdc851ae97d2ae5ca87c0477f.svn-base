package com.if2c.harald.Enums;

public enum CancelType {

	CANCELGOODS(1,4,"退货"),
	CANCELMONEY(2,3,"退款"),
	;
	private int saleCode;
	private int giftcardCode;
	private String desc;
	private CancelType(int saleCode,int giftcardCode,String desc){
		this.saleCode=saleCode;
		this.giftcardCode=giftcardCode;
		this.desc=desc;
		
	}
	
	public int getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(int saleCode) {
		this.saleCode = saleCode;
	}

	public int getGiftcardCode() {
		return giftcardCode;
	}

	public void setGiftcardCode(int giftcardCode) {
		this.giftcardCode = giftcardCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static CancelType getCancelType(int saleCode){
		for(CancelType ct:CancelType.values()){
			if(saleCode==ct.getSaleCode()){
				return ct;
			}
		}
		return null;
	}

}
