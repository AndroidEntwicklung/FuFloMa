package com.example.fufloma;

public class UserListItem {
	private String _id;
	private String phoneNr;
	private String _rev;
	private int sellCt;
	private int buyCt;

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public String getPhoneNr() {
		return phoneNr;
	}

	public void setPhoneNr(String phoneNr) {
		this.phoneNr = phoneNr;
	}

	public int getBuyCt() {
		return buyCt;
	}

	public void setBuyCt(int buyCt) {
		this.buyCt = buyCt;
	}

	public int getSellCt() {
		return sellCt;
	}

	public void setSellCt(int sellCt) {
		this.sellCt = sellCt;
	}

	public String getRev() {
		return _rev;
	}

	public void setRev(String _rev) {
		this._rev = _rev;
	}
}
