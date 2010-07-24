package com.wifi.sapguestconnect;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;

public class MessagesHandler extends Handler {
	private ProgressDialog progressDialog;
	private WiFiConnect wifiActivity;
	private LogHelper logHelper;
	private boolean isLogEnabled;
	
	public MessagesHandler(ProgressDialog progressDialog,
			WiFiConnect wifiActivity){
		this.progressDialog = progressDialog;
		this.wifiActivity = wifiActivity;
		logHelper = LogHelper.getLog();
		isLogEnabled = true;
	}
	
	@Override
	public void handleMessage(Message msg){
		logHelper.toLog(isLogEnabled, "MessagesHandler -> handleMessage() started.");
		super.handleMessage(msg);
		switch (msg.what) {
			case 0: wifiActivity.setLogMessage(errorMessages.NOT_CONNECTED);
					break;
			case 1: wifiActivity.setLogMessage(errorMessages.ALREADY_CONNECTED);
					break;
			case 2: wifiActivity.setLogMessage(errorMessages.SUCCESS);
					break;
			case 3: wifiActivity.setLogMessage(errorMessages.FAILED);
					break;
			case 4: wifiActivity.setLogMessage(errorMessages.WIFI_TURNED_OFF);
					break;
			case 5: wifiActivity.setLogMessage(errorMessages.NOT_CORRECT_WIFI);
					break;
		}
		progressDialog.dismiss();
		logHelper.toLog(isLogEnabled, "MessagesHandler -> handleMessage() ended.");
	}
}

