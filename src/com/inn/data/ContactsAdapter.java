package com.inn.data;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inn.services.Utils;
import com.inn.walletapp.R;

public class ContactsAdapter extends ArrayAdapter<ContactsData> {
	private final Context mcontext;
	private final ArrayList<ContactsData> mRowData;

	public ContactsAdapter(ArrayList<ContactsData> data, Context c) {
		super(c, R.layout.contact_row);
		mRowData = data;
		mcontext = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRowData.size();
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
			view = vi.inflate(R.layout.contact_row, null);
		}

		ImageView img = (ImageView) view.findViewById(R.id.img);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView id = (TextView) view.findViewById(R.id.id);
		
		ContactsData rData = mRowData.get(position);
		name.setText(rData.mName);
		id.setText(rData.m_id);

		if (rData.mImg.length() > 0) {
			byte[] blob = rData.mImg.getBytes();
			byte[] decodedString = Base64.decode(blob, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);
			img.setImageBitmap(Utils.getRoundedCornerBitmap(decodedByte));
		}
		
		return view;
	}
}
