package com.example.fufloma;

import java.text.DecimalFormat;

import android.graphics.Bitmap;

public class ProductListItem {
	private int shortDescriptionLength = 30;
	private int shortLocationLength = 25;

	private int id;
	private int sellerID;
	private String description;
	private String location;
	private double locLat;
	private double locLong;
	private Bitmap bm;
	private float price;
	private float curDistance;
	private StateEnum state;

	public int getId() {
		return id;
	}

	public void setId(int _id) {
		this.id = _id;
	}

	public int getSellerID() {
		return sellerID;
	}

	public void setSellerID(int sellerID) {
		this.sellerID = sellerID;
	}

	public String getDescription() {
		return description;
	}

	public String getShortDescription() {
		if (description.length() <= shortDescriptionLength)
			return description;
		else
			return description.substring(0, shortDescriptionLength) + " ...";
	}

	public void setDescription(String _description) {
		this.description = _description;
	}

	public String getLocation() {
		return location;
	}

	public String getPublicLocation() {
		return getPublicLocation(true);
	}

	public String getPublicLocation(boolean withDist) {
		String[] locationsplitter = location.split(",");
		String locResult = locationsplitter[locationsplitter.length - 1];

		if (locResult.length() <= shortLocationLength)
			locResult = locResult.substring(1, locResult.length());
		else
			locResult = locResult.substring(1, shortLocationLength) + "...";

		return (withDist && curDistance > 0) ? locResult + " (" + getDistance()
				+ ")" : locResult;
	}

	public void setLocation(String _location) {
		this.location = _location;
	}

	public void setLocLat(double locLat) {
		this.locLat = locLat;
	}

	public double getLocLong() {
		return locLong;
	}

	public void setLocLong(double locLong) {
		this.locLong = locLong;
	}

	public double getLocLat() {
		return locLat;
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

	public void setDistance(float _dist) {
		curDistance = _dist;
	}

	public String getDistance() {
		float km = curDistance / 1000;
		String df = new DecimalFormat((km < 10) ? "#0.#" : "#0").format(km);

		return df + " km";
	}

	public String getState() {
		return state.toString();
	}

	public void setState(StateEnum almnew) {
		this.state = almnew;
	}

	public String toString() {
		boolean hasBm = false;
		if (bm != null)
			hasBm = true;

		return "[ description=" + description + ", location=" + location
				+ ", price=" + price + ", hasBitmap=" + hasBm + "]";
	}
}