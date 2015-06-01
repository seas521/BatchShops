package com.if2c.harald.dao;

import com.if2c.harald.beans.GoodsInventory;
import com.if2c.harald.beans.query.BaseQuery;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * User: Courser
 * Date: 14-5-4
 * Time: 下午8:45
 */
@Repository
public interface GoodsInventoryDao extends BaseDao<GoodsInventory,BaseQuery> {
}
