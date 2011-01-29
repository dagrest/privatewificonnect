package com.wifi.sapguestconnect.autoupdate;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;

class DownloadDialog 
{
	private ProgressDialog mProgressDialog = null;
	private Context mContext = null;
	private Resources mResources = null;
	
	public DownloadDialog(Context context) 
	{
		LogManager.LogFunctionCall("DownloadDialog", "C'tor()");
		
		mContext = context;
		
		mResources = context.getResources();
		
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setMessage(mResources.getString(R.string.downloading));
		mProgressDialog.setCancelable(false);		
	}
	
	public void show()
	{
		LogManager.LogFunctionCall("DownloadDialog", "show()");
		
		mProgressDialog.show();
	}
	
	public void cancel()
	{
		LogManager.LogFunctionCall("DownloadDialog", "cancel()");
		
		mProgressDialog.cancel();
	}
	
	public void updateState(DownloadStates downloadState)
	{
		LogManager.LogFunctionCall("DownloadDialog", "updateState(DownloadStates )");
		
		updateState(downloadState, -1);
	}
	
	public void updateState(DownloadStates downloadState, int progressValue)
	{
		LogManager.LogFunctionCall("DownloadDialog", "updateState(DownloadStates, progressValue)");
		
	    switch(downloadState) 
	    {
        	case MESSAGE_DOWNLOAD_STARTING :
        		mProgressDialog.setIndeterminate(true);
        		mProgressDialog.setTitle(mResources.getString(R.string.download_starting));
        		break;
        	case MESSAGE_DOWNLOAD_PROGRESS :
        		mProgressDialog.setIndeterminate(false);
        		mProgressDialog.setTitle(mResources.getString(R.string.downloading));
        		mProgressDialog.setProgress(progressValue);
        		break;
        	case MESSAGE_DOWNLOAD_COMPLETE :
        		mProgressDialog.cancel();
        	default:
    	}
	}
}
