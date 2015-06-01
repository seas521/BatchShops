package com.if2c.harald.dao;

import com.if2c.harald.beans.Attribute;
import com.if2c.harald.beans.AttributeCategoryValueRelation;
import com.if2c.harald.beans.query.AttributeCategoryValueRelationQuery;
import com.if2c.harald.beans.query.AttributeQuery;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * User: Courser
 * Date: 14-5-2
 * Time: 上午11:41
 */
@Repository
public interface AttributeCategoryValueRelationDao extends BaseDao<AttributeCategoryValueRelation,AttributeCategoryValueRelationQuery>  {
}
