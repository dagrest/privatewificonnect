package com.wifi.sapguestconnect;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

public class IsLoggedInProgress implements Runnable {
	private ProgressDialog progressDialog;
	private ConnectHelper connectHelper;
	private boolean isLoggedIn;
	private WiFiConnect wifiActivity;
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}


	public IsLoggedInProgress(WiFiConnect wifiActivity, final ProgressDialog progressDialog, final ConnectHelper connectHelper){
		this.progressDialog = progressDialog;
		this.connectHelper = connectHelper;
		this.wifiActivity = wifiActivity;
	}
	
	public void run() {
		isLoggedIn = connectHelper.isLoggedInToSAP();
		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
		}
	};
}

