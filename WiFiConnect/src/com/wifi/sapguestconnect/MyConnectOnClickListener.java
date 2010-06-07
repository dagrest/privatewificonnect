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

				progressDialog = ProgressDialog.show(wifiActivity, "Please wait...", "Connecting...", true,
		                false);

				MessagesHandler handler = new MessagesHandler(progressDialog, wifiActivity);

				ConnectToWiFi connectToWiFi = new ConnectToWiFi(wifiActivity, progressDialog, connectHelper, handler);
				Thread t = new Thread(connectToWiFi);
		        t.start();
				
				this.wifiActivity.setStatusText("");
			}
			else {
				this.wifiActivity.setLogMessage(errorMessages.NOT_CORRECT_WIFI);
			}
		}
	}
}
