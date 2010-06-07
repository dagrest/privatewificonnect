package com.wifi.sapguestconnect;

import android.app.ProgressDialog;

public class IsLoggedInProgress implements Runnable {
	private ProgressDialog progressDialog;
	private ConnectHelper connectHelper;
	private boolean isLoggedIn;
	private WiFiConnect wifiActivity;
	private MessagesHandler handler;
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public IsLoggedInProgress(WiFiConnect wifiActivity, final ProgressDialog progressDialog, final ConnectHelper connectHelper, MessagesHandler handler){
		this.progressDialog = progressDialog;
		this.connectHelper = connectHelper;
		this.wifiActivity = wifiActivity;
		this.handler = handler;
	}
	
	public void run() {
		isLoggedIn = connectHelper.isLoggedInToSAP();
		handler.sendEmptyMessage((isLoggedIn)?1:0);
	}
}

