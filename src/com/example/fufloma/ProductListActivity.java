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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProductListActivity extends Activity {
	SharedPreferences sharedPref = null;
	TextView tv_lon = null;
	TextView tv_lat = null;
	TextView tv_adr = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_activity);
		
		tv_lon = (TextView) findViewById(R.id.textView1);
		tv_lat = (TextView) findViewById(R.id.textView2);
		tv_adr = (TextView) findViewById(R.id.textView3);
		
		sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		tv_lon.setText("Lon: " + String.valueOf(sharedPref.getFloat("lon", 0.0f)));
		tv_lat.setText("Lat: " + String.valueOf(sharedPref.getFloat("lat", 0.0f)));
		
		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		
        List<Address> addresses;
        
		try {
			addresses = geoCoder.getFromLocation(sharedPref.getFloat("lat", 0.0f) , sharedPref.getFloat("lon", 0.0f), 1);

        String strCompleteAddress= "";
        //if (addresses.size() > 0)
        //{
        for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++)
        	strCompleteAddress+= addresses.get(0).getAddressLine(i) + "\n";
       // }
        //Log.i("MyLocTAG => ", strCompleteAddress);
        tv_adr.setText(strCompleteAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		DummyDatabase localDB = new DummyDatabase();
		ArrayList<ProductListItem> productList = localDB.getListData();
		final ListView lv1 = (ListView) findViewById(R.id.product_list);
		lv1.setAdapter(new ProductListAdapter(this, productList));
		
	}
	

}

