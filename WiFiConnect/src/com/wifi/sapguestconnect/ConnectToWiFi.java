package com.wifi.sapguestconnect;

import android.app.ProgressDialog;
import com.wifi.sapguestconnect.ErrorMessages.errorMessages;

public class ConnectToWiFi implements Runnable {

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

	public ConnectToWiFi(WiFiConnect wifiActivity, final ProgressDialog progressDialog, final ConnectHelper connectHelper, MessagesHandler handler){
		logHelper = LogHelper.getLog();
		isLogEnabled = true;
		this.progressDialog = progressDialog;
		this.connectHelper = connectHelper;
		this.wifiActivity = wifiActivity;
		this.handler = handler;
	}
	
	public void run() {
    	logHelper.toLog(isLogEnabled, "ConnectToWiFi -> run() started.");
		isLoggedIn = connectHelper.isLoggedInToSAP();

		String user = this.wifiActivity.getUserEditText().getText().toString();
		String pass = this.wifiActivity.getPassEditText().getText().toString();
		String ssid = this.wifiActivity.getNetworkID().getText().toString();

		if(isLoggedIn() == false) {
			//show();
			if(this.connectHelper.isLoginDataExist(user, pass, ssid) == false){
				this.connectHelper.saveLoginData(user, pass, ssid);
				this.wifiActivity.fillLoginDataDialog();
			}
			
			if(this.connectHelper.isLoginDataExist(user, pass, ssid) == true && 
				this.connectHelper.isLoginDataChanged() == false){
				handler.sendEmptyMessage(ErrorMessages.toInt(connectHelper.loginToSAPWiFi()));
			}
			else{
				this.connectHelper.saveLoginData(user, pass, ssid);
				this.wifiActivity.fillLoginDataDialog();
				handler.sendEmptyMessage(ErrorMessages.toInt(connectHelper.loginToSAPWiFi()));
			}
		}
		else{
			this.connectHelper.saveLoginData(user, pass, ssid);
			handler.sendEmptyMessage(ErrorMessages.toInt(errorMessages.ALREADY_CONNECTED));
		}
    	logHelper.toLog(isLogEnabled, "ConnectToWiFi -> run() ended.");
	}
}


