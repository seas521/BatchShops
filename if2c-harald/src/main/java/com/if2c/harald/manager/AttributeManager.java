package com.if2c.harald.manager;

import com.if2c.harald.beans.Attribute;
import com.if2c.harald.beans.query.AttributeQuery;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: Courser
 * Date: 14-4-30
 * Time: 下午2:07
 */
public interface AttributeManager {
    /**
     * 获取所有的spu
     * @param query
     * @return
     */
    public List<Attribute> getAll(AttributeQuery query);
}
