package com.if2c.harald.beans.query;

/**
 * Description:
 * User: Courser
 * Date: 14-5-2
 * Time: 上午11:42
 */
public class AttributeCategoryValueRelationQuery extends BaseQuery {
    private Long categoryId ;
    public AttributeCategoryValueRelationQuery(){

    }
    public AttributeCategoryValueRelationQuery(Long categoryId){
        this.categoryId = categoryId ;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
