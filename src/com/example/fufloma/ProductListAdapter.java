package com.example.fufloma;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeSet;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends BaseAdapter {

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
	private Context context;
	
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private final ArrayList<ProductListItem> listData;
	private LayoutInflater layoutInflater;
	NumberFormat form = NumberFormat
			.getCurrencyInstance(new Locale("de", "DE"));

	@SuppressWarnings("rawtypes")
	private TreeSet mSeparatorsSet = new TreeSet();

	public ProductListAdapter(Context context,
			ArrayList<ProductListItem> listData) {
		mRequestQueue = Volley.newRequestQueue(context);
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		
		mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
		    private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
		    public void putBitmap(String url, Bitmap bitmap) {
		        mCache.put(url, bitmap);
		    }
		    public Bitmap getBitmap(String url) {
		        return mCache.get(url);
		    }
		});
	}

	@SuppressWarnings("unchecked")
	public void addSeparatorItem(int index, final String item) {
		ProductListItem sep = new ProductListItem();
		sep.setDescription(item);

		this.listData.add(index, sep);
		mSeparatorsSet.add(index);

		notifyDataSetChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void addSeparatorItem(String item) {
		ProductListItem sep = new ProductListItem();
		sep.setDescription(item);

		this.listData.add(sep);
		mSeparatorsSet.add(this.listData.size()-1);

		notifyDataSetChanged();	
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return (getItemViewType(position) == TYPE_ITEM);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		int type = getItemViewType(position);

		if (convertView == null) {
			holder = new ViewHolder();

			switch (type) {
			case TYPE_ITEM:
				convertView = layoutInflater.inflate(
						R.layout.product_list_item, null);

				holder.shortdesc = (TextView) convertView
						.findViewById(R.id.shortdesc);
				holder.location = (TextView) convertView
						.findViewById(R.id.location);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.bm = (NetworkImageView) convertView.findViewById(R.id.bitmap);
				break;
			case TYPE_SEPARATOR:
				convertView = layoutInflater.inflate(R.layout.product_list_sep,
						null);

				holder.shortdesc = (TextView) convertView
						.findViewById(R.id.sepTxt);
				break;
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BigDecimal price1 = new BigDecimal(listData.get(position).getPrice())
				.setScale(2, BigDecimal.ROUND_HALF_UP);

		switch (type) {
		case TYPE_ITEM:
			holder.shortdesc.setText(listData.get(position)
					.getDescription());
			holder.location.setText(listData.get(position).getPublicLocation());
			holder.price.setText(form.format(price1));
			holder.bm.setImageUrl(listData.get(position).getFullAttachmentURL(),mImageLoader);
			break;
		case TYPE_SEPARATOR:
			holder.shortdesc.setText(listData.get(position).getDescription());
			break;
		}

		return convertView;
	}

	static class ViewHolder {
		TextView shortdesc;
		TextView location;
		TextView price;
		NetworkImageView bm;

	}
	

}
