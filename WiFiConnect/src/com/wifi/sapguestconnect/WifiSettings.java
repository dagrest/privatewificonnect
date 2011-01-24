package com.wifi.sapguestconnect;

import com.wifi.sapguestconnect.data.DataFacade;
import com.wifi.sapguestconnect.dialog.IDialogResult;
import com.wifi.sapguestconnect.dialog.PasswordDialog;
import com.wifi.sapguestconnect.dialog.SelectNetworkListener;
import com.wifi.sapguestconnect.dialog.UsernameDialog;
import com.wifi.sapguestconnect.log.LogHelper;
import com.wifi.sapguestconnect.preferences.PreferencesFacade;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WifiSettings extends Activity
{
	private LogHelper mLogHelper = null;
	private boolean isLogEnabled = false;
	
	private LoginData mLoginData = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.wifi_settings);
	    
		// Init Log
	    mLogHelper = LogHelper.getLog();
		isLogEnabled = mLogHelper.isLogEnabled();
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> onCreate()");
	
	    loadLoginData();
	    
	    initLayout(); // Init UI Layout
	}
	
	
	/***
	 * UI Initializers
	 */
	private void initLayout()
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> initLayout()");
		
		initUsernameEntryLayout();
		initPasswordEntryLayout();
		initSSIDEntryLayout();
	}
	
	private void initUsernameEntryLayout() 
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> initUsernameEntryLayout()");
		
	    setViewOnClickListener(R.id.user_name_entry, new OnClickListener() {
				
	    	@Override
				public void onClick(View v) {
					UsernameDialog.show(WifiSettings.this, mLoginData.getUser(), new IDialogResult() {
						@Override
						public void OnFinish(Object result) {
							setUserName((String)result);
						}
					});
				}
			});
	}
	
	private void initPasswordEntryLayout()
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> initPasswordEntryLayout()");
		
	    setViewOnClickListener(R.id.password_entry, new OnClickListener() {
			@Override
			public void onClick(View v) {
				PasswordDialog.show(WifiSettings.this, new IDialogResult() {
					@Override
					public void OnFinish(Object result) {
						setPassword((String)result);
					}
				});
			}
		});
	}
	
	private void initSSIDEntryLayout() 
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> initSSIDEntryLayout()");
		
		setViewOnClickListener(R.id.wifi_ssid_entry, 
				new SelectNetworkListener(this, new IDialogResult() {
					
					@Override
					public void OnFinish(Object result) {
						setWifiSSID((String)result);
					}
				}
		));
	}
	
	private void setViewOnClickListener(int viewId, OnClickListener onClickListener)
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> setViewOnClickListener()");
		
	    View view = (View) findViewById(viewId);
	    view.setOnClickListener( onClickListener );
	}
	
	/***
	 * UI Setters and getters
	 */
	private void setUserName(String userName) 
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> setUserName()");
		
		validateLoginDataMember();
		
		if (userName != null)
		{
			mLoginData.setUser(userName);
		}
		else
		{
			mLoginData.setUser("");
		}
		
		// Commit to DB
		persistLoginData();
		
		// Update UI
		TextView userNameText = (TextView)findViewById(R.id.user_name_value);
		userNameText.setText(mLoginData.getUser());
	}

	protected String getUserName() 
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> getUserName()");
		
		return mLoginData.getUser();
	}

	private void setPassword(String password) 
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> setPassword()");
		
		validateLoginDataMember();
		
		if (password != null)
		{
			mLoginData.setPass(password);
		}
		else
		{
			mLoginData.setPass("");
		}
		
		// Commit to DB
		persistLoginData();
	}

	protected String getPassword() 
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> getPassword()");
		
		return mLoginData.getPass();
	}

	private void setWifiSSID(String wifiSSID) 
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> setWifiSSID()");
		
		validateLoginDataMember();
		
		if (wifiSSID != null)
		{
			mLoginData.setSSID(wifiSSID);
		}
		else
		{
			mLoginData.setSSID("");
		}
		
		// Commit to DB
		persistLoginData();
		
		// Update UI
		TextView ssidText = (TextView)findViewById(R.id.wifi_ssid_value);
		ssidText.setText(mLoginData.getSSID());
	}

	protected String getWifiSSID() 
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> getWifiSSID()");
		
		return mLoginData.getSSID();
	}
	
	private void validateLoginDataMember()
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> validateLoginDataMember()");
		
		if (mLoginData == null)
		{
			mLoginData = new LoginData();
		}
	}
	
	
	private void loadLoginData()
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> loadLoginData()");
		
	    LoginData loadedLoginData = DataFacade.LoadLoginData(this);
	    setUserName(loadedLoginData.getUser());
	    setPassword(loadedLoginData.getPass());
	    setWifiSSID(loadedLoginData.getSSID());
	}
	
	private void persistLoginData()
	{
		mLogHelper.toLog(isLogEnabled, "WifiSettings -> persistLoginData()");
		
	    DataFacade.PersistLoginData(this, mLoginData);
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		
		PreferencesFacade.refreshRunAsService(this);
	}
}
