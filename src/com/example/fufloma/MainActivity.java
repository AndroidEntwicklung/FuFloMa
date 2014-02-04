package com.example.fufloma;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
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
import android.view.WindowManager;
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
		
		getActionBar().hide();		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		tv_network = (TextView) findViewById(R.id.NetworkSearchInfo);
		tv_gps = (TextView) findViewById(R.id.GpsSearchInfo);

		nextIntent = new Intent(MainActivity.this, ProductListActivity.class);
		nextIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				onLocationChange);

		sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		iv_hfu_spin = (ImageView) findViewById(R.id.hfuRotator);

		iv_hfu_spin.setBackgroundResource(R.drawable.hfu_spin_animation);
		frameAnimation = (AnimationDrawable) iv_hfu_spin.getBackground();
		frameAnimation.start();

		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				onLocationChange);

		tv_gps.setText("Frage aktuelle Position ab...");

		tv_network.setText("Suche nach Netzwerkverbindung...");
		tv_network.setTextColor(Color.BLACK);

		if (isNetworkAvailable()) {
			tv_network.setText("Netzwerkverbindung gefunden!");
			networkStatus = true;
		} else {
			tv_network.setText("Keine Netzwerkverbindung :(");
			tv_network.setTextColor(Color.RED);
			networkStatus = false;
		}

		//saveLocation(48.050278f, 8.209167f);
	}

	@Override
	protected void onPause() {
		super.onPause();

		locMgr.removeUpdates(onLocationChange);
		frameAnimation.stop();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		return (info != null);
	}

	private void saveLocation(double lat, double lon) {
		List<Address> addresses = null;

		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		
		try {
			addresses = geoCoder.getFromLocation(lat, lon, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String locName = (String) addresses.get(0).getLocality();
		sharedPref.edit().putString("locName", locName).commit();
		
		try {
			addresses = geoCoder.getFromLocationName(locName, 1);
			
			lat = addresses.get(0).getLatitude();
			lon = addresses.get(0).getLongitude();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		sharedPref.edit().putFloat("lat", (float) lat).commit();
		sharedPref.edit().putFloat("lon", (float) lon).commit();

		locMgr.removeUpdates(onLocationChange);
		frameAnimation.stop();
		tv_gps.setText("Location found!");
		
		if (networkStatus)
			startActivity(nextIntent);
	}

	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged(Location fix) {
			saveLocation(fix.getLatitude(), fix.getLongitude());
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
