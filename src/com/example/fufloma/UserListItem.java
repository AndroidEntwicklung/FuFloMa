package com.example.fufloma;

public class UserListItem {
	private int id;
	private String phoneNr;
	
	private int sellCt;
	private int buyCt; 
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
}
