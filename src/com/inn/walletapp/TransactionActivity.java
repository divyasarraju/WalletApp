package com.inn.walletapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.inn.data.AccountsDataManager;
import com.inn.data.Category;
import com.inn.data.ExpandableAdapter;
import com.inn.data.ItemDetail;
import com.inn.services.AsyncTaskManager;
import com.inn.services.JSONResponseImpl;
import com.inn.services.Utils;

public class TransactionActivity extends Activity implements JSONResponseImpl {

	private TextView tv_balance;
	private ExpandableListView exList;
	private List<Category> catList;
	private ExpandableAdapter exAdpt;
	private JSONArray all_transactions;
	protected Context mContext;
	AsyncTaskManager asyncReq;
	ImageButton image_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction_layout);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		String login_str = "https://www.qajotfromchase.com/innovations/relay/InnovationCenter/account/find/"
				+ AccountsDataManager.getInstance().getMuserId()
				+ "/"
				+ AccountsDataManager.getInstance().getmPIn();
		asyncReq = new AsyncTaskManager("Refreshing the list... Please wait.",
				this, TransactionActivity.this);
		asyncReq.execute(login_str);
		// initData();

	}

	private void init() {
		mContext = this;
		tv_balance = (TextView) findViewById(R.id.tv_balance);
		image_icon = (ImageButton) findViewById(R.id.profile_icon);

		byte[] blob = AccountsDataManager.getInstance().getmImage().getBytes();
		byte[] decodedString = Base64.decode(blob, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
				decodedString.length);
		image_icon.setImageBitmap(Utils.getRoundedCornerBitmap(decodedByte));
		image_icon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmLogout();
			}
		});
		exList = (ExpandableListView) findViewById(R.id.expandableListView1);
		exList.setGroupIndicator(null);
		exList.setChildDivider(getResources().getDrawable(R.color.white));

	}

	public void confirmLogout() {
		new AlertDialog.Builder(this)
				.setTitle("Logout")
				.setMessage("Are you sure you want to Logout?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Utils.getInstance().clearPreferences(
										getApplicationContext());
								startActivity(new Intent(
										TransactionActivity.this,
										PinActivity.class));
								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				}).show();
	}

	private void initData() {

		tv_balance.setText("$"
				+ Utils.formatString(AccountsDataManager.getInstance()
						.getmAccBalance()));
		all_transactions = AccountsDataManager.getInstance().getmRecepients();

		catList = new ArrayList<Category>();

		Category cat;
		String total;
		String s;
		for (int i = 0; i < all_transactions.length(); i++) {
			try {

				s = new DecimalFormat("0.00").format(Double
						.parseDouble(all_transactions.getJSONObject(i)
								.getString("total")));
				cat = createCategory(all_transactions.getJSONObject(i)
						.getString("sender_name"), "$" + s, all_transactions
						.getJSONObject(i).getString("type"));

				JSONArray lineItems = all_transactions.getJSONObject(i)
						.getJSONArray("items");
				for (int j = 0; j < lineItems.length(); j++) {
					total = Float.toString(Integer.parseInt(lineItems
							.getJSONObject(j).getString("qty"))
							* Float.parseFloat(lineItems.getJSONObject(j)
									.getString("price")));
					ItemDetail itemDetail = createItemDetail(lineItems
							.getJSONObject(j).getString("name"), lineItems
							.getJSONObject(j).getString("qty"), lineItems
							.getJSONObject(j).getString("price"), total);
					cat.addItemDetails(itemDetail);

				}

				catList.add(cat);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		exAdpt = new ExpandableAdapter(catList, this);
		exList.setAdapter(exAdpt);
		exList.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					exList.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});

	}

	private Category createCategory(String name, String descr, String id) {
		return new Category(id, name, descr);
	}

	//
	private ItemDetail createItemDetail(String name, String qty, String price,
			String total) {
		return new ItemDetail(name, qty, price, total);
	}

	public void onScanQR(View v) {
		Intent i = new Intent(TransactionActivity.this, QRScannerActivity.class);
		startActivity(i);

	}

	public void onSendMoney(View v) {
		Intent i = new Intent(TransactionActivity.this, SendMoneyActivity.class);
		startActivity(i);
	}

	@Override
	public void httpResult(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			AccountsDataManager.getInstance().setmAccBalance(
					jsonObject.getString("balance"));
			JSONArray transactions = jsonObject.getJSONArray("transactions");
			AccountsDataManager.getInstance().setmRecepients(transactions);
			initData();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void httpResultforOrder(String result) {

	}
}
