package com.example.fufloma;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.util.Log;

public class DataStorage extends Application {

	private boolean finished = false;
	private int requestCnt = 0;
	private RequestQueue queue;
	private ArrayList<String> indexListProducts = new ArrayList<String>();
	private ArrayList<String> indexListUsers = new ArrayList<String>();
	public final ArrayList<ProductListItem> productDB = new ArrayList<ProductListItem>();
	public final ArrayList<UserListItem> userDB = new ArrayList<UserListItem>();
	private OnTaskCompleted listener;

	@Override
	public void onCreate() {
		queue = Volley.newRequestQueue(this);
		finished = false;
		initData();
	}

	public void newDocument(JSONObject obj) {
		// JsonObjectRequest jsonSend = new
		// JsonObjectRequest(Request.Method.POST,
		// "http://141.28.122.106:5984/fufloma/", jsonObject, null, null);

		final String URL = "http://141.28.122.106:5984/fufloma/";

		// Post params to be sent to the server
		JsonObjectRequest jsonSend = new JsonObjectRequest(URL, obj,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							VolleyLog.v("Response:%n %s", response.toString(4));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
					}
				});

		// add the request object to the queue to be executed
		queue.add(jsonSend);
	}
	
	public void deleteObj(String id, String rev) {
		final String URL = "http://141.28.122.106:5984/fufloma/" + id + "?rev=" + rev;
		
		JSONObject object = new JSONObject();

		// Post params to be sent to the server
		JsonObjectRequest jsonSend = new JsonObjectRequest(Request.Method.DELETE, URL, object,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							VolleyLog.v("Response:%n %s", response.toString(4));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
					}
				});

		// add the request object to the queue to be executed
		queue.add(jsonSend);
	}

	public void initData() {
		queue.getCache().clear();

		indexListProducts.clear();
		indexListUsers.clear();
		productDB.clear();
		userDB.clear();

		String url = "http://141.28.122.106:5984/fufloma/_all_docs";
		JsonObjectRequest jsObjRequestProducts = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jsonArray = response.getJSONArray("rows");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject childJSONObject = jsonArray
										.getJSONObject(i);
								indexListProducts.add(childJSONObject
										.getString("id"));
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
												temp.setId(response
														.optString("_id"));
												temp.setRev(response
														.optString("_rev"));
												temp.setDescription(response
														.optString("description"));
												temp.setLocation(response
														.optString("location"));
												temp.setPrice((float) response
														.optDouble("price"));
												temp.setSellerId(response
														.optString("sellerId"));
												temp.setState(response
														.optString("state"));
												temp.setAttachment(response
														.optJSONObject(
																"_attachments")
														.names().opt(0)
														.toString());
												temp.setPhoneNumber(response
														.optString("phoneNumber"));
												Log.i("FuFloMa",
														temp.toString());
												productDB.add(temp);
												requestDone();
											}

										}, new Response.ErrorListener() {

											@Override
											public void onErrorResponse(
													VolleyError error) {
											}
										});

								queue.add(jsObjRequest);
								addedRequest();
							}

							requestDone();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.wtf("FuFloMa", "Didn't get shit!");
					}
				});

		queue.add(jsObjRequestProducts);
		addedRequest();

		url = "http://141.28.122.106:5984/fufloma_user/_all_docs";
		JsonObjectRequest jsObjRequestUser = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jsonArray = response.getJSONArray("rows");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject childJSONObject = jsonArray
										.getJSONObject(i);
								indexListUsers.add(childJSONObject
										.getString("id"));
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
												temp.setId(response
														.optString("_id"));
												temp.setRev(response
														.optString("_rev"));
												temp.setPhoneNr(response
														.optString("phoneNr"));
												temp.setBuyCt(response
														.optInt("buyCt"));
												temp.setSellCt(response
														.optInt("sellCt"));

												userDB.add(temp);
												requestDone();
											}

										}, new Response.ErrorListener() {

											@Override
											public void onErrorResponse(
													VolleyError error) {
											}
										});

								queue.add(jsObjRequest);
								addedRequest();
							}

							requestDone();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.wtf("FuFloMa", "Didn't get shit!");
					}
				});

		queue.add(jsObjRequestUser);
		addedRequest();

	}

	public int getProductCount(String searchLocation) {
		int count = 0;

		for (ProductListItem item : productDB)
			if (item.getLocation().contains(searchLocation))
				count++;

		return count;
	}

	public int getProductCount() {
		return productDB.size();
	}

	public UserListItem getUserItem(String id) {
		for (UserListItem u : userDB) {
			if (u.getId().equalsIgnoreCase(id))
				return u;
		}
		return null;
	}

	private void addedRequest() {
		requestCnt++;
		finished = false;
	}

	private void requestDone() {
		requestCnt--;

		if (requestCnt == 0) {
			finished = true;

			if (listener != null)
				listener.onTaskCompleted();
		}

	}

	public boolean isFinished() {
		return finished;
	}

	public OnTaskCompleted getListener() {
		return listener;
	}

	public void setListener(OnTaskCompleted listener) {
		this.listener = listener;
		if (finished)
			listener.onTaskCompleted();
	}

}
