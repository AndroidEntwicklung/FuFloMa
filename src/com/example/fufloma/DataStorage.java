package com.example.fufloma;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.util.Log;

public class DataStorage extends Application {

	private RequestQueue queue;
	private ArrayList<String> indexListProducts = new ArrayList<String>();
	private ArrayList<String> indexListUsers = new ArrayList<String>();
	public ArrayList<ProductListItem> productDB = new ArrayList<ProductListItem>();
	public ArrayList<UserListItem> userDB = new ArrayList<UserListItem>();
	
	@Override
	public void onCreate() {
		queue = Volley.newRequestQueue(this);
		initData();
	}
	
	
	public void initData() {
		queue.getCache().clear();
		String url = "http://141.28.122.106:5984/fufloma/_all_docs";
		JsonObjectRequest jsObjRequestProducts = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						try {
							JSONArray jsonArray = response.getJSONArray("rows");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject childJSONObject = jsonArray
										.getJSONObject(i);
								indexListProducts.add(childJSONObject.getString("id"));
							}

							// Second Request stuff
							String baseURL = "http://141.28.122.106:5984/fufloma/";
							for (String id : indexListProducts) {

								JsonObjectRequest jsObjRequest = new JsonObjectRequest(
										Request.Method.GET, baseURL + id, null,
										new Response.Listener<JSONObject>() {

											@Override
											public void onResponse(
													JSONObject response) {

												ProductListItem temp = new ProductListItem();
												temp.setId(response.optString("_id"));
												temp.setRev(response.optString("_rev"));
												temp.setDescription(response.optString("description"));
												temp.setLocation(response.optString("location"));
												temp.setLocLat(response.optDouble("locLat"));
												temp.setLocLon(response.optDouble("locLon"));
												temp.setPrice((float) response.optDouble("price"));
												temp.setSellerId(response.optString("sellerId"));
												temp.setState(StateEnum.getStatus(response.optInt("state")));
												temp.setAttachment(response.optJSONObject("_attachments").names().opt(0).toString());
												Log.e("FuFloMa", temp.toString());												
												productDB.add(temp);
											}

										}, new Response.ErrorListener() {

											@Override
											public void onErrorResponse(
													VolleyError error) {
												// TODO Auto-generated method
												// stub

											}
										});

								queue.add(jsObjRequest);

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.wtf("FuFloMa", "Didn't get shit!");
					}
				});

		queue.add(jsObjRequestProducts);

		
		url = "http://141.28.122.106:5984/fufloma_user/_all_docs";
		JsonObjectRequest jsObjRequestUser = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						try {
							JSONArray jsonArray = response.getJSONArray("rows");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject childJSONObject = jsonArray
										.getJSONObject(i);
								indexListUsers.add(childJSONObject.getString("id"));
							}

							// Second Request stuff
							String baseURL = "http://141.28.122.106:5984/fufloma_user/";
							for (String id : indexListUsers) {

								JsonObjectRequest jsObjRequest = new JsonObjectRequest(
										Request.Method.GET, baseURL + id, null,
										new Response.Listener<JSONObject>() {

											@Override
											public void onResponse(
													JSONObject response) {

												UserListItem temp = new UserListItem();
												temp.setId(response.optString("_id"));
												temp.setRev(response.optString("_rev"));
												temp.setPhoneNr(response.optString("phoneNr"));
												temp.setBuyCt(response.optInt("buyCt"));
												temp.setSellCt(response.optInt("sellCt"));

												userDB.add(temp);
											}

										}, new Response.ErrorListener() {

											@Override
											public void onErrorResponse(
													VolleyError error) {
												// TODO Auto-generated method
												// stub

											}
										});

								queue.add(jsObjRequest);

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.wtf("FuFloMa", "Didn't get shit!");
					}
				});

		queue.add(jsObjRequestUser);

	}
	
	public int getProductCount(String searchLocation) {
		int count = 0;
		
		for (ProductListItem item: productDB)
			if (item.getLocation().contains(searchLocation))
				count++;
		
		return count;
	}
	public int getProductCount()
	{
		return productDB.size();
	}
	
	public UserListItem getUserItem(String id) {
		for (UserListItem u : userDB) {
			if (u.getId().equalsIgnoreCase(id))
			return u;
		}
		return null;
	}

}
