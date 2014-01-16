package com.example.fufloma;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.app.NavUtils;

public class ProductPhotoFullActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_product_photo_full);
		setupActionBar();

		final ImageView contentView = (ImageView) findViewById(R.id.fullscreen_content);

		// ImageView below ActionBar
		TypedValue tv = new TypedValue();
		
		int actionBarHeight = 0;
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
		
		setMargins(contentView, 0, -actionBarHeight, 0, 0);

		// load image
		int bitmapID = R.drawable.app_hintergrund;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
            	bitmapID = 0;
            } else {
            	bitmapID = extras.getInt("bitmapID");
            }
        } else {
        	bitmapID = (Integer) savedInstanceState.getSerializable("bitmapID");
        }
		
        contentView.setImageResource(bitmapID); 
        contentView.setScaleType(ImageView.ScaleType.FIT_XY);
	}

	public static void setMargins (View v, int l, int t, int r, int b) {
	    if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
	        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
	        p.setMargins(l, t, r, b);
	        v.requestLayout();
	    }
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
