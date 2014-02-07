package com.example.fufloma;

import java.util.Comparator;

public class DistanceSort implements Comparator<ProductListItem> {
	// sorts products by distance and name
	public int compare(ProductListItem left, ProductListItem right) {
		int test = left.getDistanceVal().compareTo(right.getDistanceVal());

		if (test == 0)
			test = left.getDescription().compareTo(right.getDescription());

		return test;
	}
}