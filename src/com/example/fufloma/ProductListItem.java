package com.example.fufloma;

import java.text.DecimalFormat;

public class ProductListItem {
	private int shortDescriptionLength = 30;
	private int shortLocationLength = 25;

	private String id;
	private String sellerID;
	private String description;
	private String location;
	private double locLat;
	private double locLon;
	private String _attachment;
	private float price;
	private float curDistance;
	private StateEnum state;
	private String _rev;
	private String sellerId;

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getSellerID() {
		return sellerID;
	}

	public void setSellerID(String sellerID) {
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

	public double getLocLon() {
		return locLon;
	}

	public void setLocLon(double locLon) {
		this.locLon = locLon;
	}

	public double getLocLat() {
		return locLat;
	}

	public String getAttachment() {
		return _attachment;
	}

	public void setAttachment(String _attachment) {
		this._attachment = _attachment;
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
		String df = new DecimalFormat("#0").format(km);

		return df + " km";
	}

	public String getState() {
		return state.toString();
	}

	public void setState(StateEnum almnew) {
		this.state = almnew;
	}

	public String toString() {

		return "[ description=" + description + ", location=" + location
				+ ", price=" + price + "]";
	}

	public String getRev() {
		return _rev;
	}

	public void setRev(String _rev) {
		this._rev = _rev;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

}