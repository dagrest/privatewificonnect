package com.wifi.sapguestconnect.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;

public class UsernameDialog extends OkCancelDialog
{

	protected UsernameDialog(Context context, String initialUsername, IDialogResult dialogResultCallback) 
	{
		super(context, R.layout.username_dialog, R.id.ok_button, R.id.cancel_button, dialogResultCallback);
		
		LogManager.LogFunctionCall("UsernameDialog", "C'tor()");
		
		Resources resources = context.getResources();
		
		// Set Title
		setTitle(resources.getString(R.string.user_name));
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); // Workaround for a bug where Fill_Parent doesn't work in XML
		
		// Set Initial Username
		EditText usernameEditText = (EditText)findViewById(R.id.username_input);
		usernameEditText.setText(initialUsername);
	}
	
	
	@Override
	protected String getResult() 
	{
		LogManager.LogFunctionCall("UsernameDialog", "getResult()");
		
		EditText usernameEditText = (EditText)findViewById(R.id.username_input);
		return usernameEditText.getText().toString();
	}
	
	
	public static void show(Context context, IDialogResult dialogResultCallback)
	{
		LogManager.LogFunctionCall("UsernameDialog", "show(Context, String)");
		
		show(context, "", dialogResultCallback);
	}
	
	public static void show(Context context, String initialUsername, IDialogResult dialogResultCallback)
	{	
		LogManager.LogFunctionCall("UsernameDialog", "show(Context, String, IDialogResult)");
		
		Dialog dialog = new UsernameDialog(context, initialUsername, dialogResultCallback);
		
		dialog.show();
	}

}
