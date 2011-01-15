package com.wifi.sapguestconnect.dialog;

import java.util.HashSet;
import java.util.List;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.View.OnClickListener;

public class SelectNetworkListener implements OnClickListener 
{
	private int WIFI_ENABLE_CHECK_TICK = 500;
	private int WIFI_ENABLE_TIMEOUT = 10000; 
	
	private WifiManager wifiManager = null;

	private Context context = null;
	private Resources resources = null;
	
	String ssidArray[]  = null;
	private LogHelper logHelper;
	private boolean isLogEnabled;
	private IDialogResult mDialogResult;
	
	public SelectNetworkListener(Context context, IDialogResult onDialogResult) 
	{
		super();
		
		// Init Log
		logHelper = LogHelper.getLog();
		isLogEnabled = logHelper.isLogEnabled();
		logHelper.toLog(isLogEnabled, "SelectNetworkListener -> C'tor()");
		
		this.context = context; // intentional no NULL check for this
		
		resources = context.getResources();
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		mDialogResult = onDialogResult; // Set Dialog Callback
	}
	
	public void onClick(View view) 
	{
		logHelper.toLog(isLogEnabled, "SelectNetworkListener-> displayWifiPickerDialog() started.");
		if (wifiManager.isWifiEnabled())
		{
			displayWifiPickerDialog();
		}
		else
		{
			displayWifiDisabledDialog();
		}
		
	}
	
	private void displayWifiPickerDialog()
	{
		logHelper.toLog(isLogEnabled, "SelectNetworkListener-> displayWifiPickerDialog() started.");

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
	
		builder.setTitle(resources.getString(R.string.pick_a_network));
		builder.setItems(ssidArray, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) 
		    {
		    	if (mDialogResult != null)
		    	{
		    		mDialogResult.OnFinish(ssidArray[item]);
		    	}
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void displayWifiDisabledDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(resources.getString(R.string.wifi_enable_question))
		       .setCancelable(false)
		       .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		               if (wifiManager.setWifiEnabled(true))
		               {
		            	   int timout = 0;
		            	   while ((wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) && (timout < WIFI_ENABLE_TIMEOUT))
		            	   {
		            		   try 
			            	   {
		            			   Thread.sleep(WIFI_ENABLE_CHECK_TICK);
		            			   timout += WIFI_ENABLE_CHECK_TICK;
			            	   } 
			            	   catch (InterruptedException e) 
			            	   {
									
			            	   }
		            	   }
		            	   
		            	   if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
		            		   displayWifiPickerDialog();
		            	   else
		            		   displayUnableToEnableWifiDialog();
		               }
		               else
		               {
		            	   displayUnableToEnableWifiDialog();
		               }
		           }
		       })
		       .setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void displayUnableToEnableWifiDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(resources.getString(R.string.wifi_enable_error))
		       .setCancelable(false)
//		       .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
//		           public void onClick(DialogInterface dialog, int id) 
//		           {
//
//		           }
//		       })
		       .setNegativeButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
}
