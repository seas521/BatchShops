package com.if2c.harald.service.impl;

import com.if2c.harald.beans.Attribute;
import com.if2c.harald.beans.AttributeValue;
import com.if2c.harald.beans.query.AttributeQuery;
import com.if2c.harald.beans.query.AttributeValueQuery;
import com.if2c.harald.manager.AttributeManager;
import com.if2c.harald.manager.AttributeValueManager;
import com.if2c.harald.service.AttributeService;
import com.if2c.harald.service.AttributeValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * User: Courser
 * Date: 14-4-30
 * Time: 下午2:12
 */
@Service("attributeValueService")
public class AttributeValueServiceImpl implements AttributeValueService {
    @Resource
    private AttributeValueManager attributeValueManager ;
    /**
     * 获取所有的spu
     *
     * @param query
     * @return
     */
    @Override
    public List<AttributeValue> getAll(AttributeValueQuery query) {
        return attributeValueManager.getAll(query);
    }
}
