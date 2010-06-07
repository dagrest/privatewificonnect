package com.wifi.sapguestconnect;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
    boolean isConnected = false;
    
	private Button selectNetworkBtn = null;

//	@Override
//    public void onStart()
//    {
//		super.onStart();
//    }
//
//	@Override
//    public void onPause()
//    {
//		super.onPause();
//    }

	@Override
    public void onResume()
    {
		super.onResume();
		
        if(! wm.isWifiEnabled()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("WiFi is disabled, please enable it and start application again")
			       .setCancelable(false)
			       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   WiFiConnect.this.finish();
			           }
			       });	     
			AlertDialog alert = builder.create();
			alert.show();
		}
		else{
			progressDialog = ProgressDialog.show(this, "Please wait...", "Checking connection state...", true,
                    false);
			MessagesHandler handler = new MessagesHandler(progressDialog, this);
			IsLoggedInProgress isLoggedInProgress = new IsLoggedInProgress(this, progressDialog, connectHelper, handler);
			Thread t = new Thread(isLoggedInProgress);
	        t.start();
			
			// try to delete "isLoggedInProgress" instance from memory
		    isLoggedInProgress = null;
		}
    }	
	
    /** Called with the activity is first created. */
	@Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'onCreate' function started...");
        setContentView(R.layout.main);

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
			wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		}
		
		connectHelper = new ConnectHelper(this, wm);
		
		selectNetworkBtn.setOnClickListener(new SelectNetworkListener(wm, this));
		//ssidSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener(connectHelper));

		//
		// Here were actions that were removed to onResume() action
        //
		
		connectButton.setOnClickListener(new MyConnectOnClickListener(this, wm, connectHelper, progressDialog));
		
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
		connectHelper.LoadLoginData();
		fillLoginDataDialog();
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
		case NOT_CONNECTED: 	setValue(statusText, Color.YELLOW, "Not connected");
								break;
    	case ALREADY_CONNECTED: setValue(statusText, Color.GREEN, "Already connected");
    							break;
	    case SUCCESS: 			setValue(statusText, Color.GREEN, "Logged in successfully");
	                   			break;
	    case FAILED: 			setValue(statusText, Color.RED ,"Login failed");		
	    						break;
	    case WIFI_TURNED_OFF: 	setValue(statusText, Color.RED, "WiFi is turned off");
	                    		break;
	    case NOT_CORRECT_WIFI: 	setValue(statusText, Color.RED, "Connect to the correct WiFi");
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
}