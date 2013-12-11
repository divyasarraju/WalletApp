package com.inn.data;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inn.services.Utils;
import com.inn.walletapp.R;
import com.inn.walletapp.R.id;
import com.inn.walletapp.R.layout;

public class ItemsAdapter extends BaseAdapter {
	private final Context mcontext;
	private ArrayList<ItemData> mRowData;

	public ItemsAdapter(ArrayList<ItemData> data, Context c) {
		mRowData = data;
		mcontext = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRowData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mRowData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) mcontext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.item_row, null);
		}

		TextView name = (TextView) view.findViewById(R.id.name);
		TextView qty = (TextView) view.findViewById(R.id.qty);
		TextView price = (TextView) view.findViewById(R.id.price);
		TextView price1 = (TextView) view.findViewById(R.id.price1);
		
		
		ItemData rData = mRowData.get(position);
		name.setText(rData.getName());
		qty.setText(rData.getQty());
		String s = "$"+ (new DecimalFormat("0.00").format(Double.parseDouble(rData.getPrice())));
		price.setText(s);
		float amount = (Integer.parseInt(rData.getQty())) * (Float.parseFloat(rData.getPrice()));
		s  = new DecimalFormat("0.00").format(Double.parseDouble(String.valueOf(amount)));
		price1.setText("$"+s);
		return view;
	}
}
