package com.if2c.harald.manager;

import java.util.List;

import com.if2c.harald.beans.GoodsAttrNameValue;
import com.if2c.harald.beans.GoodsSeries;
import com.if2c.harald.beans.query.GoodsSeriesQuery;

/**
 * 类目manager接口
 * @author Courser
 * @version 1.0
 * @created 28-四月-2014 14:38:06
 */
public interface GoodsSeriesManager {

	/**
	 * 分页查询线上类目数据对象
	 * 
	 * @param query    分页查询类目的查询对象
	 */
	public List<GoodsSeries> qryBatchGoodSeries(GoodsSeriesQuery query);
    /**
     * categoryid seriesid trade
     *
     * @param query
     * @return 返回类目对象
     */
    public GoodsSeries qryGoodSeries(GoodsSeriesQuery query);

    /**
     * 保存本地类目
     *
     * @param bean    保存到本地类目
     */
    public void save(GoodsSeries bean) ;

    /**
     * 获取属性类目关系的ID
     * @param queryBean
     * @return
     */
    public Integer getAcvrId(GoodsSeriesQuery queryBean );

    public List<GoodsAttrNameValue> getAttrNameValueByGoodsId(Long id) ;

}