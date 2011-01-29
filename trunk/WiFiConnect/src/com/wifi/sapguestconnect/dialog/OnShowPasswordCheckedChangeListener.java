package com.wifi.sapguestconnect.dialog;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;

import android.app.Dialog;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

class OnShowPasswordCheckedChangeListener implements OnCheckedChangeListener
{
	private Dialog mDialog;
	
	public OnShowPasswordCheckedChangeListener(Dialog dialog)
	{
		LogManager.LogFunctionCall("OnShowPasswordCheckedChangeListener", "OnShowPasswordCheckedChangeListener()");
		
		this.mDialog = dialog;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
	{
		LogManager.LogFunctionCall("OnShowPasswordCheckedChangeListener", "onCheckedChanged()");
		
		EditText pwddEditText = (EditText) mDialog.findViewById(R.id.password_input);
		if (isChecked)
		{
			pwddEditText.setTransformationMethod(null);
		}
		else
		{
			pwddEditText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
		}
		return;
	}

}
