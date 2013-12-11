/**
 * 
 */
package com.inn.data;

import org.json.JSONArray;

/**
 * @author e698190
 *
 */
public class AccountsDataManager {
	
	private String mAccID, mAccBalance,mEmail,muserId,mImage,mPIn;
	/**
	 * @return the mPIn
	 */
	public String getmPIn() {
		return mPIn;
	}
	/**
	 * @param mPIn the mPIn to set
	 */
	public void setmPIn(String mPIn) {
		this.mPIn = mPIn;
	}
	/**
	 * @return the mImage
	 */
	public String getmImage() {
		return mImage;
	}
	/**
	 * @param mImage the mImage to set
	 */
	public void setmImage(String mImage) { 
		this.mImage = mImage;
	}
	/**
	 * @return the muserId
	 */
	public String getMuserId() {
		return muserId;
	}
	/**
	 * @param muserId the muserId to set
	 */
	public void setMuserId(String muserId) {
		this.muserId = muserId;
	}
	private JSONArray mreceiveTrans,msentTrans, mtransactions;
	
	/**
	 * @return the mAccID
	 */
	public String getmAccID() {
		return mAccID;
	}
	/**
	 * @param mAccID the mAccID to set
	 */
	public void setmAccID(String mAccID) {
		this.mAccID = mAccID;
	}
	/**
	 * @return the mAccBalance
	 */
	public String getmAccBalance() {
		return mAccBalance;
	}
	/**
	 * @param mAccBalance the mAccBalance to set
	 */
	public void setmAccBalance(String mAccBalance) {
		this.mAccBalance = mAccBalance;
	}
	/**
	 * @return the mEmail
	 */
	public String getmEmail() {
		return mEmail;
	}
	/**
	 * @param mEmail the mEmail to set
	 */
	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	/**
	 * @return the mRecepients
	 */
	public JSONArray getmRecepients() {
		return mtransactions;
	}
	/**
	 * @param mRecepients the mRecepients to set
	 */
	public void setmRecepients(JSONArray mtransactions) {
		this.mtransactions = mtransactions;
	}
	/**
	 * @return the mreceiveTrans
	 */
	public JSONArray getMreceiveTrans() {
		return mreceiveTrans;
	}
	/**
	 * @param mreceiveTrans the mreceiveTrans to set
	 */
	public void setMreceiveTrans(JSONArray mreceiveTrans) {
		this.mreceiveTrans = mreceiveTrans;
	}
	/**
	 * @return the msendTrans
	 */
	public JSONArray getMsendTrans() {
		return msentTrans;
	}
	/**
	 * @param msendTrans the msendTrans to set
	 */
	public void setMsendTrans(JSONArray msentTrans) {
		this.msentTrans = msentTrans;
	}
	/**
	 * @param instance the instance to set
	 */
	public static void setInstance(AccountsDataManager instance) {
		AccountsDataManager.instance = instance;
	}
	private static AccountsDataManager instance=null;
	public AccountsDataManager() {
		// TODO Auto-generated constructor stub
	}
	public static AccountsDataManager getInstance(){
		if(instance==null)
			instance = new AccountsDataManager();
		return instance;
	}
}
