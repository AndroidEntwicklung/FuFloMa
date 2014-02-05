package com.example.fufloma;

public class DataProductItem {
	private String _id;
	private String _rev;
	private String _attachment;
	private String description;
	private double locLat;
	private double locLon;
	private String location;
	private float price;
	private String sellerId;
	private int state;
	
	
	public String getId() {
		return _id;
	}
	public void setId(String _id) {
		this._id = _id;
	}
	public String getRev() {
		return _rev;
	}
	public void setRev(String _rev) {
		this._rev = _rev;
	}
	public String getAttachment() {
		return _attachment;
	}
	public void setAttachment(String _attachment) {
		this._attachment = _attachment;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getLocLat() {
		return locLat;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
