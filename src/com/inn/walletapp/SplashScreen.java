package com.inn.walletapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.inn.data.AccountsDataManager;
import com.inn.services.JSONResponseImpl;
import com.inn.services.OrderTaskManager;
import com.inn.services.Utils;
import com.inn.walletapp.util.SystemUiHider;

public class SplashScreen extends Activity implements JSONResponseImpl {
	private static final int AUTO_HIDE_DELAY_MILLIS = 2000;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;
	OrderTaskManager asyncReq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_laout);
		final View contentView = findViewById(R.id.fullscreen_content);
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, AUTO_HIDE_DELAY_MILLIS);
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			String user, pin;
			user = Utils.readUserFromPreferences(getApplicationContext());
			pin = Utils.readPinFromPreferences(getApplicationContext());
			Log.d("DLOG",
					"user from pref="
							+ Utils.readUserFromPreferences(getApplicationContext()));
			Log.d("DLOG",
					"pin from pref="
							+ Utils.readPinFromPreferences(getApplicationContext()));
			if (user != null && pin != null) {
				// call service
				login(user,pin);

			} else {
				Intent i = new Intent(SplashScreen.this, PinActivity.class);
				startActivity(i);
				finish();
			}
		}
	};
	
	public void login(String user, String pin){
		String login_str = (getResources().getString(R.string.qa_url_login)) + user + "/" + pin;
		asyncReq = new OrderTaskManager("Logging in... Please wait.", this,
				SplashScreen.this);
		asyncReq.execute(login_str);
	}

	@Override
	public void httpResultforOrder(String response) {
		Intent i;
		if (response != null) {
			if (response.length() > 2) {
					try {
						Log.d("DLOG", "response=> "+response);
						response=response+" \" ";
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
						i = new Intent(SplashScreen.this,
								TransactionActivity.class);
						startActivity(i);
						finish();
						

					} catch (JSONException e) {
						Log.d("DLOG", "SplachScreen->JSONException   "+e.toString());
						Toast.makeText(SplashScreen.this, "Error connecting to Server", Toast.LENGTH_LONG).show();
						finish();
					}
				
				
			} else
				startActivity(new Intent(SplashScreen.this,PinActivity.class));
				finish();
		}		
	}

	@Override
	public void httpResult(String result) {
		// TODO Auto-generated method stub
		
	}

}
