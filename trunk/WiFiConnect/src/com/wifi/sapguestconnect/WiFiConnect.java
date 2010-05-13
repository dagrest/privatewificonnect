package com.wifi.sapguestconnect;

import java.util.HashSet;
import java.util.List;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;
import android.app.Activity;
import android.content.Context;
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
    private String[] ssidArray = null;
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
        ssidSpinner = (Spinner) findViewById(R.id.Spinner01);
        
		if (wm == null) {
			wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		}
		
		connectHelper = new ConnectHelper(this, wm);
		ssidSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener(connectHelper));
		
		if(wm.isWifiEnabled()) {
			List<ScanResult> scanRes = wm.getScanResults();
			
			HashSet<String> tmpSet = new HashSet<String>(scanRes.size());
			
			for(ScanResult res : scanRes) {
				tmpSet.add(res.SSID);
			}
			
			ssidArray = new String[tmpSet.size()];
			int i = 0;
			for(String ssid : tmpSet) {
				ssidArray[i++] = ssid;
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ssidArray);   
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);                  
			ssidSpinner.setAdapter(adapter); 
			
			connectButton.setOnClickListener(new MyConnectOnClickListener(this, wm, connectHelper));
		}
		
		

//		connectButton.setOnClickListener(new View.OnClickListener() {
//	        public void onClick(View v) 
//	        {
//	        	if(connectHelper.isLoggedInToSAP() == false)
//	        	{
//		    		setValue(statusText, "");
//		        	//show();
//		        	if(connectHelper.isLoginDataExist() == true && connectHelper.isLoginDataChanged() == false){
//		        		errorCode = connectHelper.loginToSAPWiFi();
//		        		setLogMessage(errorCode);
//		        	}
//		        	else{
//		        		connectHelper.SaveLoginData(userEditText.getText().toString(), 
//		        							 passEditText.getText().toString(), 
//		        							 netIDEditText.getText().toString());
//		        		fillLoginDataDialog();
//		        		errorCode = connectHelper.loginToSAPWiFi();
//		        		setLogMessage(errorCode);
//		        	}
//	        	}
//	        	else{
//	        		setLogMessage(errorMessages.SUCCESS);
//	        	}
//	        	//finish();
//	        }         
//        });
        
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

//		netIDEditText.addTextChangedListener(new TextWatcher() {
//			public void afterTextChanged(Editable s) {
//				connectHelper.setLoginDataChanged(true);
//			}
//
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// do nothing
//			}
//
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// do nothing
//			}
//		});

		// Get login data from DB 
		connectHelper.LoadLoginData();
		fillLoginDataDialog();
    }

	public EditText getUserEditText() {
		return userEditText;
	}

	public void setUserEditText(EditText userEditText) {
		this.userEditText = userEditText;
	}

	public EditText getPassEditText() {
		return passEditText;
	}

	public void setPassEditText(EditText passEditText) {
		this.passEditText = passEditText;
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
		setValue(statusText, 65407, value);
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
		//setValue(netIDEditText, loginData.getSSID());
		setValue(statusText, "");
		connectHelper.setLoginDataChanged(false);
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