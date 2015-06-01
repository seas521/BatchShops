package com.if2c.harald.beans.query;

public class GoodsSeriesQuery extends BaseQuery {

    /**
     * 进出口 {1：进口，2：出口}
     */
    private Integer trade ;

    private Long categoryId ;
    private Integer attrId ;
    private Integer attrValueId ;
    private Long  goodsSeriesId ;
    public GoodsSeriesQuery(){

    }
    public GoodsSeriesQuery(Integer attrId,Integer attrValueId){
        this.attrId = attrId ;
        this.attrValueId = attrValueId ;
    }

    /**
     *
     * @param trade 进出口
     * @param categoryId 类目
     * @param goodsSeriesId 系列
     */
    public GoodsSeriesQuery(Integer trade,Long categoryId,Long goodsSeriesId){
        this.trade = trade ;
        this.categoryId = categoryId ;
        this.goodsSeriesId = goodsSeriesId;
    }
    public Integer getTrade() {
        return trade;
    }

    public void setTrade(Integer trade) {
        this.trade = trade;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public Integer getAttrValueId() {
        return attrValueId;
    }

    public void setAttrValueId(Integer attrValueId) {
        this.attrValueId = attrValueId;
    }

    public Long getGoodsSeriesId() {
        return goodsSeriesId;
    }

    public void setGoodsSeriesId(Long goodsSeriesId) {
        this.goodsSeriesId = goodsSeriesId;
    }
}
