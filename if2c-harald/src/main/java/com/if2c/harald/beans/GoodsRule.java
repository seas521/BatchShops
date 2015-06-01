package com.if2c.harald.beans;

public class GoodsRule {
	private Long id;
	private Integer inventory;
	private Integer ruleId;
	public Long getId() {
		return id;
	}
	public Integer getInventory() {
		return inventory;
	}
	public Integer getRuleId() {
		return ruleId;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
}
