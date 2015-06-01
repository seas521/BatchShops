package com.if2c.harald.service.impl;

import com.if2c.harald.beans.AttributeCategoryValueRelation;
import com.if2c.harald.beans.query.AttributeCategoryValueRelationQuery;
import com.if2c.harald.manager.AttributeCategoryValueRelationManager;
import com.if2c.harald.service.AttributeCategoryValueRelationService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:获取所有的属性类目值关系 service 层实现
 * User: Courser
 * Date: 14-5-2
 * Time: 上午11:47
 */
@Service("attributeCategoryValueRelationService")
public class AttributeCategoryValueRelationServiceImpl implements AttributeCategoryValueRelationService {
    /**
     * manager
     */
    @Resource
    private AttributeCategoryValueRelationManager attributeCategoryValueRelationManager ;

    /**
     * 获取所有的属性类目值关系
     * @param query
     * @return
     */
    @Override
    public List<AttributeCategoryValueRelation> getAll(AttributeCategoryValueRelationQuery query) {
        return attributeCategoryValueRelationManager.getAll(query);
    }
}
