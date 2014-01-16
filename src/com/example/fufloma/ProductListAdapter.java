package com.example.fufloma;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends BaseAdapter {

	private ArrayList<ProductListItem> listData;
	private LayoutInflater layoutInflater;
	NumberFormat form = NumberFormat.getCurrencyInstance(new Locale("de", "DE"));
	
	public ProductListAdapter(Context context, ArrayList<ProductListItem> listData)
	{
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.product_list_item, null);
			holder = new ViewHolder();
			holder.shortdesc = (TextView) convertView.findViewById(R.id.shortdesc);
			holder.location = (TextView) convertView.findViewById(R.id.location);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.bm = (ImageView) convertView.findViewById(R.id.bitmap);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		BigDecimal price1 = new BigDecimal(listData.get(position).getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		holder.shortdesc.setText(listData.get(position).getShortDescription());
		holder.location.setText(listData.get(position).getPublicLocation());
		holder.price.setText(form.format(price1));
		holder.bm.setImageResource(R.id.bitmap);
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView shortdesc;
		TextView location;
		TextView price;
		ImageView bm;
		
	}


}
