package com.wifi.sapguestconnect;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyOnItemSelectedListener implements OnItemSelectedListener {

	private ConnectHelper connectHelper = null;
	private LogHelper logHelper;
	private boolean isLogEnabled;
	
	public MyOnItemSelectedListener(ConnectHelper connectHelper) {
		logHelper = LogHelper.getLog();
		isLogEnabled = logHelper.isLogEnabled();
		this.connectHelper = connectHelper;
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		logHelper.toLog(isLogEnabled, "MyOnItemSelectedListener -> onItemSelected() started.");
		connectHelper.getLoginData().setSSID(parent.getItemAtPosition(pos).toString());
		logHelper.toLog(isLogEnabled, "MyOnItemSelectedListener -> onItemSelected() ended.");
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
