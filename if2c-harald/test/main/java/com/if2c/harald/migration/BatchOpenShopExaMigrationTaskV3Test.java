package com.if2c.harald.migration;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BatchOpenShopExaMigrationTaskV3Test {
	BatchOpenShopExaMigrationTaskV3 v3 = null;

	@Before
	public void setUp() throws Exception {
		try {
			v3 = new BatchOpenShopExaMigrationTaskV3();
		} catch (Exception e) {

		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculateCategoryLevel2() {
		String input = "[214001]";
		List<String> result = v3.calculateCategoryLevel2(input);
		assertEquals("214001", result.get(0));

		input = "[214001][214003]";
		result = v3.calculateCategoryLevel2(input);
		assertEquals("214001", result.get(0));
		assertEquals("214003", result.get(1));

		input = "[214001,214004]";
		result = v3.calculateCategoryLevel2(input);
		assertEquals("214001", result.get(0));
		assertEquals("214002", result.get(1));
		assertEquals("214003", result.get(2));
		assertEquals("214004", result.get(3));

		input = "[214001,214004][214008]";
		result = v3.calculateCategoryLevel2(input);
		assertEquals("214001", result.get(0));
		assertEquals("214002", result.get(1));
		assertEquals("214003", result.get(2));
		assertEquals("214004", result.get(3));
		assertEquals("214008", result.get(4));

		input = "[214008][214001,214004]";
		result = v3.calculateCategoryLevel2(input);
		assertEquals("214008", result.get(0));
		assertEquals("214001", result.get(1));
		assertEquals("214002", result.get(2));
		assertEquals("214003", result.get(3));
		assertEquals("214004", result.get(4));

		input = "[214001,214004][214008,214010]";
		result = v3.calculateCategoryLevel2(input);
		assertEquals(7, result.size());
		assertEquals("214001", result.get(0));
		assertEquals("214002", result.get(1));
		assertEquals("214003", result.get(2));
		assertEquals("214004", result.get(3));
		assertEquals("214008", result.get(4));
		assertEquals("214009", result.get(5));
		assertEquals("214010", result.get(6));

		input = "[214001,214004][214003,214005]";
		result = v3.calculateCategoryLevel2(input);
		assertEquals(5, result.size());
		assertEquals("214001", result.get(0));
		assertEquals("214002", result.get(1));
		assertEquals("214003", result.get(2));
		assertEquals("214004", result.get(3));
		assertEquals("214005", result.get(4));

	}
}
