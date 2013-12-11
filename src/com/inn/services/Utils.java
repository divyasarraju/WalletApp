package com.inn.services;

import com.inn.walletapp.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff;

public class Utils {
	
	static SharedPreferences sharedPref;
	static SharedPreferences.Editor editor;
	private static Utils instance;
	public static Utils getInstance(){
		
		if(instance == null)
			instance = new Utils();
		
		return instance;
	}
	
	public Utils(){ }
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		int width = (int) (bitmap.getWidth() * 1);
		int height = (int) (bitmap.getHeight() * 1);
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);
		final float roundPx = 30;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		output = Bitmap.createScaledBitmap(output,
				(int) (output.getWidth() * .5),
				(int) (output.getHeight() * .5), true);
		return output;
	}

	
	public static String formatString(String val) {
		String myVal = val;
		
		if(val==null || val.equals("null") || val.equals(""))
			myVal="0";
		
		double value = Double.parseDouble(myVal);
		return String.format("%.2f", value);

	}
	
	public static void writeToPreferences(Context c, String user, String pin){
		sharedPref = c.getSharedPreferences(c.getString(R.string.profile_key), Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		editor.putString(c.getString(R.string.profile_key_username), user);
		editor.putString(c.getString(R.string.profile_key_pin), pin);
		editor.commit();
	}
	public static String readUserFromPreferences(Context c){

		sharedPref = c.getSharedPreferences(c.getString(R.string.profile_key), Context.MODE_PRIVATE);
		return sharedPref.getString(c.getString(R.string.profile_key_username), "");
		
	}
	public void clearPreferences(Context c){
		editor = sharedPref.edit();
		editor.putString(c.getString(R.string.profile_key_username), "");
		editor.putString(c.getString(R.string.profile_key_pin), "");
		editor.commit();
	}
	public static String readPinFromPreferences(Context c){
		sharedPref = c.getSharedPreferences(c.getString(R.string.profile_key), Context.MODE_PRIVATE);
		return sharedPref.getString(c.getString(R.string.profile_key_pin), "");
		
	}
}
