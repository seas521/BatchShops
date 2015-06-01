package com.if2c.harald.migration.category;

import java.util.ArrayList;
import java.util.List;

import com.if2c.harald.migration.category.model.ExtendedAttribute;

//extends the jaxb generated model ExtendedAttribute for detailed processing
public class LeanExtendedAttribute extends ExtendedAttribute {
	List<LeanExtendedAttributeValue> values = new ArrayList<LeanExtendedAttributeValue>();

	public List<LeanExtendedAttributeValue> getValues() {
		return values;
	}

	public void setValues(List<LeanExtendedAttributeValue> values) {
		this.values = values;
	}

	public void addValue(LeanExtendedAttributeValue value) {
		values.add(value);
	}

	public LeanExtendedAttribute(ExtendedAttribute ga) {
		setId(ga.getId());
		setName(ga.getName());
		setExtendedAttributeValue(ga.getExtendedAttributeValue());
		setIsCheck(ga.isIsCheck());
	}

	@Override
	public String toString() {
		return getId() + " " + getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isCheck ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtendedAttribute other = (ExtendedAttribute) obj;
		if (isCheck != other.isIsCheck())
			return false;
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}
}
