package com.example.fufloma;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProductDetailActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.product_detail_activity);
		setupActionBar();
		
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
        
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), GMapsActivity.class);
                
                myIntent.putExtra("lat", 48.0501);
                myIntent.putExtra("lng", 8.2014);
                
                startActivity(myIntent);
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
