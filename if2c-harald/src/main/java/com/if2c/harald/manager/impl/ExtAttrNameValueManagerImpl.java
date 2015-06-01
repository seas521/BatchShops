package com.if2c.harald.manager.impl;

import com.if2c.harald.beans.ExtAttrNameValue;
import com.if2c.harald.beans.query.ExtAttrNameValueQuery;
import com.if2c.harald.dao.ExtAttrNameValueDao;
import com.if2c.harald.manager.ExtAttrNameValueManager;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:扩展属性事务处理层实现类
 * User: Courser
 * Date: 14-5-4
 * Time: 下午2:33
 */
@Repository("extAttrNameValueManager")
public class ExtAttrNameValueManagerImpl implements ExtAttrNameValueManager {
    @Resource
    private ExtAttrNameValueDao extAttrNameValueDao;

    /**
     * 通过源数据库seriesId获取扩展属性
     *
     * @param seriesId
     * @return
     */
    @Override
    public List<ExtAttrNameValue> qryByGoodsSeriesId(Long seriesId) {
        return extAttrNameValueDao.qryByGoodsSeriesId(seriesId);
    }

    /**
     * 查询目标库所有的扩展属性
     *
     * @param query
     * @return
     */
    @Override
    public List<ExtAttrNameValue> getAll(ExtAttrNameValueQuery query) {
        return extAttrNameValueDao.getList(query);
    }
}
