package com.wifi.sapguestconnect;

import java.util.HashSet;
import java.util.List;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class WiFiConnect extends Activity {
	
	private WifiManager wm = null;
    private EditText userEditText;
	private EditText passEditText;
    //private EditText netIDEditText;
    private TextView statusText;
    private Button connectButton;
    private ConnectHelper connectHelper;
    private errorMessages errorCode;
    private Spinner ssidSpinner = null;
    private TextView networkID = null;
   

	private Button selectNetworkBtn = null;
    
    /** Called with the activity is first created. */
	@Override
    public void onCreate(Bundle icicle)
    {
        Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'onCreate' function started...");
        super.onCreate(icicle);
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
			if(this.connectHelper.isLoggedInToSAP() == true) {
				this.setLogMessage(errorMessages.ALREADY_CONNECTED);
			}
		}
        
		connectButton.setOnClickListener(new MyConnectOnClickListener(this, wm, connectHelper));
		
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
    }

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
	}
	
	// Convert and set error message according to error code
	void setLogMessage(errorMessages errorCode){
		this.errorCode = errorCode;
		if(errorCode == errorMessages.SUCCESS){
			setValue(statusText, Color.GREEN,"Logged in successfully");
		}
		else if(errorCode == errorMessages.FAILED){
			setValue(statusText, Color.RED ,"Login failed");
		}
		else if(errorCode == errorMessages.WIFI_TURNED_OFF){
			setValue(statusText, Color.RED, "WiFi is turned off");
		}
		else if(errorCode == errorMessages.NOT_CORRECT_WIFI){
			setValue(statusText, Color.RED, "Connect to the correct WiFi");
		}
		else if(errorCode == errorMessages.ALREADY_CONNECTED){
			setValue(statusText, Color.GREEN, "Already connected");
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