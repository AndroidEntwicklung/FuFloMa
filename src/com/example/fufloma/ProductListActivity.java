package com.example.fufloma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProductListActivity extends Activity {
	private SharedPreferences sharedPref;
	private TextView tv_mainloc;
	private float curLat;
	private float curLon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_activity);

		tv_mainloc = (TextView) findViewById(R.id.MainLocation);

		sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		curLat = sharedPref.getFloat("lat", 0.0f);
		curLon = sharedPref.getFloat("lon", 0.0f);

		DummyDatabase localDB = new DummyDatabase();

		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());

		Location locationA = new Location("A");

		locationA.setLatitude(curLat);
		locationA.setLongitude(curLon);

		List<Address> addresses;

		// Log.i("FuFloMa","Blub1");
		try {
			// Location locationA;
			addresses = geoCoder.getFromLocation(curLat, curLon, 1);
			tv_mainloc.setText(addresses.get(0).getLocality());
			sharedPref.edit().putString("location_name", (String) tv_mainloc.getText()).commit();

			final ListView lv1 = (ListView) findViewById(R.id.product_list1);
			final ListView lv2 = (ListView) findViewById(R.id.product_list2);

			ArrayList<ProductListItem> productList = localDB
					.getProductListData(addresses.get(0).getLocality(), true);
			for (ProductListItem item : productList) {
				Location locationB = new Location("B");
				locationB.setLatitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLatitude());
				locationB.setLongitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLongitude());

				item.setDistance(locationA.distanceTo(locationB));
			}

			lv1.setAdapter(new ProductListAdapter(this, productList));
			lv1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					Object o = lv1.getItemAtPosition(position);
					ProductListItem productData = (ProductListItem) o;

					Intent myIntent = new Intent(v.getContext(),
							ProductDetailActivity.class);
					myIntent.putExtra("ID", productData.getId());
					startActivity(myIntent);
				}
			});

			productList = localDB.getProductListData(addresses.get(0)
					.getLocality(), false);
			for (ProductListItem item : productList) {
				Location locationB = new Location("B");
				locationB.setLatitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLatitude());
				locationB.setLongitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLongitude());

				item.setDistance(locationA.distanceTo(locationB));
			}

			lv2.setAdapter(new ProductListAdapter(this, productList));
			lv2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					Object o = lv2.getItemAtPosition(position);
					ProductListItem productData = (ProductListItem) o;

					Intent myIntent = new Intent(v.getContext(),
							ProductDetailActivity.class);
					myIntent.putExtra("ID", productData.getId());
					startActivity(myIntent);
				}
			});

			Log.i("FuFloMa", "Blub");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("FuFloMa", "Bla");
		}
	}
}
