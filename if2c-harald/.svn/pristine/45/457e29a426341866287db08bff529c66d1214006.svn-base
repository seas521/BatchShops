package com.if2c.harald.migration.category;

import java.util.ArrayList;
import java.util.List;

import com.if2c.harald.migration.category.model.GoodsAttribute;

//extends the jaxb generated model GoodsAttribute for detailed processing
public class LeanGoodsAttribute extends GoodsAttribute {
	List<LeanGoodsAttributeValue> values = new ArrayList<LeanGoodsAttributeValue>();

	public List<LeanGoodsAttributeValue> getValues() {
		return values;
	}

	public void setValues(List<LeanGoodsAttributeValue> values) {
		this.values = values;
	}

	public void addValue(LeanGoodsAttributeValue value) {
		values.add(value);
	}

	public LeanGoodsAttribute(GoodsAttribute ga) {
		setId(ga.getId());
		setName(ga.getName());
		setGoodsAttributeValue(ga.getGoodsAttributeValue());
		setHasImage(ga.isHasImage());
	}

	@Override
	public String toString() {
		return getId() + " " + getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((goodsAttributeValue == null) ? 0 : goodsAttributeValue
						.hashCode());
		result = prime * result + (hasImage ? 1231 : 1237);
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
		GoodsAttribute other = (GoodsAttribute) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		if (hasImage != other.isHasImage())
			return false;
		return true;
	}

}
