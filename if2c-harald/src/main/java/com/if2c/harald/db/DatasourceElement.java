package com.if2c.harald.db;

import java.util.Map;

public class DatasourceElement {
	private String name;
	private Map<String, String> proMap;

	public Map<String, String> getProMap() {
		return proMap;
	}

	public void setProMap(Map<String, String> proMap) {
		this.proMap = proMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
