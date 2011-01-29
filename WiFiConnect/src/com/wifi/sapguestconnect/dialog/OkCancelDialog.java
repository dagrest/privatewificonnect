package com.wifi.sapguestconnect.dialog;

import com.wifi.sapguestconnect.log.LogManager;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public abstract class OkCancelDialog extends Dialog
{
	private IDialogResult mDialogResult = null;
	
	public OkCancelDialog(Context context, int contentView, int okBtnId, int cancelBtnId, IDialogResult dialogResultCallback) 
	{		
		super(context);
		
		LogManager.LogFunctionCall("OkCancelDialog", "OkCancelDialog()");
		
		setContentView(contentView);
		
		mDialogResult = dialogResultCallback;
		
		Button okBtn = (Button) findViewById(okBtnId);	
		if (okBtn != null)
			okBtn.setOnClickListener(new OnOkClickListener());
		
		Button cancelBtn = (Button) findViewById(cancelBtnId);	
		if (cancelBtn != null)
			cancelBtn.setOnClickListener(new OnCancelClickListener());
	}
	
    private class OnOkClickListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
        	LogManager.LogFunctionCall("OkCancelDialog.OnOkClickListener", "onClick()");
        	
			if( mDialogResult != null ){
                mDialogResult.OnFinish(getResult());
            }
			OkCancelDialog.this.dismiss();
        }
    }
    
    private class OnCancelClickListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) 
        {
        	LogManager.LogFunctionCall("OkCancelDialog.OnCancelClickListener", "onClick()");
        	
			OkCancelDialog.this.cancel();
        }
    }
    
    protected abstract Object getResult();
}
