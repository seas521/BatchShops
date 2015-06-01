package com.if2c.harald.dao;

import com.if2c.harald.beans.ExtAttrNameValue;
import com.if2c.harald.beans.query.ExtAttrNameValueQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:扩展属性dao接口
 * User: Courser
 * Date: 14-5-4
 * Time: 下午2:27
 */
@Repository
public interface ExtAttrNameValueDao extends BaseDao<ExtAttrNameValue,ExtAttrNameValueQuery> {
    /**
     * 通过源数据库seriesId获取扩展属性
     * @param seriesId
     * @return
     */
    public List<ExtAttrNameValue> qryByGoodsSeriesId (Long seriesId);
}
