package com.if2c.harald.dao;

import org.springframework.stereotype.Repository;

import com.if2c.harald.beans.Goods;
import com.if2c.harald.beans.query.BaseQuery;

import java.util.List;

@Repository
public interface GoodsDao  extends BaseDao<Goods, BaseQuery>{

    /**
     * 批量保存
     * @param list
     */
    public void insertBatch(List<Goods> list);
}
