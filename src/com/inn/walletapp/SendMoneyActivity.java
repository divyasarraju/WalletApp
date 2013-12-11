package com.inn.walletapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.inn.data.AccountsDataManager;
import com.inn.data.ContactsAdapter;
import com.inn.data.ContactsData;
import com.inn.services.AsyncTaskManager;
import com.inn.services.JSONResponseImpl;
import com.inn.services.Utils;

//2 service calls are here. 1 is to call all to populate the user accounts and then 2 to make a p2p payment
public class SendMoneyActivity extends Activity implements JSONResponseImpl {
	int index;
	private ListView contact_list;
	private ArrayList<ContactsData> contact_details;
	ContactsData cd = null;
	String recepient_id;
	private EditText et_amt, et_memo;
	AsyncTaskManager asyncReq;
	String payee_accId;
	ImageButton image_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_money_layout);
		et_amt = (EditText) findViewById(R.id.et_amt);
		et_memo = (EditText) findViewById(R.id.et_memo);
		contact_list = (ListView) findViewById(R.id.contact_list);
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

		// make call to all
		asyncReq = new AsyncTaskManager("Loading... Please wait.", this,
				SendMoneyActivity.this);
		asyncReq.execute("https://www.qajotfromchase.com/innovations/relay/InnovationCenter/account/all");

	}
	public void confirmLogout(){
		new AlertDialog.Builder(this)
	    .setTitle("Logout")
	    .setMessage("Are you sure you want to Logout?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	Utils.getInstance().clearPreferences(getApplicationContext());
				startActivity(new Intent(SendMoneyActivity.this,
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

	public void onPay(View v) {
		Log.d("DLOG", "onPay=> "
				+ getResources().getString(R.string.qa_url_p2p)
				+ AccountsDataManager.getInstance().getmAccID());
		if(et_amt.getText().toString().length()>0 && et_memo.getText().toString().length()>0)
		new RequestTask().execute(getResources().getString(R.string.qa_url_p2p)
				+ AccountsDataManager.getInstance().getmAccID());// from guy
		else{
			if(et_amt.getText().toString().length()==0)
			et_amt.setError("Enter All the values");
			if(et_memo.getText().toString().length()==0)
				et_memo.setError("Enter All the values");
		}

	}

	public void onCancel(View v) {
		finish();
	}

	public class RequestTask extends AsyncTask<String, String, String> {

		private JSONResponseImpl jsonResponseImpl;
		private String url;
		private String dialog_message;
		private ProgressDialog dialog = new ProgressDialog(
				SendMoneyActivity.this);
		JSONObject jObject;
		List<NameValuePair> pairs;
		JSONArray jsonArray;

		@Override
		protected void onPreExecute() {
			dialog.setMessage(dialog_message);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... uri) {

			HttpClient httpclient1 = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri[0]);
			HttpResponse response1 = null;
			String responseString = null;
			JSONObject payload = new JSONObject();
			try {
			

				payload.put("name", et_memo.getText().toString());
				payload.put("_id", payee_accId);
				payload.put("price", et_amt.getText().toString());
				payload.put("qty", "1");

				StringEntity se = new StringEntity(payload.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httppost.setEntity(se);

				// Execute HTTP Post Request
				response1 = httpclient1.execute(httppost);
				StatusLine statusLine = response1.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response1.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response1.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
				Log.d("DLOG", "post req =>  " + response1);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (JSONException e) {
			}

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialog.isShowing())
				dialog.dismiss();
			if (result != null)
				// jsonResponseImpl.httpResult(result);
				Log.d("DLOG", "p2p result =>  " + result);
			finish();
		}

	}

	// Result for all call. Returns all the users in the system
	@Override
	public void httpResult(String result) {
		// TODO Auto-generated method stub
		if (result != null) {
			if (result.length() > 0) {
				contact_details = new ArrayList<ContactsData>();

				try {
					// JSONObject obj = new JSONObject(result);
					JSONArray jArray = new JSONArray(result);
					JSONObject recItem;
					Log.d("DLOG  ", "size  = " + jArray.length());
					for (int i = 0; i < jArray.length(); i++) {
						recItem = jArray.getJSONObject(i);
						Log.d("DLOG  ",
								"response  = " + recItem.getString("name"));
						if (recItem.getString("name").equals("DivyaTest")
								|| recItem.getString("name").equals(
										AccountsDataManager.getInstance()
												.getMuserId()))
							continue;

						cd = new ContactsData();
						cd.setmName(recItem.getString("name"));
						cd.setM_id(recItem.getString("_id"));
						cd.setmImg(recItem.getString("image"));
						contact_details.add(cd);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			ContactsAdapter adapter = new ContactsAdapter(contact_details, this);
			contact_list.setAdapter(adapter);
			int pos;
			contact_list
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								final View view, int position, long id) {
							contact_list.setItemChecked(position, true);
							final ContactsData item = contact_details
									.get(position);
							// (ContactsData) parent
							// .getItemAtPosition(position);
							Log.d("DLOG", "item=> " + item.getM_id());
							payee_accId = item.getM_id();

							// recepient_id = item.getM_id();
							for (int a = 0; a < parent.getChildCount(); a++) {
								parent.getChildAt(a).setBackgroundColor(
										Color.TRANSPARENT);
							}
							view.setBackgroundColor(getResources().getColor(
									R.color.btn_blue));
						}

					});

		}
	}

	@Override
	public void httpResultforOrder(String result) {
		// TODO Auto-generated method stub

	}

}