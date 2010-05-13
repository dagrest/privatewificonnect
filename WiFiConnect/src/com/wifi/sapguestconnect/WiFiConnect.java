package com.wifi.sapguestconnect;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WiFiConnect extends Activity {
	
	private WifiManager wm = null;
    private EditText userEditText;
    private EditText passEditText;
    private EditText netIDEditText;
    private TextView statusText;
    private Button connectButton;
    private ConnectLib conLib;
    private errorMessages errorCode;

    /** Called with the activity is first created. */
	@Override
    public void onCreate(Bundle icicle)
    {
        Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'onCreate' function started...");
        super.onCreate(icicle);
        setContentView(R.layout.main);
        
        // Get the EditText, TextView and Button References 
        userEditText = (EditText)findViewById(R.id.UserEditText);
        passEditText = (EditText)findViewById(R.id.PassEditText);
        netIDEditText = (EditText)findViewById(R.id.NetIDEditText);
        statusText = (TextView)findViewById(R.id.StatusText);
        connectButton = (Button)findViewById(R.id.ConnectButton); 

		if (wm == null) {
			wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		}

		conLib = new ConnectLib(this, wm);

		connectButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) 
	        {
	        	if(conLib.isLoggedInToSAP() == false)
	        	{
		    		setValue(statusText, "");
		        	//show();
		        	if(conLib.isLoginDataExist() == true && conLib.isLoginDataChanged() == false){
		        		errorCode = conLib.loginToSAPWiFi();
		        		setLogMessage(errorCode);
		        	}
		        	else{
		        		conLib.SaveLoginData(userEditText.getText().toString(), 
		        							 passEditText.getText().toString(), 
		        							 netIDEditText.getText().toString());
		        		fillLoginDataDialog();
		        		errorCode = conLib.loginToSAPWiFi();
		        		setLogMessage(errorCode);
		        	}
	        	}
	        	else{
	        		setLogMessage(errorMessages.SUCCESS);
	        	}
	        	//finish();
	        }         
        });
        
		userEditText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				conLib.setLoginDataChanged(true);
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
				conLib.setLoginDataChanged(true);
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

		netIDEditText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				conLib.setLoginDataChanged(true);
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
		conLib.LoadLoginData();
		fillLoginDataDialog();
    }

	// Convert and set error message according to error code
	private void setLogMessage(errorMessages errorCode){
		if(errorCode == errorMessages.SUCCESS){
			setValue(statusText, "Logged in successfully.");
		}
		else if(errorCode == errorMessages.FAILED){
			setValue(statusText, "Login failed.");
		}
		else if(errorCode == errorMessages.WIFI_TURNED_OFF){
			setValue(statusText, "WiFi is turned off.");
		}
		else if(errorCode == errorMessages.NOT_SAP_WIFI){
			setValue(statusText, "Not SAP WiFi.");
		}
	}
	
	// Set value to UI elements
	private void setValue(final TextView tv, final String value){
        if(value == null)
        	tv.setText("");
        else
        	tv.setText(value);
	}
	
	// Fill login data to main dialog 
	private void fillLoginDataDialog(){
		LoginData loginData = conLib.getLoginData();
		setValue(userEditText, loginData.getUser());
		setValue(passEditText, loginData.getPass());
		setValue(netIDEditText, loginData.getBssID());
		setValue(statusText, "");
		conLib.setLoginDataChanged(false);
	}

//	// Create popup window	
//	public void show(){
//		LayoutInflater inflater = this.getLayoutInflater();
//    	View view= inflater.inflate(R.layout.notification,(ViewGroup)findViewById(R.id.Notification)); 
//
//    	PopupWindow popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, false); 
//    	popupWindow.setAnimationStyle(android.R.style.Animation_Dialog); 
//
//    	setContentView(R.layout.main);
//    	
//    	TextView layout = null;
//    	try {
//        	layout=(TextView)this.findViewById(R.id.mainLayout); 
//		} catch (Exception e) {
//			String errorMessage = e.getMessage();
//			int i = 0;
//		} 
//    	try {
//			popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
//		} catch (Exception e) {
//			String errorMessage = e.getMessage();
//			int i = 0;
//		} 
//	}
}