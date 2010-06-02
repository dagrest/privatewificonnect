package com.wifi.sapguestconnect;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;

public class MyConnectOnClickListener implements View.OnClickListener{

	private WifiManager wifimanager = null;
	private ConnectHelper connectHelper = null;
	private WiFiConnect wifiActivity = null;
	private ProgressDialog progressDialog = null;
	private boolean isConnected = false;
	
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

				IsLoggedInProgress isLoggedInProgress = new IsLoggedInProgress(wifiActivity, progressDialog, connectHelper);
				Thread t = new Thread(isLoggedInProgress);
		        t.start();
		        try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    if(isLoggedInProgress.isLoggedIn() == false) {
					this.wifiActivity.setStatusText("");
					//show();
					if(this.connectHelper.isLoginDataExist() == true && this.connectHelper.isLoginDataChanged() == false){
						this.wifiActivity.setLogMessage(connectHelper.loginToSAPWiFi());
					}
					else{
						this.connectHelper.saveLoginData(this.wifiActivity.getUserEditText().getText().toString(), 
								this.wifiActivity.getPassEditText().getText().toString(), 
								this.wifiActivity.getNetworkID().getText().toString());
						this.wifiActivity.fillLoginDataDialog();
						this.wifiActivity.setLogMessage(this.connectHelper.loginToSAPWiFi());
					}
				}
				else{
					this.wifiActivity.setLogMessage(errorMessages.ALREADY_CONNECTED);
					this.connectHelper.saveLoginData(this.wifiActivity.getUserEditText().getText().toString(), 
							this.wifiActivity.getPassEditText().getText().toString(), 
							this.wifiActivity.getNetworkID().getText().toString());
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
