package com.wifi.sapguestconnect;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;

import android.app.ProgressDialog;
import android.net.wifi.WifiManager;
import android.view.View;

public class MyConnectOnClickListener implements View.OnClickListener{

	private WifiManager wifimanager = null;
	private ConnectHelper connectHelper = null;
	private WiFiConnect wifiActivity = null;
	private ProgressDialog progressDialog = null;
	
	public MyConnectOnClickListener(WiFiConnect wifiActivity, WifiManager wifimanager, ConnectHelper connectHelper, ProgressDialog progressDialog) {
		this.wifimanager = wifimanager;
		this.connectHelper = connectHelper;
		this.wifiActivity = wifiActivity;
		this.progressDialog = progressDialog;
	}
	
	public void onClick(View v) {

	    if(this.wifimanager.isWifiEnabled() == false) {
			this.wifiActivity.setLogMessage(errorMessages.WIFI_TURNED_OFF);
		}
		else {
			if(this.wifiActivity.getNetworkID().getText().toString().
					compareToIgnoreCase((this.wifimanager.getConnectionInfo().getSSID())) == 0) {

				progressDialog = ProgressDialog.show(wifiActivity, "Working..", "Connecting...", true,
		                false);

				MessagesHandler handler = new MessagesHandler(progressDialog, wifiActivity);

				IsLoggedInProgress isLoggedInProgress = new IsLoggedInProgress(wifiActivity, progressDialog, connectHelper, handler);
				Thread t = new Thread(isLoggedInProgress);
		        t.start();
				
				String user = this.wifiActivity.getUserEditText().getText().toString();
				String pass = this.wifiActivity.getPassEditText().getText().toString();
				String ssid = this.wifiActivity.getNetworkID().getText().toString();
				this.wifiActivity.setStatusText("");
				
//			    if(isLoggedInProgress.isLoggedIn() == false) {
//					//show();
//					if(this.connectHelper.isLoginDataExist(user, pass, ssid) == true && 
//						this.connectHelper.isLoginDataChanged() == false){
//							this.wifiActivity.setLogMessage(connectHelper.loginToSAPWiFi());
//					}
//					else{
//						this.connectHelper.saveLoginData(user, pass, ssid);
//						this.wifiActivity.fillLoginDataDialog();
//						this.wifiActivity.setLogMessage(this.connectHelper.loginToSAPWiFi());
//					}
//				}
//				else{
//					this.wifiActivity.setLogMessage(errorMessages.ALREADY_CONNECTED);
//					this.connectHelper.saveLoginData(user, pass, ssid);
//				}
				if(this.connectHelper.isLoginDataChanged() == true){
					this.connectHelper.saveLoginData(user, pass, ssid);
					this.wifiActivity.fillLoginDataDialog();
				}
				// try to delete "isLoggedInProgress" instance from memory
			    isLoggedInProgress = null;
			}
			else {
				this.wifiActivity.setLogMessage(errorMessages.NOT_CORRECT_WIFI);
			}
		}
	}
}
