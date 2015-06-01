package com.if2c.harald.beans;

import java.util.ArrayList;
import java.util.List;

public class PayInfo {
	
	//商品ID
	String goodsId;
	//商品上传价格
	float basePrice;
	//类目ID
	String categoryId;
	//重量
	float weight;
	//订单商品数量
	float num;
	//是否是退货商品
	boolean refundFlag;
	//币种
	String currency;
	//参加的活动
	List<GoodsPromotion> promotions;

	//原价（RMB）
	private float price;
	
	//促销价格（单品 直降）
	private float promotionprice;
	
	//扣点百分比
	private int countPercent;
	
	//促销金额（商家承担）
	private float sellerPromotionPrice;

	//优惠券金额（商家承担）
	private float sellerCouponPrice;
	
	//商品对应类目的关税税率
	private float taxPoint;
	
	
	
	public float getTaxPoint() {
		return taxPoint;
	}
	public void setTaxPoint(float taxPoint) {
		this.taxPoint = taxPoint;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public float getSellerCouponPrice() {
		return sellerCouponPrice;
	}
	public void setSellerCouponPrice(float sellerCouponPrice) {
		this.sellerCouponPrice = sellerCouponPrice;
	}
	public float getSellerPromotionPrice() {
		return sellerPromotionPrice;
	}
	public void setSellerPromotionPrice(float sellerPromotionPrice) {
		this.sellerPromotionPrice = sellerPromotionPrice;
	}
	public int getCountPercent() {
		return countPercent;
	}
	public void setCountPercent(int countPercent) {
		this.countPercent = countPercent;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getPromotionprice() {
		return promotionprice;
	}
	public float getRealprice() {
		float real=promotionprice==0f?price:promotionprice;
		return real;
	}
	public void setPromotionprice(float promotionprice) {
		this.promotionprice = promotionprice;
	}
	public boolean isRefundFlag() {
		return refundFlag;
	}
	public void setRefundFlag(boolean refundFlag) {
		this.refundFlag = refundFlag;
	}
	public List<GoodsPromotion> getPromotions() {
		if(promotions==null){
			promotions =  new ArrayList<GoodsPromotion>();
		}
		return promotions;
	}
	public void setPromotions(List<GoodsPromotion> promotions) {
		this.promotions = promotions;
	}
	public float getNum() {
		return num;
	}
	public void setNum(float num) {
		this.num = num;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public float getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
