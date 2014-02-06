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
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tv_network = null;
	TextView tv_gps = null;

	boolean networkStatus = false;
	LocationManager locMgr = null;
	SharedPreferences sharedPref = null;
	Intent nextIntent = null;
	DataStorage dataStorage;

	ImageView iv_hfu_spin = null;
	MyAnimationDrawable frameAnimation = null;
	private float mPreviousX;
	private int lastDirection;
	private float screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataStorage = (DataStorage) getApplication();
		
		//dataStorage.newDocument();
		
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
		
		// get mime ID as unique UserID
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		String imei = "1337";
		if (telephonyManager.getDeviceId() != null)
			imei = telephonyManager.getDeviceId();
		
		sharedPref.edit().putString("imei", imei).commit();

		// screen width
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		screenWidth = size.x;

		// touch screen
		final View touchView = findViewById(R.id.mainTouchView);
		touchView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				float x = e.getX();

				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mPreviousX = x;
					frameAnimation.pause();
					lastDirection = frameAnimation.getDirection();

					break;

				case MotionEvent.ACTION_MOVE:
					float dx = 30*((x - mPreviousX)/screenWidth);
					
					if (dx < -1) {
						frameAnimation.prevFrame();
						mPreviousX = x;
						lastDirection = -1;
					}
					if (dx > +1) {
						frameAnimation.nextFrame();
						mPreviousX = x;
						lastDirection = +1;
					}

					break;

				case MotionEvent.ACTION_UP:
					frameAnimation.setDuration(lastDirection*frameAnimation.getDuration());
					frameAnimation.resume();

					break;
				}

				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		iv_hfu_spin = (ImageView) findViewById(R.id.hfuRotator);

		frameAnimation = new MyAnimationDrawable();
		frameAnimation.setOneShot(false);

		for (int i = 1; i <= 61; i++) {
			String drawName = (i < 10) ? "000" + i : "00" + i;
			Drawable tmp = getResources().getDrawable(
					getResources().getIdentifier("hfu_drehend" + drawName, "drawable",
							getPackageName()));
			frameAnimation.addFrame(tmp, 50);
		}

		// this is deprecated but setBackground requires API 16!
		iv_hfu_spin.setBackgroundDrawable(frameAnimation);
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

		//saveLocation(48.050278d, 8.209167d);
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
		tv_gps.setText("Aktuelle Position gefunden!");

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
