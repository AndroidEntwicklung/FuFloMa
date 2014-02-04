package com.example.fufloma;

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

public class ProductDetailFragment extends Fragment {

	private View fragView;

	private int itemID;

	private double productLat;
	private double productLong;

	public ProductDetailFragment() {

	}

	public void setItemID(int id) {
		itemID = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fragView = inflater.inflate(R.layout.fragment_product_detail,
				container, false);
		
		loadElement();

		return fragView;
	}

	private void loadElement() {

		// get product data
		DummyDatabase localDB = new DummyDatabase();
		ProductListItem product = localDB.getProductItem(0);
		UserListItem seller = localDB.getUserItem(product.getSellerID());

		// setup product image
		ImageView imageView = (ImageView) fragView
				.findViewById(R.id.product_detail_image);

		Drawable drawable = getResources().getDrawable(R.drawable.produkt_maus);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		imageView.setImageBitmap(bitmap);

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

		productLat = product.getLocLat();
		productLong = product.getLocLong();
		String distTo = product.getDistance();

		TextView txtView = (TextView) fragView.findViewById(R.id.cityRepText);
		txtView.setText(product.getPublicLocation() + " (" + distTo + ")\n"
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
		Button intrButton = (Button) fragView.findViewById(R.id.interestButton);
		intrButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage(R.string.intrAlert).setPositiveButton(
						R.string.intrAlertOK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

				builder.create().show();
			}
		});
	}
}
