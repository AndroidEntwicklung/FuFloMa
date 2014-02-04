package com.example.fufloma;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailActivity extends Activity {
	
	private double productLat;
	private double productLong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.product_detail_activity);
		setupActionBar();
		
		// get item ID
		int itemID = 0;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
            	itemID = 0;
            } else {
            	itemID = extras.getInt("ID");
            }
        } else {
        	itemID = (Integer) savedInstanceState.getSerializable("ID");
        }
		
		// get product data
		DummyDatabase localDB = new DummyDatabase();
		ProductListItem product = localDB.getProductItem(itemID);
		UserListItem seller = localDB.getUserItem(product.getSellerID());
		
        // setup product image
		ImageView imageView = (ImageView)findViewById(R.id.product_detail_image);

        Drawable drawable = getResources().getDrawable(R.drawable.produkt_maus);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        imageView.setImageBitmap(bitmap);
        
    	Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        imageView.getLayoutParams().width = size.x;
        
        imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), ProductPhotoFullActivity.class);
                    myIntent.putExtra("bitmapID", R.drawable.produkt_maus);
                    startActivity(myIntent);
                }
            });
        
        // setup map button
        ImageButton imageButton = (ImageButton) findViewById(R.id.mapButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), GMapsActivity.class);
                
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
        
        TextView txtView = (TextView) findViewById(R.id.cityRepText);
        txtView.setText(product.getPublicLocation() + " (" + distTo + ")\n" + 
        		"Verkäufer:\t\t" + sellCt + " V / " + buyCt + " K");
        
        // setup description TextView
        TextView descView = (TextView) findViewById(R.id.descText);
        descView.setText(product.getDescription());
        descView.setMovementMethod(new ScrollingMovementMethod());
        descView.setScrollbarFadingEnabled(false);
        
        // setup price TextView
        TextView priceView = (TextView) findViewById(R.id.priceText);
        priceView.setText(String.format("%.2f", product.getPrice()) + "€");
        
        // setup state TextView
        TextView stateView = (TextView) findViewById(R.id.stateText);
        stateView.setText("Zustand:\t\t" + product.getState());
        
        // setup interest Button
        Button intrButton = (Button) findViewById(R.id.interestButton);
        intrButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                builder.setMessage(R.string.intrAlert)
                       .setPositiveButton(R.string.intrAlertOK, new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                        	   dialog.cancel();
                           }
                       });

                builder.create().show();
    		}
        });
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
