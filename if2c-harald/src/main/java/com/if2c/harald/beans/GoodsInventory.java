package com.if2c.harald.beans;

public class GoodsInventory {
    private Long goodsId;

    private Long goodsSeriesId;

    private Integer frontInventory;

    private Integer virtualInventory;

    private Integer realityInventory;

    public GoodsInventory(Long goodsSeriesId,Long goodsId,Integer frontInventory,Integer virtualInventory ,Integer realityInventory){
        this.goodsId = goodsId ;
        this.goodsSeriesId = goodsSeriesId ;
        this.frontInventory = frontInventory ;
        this.virtualInventory = frontInventory ;
        this.realityInventory = realityInventory ;
    }
    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getGoodsSeriesId() {
        return goodsSeriesId;
    }

    public void setGoodsSeriesId(Long goodsSeriesId) {
        this.goodsSeriesId = goodsSeriesId;
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