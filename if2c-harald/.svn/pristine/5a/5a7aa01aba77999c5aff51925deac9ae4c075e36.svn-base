package com.if2c.harald.service.impl;

import com.if2c.harald.beans.ExtAttrNameValue;
import com.if2c.harald.beans.query.ExtAttrNameValueQuery;
import com.if2c.harald.manager.ExtAttrNameValueManager;
import com.if2c.harald.service.ExtAttrNameValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:扩展属性服务层实现类
 * User: Courser
 * Date: 14-5-4
 * Time: 下午2:39
 */
@Service("extAttrNameValueService")
public class ExtAttrNameValueServiceImpl implements ExtAttrNameValueService {
    @Resource
    private ExtAttrNameValueManager extAttrNameValueManager ;
    /**
     * 通过源数据库seriesId获取扩展属性
     *
     * @param seriesId
     * @return
     */
    @Override
    public List<ExtAttrNameValue> qryByGoodsSeriesId(Long seriesId) {
        return extAttrNameValueManager.qryByGoodsSeriesId(seriesId);
    }

    /**
     * 查询目标库所有的扩展属性
     *
     * @param query
     * @return
     */
    @Override
    public List<ExtAttrNameValue> getAll(ExtAttrNameValueQuery query) {
        return extAttrNameValueManager.getAll(query);
    }
}
