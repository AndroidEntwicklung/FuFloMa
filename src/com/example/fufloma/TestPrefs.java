package com.example.fufloma;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class TestPrefs extends Activity {
	TextView location = null;
	SharedPreferences sharedPref = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		location = (TextView) findViewById(R.id.textView1);
		sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		int bla = sharedPref.getInt("bla", 1);
		//int bla = 2;
		String blab = String.valueOf(bla);
		
		location.setText(blab);
		
	}

}
