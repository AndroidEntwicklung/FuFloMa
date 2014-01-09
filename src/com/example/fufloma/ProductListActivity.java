package com.example.fufloma;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class ProductListActivity extends Activity {
	SharedPreferences sharedPref = null;
	TextView tv_lon = null;
	TextView tv_lat = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_activity);
		
		tv_lon = (TextView) findViewById(R.id.textView1);
		tv_lat = (TextView) findViewById(R.id.textView2);
		
		sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		tv_lon.setText("Lon: " + String.valueOf(sharedPref.getFloat("lon", 0.0f)));
		tv_lat.setText("Lat: " + String.valueOf(sharedPref.getFloat("lat", 0.0f)));
	}
	

}

