package com.if2c.harald.migration.category;

public class CategoryString {
	private String first;
	private String second;
	private String third;
	private String fouth;
	private String attribute;
	private String attributeValue;
	private boolean isCheck;
	private boolean haveImg;

	public boolean isHaveImg() {
		return haveImg;
	}

	public void setHaveImg(boolean haveImg) {
		this.haveImg = haveImg;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String getThird() {
		return third;
	}

	public void setThird(String third) {
		this.third = third;
	}

	public String getFouth() {
		return fouth;
	}

	public void setFouth(String fouth) {
		this.fouth = fouth;
	}

	public void put(String name) {
		if (name == null) {
			name = "null";
		}
		if (first == null || first == "") {
			first = name;
			return;
		}
		if (second == null || second == "") {
			second = name;
			return;
		}
		if (third == null || third == "") {
			third = name;
			return;
		}
		if (fouth == null || fouth == "") {
			fouth = name;
			return;
		}
		if (attribute == null || attribute == "") {
			attribute = name;
			if ("Color(颜色)".equals(attribute)) {
				haveImg = true;
				isCheck = true;
			}
			return;
		}
		if (attributeValue == null || attributeValue == "") {
			attributeValue = name;
			return;
		}
		if ("是".equals(name)) {
			isCheck = true;
			return;
		}
		if ("否".equals(name)) {
			isCheck = false;
			return;
		}
	}

	public void clear() {
		if ("null".equals(first)) {
			first = null;
		}
		if ("null".equals(second)) {
			second = null;
		}
		if ("null".equals(third)) {
			third = null;
		}
		if ("null".equals(fouth)) {
			fouth = null;
		}
		if ("null".equals(attribute)) {
			attribute = null;
		}
		if ("null".equals(attributeValue)) {
			attributeValue = null;
		}
	}

	public String getCheck(int level) {
		if (level == 2) {
			return first;
		} else if (level == 3) {
			return second;
		} else if (level == 4) {
			return third;
		}
		return null;
	}

	public String getNowLevel(int level) {
		if (level == 2) {
			return second;
		} else if (level == 3) {
			return third;
		} else if (level == 4) {
			return fouth;
		}
		return null;
	}

	@Override
	public String toString() {
		return first + "\t" + second + "\t" + third + "\t" + fouth + "\t"
				+ attribute + "\t" + attributeValue;
	}

}
