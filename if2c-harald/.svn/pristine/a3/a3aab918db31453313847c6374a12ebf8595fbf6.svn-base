/**
 * 
 */
package com.if2c.harald.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.if2c.harald.beans.Goods;
import com.if2c.harald.beans.query.GoodsQuery;
import com.if2c.harald.manager.GoodsManager;
import com.if2c.harald.service.GoodsService;

/**
 * @author 141806
 *
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {
	@Resource 
	private GoodsManager goodsManager ;

	/* (non-Javadoc)
	 * @see com.if2c.harald.service.GoodsService#getAll(com.if2c.harald.beans.query.GoodsQuery)
	 */
	@Override
	public List<Goods> getAll(GoodsQuery query) {
		
		return goodsManager.getGoodsAll(query);
	}

}
