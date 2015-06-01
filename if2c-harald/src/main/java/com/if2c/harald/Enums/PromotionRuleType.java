package com.if2c.harald.Enums;


public enum PromotionRuleType {

	DISCOUNT("1", "discount", "折扣"), PRICE_DOWN("2", "price_down", "直降"), PRICE_REACH(
			"3", "price_reach", "满减"), ;
	private String rule;
	private String opt;
	private String desc;

	private PromotionRuleType(String rule, String opt, String desc) {
		this.rule = rule;
		this.opt = opt;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public String getRule() {
		return rule;
	}

	public String getOpt() {
		return opt;
	}

	public static PromotionRuleType getObjByCode(String rule) {
		for (PromotionRuleType obj : PromotionRuleType.values()) {
			if (obj.getRule().equals(rule)) {
				return obj;
			}
		}
		return null;
	}

}
