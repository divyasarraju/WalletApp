package com.inn.walletapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.inn.data.AccountsDataManager;
import com.inn.data.GroupData;
import com.inn.data.ItemData;
import com.inn.services.AsyncTaskManager;
import com.inn.services.JSONResponseImpl;
import com.inn.services.Utils;

public class PinActivity extends Activity implements JSONResponseImpl {
	private EditText username, pin1, pin2, pin3, pin4;
	AsyncTaskManager asyncReq;
	public static ArrayList<GroupData> group_details;
	public static ArrayList<ItemData> items_details;
	GroupData id = null;
	ItemData it = null;
	String pin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_layout);
		initUI();
	}

	public void onOK(View v) {
		pin = pin1.getText().toString() + pin2.getText().toString()
				+ pin3.getText().toString() + pin4.getText().toString();
		if(pin.length()>0 && username.getText().toString().trim().length()>0){
		String login_str = (getResources().getString(R.string.qa_url_login)) + username.getText().toString().trim() + "/" + pin;
		asyncReq = new AsyncTaskManager("Logging in... Please wait.", this,
				PinActivity.this);
		asyncReq.execute(login_str);
		}
		else 
			username.setError("Oops! Invalid Login!");


	}
	private void initUI() {
		username = (EditText) findViewById(R.id.et_user);
		username.setText("Alpha");
		pin1 = (EditText) findViewById(R.id.et_pin1);
		pin1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				pin2.requestFocus();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		pin2 = (EditText) findViewById(R.id.et_pin2);
		pin2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				pin3.requestFocus();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		pin3 = (EditText) findViewById(R.id.et_pin3);
		pin3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				pin4.requestFocus();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		pin4 = (EditText) findViewById(R.id.et_pin4);
	}

	@Override
	public void httpResult(String result) {
		if (result != null) {
			if (result.length() > 2) {
				Utils.writeToPreferences(getApplicationContext(), username.getText().toString(), pin);
				updateAccountsDataManager(result);
				Intent i = new Intent(PinActivity.this,
						TransactionActivity.class);
				startActivity(i);
				finish();
			} else
				username.setError("Oops! Invalid Login!");
		}
	}

	public void updateAccountsDataManager(String response) {
		
//		group_details = new ArrayList<GroupData>();
//		items_details = new ArrayList<ItemData>();
//		id = new GroupData();
			if (response.length() > 0) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					AccountsDataManager.getInstance().setMuserId(
							jsonObject.getString("name"));
					AccountsDataManager.getInstance().setmAccBalance(
							jsonObject.getString("balance"));
					AccountsDataManager.getInstance().setmEmail(
							jsonObject.getString("email"));
					AccountsDataManager.getInstance().setmAccID(
							jsonObject.getString("_id"));
					AccountsDataManager.getInstance().setmImage(
							jsonObject.getString("image"));
					AccountsDataManager.getInstance().setmPIn(
							jsonObject.getString("PIN"));
					
					JSONArray transactions = jsonObject
							.getJSONArray("transactions");
					AccountsDataManager.getInstance().setmRecepients(transactions);
					
//					
//					JSONObject recItem;
//					JSONObject trans_item;
//					for (int i = 0; i < transactions.length(); i++) {
//						
//						//Get Header
//						trans_item = transactions.getJSONObject(i);
//						id.setSender_id(trans_item.getString("sender_name"));
//						id.setTotal_price(trans_item.getString("total"));
//						group_details.add(id);
//						
//						//Get Item
//						JSONArray items = trans_item.getJSONArray("items");
//						for (int j = 0; j < items.length(); j++) {
//							recItem = items.getJSONObject(j);
//							it = new ItemData();
//							it.setName(recItem.getString("name"));
//							it.setPrice(recItem.getString("price"));
//							it.setQty(recItem.getString("qty"));
//							items_details.add(it);
//						}
//						
//						
//					}
					 
					

				} catch (JSONException e) {
				}
		}
	}
	


	@Override
	public void httpResultforOrder(String result) {
		// TODO Auto-generated method stub
		
	}

}
