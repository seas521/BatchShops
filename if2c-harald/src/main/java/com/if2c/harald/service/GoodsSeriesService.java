package com.if2c.harald.service;

import java.util.List;

import com.if2c.harald.beans.GoodsAttrNameValue;
import com.if2c.harald.beans.query.GoodsSeriesQuery;
import com.if2c.harald.beans.GoodsSeries;
import org.springframework.stereotype.Service;

/**
 * 类目service 接口
 * @author Courser
 * @version 1.0
 * @updated 28-四月-2014 14:37:54
 */

public interface GoodsSeriesService {

	/**
	 * 分页批量查询线上类目数据,返回类目集合
	 * 
	 * @param goodsSeriesQuery    类目查询对象，帮忙开始和结束行
	 */
	public List<GoodsSeries> qryBatchGoodSeries(GoodsSeriesQuery goodsSeriesQuery);

    /**
     * 通过 seriesID,categoryId,trade 查询
     * @param goodsSeriesQuery
     * @return
     */
    public GoodsSeries qryGoodsSeries(GoodsSeriesQuery goodsSeriesQuery);



    /**
     * 保存类目到本地数据源
     * @param bean
     */
    public void save(GoodsSeries bean);

    /**
     * 获取商品属性类目关系的ID
     * @param queryBean
     * @return
     */
    public Integer getAcvrId(GoodsSeriesQuery queryBean );
    public List<GoodsAttrNameValue> getAttrNameValueByGoodsId(Long id) ;

}