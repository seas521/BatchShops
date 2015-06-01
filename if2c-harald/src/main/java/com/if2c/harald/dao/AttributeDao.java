package com.if2c.harald.dao;

import com.if2c.harald.beans.Attribute;
import com.if2c.harald.beans.query.AttributeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Description:SPU属性dao
 * User: Courser
 * Date: 14-4-30
 * Time: 下午12:05
 */
@Repository
public interface AttributeDao  extends BaseDao<Attribute,AttributeQuery> {
    /**
     * 获取所有的spu
     * @param query
     * @return
     */
     public List<Attribute> getAll(AttributeQuery query);


}
