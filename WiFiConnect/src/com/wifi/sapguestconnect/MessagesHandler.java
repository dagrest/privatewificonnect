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
		isLogEnabled = logHelper.isLogEnabled();
	}
	
	@Override
	public void handleMessage(Message msg){
		logHelper.toLog(isLogEnabled, "MessagesHandler -> handleMessage() started.");
		super.handleMessage(msg);
		try {
			wifiActivity.fillLoginDataDialog();
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
		} catch (Exception e) {
			// This catch will "fix" the folllwoing problem as well:
			// Exception: view not attached to window manager ...
			// The problem is related to handling of screen orientation change 
			// when progress dialog and background thread active
			// See the following link:
			// http://stackoverflow.com/questions/1111980/how-to-handle-screen-orientation-change-when-progress-dialog-and-background-threa
			// Citation:
			// When you switch orientations, Android will create a new View. You're probably 
			// getting crashes because your background thread is trying to change the state on the old one. 
			// (It may also be having trouble because your background thread isn't on the UI thread)
			// I'd suggest making that mHandler volatile and updating it when the orientation changes.
			
			logHelper.toLog(isLogEnabled, "EXCEPTION: MessagesHandler -> handleMessage(): " + e.getMessage());
		}
		logHelper.toLog(isLogEnabled, "MessagesHandler -> handleMessage() ended.");
	}
}

