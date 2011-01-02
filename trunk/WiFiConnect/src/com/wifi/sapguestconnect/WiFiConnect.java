package com.wifi.sapguestconnect;

import java.util.Locale;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;
import com.wifi.sapguestconnect.preferences.SettingsActivity;
import com.wifi.sapguestconnect.wifi.WatchdogService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WiFiConnect extends Activity {
	
	private WifiManager wm = null;
    private EditText userEditText;
	private EditText passEditText;
    private TextView statusText;
    private Button connectButton;
    private ConnectHelper connectHelper;
    private TextView networkID = null;
    private ProgressDialog progressDialog;
    private LogHelper logHelper;
    private boolean isLogEnabled;
    private Resources resources;
	private Button selectNetworkBtn = null;

	
	@Override
    public void onResume()
    {
		logHelper.toLog(isLogEnabled, "WiFiConnect -> onResume() started.");
		super.onResume();
		
        if(! wm.isWifiEnabled()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("WiFi is disabled, please enable it and start application again") // TODO: Put String into Resource XML
			       .setCancelable(false)
			       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
//			        	   WiFiConnect.this.finish();
			           }
			       });	     
			AlertDialog alert = builder.create();
			alert.show();
		}
		else{
			progressDialog = ProgressDialog.show(this, "Please wait...", "Checking connection state...", true, false); // TODO: Put String into Resource XML
			MessagesHandler handler = new MessagesHandler(progressDialog, this);
			IsLoggedInProgress isLoggedInProgress = new IsLoggedInProgress(this, progressDialog, connectHelper, handler);
			Thread t = new Thread(isLoggedInProgress);
	        t.start();
			
			// try to delete "isLoggedInProgress" instance from memory
		    isLoggedInProgress = null;
		}
		logHelper.toLog(isLogEnabled, "WiFiConnect -> onResume() ended.");
    }	
	
    /** Called with the activity is first created. */
	@Override
    public void onCreate(Bundle icicle)
    {
		logHelper = LogHelper.getLog();
		isLogEnabled = logHelper.isLogEnabled();
		logHelper.toLog(isLogEnabled, "WiFiConnect -> onCreate() started.");

        super.onCreate(icicle);
        Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'onCreate' function started...");
        setContentView(R.layout.main);

        resources = getResources();
        
        // Lock screen orientation to vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
        // Get the EditText, TextView and Button References 
        userEditText = (EditText)findViewById(R.id.UserEditText);
        passEditText = (EditText)findViewById(R.id.PassEditText);
        statusText = (TextView)findViewById(R.id.StatusText);
        connectButton = (Button)findViewById(R.id.ConnectButton); 
        selectNetworkBtn = (Button)findViewById(R.id.NetSelectBtn); 
        networkID = (TextView)findViewById(R.id.NetworkIDText);
        
		if (wm == null) {
			logHelper.toLog(isLogEnabled, "WiFiConnect -> onCreate() -> getSystemService() started.");
			wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			logHelper.toLog(isLogEnabled, "WiFiConnect -> onCreate() -> getSystemService() ended.");
		}
		
		connectHelper = new ConnectHelper(this, wm);
		
		logHelper.toLog(isLogEnabled, "WiFiConnect -> onCreate() -> setOnClickListener() started.");
		selectNetworkBtn.setOnClickListener(new SelectNetworkListener(wm, this));
		logHelper.toLog(isLogEnabled, "WiFiConnect -> onCreate() -> setOnClickListener() ended.");
		//ssidSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener(connectHelper));

		//
		// Here were actions that were removed to onResume() action
        //
		
		logHelper.toLog(isLogEnabled, "WiFiConnect -> onCreate() -> MyConnectOnClickListener() started.");
		connectButton.setOnClickListener(new MyConnectOnClickListener(this, wm, connectHelper, progressDialog));
		logHelper.toLog(isLogEnabled, "WiFiConnect -> onCreate() -> MyConnectOnClickListener() ended.");
		
		userEditText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				connectHelper.setLoginDataChanged(true);
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// do nothing
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// do nothing
			}
		});

//		Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//		    @Override
//		    public void uncaughtException(Thread thread, Throwable ex) {
//
//		    PrintWriter pw;
//		    try {
//		        pw = new PrintWriter(
//		                new FileWriter(Environment.getExternalStorageDirectory()+"/rt.log", true));
//		        ex.printStackTrace(pw);
//		        pw.flush();
//		        pw.close();
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
//		}
//		});
		
		passEditText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				connectHelper.setLoginDataChanged(true);
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// do nothing
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// do nothing
			}
		});

		// Get login data from DB 
		if( connectHelper.LoadLoginData() ){
			fillLoginDataDialog();
		}
		
		WatchdogService.Start(this);
		
		logHelper.toLog(isLogEnabled, "WiFiConnect -> onCreate() ended.");
    } // on Create()

	EditText getUserEditText() {
		return userEditText;
	}

	TextView getNetworkID() {
		return networkID;
	}
	 
	 
	void setUserEditText(EditText userEditText) {
		this.userEditText = userEditText;
	}

	EditText getPassEditText() {
		return passEditText;
	}

	void setPassEditText(EditText passEditText) {
		this.passEditText = passEditText;
	}
	
	void setNetworkID(final String networkID) {
		this.networkID.setText(networkID);
		connectHelper.setLoginDataChanged(true);
	}
	
	// Convert and set error message according to error code
	void setLogMessage(errorMessages errorCode){
		switch (errorCode) {
		case NOT_CONNECTED: 	setValue(statusText, Color.YELLOW, "Not connected");  // TODO: Put String into Resource XML
								break;
    	case ALREADY_CONNECTED: setValue(statusText, Color.GREEN, "Already connected");  // TODO: Put String into Resource XML
    							break;
	    case SUCCESS: 			setValue(statusText, Color.GREEN, "Logged in successfully");  // TODO: Put String into Resource XML
	                   			break;
	    case FAILED: 			setValue(statusText, Color.RED ,"Login failed");		  // TODO: Put String into Resource XML
	    						break;
	    case WIFI_TURNED_OFF: 	setValue(statusText, Color.RED, "WiFi is turned off"); // TODO: Put String into Resource XML
	                    		break; 
	    case NOT_CORRECT_WIFI: 	setValue(statusText, Color.RED, "Connect to the correct WiFi"); // TODO: Put String into Resource XML
								break;
		}
	}
	
	void setStatusText(final String value){
		setValue(statusText, value);
	}
	
	private void setValue(final TextView tv, int color, final String value){
        	setValue(tv, value);
        	tv.setTextColor(color);
	}
	
	// Set value to UI elements
	private void setValue(final TextView tv, final String value){
        if(value == null) {
        	tv.setText("");	
        }
        else {
        	tv.setText(value);
        }
	}
	
	// Fill login data to main dialog 
	void fillLoginDataDialog(){
		LoginData loginData = connectHelper.getLoginData();
		setValue(userEditText, loginData.getUser());
		setValue(passEditText, loginData.getPass());
		setValue(networkID, loginData.getSSID());
		setValue(statusText, "");
		connectHelper.setLoginDataChanged(false);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		logHelper.toLog(isLogEnabled, "WiFiConnect -> onCreateOptionsMenu () started.");
		
		super.onCreateOptionsMenu(menu);
       
		
		// Settings Item
		MenuItem settingsMenuItem = menu.add(0, // Group ID
									0,  // Item ID
									0, 	// Order ID			
									resources.getString(R.string.menu_settings)); // Title
		
		
		settingsMenuItem.setIcon(R.drawable.settings_48);
		
		settingsMenuItem.setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem item) {
					            Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
					            startActivity(settingsActivity);
					            return true;
							}
		});
		
		// About Item
		MenuItem aboutMenuItem = menu.add(0, // Group ID
									1,  // Item ID
									1, 	// Order ID			
									resources.getString(R.string.menu_about)); // Title

		aboutMenuItem.setIcon(R.drawable.light_48);
		
		aboutMenuItem.setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem item) 
							{
								Context mContext = WiFiConnect.this;
								Resources resources = mContext.getResources();
								Dialog dialog = new Dialog(mContext);

								// Set Title
								dialog.setContentView(R.layout.about_dialog);
								dialog.setTitle(resources.getString(R.string.app_name));

								// Set Text
								TextView text = (TextView) dialog.findViewById(R.id.text);
								text.setText(resources.getString(R.string.app_about_summary));
								
								// Set Image
								ImageView image = (ImageView) dialog.findViewById(R.id.image);
								image.setImageResource(R.drawable.sap_connect);
								
								dialog.show();
								
								return true;
							}
		});
		
		
		// Quit Item
		MenuItem quitMenuItem = menu.add(0, // Group ID
									2,  // Item ID
									2, 	// Order ID			
									resources.getString(R.string.menu_quit)); // Title
		
		quitMenuItem.setIcon(R.drawable.exit_48);
		
		quitMenuItem.setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem item) {
								WiFiConnect.this.finish();
								return true;
							}
		});

		return true;
	}
	
//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		super.onMenuItemSelected(featureId, item);
//		
//		return true;
//	}
	


}