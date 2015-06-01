package com.if2c.harald.service.impl;

import com.if2c.harald.beans.Attribute;
import com.if2c.harald.beans.query.AttributeQuery;
import com.if2c.harald.manager.AttributeManager;
import com.if2c.harald.service.AttributeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: Courser
 * Date: 14-4-30
 * Time: 下午2:12
 */
@Service("attributeService")
public class AttributeServiceImpl implements AttributeService {
    @Resource
    private AttributeManager attributeManager ;
    /**
     * 获取所有的spu
     *
     * @param query
     * @return
     */
    @Override
    public List<Attribute> getAll(AttributeQuery query) {
        return attributeManager.getAll(query);
    }
}
