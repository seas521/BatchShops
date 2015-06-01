package com.if2c.harald.service;

import com.if2c.harald.beans.AttributeCategoryValueRelation;
import com.if2c.harald.beans.query.AttributeCategoryValueRelationQuery;

import java.util.List;

/**
 * Description:属性 类目值关系Service层
 * User: Courser
 * Date: 14-5-2
 * Time: 上午11:47
 */
public interface AttributeCategoryValueRelationService {
    /**
     * 获取所有的属性类目值关系
     * @param query
     * @return
     */
    public List<AttributeCategoryValueRelation> getAll(AttributeCategoryValueRelationQuery query) ;
}
