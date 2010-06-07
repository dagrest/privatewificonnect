package com.wifi.sapguestconnect;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;

public class MessagesHandler extends Handler {
	private ProgressDialog progressDialog;
	private WiFiConnect wifiActivity;

	public MessagesHandler(ProgressDialog progressDialog,
			WiFiConnect wifiActivity){
		this.progressDialog = progressDialog;
		this.wifiActivity = wifiActivity;
	}
	
	@Override
	public void handleMessage(Message msg){
		super.handleMessage(msg);
		progressDialog.dismiss();
		if(msg.what == 1){
			wifiActivity.setLogMessage(errorMessages.ALREADY_CONNECTED);
		}else{
			wifiActivity.setLogMessage(errorMessages.NOT_CONNECTED);
		}
	}
}

