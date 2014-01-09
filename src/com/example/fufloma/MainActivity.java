package com.example.fufloma;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tv_network = null;
	TextView tv_gps = null;
	ImageView iv_hfu_spin = null;
	AnimationDrawable frameAnimation = null;
	boolean networkStatus = false;
	LocationManager locMgr = null;
	SharedPreferences sharedPref = null;
	Intent nextIntent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		tv_network = (TextView) findViewById(R.id.NetworkSearchInfo);
		tv_gps = (TextView) findViewById(R.id.GpsSearchInfo);

		nextIntent = new Intent(MainActivity.this, ProductListActivity.class);
		
		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				onLocationChange);

		sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		iv_hfu_spin = (ImageView)findViewById(R.id.hfuRotator);
		
		iv_hfu_spin.setBackgroundResource(R.drawable.hfu_spin_animation);
		frameAnimation = (AnimationDrawable) iv_hfu_spin.getBackground();
		frameAnimation.start();

		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
		
		tv_gps.setText("Checking for Location ...");
		
		tv_network.setText("Checking for network connection ...");
		tv_network.setTextColor(Color.BLACK);
		
		if (isNetworkAvailable())
		{
			tv_network.setText("Network found!");
			networkStatus = true;
		}
		else
		{
			tv_network.setText("No network :(");
			tv_network.setTextColor(Color.RED);
			networkStatus = false;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		locMgr.removeUpdates(onLocationChange);
		frameAnimation.stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		return (info != null);
	}

	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged(Location fix) {
			
			sharedPref.edit().putFloat("lat", (float) fix.getLatitude()).commit();
			sharedPref.edit().putFloat("lon", (float) fix.getLongitude()).commit();
			
			locMgr.removeUpdates(onLocationChange);
			frameAnimation.stop();
			tv_gps.setText("Location found!");
			if (networkStatus)
				startActivity(nextIntent);
		}

		public void onProviderDisabled(String provider) {
			// required for interface, not used
		}

		public void onProviderEnabled(String provider) {
			// required for interface, not used
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// required for interface, not used
		}
	};

}
