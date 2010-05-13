package com.wifi.sapguestconnect;

import java.util.HashSet;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

public class SelectNetworkListener implements OnClickListener {

	private WifiManager wifiManager = null;
	private WiFiConnect activity = null;
	String ssidArray[]  = null;
	public SelectNetworkListener(WifiManager wifiManager, WiFiConnect activity) {
		this.wifiManager = wifiManager;
		this.activity = activity;
	}
	
	@Override
	public void onClick(View v) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		List<ScanResult> scanRes = this.wifiManager.getScanResults();
		
		HashSet<String> tmpSet = new HashSet<String>(scanRes.size());
		
		for(ScanResult res : scanRes) {
			tmpSet.add(res.SSID);
		}
		
		ssidArray = new String[tmpSet.size()];
		int i = 0;
		for(String ssid : tmpSet) {
			ssidArray[i++] = ssid;
		}
	
		
		builder.setTitle("Pick a network");
		builder.setItems(ssidArray, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	activity.setNetworkID(ssidArray[item]);
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
