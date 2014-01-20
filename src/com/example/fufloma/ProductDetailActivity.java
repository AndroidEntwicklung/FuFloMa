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
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.product_detail_activity);
		setupActionBar();
		
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
                
                myIntent.putExtra("lat", 48.0501);
                myIntent.putExtra("lng", 8.2014);
                
                startActivity(myIntent);
            }
        });
        
        // setup city / reputation TextView
        TextView txtView = (TextView) findViewById(R.id.cityRepText);
        txtView.setText("78147 Vöhrenbach (8 km)\nVerkäufer:\t\t5 V / 10 K");
        
        // setup description TextView
        TextView descView = (TextView) findViewById(R.id.descText);
        descView.setText("Computermaus ideal für Laptops, etc.!! Wie neu, kaum gebraucht. Ohne Batterie! Müssen extra gekauft werden, braucht AA-Batterien. Die Maus ist ohne Kabel, also per Funk, sehr praktisch. Computermaus ideal für Laptops, etc.!! Wie neu, kaum gebraucht. Ohne Batterie! Müssen extra gekauft werden, braucht AA-Batterien. Die Maus ist ohne Kabel, also per Funk, sehr praktisch.");
        descView.setMovementMethod(new ScrollingMovementMethod());
        descView.setScrollbarFadingEnabled(false);
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
