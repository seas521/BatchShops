package com.if2c.harald.manager.impl;

import com.if2c.harald.beans.*;
import com.if2c.harald.beans.query.GoodsSeriesQuery;
import com.if2c.harald.dao.*;
import com.if2c.harald.manager.GoodsSeriesManager;
import org.apache.ibatis.transaction.Transaction;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Description: 类目事务类
 * User: Courser
 * Date: 14-4-28
 * Time: 下午4:48
 */
@Repository
public class GoodsSeriesManagerImpl implements GoodsSeriesManager {
    private static final Logger logger = Logger.getLogger(GoodsSeriesManagerImpl.class.getName());
    @Resource
    private GoodsSeriesDao goodsSeriesDao ;
    @Resource
    private GoodsDao goodsDao;
    @Resource
    private GacvrDao gacvrDao ;
    @Resource
    private GeavrDao geavrDao ;
    @Resource
    private GoodsInventoryDao goodsInventoryDao ;
    /**
     * 分页查询线上类目数据对象
     *
     * @param query 分页查询类目的查询对象
     */
    @Override
    public List<GoodsSeries> qryBatchGoodSeries(GoodsSeriesQuery query) {
        return goodsSeriesDao.qryBatchGoodSeries(query);
    }

    /**
     * categoryid seriesid trade
     *
     * @param query
     * @return 返回类目对象
     */
    @Override
    public GoodsSeries qryGoodSeries(GoodsSeriesQuery query) {
        return goodsSeriesDao.qryGoodSeries(query);
    }


    /**
     * 保存本地类目
     *
     * @param bean 保存到本地类目
     */
    @Override
    @Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackFor = {Exception.class})
    public void save(GoodsSeries bean){
        goodsSeriesDao.insert(bean);

        //保存扩展属性
        if(null != bean.getExtAttrNameValueList() && 0< bean.getExtAttrNameValueList().size()){
            for(ExtAttrNameValue attrNameValue : bean.getExtAttrNameValueList()){
                geavrDao.insert( new Geavr(bean.getId(),attrNameValue.getEavrId(),1));
            }
        }

        //保存商品
        for (Goods goods:bean.getGoodsList()){
//             goods.setSeriesId(bean.getId());

            goodsDao.insert(goods);//保存商品
//            System.out.println("goods.id"+goods.getId());
//            goodsInventoryDao.insert(new GoodsInventory(bean.getId(),goods.getId(),goods.getFrontInventory(),goods.getVirtualInventory(),goods.getRealityInventory()));
            logger.info("oldGoodsId="+goods.getBeforeId()+",newGoodsId="+goods.getId());
            for(GoodsAttrNameValue nameValue :goods.getGoodsAttrNameValueList()){
                Gacvr gacvr = new Gacvr(goods.getId(),nameValue.getAcvrId(),1);
                gacvrDao.insert(gacvr);//保存商品属性
//                logger.info("写入商品属性类目值关系表：id="+gacvr.getId()+",goodsId="+gacvr.getGoodsId()+",acvrId="+gacvr.getAttributeCategoryValueRelationId());

            }
        }

//        goodsDao.insertBatch(bean.getGoodsList());
//        throw new RuntimeException("回滚");

    }

    /**
     * 获取属性类目关系的ID
     *
     * @param queryBean
     * @return
     */
    @Override
    public Integer getAcvrId(GoodsSeriesQuery queryBean) {
        return  goodsSeriesDao.getAcvrId(queryBean);
    }

    @Override
    public List<GoodsAttrNameValue> getAttrNameValueByGoodsId(Long id) {
       return goodsSeriesDao.getAttrNameValueByGoodsId(id);
    }


}
