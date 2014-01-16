package com.example.fufloma;

import java.util.ArrayList;

public class DummyDatabase {
	public ArrayList<ProductListItem> getListData() {
		ArrayList<ProductListItem> zwresults = new ArrayList<ProductListItem>();
		//ArrayList<ProductListItem> results = new ArrayList<ProductListItem>();
		ProductListItem productData = new ProductListItem();
		productData.setDescription("Gaaaaannnnnz toller kram und Zeugs hier, und natürlich auch vieles mehr und so.");
		productData.setLocation("Robert-Gerwig-Platz 1, 78120 Furtwangen im Schwarzwald");
		productData.setPrice(1.30f);
		zwresults.add(productData);
		
		productData = new ProductListItem();
		productData.setDescription("PlaySystem 5 wie neu!");
		productData.setLocation("WG 3, 78120 Furtwangen im Schwarzwald");
		productData.setPrice(10.77f);
		zwresults.add(productData);

		productData = new ProductListItem();
		productData.setDescription("Comodore C4096, bester ever");
		productData.setLocation("Blub,Daheim in, 78120 Furtwangen im Schwarzwald");
		productData.setPrice(666.77f);
		zwresults.add(productData);
		
		return zwresults;
	}
}
