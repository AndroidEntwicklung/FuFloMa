package com.example.fufloma;

import java.util.ArrayList;



public class DummyDatabase {
	
	private ArrayList<ProductListItem> dbcontent = new ArrayList<ProductListItem>();
	
	public DummyDatabase() {
		
		//ArrayList<ProductListItem> results = new ArrayList<ProductListItem>();
		ProductListItem productData = new ProductListItem();
		productData.setDescription("Gaaaaannnnnz toller kram und Zeugs hier, und nat¸rlich auch vieles mehr und so.");
		productData.setLocation("Gerwigstraﬂe 1, 78120 Furtwangen im Schwarzwald");
		productData.setPrice(1.30f);
		dbcontent.add(productData);
		
		productData = new ProductListItem();
		productData.setDescription("PlaySystem 5 wie neu!");
		productData.setLocation("Baumannstraﬂe 15, 78120 Furtwangen im Schwarzwald");
		productData.setPrice(10.77f);
		dbcontent.add(productData);

		productData = new ProductListItem();
		productData.setDescription("Comodore C4096, bester ever");
		productData.setLocation("Rabenstraﬂe 31, 78120 Furtwangen im Schwarzwald");
		productData.setPrice(666.77f);
		dbcontent.add(productData);

		productData = new ProductListItem();
		productData.setDescription("Bla");
		productData.setLocation("Allmendweg 2, Vˆhrenbach");
		productData.setPrice(667.77f);
		dbcontent.add(productData);

		productData = new ProductListItem();
		productData.setDescription("Schrottwagen");
		productData.setLocation("Roﬂbergstraﬂe 5, St. Georgen");
		productData.setPrice(392.74f);
		dbcontent.add(productData);
}
	
	public ArrayList<ProductListItem> getListData(String searchLocation, boolean checkBool) {
		ArrayList<ProductListItem> results = new ArrayList<ProductListItem>();
		
		for (ProductListItem item: dbcontent)
		{
			if (item.getLocation().contains(searchLocation) == checkBool)
				results.add(item);
		}
		
		return results;
	}

}
