package com.if2c.harald.manager;

import com.if2c.harald.beans.ExtAttrNameValue;
import com.if2c.harald.beans.query.ExtAttrNameValueQuery;

import java.util.List;

/**
 * Description:扩展属性事务处理层
 * User: Courser
 * Date: 14-5-4
 * Time: 下午2:31
 */
public interface ExtAttrNameValueManager {
    /**
     * 通过源数据库seriesId获取扩展属性
     * @param seriesId
     * @return
     */
    public List<ExtAttrNameValue> qryByGoodsSeriesId (Long seriesId);

    /**
     * 查询目标库所有的扩展属性
     * @param query
     * @return
     */
    public List<ExtAttrNameValue> getAll(ExtAttrNameValueQuery query);
}
