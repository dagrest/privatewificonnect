package com.wifi.sapguestconnect.autoupdate;

import android.os.Handler;
import android.os.Message;

class DownloadDialogHandler extends Handler 
{
	DownloadDialog mDownloadDialog;
	
	public DownloadDialogHandler(DownloadDialog downloadDialog) 
	{
		mDownloadDialog = downloadDialog;
	}
	
	@Override
	public void handleMessage(Message msg) 
	{
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
