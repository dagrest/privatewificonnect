package com.wifi.sapguestconnect;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiManager;
import android.view.View;

public class MyConnectOnClickListener implements View.OnClickListener {

	private WifiManager wifimanager = null;
	private ConnectHelper connectHelper = null;
	private WiFiConnect wifiActivity = null;
	
	public MyConnectOnClickListener(WiFiConnect wifiActivity, WifiManager wifimanager, ConnectHelper connectHelper) {
		this.wifimanager = wifimanager;
		this.connectHelper = connectHelper;
		this.wifiActivity = wifiActivity;
	}
	
	@Override
	public void onClick(View v) {
		if(this.wifimanager.isWifiEnabled() == false) {
			this.wifiActivity.setLogMessage(errorMessages.WIFI_TURNED_OFF);
		}
		else {
			if(this.wifiActivity.getNetworkID().getText().toString().
					compareToIgnoreCase((this.wifimanager.getConnectionInfo().getSSID())) == 0) {
				if(this.connectHelper.isLoggedInToSAP() == false) {
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
			}
			else {
				this.wifiActivity.setLogMessage(errorMessages.NOT_CORRECT_WIFI);
			}
		}
	}
}
