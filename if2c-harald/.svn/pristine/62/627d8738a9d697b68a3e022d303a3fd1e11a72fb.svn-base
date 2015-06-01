package com.if2c.harald.dao;

import java.util.List;

import com.if2c.harald.beans.GoodsAttrNameValue;
import com.if2c.harald.beans.GoodsSeries;
import com.if2c.harald.beans.query.GoodsSeriesQuery;
import org.springframework.stereotype.Repository;

/**
 * 类目DAO接口
 * @author Courser
 * @version 1.0
 * @created 28-四月-2014 14:38:01
 */
@Repository
public interface GoodsSeriesDao {

	/**
	 * 持久化本地类目保存
	 * 
	 * @param goodsSeries    商品类目
	 */
	public void insert(GoodsSeries goodsSeries);

	/**
	 * 分页查询线上数据类目对象，返回类目集合
	 * 
	 * @param queryBean    线上数据类目查询对象
	 */
	public List<GoodsSeries> qryBatchGoodSeries(GoodsSeriesQuery queryBean);
    /**
     * 通过类目ID,spuid 进出口标示返回类目对象
     *
     * @param queryBean
     * @return 类目对象
     */
    public GoodsSeries qryGoodSeries(GoodsSeriesQuery queryBean);

    /**
     * 获取商品属性类目关系的ID
     * @param queryBean
     * @return
     */
    public Integer getAcvrId(GoodsSeriesQuery queryBean );

    public List<GoodsAttrNameValue> getAttrNameValueByGoodsId(Long id) ;

}