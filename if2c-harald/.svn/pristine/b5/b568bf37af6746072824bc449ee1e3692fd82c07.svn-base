package com.if2c.harald.Enums;

public enum AuditStatusEunm {

    USERCANCEL(12,"用户取消"),
	SELLERAUDITING(20,"商家申请退货审核"),
	SELLERNOTPASS(21,"商家申请退货审核不通过"),
	SELLERAUDITED(22,"商家申请退货审核通过"),
	PLATAUDITED(42,"客服申请退货审核通过"),
	SELLERRECEIVING(60,"等待商家收货"),
	SELLERRECEIVED(70,"商家收货审核"),
	SELLERRECEIVEDPASSED(74,"商家收货审核通过"),
	SELLERRECEIVEDNOTPASS(78,"商家收货审核不通过"),
	REFUNDCOMPLETEDLY(80,"完成退款"),
	NOTREFUNDCOMPLETEDLY(81,"完成未退款"),
	SELLERRECEIVEDNOTPASSUSERNOCOMPLAIN(82,"商家收货审核不通过用户不维权"),
	SELLERRECEIVEDNOTPASSPLATNOTPASS(98,"商家收货审核不通过客服审核不通过"),
	SELLERRECEIVEDNOTPASSSENDTOUSER(102,"商家收货审核不通过寄回买家"),
	WAITTOREFUND(160,"财务查看"),
	COMPLETEGOODS(190,"完成退货"),
	UNCOMPLETEGOODS(191,"完成未退货"),
	;
	private int code;
	private String desc;
	private AuditStatusEunm(int code,String desc){
		this.code=code;
		this.desc=desc;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
