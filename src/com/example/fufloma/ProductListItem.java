package com.example.fufloma;

import java.text.DecimalFormat;

public class ProductListItem {
	private int shortDescriptionLength = 30;
	private int shortLocationLength = 25;
	private String baseURL = "http://141.28.122.106:5984/fufloma/";

	private String id;
	private String description;
	private String location;
	private String _attachment;
	private float price;
	private float curDistance;
	private String state;
	private String _rev;
	private String sellerId;
	private String phoneNumber;
	private double locLon;
	private double locLat;

	/* COPY! */
	public ProductListItem clone()
	{
		ProductListItem aClone = new ProductListItem();
		aClone.id = id;
		aClone.description = description;
		aClone.location = location;
		aClone._attachment = _attachment;
		aClone.price = price;
		aClone.curDistance = curDistance;
		aClone.state = state;
		aClone._rev = _rev;
		aClone.sellerId = sellerId;
		aClone.phoneNumber = phoneNumber;
		return aClone;
	}
	
	public String toStringFull() {
		return "_id=[" + id + "], _rev=[" + _rev + "], description=[" + description + "], location=[" + location + "], price=[" + price + "], _attachment=[" + _attachment + "], state=[" + state +"], sellerId=[" + sellerId + "], phoneNumber=[" + phoneNumber + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
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
			locResult = locResult.substring(0, locResult.length());
		else
			locResult = locResult.substring(0, shortLocationLength) + "...";

		locResult = locResult.trim();
		return (withDist && curDistance > 0) ? locResult + " (" + getDistance()
				+ ")" : locResult;
	}

	public void setLocation(String _location) {
		this.location = _location;
	}

	public String getAttachment() {
		return _attachment;
	}

	public void setAttachment(String _attachment) {
		this._attachment = _attachment;
	}
	
	public String getFullAttachmentURL() {
		return baseURL + id + "/" + _attachment;
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
	
	public Float getDistanceVal() {
		return curDistance;
	}

	public String getState() {
		return state.toString();
	}

	public void setState(String state) {
		this.state = state;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public void setLocLat(double locLat) {
		this.locLat = locLat;
	}

}