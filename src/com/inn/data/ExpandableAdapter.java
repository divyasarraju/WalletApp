package com.inn.data;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inn.walletapp.R;

public class ExpandableAdapter extends BaseExpandableListAdapter {

	private List<Category> catList;
	private Context ctx;
	Map<String, Drawable> map = new HashMap<String, Drawable>();

	public ExpandableAdapter(List<Category> catList, Context ctx) {

	
		this.catList = catList;
		this.ctx = ctx;
		map.put("qr", ctx.getResources().getDrawable(R.drawable.qricon));
		map.put("P2P", ctx.getResources().getDrawable(R.drawable.p2picon));
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return catList.get(groupPosition).getItemList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return catList.get(groupPosition).getItemList().get(childPosition)
				.hashCode();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_layout, parent, false);
		}

		TextView itemName = (TextView) v.findViewById(R.id.itemName);
		TextView itemqty = (TextView) v.findViewById(R.id.qty);
		TextView itemprice = (TextView) v.findViewById(R.id.price);
		TextView itemtotal = (TextView) v.findViewById(R.id.total);

		ItemDetail det = catList.get(groupPosition).getItemList()
				.get(childPosition);

		String s = new DecimalFormat("0.00").format(Double.parseDouble(det
				.getPrice()));
		s = "$"+s;
		itemName.setText(det.getName());
		itemqty.setText(det.getQty());
		itemprice.setText(s);
		s = new DecimalFormat("0.00")
				.format(Double.parseDouble(det.getTotal()));
		s = "$"+s;
		itemtotal.setText(s);

		return v;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = catList.get(groupPosition).getItemList().size();
		System.out.println("Child for group [" + groupPosition + "] is ["
				+ size + "]");
		return size;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return catList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return catList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return catList.get(groupPosition).hashCode();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.group_layout, parent, false);
		}
		TextView groupName = (TextView) v.findViewById(R.id.groupName);
		TextView groupDescr = (TextView) v.findViewById(R.id.groupDescr);
		ImageView groupType = (ImageView) v.findViewById(R.id.type);
		TextView groupTypeText = (TextView) v.findViewById(R.id.type_text);
		
		Category cat = catList.get(groupPosition);
		String s = cat.getDescr().replace("$-", "-$");
		
		groupName.setText(cat.getName());
		groupDescr.setText(s);
		if (cat.getDescr().contains("-"))
			groupDescr.setTextColor(Color.RED);
		else
			groupDescr.setTextColor(Color.parseColor("#316CB3"));
		groupTypeText.setText(cat.gettype());
		groupType.setImageDrawable((Drawable) map.get(cat.gettype()));
		if (isExpanded){
			groupType.setVisibility(View.VISIBLE);
			groupTypeText.setVisibility(View.VISIBLE);
		}
		else
		{
			groupType.setVisibility(View.GONE);
			groupTypeText.setVisibility(View.GONE);
		}
		
		return v;

	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
