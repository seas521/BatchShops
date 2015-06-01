package com.if2c.harald.manager;

import com.if2c.harald.beans.Attribute;
import com.if2c.harald.beans.AttributeValue;
import com.if2c.harald.beans.query.AttributeQuery;
import com.if2c.harald.beans.query.AttributeValueQuery;

import java.util.List;

/**
 * Description:
 * User: Courser
 * Date: 14-4-30
 * Time: 下午2:07
 */
public interface AttributeValueManager {
    /**
     * 获取所有的spu
     * @param query
     * @return
     */
    public List<AttributeValue> getAll(AttributeValueQuery query);
}
