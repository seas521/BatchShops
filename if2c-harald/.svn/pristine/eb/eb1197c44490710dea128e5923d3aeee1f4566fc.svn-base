package com.if2c.harald.manager.impl;

import com.if2c.harald.beans.AttributeCategoryValueRelation;
import com.if2c.harald.beans.query.AttributeCategoryValueRelationQuery;
import com.if2c.harald.dao.AttributeCategoryValueRelationDao;
import com.if2c.harald.manager.AttributeCategoryValueRelationManager;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * User: Courser
 * Date: 14-5-2
 * Time: 上午11:45
 */
@Repository("attributeCategoryValueRelationManager")
public class AttributeCategoryValueRelationManagerImpl implements AttributeCategoryValueRelationManager {
    @Resource
    private AttributeCategoryValueRelationDao attributeCategoryValueRelationDao ;
    @Override
    public List<AttributeCategoryValueRelation> getAll(AttributeCategoryValueRelationQuery query) {
        return attributeCategoryValueRelationDao.getList(query);
    }
}
