package com.if2c.harald.migration.i18n;

import java.io.Serializable;

public class RedisBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8102676703878937005L;

	private String table;
	private String lang;
	private String id;
	private String translation;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	@Override
	public String toString() {
		return "RedisBean [table=" + table + ", lang=" + lang + ", id=" + id
				+ ", translation=" + translation + "]";
	}
	
}
