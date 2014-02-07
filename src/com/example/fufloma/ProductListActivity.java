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
	private DataStorage dataStorage;
	private ArrayList<ProductListItem> pL_org;

	private boolean dummyLoading;
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

	// deep copy of list: the original and the one for the adapter
	public static ArrayList<ProductListItem> cloneList(
			ArrayList<ProductListItem> list) {
		ArrayList<ProductListItem> clone = new ArrayList<ProductListItem>(
				list.size());
		for (ProductListItem item : list)
			clone.add(((ProductListItem) item).clone());
		return clone;
	}

	// this is called as soon as volley is done!
	@Override
	public void onTaskCompleted() {
		String locName = (String) sharedPref.getString("locName",
				"Furtwangen im Schwarzwald");

		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

		Location locationA = new Location("A");
		locationA.setLatitude(curLat);
		locationA.setLongitude(curLon);

		try {
			mPullToRefreshLayout = (PullToRefreshListView) findViewById(R.id.productLV);
			pL_org = dataStorage.productDB;

			// calculate distances
			for (ProductListItem item : pL_org) {
				Location locationB = new Location("B");
				locationB.setLatitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLatitude());
				locationB.setLongitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLongitude());

				item.setDistance(locationA.distanceTo(locationB));
				item.setLocLat(locationB.getLatitude());
				item.setLocLon(locationB.getLongitude());
			}

			// sort list by distance...
			Collections.sort(pL_org, new DistanceSort());

			// ...and then clone it so the separators don't change the database
			ArrayList<ProductListItem> productList = cloneList(pL_org);
			ProductListAdapter plAdapter = new ProductListAdapter(this,
					productList);

			Resources res = getResources();
			maxItems = dataStorage.productDB.size();

			int productsInCity = dataStorage.getProductCount(locName);
			if (productsInCity > 0) {
				plAdapter.addSeparatorItem(0, locName);
				productsInCity++;
			}

			int productsOutCity = maxItems - Math.max(0, productsInCity - 1);
			if (productsOutCity > 0) {
				plAdapter.addSeparatorItem(productsInCity,
						res.getString(R.string.umgebung));
			}

			plAdapter.addSeparatorItem(res.getString(R.string.umgebung));

			mPullToRefreshLayout.setAdapter(plAdapter);
			mPullToRefreshLayout
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> a, View v,
								int position, long id) {
							Object o = a.getItemAtPosition(position);
							ProductListItem productData = (ProductListItem) o;

							Intent myIntent = new Intent(v.getContext(),
									ProductDetailActivity.class);

							int ID = 0;
							for (int i = 0; i < pL_org.size(); i++)
								if (pL_org.get(i).getId() == productData
										.getId()) {
									ID = i;
									break;
								}

							myIntent.putExtra("ID", ID);
							myIntent.putExtra("maxItems", maxItems);

							startActivityForResult(myIntent, 2);
						}
					});

			mPullToRefreshLayout
					.setOnRefreshListener(new OnRefreshListener<ListView>() {
						@Override
						public void onRefresh(
								PullToRefreshBase<ListView> refreshView) {
							new AsyncTask<Void, Void, Void>() {
								// this is for the "pull to refresh" ListView
								
								@Override
								protected Void doInBackground(Void... params) {
									try {
										if (!dummyLoading) {
											dataStorage.initData();
											dataLoaded = false;

											while (!dataLoaded)
												Thread.sleep(1000);
										} else {
											Thread.sleep(1000);
											dummyLoading = false;
										}
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

			dummyLoading = true;
			mPullToRefreshLayout.setRefreshing();

			dataLoaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// when coming back from selling or deleting
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK)
			mPullToRefreshLayout.setRefreshing();
		if (requestCode == 2 && resultCode == RESULT_OK)
			mPullToRefreshLayout.setRefreshing();
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
			startActivityForResult(sell, 1);
			return true;
		case R.id.action_help:
			helpDialog();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// help dialog
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

	// double press to exit
	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();

			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, R.string.pressAgain,
				Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}
}
