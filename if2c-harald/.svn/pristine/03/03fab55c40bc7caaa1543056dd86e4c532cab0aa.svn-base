package com.if2c.harald.manager.impl;

import com.if2c.harald.beans.Attribute;
import com.if2c.harald.beans.AttributeValue;
import com.if2c.harald.beans.query.AttributeQuery;
import com.if2c.harald.beans.query.AttributeValueQuery;
import com.if2c.harald.dao.AttributeDao;
import com.if2c.harald.dao.AttributeValueDao;
import com.if2c.harald.manager.AttributeManager;
import com.if2c.harald.manager.AttributeValueManager;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * User: Courser
 * Date: 14-4-30
 * Time: 下午2:07
 */
@Repository("attributeValueManager")
public class AttributeValueManagerImpl implements AttributeValueManager {

    @Resource
    private AttributeValueDao attributeValueDao ;
    /**
     * 获取所有的spu
     *
     * @param query
     * @return
     */
    @Override
    public List<AttributeValue> getAll(AttributeValueQuery query) {
        return attributeValueDao.getAll(query) ;
    }
}
