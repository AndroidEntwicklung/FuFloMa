package com.example.fufloma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProductListActivity extends Activity {
	private SharedPreferences sharedPref;
	private float curLat;
	private float curLon;
	private int maxItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_activity);

		sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		curLat = sharedPref.getFloat("lat", 0.0f);
		curLon = sharedPref.getFloat("lon", 0.0f);

		DummyDatabase localDB = new DummyDatabase();

		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());

		Location locationA = new Location("A");
		locationA.setLatitude(curLat);
		locationA.setLongitude(curLon);

		List<Address> addresses;

		try {
			addresses = geoCoder.getFromLocation(curLat, curLon, 1);
			String locName = (String) addresses.get(0).getLocality();

			sharedPref.edit().putString("location_name", locName).commit();

			final PullToRefreshListView plv = (PullToRefreshListView) findViewById(R.id.productLV);

			ArrayList<ProductListItem> productList = localDB.getProductListData();
			
			for (ProductListItem item : productList) {
				Location locationB = new Location("B");
				locationB.setLatitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLatitude());
				locationB.setLongitude(geoCoder
						.getFromLocationName(item.getLocation(), 1).get(0)
						.getLongitude());

				item.setDistance(locationA.distanceTo(locationB));
			}

			ProductListAdapter plAdapter = new ProductListAdapter(this, productList);
			
			int productsInCity = localDB.getProductsCount(locName);
			if (productsInCity > 0) {
				plAdapter.addSeparatorItem(0, locName);
				productsInCity++;
			}
			
			maxItems = localDB.getCount();
			int productsOutCity = maxItems - productsInCity;
			if (productsOutCity > 0) {
				Resources res = getResources();
				plAdapter.addSeparatorItem(productsInCity, res.getString(R.string.umgebung));
			}
			
			plAdapter.addSeparatorItem("Keine weiteren Ergebnisse");
			
			plv.setAdapter(plAdapter);
			plv.setOnItemClickListener(new OnItemClickListener() {

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
			
			plv.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					//new GetDataTask().execute();
					refreshView.onRefreshComplete();
				}
			});		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("FuFloMa", "Bla");
		}
	}
}
