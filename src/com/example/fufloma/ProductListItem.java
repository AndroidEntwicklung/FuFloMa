package com.example.fufloma;

import android.graphics.Bitmap;

public class ProductListItem {
	private String description;
	private String location;
	private Bitmap bm;
	private String price;

	public String getDescription() {
		return description;
	}

	public void setDescription(String _description) {
		this.description = _description;
	}

	public String getLocation() {
		return location;
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

	public String getType() {
		return type;
	}

	public void setType(String _type) {
		this.type = _type;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int _rating) {
		this.rating = _rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String _review) {
		this.review = _review;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String _distance) {
		this.distance = _distance;
	}

	public String getIconpath() {
		return iconpath;
	}

	public void setIconpath(String _iconpath) {
		this.iconpath = _iconpath;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String _imagepath) {
		this.imagepath = _imagepath;
	}

	@Override
	public String toString() {
		return "[ name=" + name + ", street=" + street + ", distance="
				+ distance + ", iconpath=" + iconpath + "]";
	}

}
