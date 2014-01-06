package com.example.fufloma;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	  TextView location=null;
	  LocationManager locMgr=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		locMgr = (LocationManager)getSystemService(LOCATION_SERVICE);
		location = (TextView) findViewById(R.id.textView1);
		
		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	  LocationListener onLocationChange=new LocationListener() {
		    public void onLocationChanged(Location fix) {
		      location.setText(String.valueOf(fix.getLatitude())
		                      +", "
		                      +String.valueOf(fix.getLongitude()));
		      locMgr.removeUpdates(onLocationChange);
		      
		      Toast
		        .makeText(MainActivity.this, "Location saved",
		                  Toast.LENGTH_LONG)
		        .show();
		    }
		    
		    public void onProviderDisabled(String provider) {
		      // required for interface, not used
		    }
		    
		    public void onProviderEnabled(String provider) {
		      // required for interface, not used
		    }
		    
		    public void onStatusChanged(String provider, int status,
		                                  Bundle extras) {
		      // required for interface, not used
		    }
		  };

}
