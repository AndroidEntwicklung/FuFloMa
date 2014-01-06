package com.example.fufloma;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView location = null;
	Button button = null;
	LocationManager locMgr = null;
	SharedPreferences sharedPref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		location = (TextView) findViewById(R.id.textView1);
		button = (Button) findViewById(R.id.button1);

		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				onLocationChange);
		
		sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		sharedPref.edit().putInt("bla", 123).commit();
		int bla = sharedPref.getInt("bla", 1);
		//int bla = 2;
		String blab = String.valueOf(bla);
		
	      Toast
	        .makeText(MainActivity.this, blab, Toast.LENGTH_LONG)
	        .show();
		
	      button.setOnClickListener(new Button.OnClickListener() { 
	        	public void onClick(View view) {
	                Intent myIntent = new Intent(view.getContext(), TestPrefs.class);
	                startActivity(myIntent);
	        	}
	        });
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged(Location fix) {
			location.setText(String.valueOf(fix.getLatitude()) + ", "
					+ String.valueOf(fix.getLongitude()));
			locMgr.removeUpdates(onLocationChange);
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
