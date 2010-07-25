package com.wifi.sapguestconnect;

import android.app.ProgressDialog;

public class IsLoggedInProgress implements Runnable {
	private ProgressDialog progressDialog;
	private ConnectHelper connectHelper;
	private boolean isLoggedIn;
	private WiFiConnect wifiActivity;
	private MessagesHandler handler;
	private LogHelper logHelper;
	private boolean isLogEnabled;
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public IsLoggedInProgress(WiFiConnect wifiActivity, final ProgressDialog progressDialog, final ConnectHelper connectHelper, MessagesHandler handler){
		this.progressDialog = progressDialog;
		this.connectHelper = connectHelper;
		this.wifiActivity = wifiActivity;
		this.handler = handler;
		logHelper = LogHelper.getLog();
		isLogEnabled = true;
	}
	
	public void run() {

		logHelper.toLog(isLogEnabled, "IsLoggedInProgress -> run() started.");
		isLoggedIn = connectHelper.isLoggedInToSAP();
		handler.sendEmptyMessage((isLoggedIn)?1:0);
		logHelper.toLog(isLogEnabled, "IsLoggedInProgress -> run() ended.");
	}
}

