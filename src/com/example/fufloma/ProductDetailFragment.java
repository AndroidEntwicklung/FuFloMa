package com.example.fufloma;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;

public class ProductDetailFragment extends Fragment {

	private View fragView;

	private int itemID;
	private String itemStr;
	private String revID;
	private String imeiID;

	private double productLat;
	private double productLong;
	private String phoneNumber;
	DataStorage dataStorage;
	RequestQueue mRequestQueue;
	ImageLoader mImageLoader;
	Bitmap bm;

	public ProductDetailFragment() {

	}

	public void setItemID(int id) {
		itemID = id;
	}

	public void setIMEIID(String imei) {
		imeiID = imei;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRequestQueue = Volley.newRequestQueue(this.getActivity());
		mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
		    private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
		    public void putBitmap(String url, Bitmap bitmap) {
		        mCache.put(url, bitmap);
		        bm = bitmap;
		    }
		    public Bitmap getBitmap(String url) {
		        return mCache.get(url);
		    }
		});

		fragView = inflater.inflate(R.layout.fragment_product_detail,
				container, false);
		dataStorage = (DataStorage) getActivity().getApplication();

		loadElement();

		return fragView;
	}

	private void loadElement() {
		ProductListItem product = dataStorage.productDB.get(itemID);
		String sellerID = product.getSellerId();

		itemStr = product.getId();
		revID = product.getRev();
		
		// UserListItem seller = dataStorage.getUserItem(product.getSellerId());
		boolean productByUser = sellerID.equals(imeiID);
		
		// setup product image
		NetworkImageView imageView = (NetworkImageView) fragView
				.findViewById(R.id.product_detail_image);

		imageView.setImageUrl(product.getFullAttachmentURL(),mImageLoader);

		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		imageView.getLayoutParams().width = size.x;

		imageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(),
						ProductPhotoFullActivity.class);
				myIntent.putExtra("itemID", itemID);
				startActivity(myIntent);
			}
		});

		// setup map button
		productLat = product.getLocLat();
		productLong = product.getLocLon();

		ImageButton imageButton = (ImageButton) fragView
				.findViewById(R.id.mapButton);
		imageButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(),
						GMapsActivity.class);

				myIntent.putExtra("lat", productLat);
				myIntent.putExtra("lng", productLong);

				startActivity(myIntent);
			}
		});

		// setup city / reputation TextView
		int sellCt = 0;// seller.getSellCt();
		int buyCt = 0; // seller.getBuyCt();

		TextView txtView = (TextView) fragView.findViewById(R.id.cityRepText);
		
		if (!productByUser)
			txtView.setText(product.getPublicLocation() + "\n" + "Verk�ufer:\t\t"
					+ sellCt + " V / " + buyCt + " K");
		else {
        	String text = product.getPublicLocation() + "<br />"
        			+ "<em>Sie sind der Verk�ufer dieses Artikels</em>";
        	txtView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
		}
			

		// setup description TextView
		TextView descView = (TextView) fragView.findViewById(R.id.descText);
		descView.setText(product.getDescription());
		descView.setMovementMethod(new ScrollingMovementMethod());
		descView.setScrollbarFadingEnabled(false);

		// setup price TextView
		TextView priceView = (TextView) fragView.findViewById(R.id.priceText);
		priceView.setText(String.format("%.2f", product.getPrice()) + "�");

		// setup state TextView
		TextView stateView = (TextView) fragView.findViewById(R.id.stateText);
		stateView.setText("Zustand:\t\t" + product.getState());

		// setup interest Button
		phoneNumber = product.getPhoneNumber();

		Button intrButton = (Button) fragView.findViewById(R.id.interestButton);

		if (!productByUser) {
			intrButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage(R.string.intrAlert)
							.setNegativeButton(R.string.intrAlertOK,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									})
							.setPositiveButton("Anrufen",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// call
											Intent callIntent = new Intent(
													Intent.ACTION_CALL);
											callIntent.setData(Uri.parse("tel:"
													+ phoneNumber));
											startActivity(callIntent);
										}
									});

					builder.create().show();
				}
			});
		} else
		{
			intrButton.setBackgroundColor(Color.RED);
			intrButton.setText("L�schen");
			
			intrButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage("Wollen Sie den Artikel wirklich l�schen?")
							.setNegativeButton("Abbrechen",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									})
							.setPositiveButton("L�schen",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dataStorage.deleteObj(itemStr, revID);
											
											Intent returnIntent = new Intent();
											getActivity().setResult(getActivity().RESULT_OK, returnIntent);
											getActivity().finish();
										}
									});

					builder.create().show();
				}
			});
		}
	}
}
