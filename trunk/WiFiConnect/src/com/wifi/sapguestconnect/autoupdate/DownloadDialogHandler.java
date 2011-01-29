package com.wifi.sapguestconnect.autoupdate;

import com.wifi.sapguestconnect.log.LogManager;

import android.os.Handler;
import android.os.Message;

class DownloadDialogHandler extends Handler 
{
	DownloadDialog mDownloadDialog;
	
	public DownloadDialogHandler(DownloadDialog downloadDialog) 
	{
		LogManager.LogFunctionCall("DownloadDialogHandler", "C'tor()");
		
		mDownloadDialog = downloadDialog;
	}
	
	@Override
	public void handleMessage(Message msg) 
	{
		LogManager.LogFunctionCall("DownloadDialogHandler", "handleMessage()");
		
		super.handleMessage(msg);
		
		DownloadStates downloadStates = (DownloadStates)msg.obj;
		
	    switch(downloadStates) 
	    {
        	case MESSAGE_DOWNLOAD_STARTING :
					mDownloadDialog.show();
        	case MESSAGE_DOWNLOAD_COMPLETE :
        			mDownloadDialog.updateState(downloadStates);
        			break;
        	case MESSAGE_DOWNLOAD_PROGRESS :
        		mDownloadDialog.updateState(downloadStates, msg.arg1*100/msg.arg2);
        		break;
        	default:
        		break;
    	}

	}
}
