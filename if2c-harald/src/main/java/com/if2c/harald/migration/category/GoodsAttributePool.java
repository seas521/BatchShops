package com.if2c.harald.migration.category;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.if2c.harald.migration.category.model.Category;
import com.if2c.harald.migration.category.model.GoodsAttribute;

public class GoodsAttributePool {
	// goods attribute set
	List<LeanGoodsAttribute> gaSet = new ArrayList<LeanGoodsAttribute>();

	public List<LeanGoodsAttribute> getGoodsAttributes() {
		return gaSet;
	}

	public Set<LeanGoodsAttributeValue> getGoodsAttributeValues() {
		return gavSet;
	}

	// goods attribute value set
	Set<LeanGoodsAttributeValue> gavSet = new LinkedHashSet<LeanGoodsAttributeValue>();

	public int getLatestGoodsAttributeValueID() {
		return gavSet.size() + 1;
	}

	// add the all the categories' goods attribute and value into this pool
	public void add(List<Category> list) {
		gaSet.clear();
		for (Category category : list) {
			addGoodsAttribute(category, gaSet);
		}
	}

	private void addGoodsAttribute(Category category,
			List<LeanGoodsAttribute> gaSet) {
		List<GoodsAttribute> galist = category.getGoodsAttribute();
		for (GoodsAttribute goodsAttribute : galist) {
			LeanGoodsAttribute lga = new LeanGoodsAttribute(goodsAttribute);
			if (!gaSet.contains(lga)) {
				// not exist, put it in
				gaSet.add(lga);
			} else {
				// if exists, compare, and put in the one with less id number
				int index = gaSet.indexOf(lga);
				LeanGoodsAttribute old = gaSet.get(index);
				if (lga.getId() < old.getId()) {
					// remove old one and add new one.
					gaSet.remove(index);
					gaSet.add(index, lga);
				}
			}

			// add goods attribute value
			if(lga.getGoodsAttributeValue()==null){
				continue;
			}
			String[] values = lga.getGoodsAttributeValue().split(",");
			for (String value : values) {
				LeanGoodsAttributeValue leanValue = new LeanGoodsAttributeValue();
				leanValue.setName(value);
				if (!gavSet.contains(leanValue)) {
					leanValue.setId(getLatestGoodsAttributeValueID());
					gavSet.add(leanValue);
				}
			}
		}

		List<Category> list = category.getCategory();
		if (list != null && !list.isEmpty()) {
			for (Category child : list) {
				addGoodsAttribute(child, gaSet);
			}
		}
	}

	public void printInfo() {
		for (LeanGoodsAttribute ga : gaSet) {
			System.out.println(ga);
		}

		for (LeanGoodsAttributeValue gav : gavSet) {
			System.out.println(gav);
		}
	}

	public GoodsAttribute returnCopy(GoodsAttribute goodsAttribute) {
		LeanGoodsAttribute lga = findGoodsAttribute(goodsAttribute);
		if(goodsAttribute.getGoodsAttributeValue()==null){
			System.err.println(goodsAttribute.getName()
					+ " 's value is null");
			return lga;
		}
		
		String[] values = goodsAttribute.getGoodsAttributeValue().split(",");

		List<LeanGoodsAttributeValue> list = new ArrayList<LeanGoodsAttributeValue>();
		for (String value : values) {
			LeanGoodsAttributeValue valueObject = findGoodsAttributeValue(value);
			assert valueObject != null;
			list.add(valueObject);
		}
		lga.setValues(list);
		lga.setGoodsAttributeValue(goodsAttribute.getGoodsAttributeValue());
		return lga;
	}

	private LeanGoodsAttributeValue findGoodsAttributeValue(String value) {
		for (LeanGoodsAttributeValue lav : gavSet) {
			if (lav.getName().equals(value)) {
				return lav;
			}
		}
		return null;
	}

	public LeanGoodsAttribute findGoodsAttribute(GoodsAttribute goodsAttribute) {
		LeanGoodsAttribute one=new LeanGoodsAttribute(goodsAttribute);
		int index = gaSet.indexOf(one);
		if(index==-1){
			System.err.println(goodsAttribute.getName());
		}
		GoodsAttribute original = gaSet.get(index);
		assert original != null;
		return new LeanGoodsAttribute(original);
	}

}
