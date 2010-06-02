package com.wifi.sapguestconnect;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyOnItemSelectedListener implements OnItemSelectedListener {

	private ConnectHelper connectHelper = null;
	
	public MyOnItemSelectedListener(ConnectHelper connectHelper) {
		this.connectHelper = connectHelper;
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		connectHelper.getLoginData().setSSID(parent.getItemAtPosition(pos).toString());
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
