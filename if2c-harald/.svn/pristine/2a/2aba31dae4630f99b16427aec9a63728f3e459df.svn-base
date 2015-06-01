package com.if2c.harald.migration.category;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.if2c.harald.migration.category.model.Category;
import com.if2c.harald.migration.category.model.ExtendedAttribute;

public class ExtendedAttributePool {
	// goods attribute set
	List<LeanExtendedAttribute> gaSet = new ArrayList<LeanExtendedAttribute>();

	public List<LeanExtendedAttribute> getExtendedAttribute() {
		return gaSet;
	}

	public Set<LeanExtendedAttributeValue> getExtendedAttributeValue() {
		return gavSet;
	}

	// goods attribute value set
	Set<LeanExtendedAttributeValue> gavSet = new LinkedHashSet<LeanExtendedAttributeValue>();

	public int getLatestExtendedAttributeValueID() {
		return gavSet.size() + 1;
	}

	// add the all the categories' goods attribute and value into this pool
	public void add(List<Category> list) {
		gaSet.clear();
		for (Category category : list) {
			addExtendedAttribute(category, gaSet);
		}
	}

	private void addExtendedAttribute(Category category,
			List<LeanExtendedAttribute> gaSet) {
		List<ExtendedAttribute> galist = category.getExtendedAttribute();
		for (ExtendedAttribute ExtendedAttribute : galist) {
			LeanExtendedAttribute lga = new LeanExtendedAttribute(
					ExtendedAttribute);
			if (!gaSet.contains(lga)) {
				// not exist, put it in
				gaSet.add(lga);
			} else {
				// if exists, compare, and put in the one with less id number
				int index = gaSet.indexOf(lga);
				LeanExtendedAttribute old = gaSet.get(index);
				if (lga.getId() < old.getId()) {
					// remove old one and add new one.
					gaSet.remove(index);
					gaSet.add(index, lga);
				}
			}

			// add extended attribute value
			String[] values = null;
			if (lga.getExtendedAttributeValue() == null) {
				System.err.println(category.getName() + " --> " + lga.getName()
						+ " 's extended attribute is null");
			} else {
				values = lga.getExtendedAttributeValue().split(",");

				for (String value : values) {
					LeanExtendedAttributeValue leanValue = new LeanExtendedAttributeValue();
					leanValue.setName(value);
					if (!gavSet.contains(leanValue)) {
						leanValue.setId(getLatestExtendedAttributeValueID());
						gavSet.add(leanValue);
					}
				}
			}

		}

		List<Category> list = category.getCategory();
		for (Category child : list) {
			addExtendedAttribute(child, gaSet);
		}
	}

	public void printInfo() {
		for (LeanExtendedAttribute ga : gaSet) {
			System.out.println(ga);
		}

		for (LeanExtendedAttributeValue gav : gavSet) {
			System.out.println(gav);
		}
	}

	public ExtendedAttribute returnCopy(ExtendedAttribute extendedAttribute) {
		LeanExtendedAttribute lga = findExtendedAttribute(extendedAttribute);

		if (extendedAttribute.getExtendedAttributeValue() == null) {
			System.err.println(extendedAttribute.getName()
					+ " 's value is null");
			return lga;
		}

		String[] values = extendedAttribute.getExtendedAttributeValue().split(
				",");

		List<LeanExtendedAttributeValue> list = new ArrayList<LeanExtendedAttributeValue>();
		for (String value : values) {
			LeanExtendedAttributeValue valueObject = findExtendedAttributeValue(value);
			assert valueObject != null;
			list.add(valueObject);
		}
		lga.setValues(list);
		lga.setExtendedAttributeValue(extendedAttribute.getExtendedAttributeValue());
		return lga;
	}

	private LeanExtendedAttributeValue findExtendedAttributeValue(String value) {
		for (LeanExtendedAttributeValue lav : gavSet) {
			if (lav.getName().equals(value)) {
				return lav;
			}
		}
		return null;
	}

	public LeanExtendedAttribute findExtendedAttribute(
			ExtendedAttribute ExtendedAttribute) {
		LeanExtendedAttribute one = new LeanExtendedAttribute(ExtendedAttribute);
		int index = gaSet.indexOf(one);
		if (index == -1) {
			System.out.println(ExtendedAttribute.getName());
		}
		ExtendedAttribute original = gaSet.get(index);
		assert original != null;
		return new LeanExtendedAttribute(original);
	}

}
