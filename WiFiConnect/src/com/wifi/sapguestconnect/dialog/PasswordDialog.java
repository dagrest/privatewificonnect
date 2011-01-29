package com.wifi.sapguestconnect.dialog;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

public class PasswordDialog extends OkCancelDialog
{
	protected PasswordDialog(Context context, IDialogResult dialogResultCallback) 
	{
		super(context, R.layout.password_dialog, R.id.ok_button, R.id.cancel_button, dialogResultCallback);
		
		LogManager.LogFunctionCall("PasswordDialog", "PasswordDialog()");
		
		Resources resources = context.getResources();
		
		// Set Title
		setTitle(resources.getString(R.string.password));
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); // Workaround for a bug where Fill_Parent doesn't work in XML
		
		// Set show password functionality
		CheckBox checkBox = (CheckBox) findViewById(R.id.show_password_checkbox);
		checkBox.setOnCheckedChangeListener(new OnShowPasswordCheckedChangeListener(this));
	}

	
	@Override
	protected String getResult() 
	{
		LogManager.LogFunctionCall("PasswordDialog", "getResult()");
		
		EditText pwdEditText = (EditText)findViewById(R.id.password_input);
		return pwdEditText.getText().toString();
	}
	
	
	public static void show(Context context, IDialogResult dialogResultCallback)
	{
		LogManager.LogFunctionCall("PasswordDialog", "show()");
		
		Dialog dialog = new PasswordDialog(context, dialogResultCallback);
	
		dialog.show();
	}

}
