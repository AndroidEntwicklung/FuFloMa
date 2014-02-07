package com.example.fufloma;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	private int NUM_VIEWS = Integer.MAX_VALUE;
	private String imei;

	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int id) {
		ProductDetailFragment pdFrag = new ProductDetailFragment();
		
		pdFrag.setItemID(id);
		pdFrag.setIMEIID(imei);

		return pdFrag;
	}

	@Override
	public int getCount() {
		return NUM_VIEWS;
	}

	public void setItemCount(int maxItems) {
		NUM_VIEWS = maxItems;
	}

	public void setIMEI(String imei) {
		this.imei = imei;
	}
}
