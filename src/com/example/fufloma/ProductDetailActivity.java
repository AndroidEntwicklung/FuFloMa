package com.example.fufloma;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;

public class ProductDetailActivity extends FragmentActivity {

	ViewPager Tab;
	TabPagerAdapter TabAdapter;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		setupActionBar();
	
		// get item ID
		int itemID = 0;
		int maxItems = 0;

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();

			if (extras == null) {
				itemID = 0;
				maxItems = 0;
			} else {
				itemID = extras.getInt("ID");
				maxItems = extras.getInt("maxItems");
			}
		} else {
			itemID = (Integer) savedInstanceState.getSerializable("ID");
			maxItems = (Integer) savedInstanceState.getSerializable("maxItems");
		}

		// setup TabPager
		TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
		TabAdapter.setItemCount(maxItems);
		
		Tab = (ViewPager) findViewById(R.id.pager);
		Tab.setAdapter(TabAdapter);
		
		Tab.setCurrentItem(itemID);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
        case R.id.action_sell:
    		Intent sell = new Intent(this, SellFormActivity.class);
			startActivity(sell);
            return true;
        case R.id.action_help:
            helpDialog();
            return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void helpDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.helpDetailDialog).setPositiveButton(
				R.string.intrAlertOK,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		builder.create().show();
	}
}
