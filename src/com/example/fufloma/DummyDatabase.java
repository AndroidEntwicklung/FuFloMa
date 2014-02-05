package com.example.fufloma;

import java.util.ArrayList;
import android.content.res.Resources;



public class DummyDatabase {
	
	private ArrayList<ProductListItem> productDB = new ArrayList<ProductListItem>();
	private ArrayList<UserListItem> userDB = new ArrayList<UserListItem>();
	
	public DummyDatabase() {
		// USER DATABASE
		UserListItem userData = new UserListItem();
		userData.setId(0);
		userData.setPhoneNr("0157-7663258469");
		userData.setBuyCt(10);
		userData.setSellCt(5);
		userDB.add(userData);
		
		// PRODUCT DATABASE
		ProductListItem productData = new ProductListItem();
		productData.setId(0);
		productData.setSellerID(0);
		productData.setDescription("Gaaaaannnnnz toller kram und Zeugs hier, und natürlich auch vieles mehr und so. Einfach mal in dieses Produkt reinschauen, da gibt es viel zu erleben :) Echt jetzt! Wenn Sie Fragen haben, können Sie mir auch eine Nachricht schicken oder einfach anrufen. Das ist kein Problem, ich werde schnellstens antworten und zwar innerhalb von wenigen Wochen.");
		productData.setLocation("Gerwigstraße 1, 78120 Furtwangen im Schwarzwald");
		productData.setLocLat(48.0501);
		productData.setLocLong(8.2014);
		productData.setPrice(1.30f);
		productData.setState(StateEnum.ALMNEW);
		productDB.add(productData);
		
		productData = new ProductListItem();
		productData.setId(1);
		productData.setSellerID(0);
		productData.setDescription("PlaySystem 5 wie neu!");
		productData.setLocation("Baumannstraße 15, 78120 Furtwangen im Schwarzwald");
		productData.setPrice(10.77f);
		productData.setState(StateEnum.ALMNEW);
		productDB.add(productData);

		productData = new ProductListItem();
		productData.setId(2);
		productData.setSellerID(0);
		productData.setDescription("Comodore C4096, bester ever");
		productData.setLocation("Rabenstraße 31, 78120 Furtwangen im Schwarzwald");
		productData.setPrice(666.77f);
		productData.setState(StateEnum.ALMNEW);
		productDB.add(productData);

		productData = new ProductListItem();
		productData.setId(3);
		productData.setSellerID(0);
		productData.setDescription("Bla");
		productData.setLocation("Allmendweg 2, 78147 Vöhrenbach");
		productData.setPrice(667.77f);
		productData.setState(StateEnum.ALMNEW);
		productDB.add(productData);

		productData = new ProductListItem();
		productData.setId(4);
		productData.setSellerID(0);
		productData.setDescription("Schrottwagen");
		productData.setLocation("Roßbergstraße 5, St. Georgen");
		productData.setPrice(392.74f);
		productData.setState(StateEnum.ALMNEW);
		productDB.add(productData);
}
	
	public ArrayList<ProductListItem> getProductListData() {
		return productDB;
	}
	
	public int getProductsCount(String searchLocation) {
		int count = 0;
		
		for (ProductListItem item: productDB)
			if (item.getLocation().contains(searchLocation))
				count++;
		
		return count;
	}
	
	public int getCount()
	{
		return productDB.size();
	}

	public ProductListItem getProductItem(int id) {
		return productDB.get(id);
	}
	
	public UserListItem getUserItem(int id) {
		return userDB.get(id);
	}
}
