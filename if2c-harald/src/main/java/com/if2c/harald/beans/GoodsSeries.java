package com.if2c.harald.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GoodsSeries {
	long id;
	int imageNumber;
	List<Goods> goods = null;
	String[] localImage = null;
	String[] tfsImage = null;

    private String name;

    private Long categoryId;

    private Integer shopId;

    private Integer brandId;

    private Integer countryId;

    private Integer isOnSale;

    private Date ceateTime;

    private Date updateTime;

    private Integer isVirtual;

    private Double basePrice;

    private Long manufacturerId;

    private Date onSaleTime;

    private Date offlineTime;

    private Integer commentNum;

    private Integer saleNum;

    private Long defaultGoodsId;

    private Integer imgNum;

    private String currencyId;

    private Double length;

    private Double width;

    private Double height;

    private Double weight;

    private String advertisement;

    private Integer status;

    private String identification;

    private Double star;

    private String describe;
    private List<Goods> goodsList ;
    /**
     * 扩展属性集合
     */
    private List<ExtAttrNameValue> extAttrNameValueList ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
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

    public Integer getOnSale() {
        return isOnSale;
    }

    public void setOnSale(Integer onSale) {
        isOnSale = onSale;
    }

    public Date getCeateTime() {
        return ceateTime;
    }

    public void setCeateTime(Date ceateTime) {
        this.ceateTime = ceateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVirtual() {
        return isVirtual;
    }

    public void setVirtual(Integer virtual) {
        isVirtual = virtual;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
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

    public Long getDefaultGoodsId() {
        return defaultGoodsId;
    }

    public void setDefaultGoodsId(Long defaultGoodsId) {
        this.defaultGoodsId = defaultGoodsId;
    }

    public Integer getImgNum() {
        return imgNum;
    }

    public void setImgNum(Integer imgNum) {
        this.imgNum = imgNum;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(String advertisement) {
        this.advertisement = advertisement;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String[] getTfsImage() {
		return tfsImage;
	}

	public void setTfsImage(String[] tfsImage) {
		this.tfsImage = tfsImage;
	}

	public String[] getLocalImage() {
		return localImage;
	}

	public void setLocalImage(String localImage[]) {
		this.localImage = localImage;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getImageNumber() {
		return imageNumber;
	}

	public void setImageNumber(int imageNumber) {
		this.imageNumber = imageNumber;
	}

	public List<Goods> getGoods() {
		return goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}

	public void addGoods(Goods g) {
		if (getGoods() == null) {
			goods = new ArrayList<Goods>();
		}
		getGoods().add(g);
	}

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
	public String toString() {
		return "GoodsSeries [id=" + id + ", imageNumber=" + imageNumber
				+ ", localImage="
				+ Arrays.toString(localImage) + ", tfsImage="
				+ Arrays.toString(tfsImage) + "]";
	}

    public List<ExtAttrNameValue> getExtAttrNameValueList() {
        return extAttrNameValueList;
    }

    public void setExtAttrNameValueList(List<ExtAttrNameValue> extAttrNameValueList) {
        this.extAttrNameValueList = extAttrNameValueList;
    }
}
