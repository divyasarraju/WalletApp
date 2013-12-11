package com.inn.walletapp;

import java.util.ArrayList;

import net.sourceforge.zbar.Symbol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.inn.data.AccountsDataManager;
import com.inn.data.ItemData;
import com.inn.data.ItemsAdapter;
import com.inn.services.AsyncTaskManager;
import com.inn.services.JSONResponseImpl;
import com.inn.services.OrderTaskManager;
import com.inn.services.Utils;

public class QRScannerActivity extends Activity implements JSONResponseImpl {

	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	private final String DTAG = this.getClass().getCanonicalName();
	private String token, receiving_amt, from_email, scan_result = null;
	AsyncTaskManager asyncReq;
	OrderTaskManager orderTask;
	private ListView item_list;
	private CheckBox chb_email;
	public static ArrayList<ItemData> items_details;
	private TextView sender_id, total;
	ItemData id = null;
	ImageButton image_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrscanner_layout);
		total = (TextView) findViewById(R.id.total);
		sender_id = (TextView) findViewById(R.id.sender_id);
		item_list = (ListView) findViewById(R.id.item_list);
		chb_email = (CheckBox) findViewById(R.id.chb_email);
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
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES,
					new int[] { Symbol.QRCODE });
			startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable",
					Toast.LENGTH_SHORT).show();
		}
	}
	public void confirmLogout(){
		new AlertDialog.Builder(this)
	    .setTitle("Logout")
	    .setMessage("Are you sure you want to Logout?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	Utils.getInstance().clearPreferences(getApplicationContext());
				startActivity(new Intent(QRScannerActivity.this,
						PinActivity.class));
				finish();
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	     .show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ZBAR_SCANNER_REQUEST:
		case ZBAR_QR_SCANNER_REQUEST:
			if (resultCode == RESULT_OK) {
				scan_result = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				Log.d(DTAG, scan_result);

				asyncReq = new AsyncTaskManager("Processing... Please wait.",
						this, QRScannerActivity.this);
				asyncReq.execute(getResources().getString(
						R.string.qa_url_review_order)
						+ scan_result);
			} else if (resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if (!TextUtils.isEmpty(error)) {
					Toast.makeText(this, error, Toast.LENGTH_LONG).show();
				}
			}
			break;
		}
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	@Override
	public void httpResult(String result) {
		// TODO Auto-generated method stub
		Log.d("DLOG", "order result - > " + result);
		if (result != null) {
			if (result.length() > 0) {
				items_details = new ArrayList<ItemData>();
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray arrayItems = jsonObject.getJSONArray("items");
					sender_id.setText(jsonObject.getString("sender_name"));
					total.setText("Your total bill is:   $"
							+ Float.parseFloat(jsonObject.getString("total"))
							* -1);
					JSONObject recItem;
					for (int i = 0; i < arrayItems.length(); i++) {
						recItem = arrayItems.getJSONObject(i);
						id = new ItemData();
						id.setName(recItem.getString("name"));
						id.setPrice(recItem.getString("price"));
						id.setQty(recItem.getString("qty"));
						id.setTotal(Float.toString(Float.parseFloat(recItem.getString("price")) * Integer.parseInt(recItem.getString("qty"))));
						items_details.add(id);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ItemsAdapter adapter = new ItemsAdapter(items_details, this);
				item_list.setAdapter(adapter);
			}
		}

	}

	public void onDecline(View v) {
		orderTask = new OrderTaskManager("Cancelling the order...", this,
				QRScannerActivity.this);
		orderTask.execute(getResources()
				.getString(R.string.qa_url_cancel_order) + scan_result);
		finish();
	}

	public void onPay(View v) {
		orderTask = new OrderTaskManager("Cancelling the order...", this,
				QRScannerActivity.this);
		orderTask.execute(getResources()
				.getString(R.string.qa_url_confirm_order)
				+ AccountsDataManager.getInstance().getmAccID()
				+ "/"
				+ scan_result + "/" + chb_email.isChecked());
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void httpResultforOrder(String result) {
		// TODO Auto-generated method stub
		Log.d("DLOG", "httpResultforOrder=> " + result);
//		after success the screen should be taken to Transaction screen
	}
}
