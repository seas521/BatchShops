/**
 * 
 */
package com.if2c.harald.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.if2c.harald.beans.Goods;
import com.if2c.harald.beans.query.GoodsQuery;
import com.if2c.harald.dao.GoodsDao;
import com.if2c.harald.manager.GoodsManager;

/**
 * @author 141806
 *
 */
@Repository("goodsManager")
public class GoodsManagerImpl implements GoodsManager {
	@Resource 
	private GoodsDao goodsDao ;
	

	/* (non-Javadoc)
	 * @see com.if2c.harald.manager.GoodsManager#getGoodsAll(com.if2c.harald.beans.query.GoodsQuery)
	 */
	@Override
	public List<Goods> getGoodsAll(GoodsQuery query) {
		
		return goodsDao.getList(query);
	}

}
