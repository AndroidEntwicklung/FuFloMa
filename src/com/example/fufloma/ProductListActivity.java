package com.example.fufloma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract.Constants;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ProductListActivity extends Activity implements OnTaskCompleted {
	private SharedPreferences sharedPref;
	private float curLat;
	private float curLon;
	private int maxItems;
	DataStorage dataStorage;

	private boolean dataLoaded;
	private PullToRefreshListView mPullToRefreshLayout;
	private boolean doubleBackToExitPressedOnce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_activity);

		sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		curLat = sharedPref.getFloat("lat", 0.0f);
		curLon = sharedPref.getFloat("lon", 0.0f);
		
		dataStorage = (DataStorage) getApplication();
		dataStorage.setListener(this);
		
		dataLoaded = false;
	}
	
	public static ArrayList<ProductListItem> cloneList(ArrayList<ProductListItem> list) {
		ArrayList<ProductListItem> clone = new ArrayList<ProductListItem>(list.size());
	    for(ProductListItem item: list) clone.add(((ProductListItem) item).clone());
	    return clone;
	}
	
	// this is called as soon as volley is done! 
    @Override
    public void onTaskCompleted() {
		String locName = (String) sharedPref.getString("locName", "Stuttgart");

		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

		Location locationA = new Location("A");
		locationA.setLatitude(curLat);
		locationA.setLongitude(curLon);

		try {

			mPullToRefreshLayout = (PullToRefreshListView) findViewById(R.id.productLV);
			ArrayList<ProductListItem> pL_org = dataStorage.productDB;
			
			for (ProductListItem item : pL_org) {
				Location locationB = new Location("B");
				locationB.setLatitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLatitude());
				locationB.setLongitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLongitude());

				item.setDistance(locationA.distanceTo(locationB));
			}

			// sort list by distance...
			Collections.sort(pL_org, new DistanceSort());
			
			// ...and then clone it so the separators don't change the database
			ArrayList<ProductListItem> productList = cloneList(pL_org);
			ProductListAdapter plAdapter = new ProductListAdapter(this,
					productList);
			maxItems = dataStorage.productDB.size();
			
			int productsInCity = dataStorage.getProductCount(locName);
			if (productsInCity > 0) {
				plAdapter.addSeparatorItem(0, locName);
				productsInCity++;
			}

			int productsOutCity = maxItems - Math.max(0, productsInCity-1);
			if (productsOutCity > 0) {
				Resources res = getResources();
				plAdapter.addSeparatorItem(productsInCity,
						res.getString(R.string.umgebung));
			}

			plAdapter.addSeparatorItem("Keine weiteren Ergebnisse");

			mPullToRefreshLayout.setAdapter(plAdapter);
			mPullToRefreshLayout.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					Object o = a.getItemAtPosition(position);
					ProductListItem productData = (ProductListItem) o;

					Intent myIntent = new Intent(v.getContext(),
							ProductDetailActivity.class);

					myIntent.putExtra("ID", productData.getId());
					myIntent.putExtra("maxItems", maxItems);

					startActivity(myIntent);
				}
			});

			mPullToRefreshLayout.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					new AsyncTask<Void, Void, Void>() {

			            @Override
			            protected Void doInBackground(Void... params) {
			                try {
			                	dataStorage.initData();
			                	dataLoaded = false;
			                	while (!dataLoaded)
			                		Thread.sleep(100);
			                } catch (InterruptedException e) {
			                    e.printStackTrace();
			                }
			                return null;
			            }

			            @Override
			            protected void onPostExecute(Void result) {
			                super.onPostExecute(result);
			                mPullToRefreshLayout.onRefreshComplete();
			            }
			        }.execute();
				}
			});
			
			dataLoaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
   

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
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
		case R.id.action_user:
			// openSettings();
			return true;
		case R.id.action_help:
			helpDialog();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void helpDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.helpListDialog).setPositiveButton(
				R.string.intrAlertOK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		builder.create().show();
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Erneut drücken zum Verlassen.",
				Toast.LENGTH_SHORT).show();
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}
}
