package com.if2c.harald.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.if2c.harald.beans.query.BaseQuery;
@Repository
public interface BaseDao<T,E extends BaseQuery> {
	
	 public <T> List<T> getList(E e);

     public void insert(T t);
	

}
