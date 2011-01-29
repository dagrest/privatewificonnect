package com.wifi.sapguestconnect.dialog;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.common.CommonFacade;
import com.wifi.sapguestconnect.log.LogManager;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutDialog 
{
	public static void show(Context context)
	{		
		LogManager.LogFunctionCall("AboutDialog", "show()");
		
		Resources resources = context.getResources();
		Dialog dialog = new Dialog(context);

		// Set Title
		dialog.setContentView(R.layout.about_dialog);
		dialog.setTitle(resources.getString(R.string.app_name)+" "+CommonFacade.getVersionName(context));

		// Set Text
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(resources.getString(R.string.app_about_summary));
		
		// Set Image
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.sap_connect);
		
		// Make dialog cancellable on touch outside;
		dialog.setCanceledOnTouchOutside(true);
		
		dialog.show();
	}

}
