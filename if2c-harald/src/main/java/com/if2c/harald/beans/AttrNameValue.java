package com.if2c.harald.beans;

import java.io.Serializable;

/**
 * Description:
 * User: Courser
 * Date: 14-5-2
 * Time: 上午11:28
 */
public class AttrNameValue implements Serializable {
    private String nameValue ;
    private String nameValueId ;

    public String getNameValue() {
        return nameValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }

    public String getNameValueId() {
        return nameValueId;
    }

    public void setNameValueId(String nameValueId) {
        this.nameValueId = nameValueId;
    }
}
