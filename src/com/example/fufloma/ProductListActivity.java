package com.example.fufloma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ProductListActivity extends Activity {
	SharedPreferences sharedPref;
	TextView tv_lon;
	TextView tv_lat;
	TextView tv_mainloc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_activity);
		
		tv_lon = (TextView) findViewById(R.id.textView1);
		tv_lat = (TextView) findViewById(R.id.textView2);
		tv_mainloc = (TextView) findViewById(R.id.MainLocation);
		
		sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		tv_lon.setText("Lon: " + String.valueOf(sharedPref.getFloat("lon", 0.0f)));
		tv_lat.setText("Lat: " + String.valueOf(sharedPref.getFloat("lat", 0.0f)));

		DummyDatabase localDB = new DummyDatabase();
		
		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		
        List<Address> addresses;
        
		try {
		
			addresses = geoCoder.getFromLocation(sharedPref.getFloat("lat", 0.0f) , sharedPref.getFloat("lon", 0.0f), 1);
			tv_mainloc.setText(addresses.get(0).getLocality());

			final ListView lv1 = (ListView) findViewById(R.id.product_list1);
			final ListView lv2 = (ListView) findViewById(R.id.product_list2);

			ArrayList<ProductListItem> productList = localDB.getListData(addresses.get(0).getLocality(), true);
			lv1.setAdapter(new ProductListAdapter(this, productList));

			productList = localDB.getListData(addresses.get(0).getLocality(), false);
			lv2.setAdapter(new ProductListAdapter(this, productList));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	

}

