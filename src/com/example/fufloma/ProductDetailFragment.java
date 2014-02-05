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
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;

public class ProductDetailFragment extends Fragment {

	private View fragView;

	private int itemID;

	private double productLat;
	private double productLong;
	private String phoneNumber;
	DataStorage dataStorage;
	RequestQueue mRequestQueue;
	ImageLoader mImageLoader;

	public ProductDetailFragment() {

	}

	public void setItemID(int id) {
		itemID = id;
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

		// get product data
		//DummyDatabase localDB = new DummyDatabase();
		
		ProductListItem product = dataStorage.productDB.get(itemID);
		UserListItem seller = dataStorage.getUserItem(product.getSellerId());

		// setup product image
		NetworkImageView imageView = (NetworkImageView) fragView
				.findViewById(R.id.product_detail_image);

		//Drawable drawable = getResources().getDrawable(R.drawable.produkt_maus);
		//Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		imageView.setImageUrl(product.getFullAttachmentURL(),mImageLoader);

		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		imageView.getLayoutParams().width = size.x;

		imageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(),
						ProductPhotoFullActivity.class);
				myIntent.putExtra("bitmapID", R.drawable.produkt_maus);
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
		int sellCt = seller.getSellCt();
		int buyCt = seller.getBuyCt();

		TextView txtView = (TextView) fragView.findViewById(R.id.cityRepText);
		txtView.setText(product.getPublicLocation() + "\n"
				+ "Verk�ufer:\t\t" + sellCt + " V / " + buyCt + " K");

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
		phoneNumber = seller.getPhoneNr();
		
		Button intrButton = (Button) fragView.findViewById(R.id.interestButton);
		intrButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage(R.string.intrAlert).setNegativeButton(
						R.string.intrAlertOK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).setPositiveButton("Anrufen", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// call
								Intent callIntent = new Intent(Intent.ACTION_CALL);
								callIntent.setData(Uri.parse("tel:" + phoneNumber));
								startActivity(callIntent);
							}
						});

				builder.create().show();
			}
		});
	}
}
