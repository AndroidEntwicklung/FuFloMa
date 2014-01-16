package com.example.fufloma;

import android.graphics.Bitmap;

public class ProductListItem {
	private int shortDescriptionLength = 30;
	private String description;
	private String location;
	private Bitmap bm;
	private float price;

	public String getDescription() {
		return description;
	}
	
	public String getShortDescription() {
		if (description.length() <= shortDescriptionLength)
			return description;
		else
			return description.substring(0,30) + " ...";
	}

	public void setDescription(String _description) {
		this.description = _description;
	}

	public String getLocation() {
		return location;
	}

	public String getPublicLocation() {
		String[] locationsplitter = location.split(",");
		return locationsplitter[locationsplitter.length-1];
	}
	public void setLocation(String _location) {
		this.location = _location;
	}

	public Bitmap getBitmap() {
		return bm;
	}

	public void setBitmap(Bitmap _bm) {
		this.bm = _bm;
	}
	
	public void setPrice(float _price) {
		this.price = _price;
	}
	
	public float getPrice() {
		return price;
	}
	
	public String toString() {
		boolean hasBm = false;
		if (bm != null)
			hasBm = true;
			
		return "[ description=" + description + ", location=" + location + ", price="
				+ price + ", hasBitmap=" + hasBm + "]";
	}
}
