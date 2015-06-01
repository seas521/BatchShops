package com.if2c.harald.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Goods  implements Serializable{
	long id;
	int attrID;
	int attrValueID;
	String snapshot;
	// this is the attribute name
	String num;
	String name;


	// this is the attribute value
	String value;

	long seriesID;

	Goods reusedImageGoods;
	String localImage = null;
	String tfsImage = null;
	int trade;
	int city;
	int type;
	int deliverCountry;
	String class1categoryId;
	String identification;

	    private Long categoryId;
private Long beforeId ;

	    private Long seriesId;

	    private Integer brandId;

	    private Integer countryId;

	    private Byte isOnSale;

	    private Date createTime;

	    private Date updateTime;

	    private Byte isVirtual;

	    private BigDecimal basePrice;

	    private Long manufacturerId;

	    private Date onSaleTime;

	    private Date offlineTime;

	    private Integer commentNum;

	    private Integer saleNum;

	    private Integer inventory;

	    private Integer shopId;

	    private String itemNum;

	    private String img;

	    private Byte status;

	    private Byte active;
        private Integer frontInventory ;
    private Integer virtualInventory ;
    private Integer realityInventory ;
    private List<GoodsAttrNameValue> goodsAttrNameValueList ;

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}



	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTrade() {
		return trade;
	}

	public void setTrade(int trade) {
		this.trade = trade;
	}

	public int getDeliverCountry() {
		return deliverCountry;
	}

	public void setDeliverCountry(int deliverCountry) {
		this.deliverCountry = deliverCountry;
	}

	public String getClass1categoryId() {
		return class1categoryId;
	}

	public void setClass1categoryId(String class1categoryId) {
		this.class1categoryId = class1categoryId;
	}

	public String getTfsImage() {
		if (reusedImageGoods != null) {
			return reusedImageGoods.getTfsImage();
		}
		return tfsImage;
	}

	public void setTfsImage(String tfsImage) {

		this.tfsImage = tfsImage;
	}

	public String getLocalImage() {
		return localImage;
	}

	public void setLocalImage(String localImage) {
		this.localImage = localImage;
	}

	public boolean canReuseImage(Goods goods) {
		return this.seriesID == goods.getSeriesID()
				&& this.attrValueID == goods.getAttrValueID();
	}

	public Goods getReusedImageGoods() {
		return reusedImageGoods;
	}

	public void setReusedImageGoods(Goods reusedImageGoods) {
		this.reusedImageGoods = reusedImageGoods;
	}

	@Override
	public String toString() {
		return "Goods [id=" + id + ", attrID=" + attrID + ", attrValueID="
				+ attrValueID + ", name=" + name + ", value=" + value
				+ ", seriesID=" + seriesID + ", localImage=" + localImage
				+ ", TFSImage=" + this.getTfsImage() + ", reusedImageGoods="
				+ reusedImageGoods + "]";
	}

	public long getSeriesID() {
		return seriesID;
	}

	public void setSeriesID(long seriesID) {
		this.seriesID = seriesID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAttrID() {
		return attrID;
	}

	public void setAttrID(int attrID) {
		this.attrID = attrID;
	}

	public int getAttrValueID() {
		return attrValueID;
	}

	public void setAttrValueID(int attrValueID) {
		this.attrValueID = attrValueID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Byte getIsOnSale() {
		return isOnSale;
	}

	public void setIsOnSale(Byte isOnSale) {
		this.isOnSale = isOnSale;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Byte getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(Byte isVirtual) {
		this.isVirtual = isVirtual;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public Long getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Date getOnSaleTime() {
		return onSaleTime;
	}

	public void setOnSaleTime(Date onSaleTime) {
		this.onSaleTime = onSaleTime;
	}

	public Date getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getActive() {
		return active;
	}

	public void setActive(Byte active) {
		this.active = active;
	}

    public List<GoodsAttrNameValue> getGoodsAttrNameValueList() {
        return goodsAttrNameValueList;
    }

    public void setGoodsAttrNameValueList(List<GoodsAttrNameValue> goodsAttrNameValueList) {
        this.goodsAttrNameValueList = goodsAttrNameValueList;
    }

    public Long getBeforeId() {
        return beforeId;
    }

    public void setBeforeId(Long beforeId) {
        this.beforeId = beforeId;
    }

    public Integer getFrontInventory() {
        return frontInventory;
    }

    public void setFrontInventory(Integer frontInventory) {
        this.frontInventory = frontInventory;
    }

    public Integer getVirtualInventory() {
        return virtualInventory;
    }

    public void setVirtualInventory(Integer virtualInventory) {
        this.virtualInventory = virtualInventory;
    }

    public Integer getRealityInventory() {
        return realityInventory;
    }

    public void setRealityInventory(Integer realityInventory) {
        this.realityInventory = realityInventory;
    }
}
