package com.if2c.harald.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DBConf {
	String url();

	String userName();

	String password();
}
