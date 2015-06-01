package com.if2c.harald.Enums;

import org.apache.commons.lang3.StringUtils;

public enum DateEnums {
    January("01"),
    February ("02"),
    March ("03"),
    April ("04"),
    May ("05"),
    June("06"),
    July("07"),
    August("08"),
    September("09"),
    October("10"),
    November("11"),
    December("12"), 
;
    private String key;
    
    DateEnums(String key){
        this.key=key;
    }
     
    public String getKey() {
        return key;
    }

    public static String getEnumByKey(String key){
        for(DateEnums de:DateEnums.values()){
            if(StringUtils.equals(de.getKey(), key)){
                return de.name();
            }
        }
        return null;
    }
}
