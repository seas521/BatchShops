package com.if2c.harald.service.impl;

import com.if2c.harald.beans.GoodsAttrNameValue;
import com.if2c.harald.beans.GoodsSeries;
import com.if2c.harald.beans.query.GoodsSeriesQuery;
import com.if2c.harald.manager.GoodsSeriesManager;
import com.if2c.harald.service.GoodsSeriesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 类目服务类
 * User: Courser
 * Date: 14-4-28
 * Time: 下午4:45
 */
@Service
public class GoodsSeriesServiceImpl implements GoodsSeriesService {
    @Resource
    private GoodsSeriesManager goodsSeriesManager ;
    /**
     * 分页批量查询线上类目数据,返回类目集合
     *
     * @param goodsSeriesQuery 类目查询对象，帮忙开始和结束行
     */
    @Override
    public List<GoodsSeries> qryBatchGoodSeries(GoodsSeriesQuery goodsSeriesQuery) {
        return goodsSeriesManager.qryBatchGoodSeries(goodsSeriesQuery);
    }

    /**
     * 通过 seriesID,categoryId,trade 查询
     *
     * @param goodsSeriesQuer
     * @return
     */
    @Override
    public GoodsSeries qryGoodsSeries(GoodsSeriesQuery goodsSeriesQuer) {
        return goodsSeriesManager.qryGoodSeries(goodsSeriesQuer);
    }


    /**
     * 保存类目到本地数据源
     *
     * @param bean
     */
    @Override
    public void save(GoodsSeries bean) {
        goodsSeriesManager.save(bean);
    }

    /**
     * 获取商品属性类目关系的ID
     *
     * @param queryBean
     * @return
     */
    @Override
    public Integer getAcvrId(GoodsSeriesQuery queryBean) {
        return goodsSeriesManager.getAcvrId(queryBean);
    }

    @Override
    public List<GoodsAttrNameValue> getAttrNameValueByGoodsId(Long id) {
       return goodsSeriesManager.getAttrNameValueByGoodsId(id);
    }

}
