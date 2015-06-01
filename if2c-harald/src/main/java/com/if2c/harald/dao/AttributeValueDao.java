package com.if2c.harald.dao;

import com.if2c.harald.beans.Attribute;
import com.if2c.harald.beans.AttributeValue;
import com.if2c.harald.beans.query.AttributeQuery;
import com.if2c.harald.beans.query.AttributeValueQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:SPU属性dao
 * User: Courser
 * Date: 14-4-30
 * Time: 下午12:05
 */
@Repository
public interface AttributeValueDao extends BaseDao<AttributeValue,AttributeValueQuery> {
    /**
     * 获取所有的spu
     * @param query
     * @return
     */
     public List<AttributeValue> getAll(AttributeValueQuery query);


}
