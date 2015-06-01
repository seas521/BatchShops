package com.if2c.harald.manager.impl;

import com.if2c.harald.beans.Attribute;
import com.if2c.harald.beans.query.AttributeQuery;
import com.if2c.harald.dao.AttributeDao;
import com.if2c.harald.manager.AttributeManager;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: Courser
 * Date: 14-4-30
 * Time: 下午2:07
 */
@Repository("attributeManager")
public class AttributeManagerImpl implements AttributeManager {

    @Resource
    private AttributeDao attributeDao ;
    /**
     * 获取所有的spu
     *
     * @param query
     * @return
     */
    @Override
    public List<Attribute> getAll(AttributeQuery query) {
        return attributeDao.getAll(query) ;
    }
}
