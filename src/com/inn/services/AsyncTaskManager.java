package com.inn.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskManager extends AsyncTask<String, String, String> {
	
	private JSONResponseImpl jsonResponseImpl;
	private String dialog_message;
	private ProgressDialog dialog;
	
	
	public AsyncTaskManager(String dialog_message, JSONResponseImpl jsonResponseImpl,Context context) {
		this.dialog = new ProgressDialog(context);
	    this.dialog_message = dialog_message;
	    this.jsonResponseImpl = jsonResponseImpl;
	}

	public AsyncTaskManager( JSONResponseImpl jsonResponseImpl,Context context) {
		this.dialog = new ProgressDialog(context);
	    this.jsonResponseImpl = jsonResponseImpl;
	}
	@Override
	protected void onPreExecute() {
		dialog.setMessage(dialog_message);
		dialog.show();
	}

	@Override 
	protected String doInBackground(String... uri) {
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {
			Log.d("DLOG", "AsyncTaskManager.uri =  " + uri[0]);
			response = httpclient.execute(new HttpGet(uri[0]));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			// TODO Handle problems..
			Log.d("DLOG", "ClientProtocolException =  " + e.toString());
		} catch (IOException e) {
			// TODO Handle problems..
			Log.d("DLOG", "IOException =  " + e.toString());
		}
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if(result!=null)
		jsonResponseImpl.httpResult(result);
		

	}

}
