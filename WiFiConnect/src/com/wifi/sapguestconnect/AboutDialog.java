package com.wifi.sapguestconnect;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutDialog 
{
	public static void show(Context context)
	{
		Resources resources = context.getResources();
		Dialog dialog = new Dialog(context);

		// Set Title
		dialog.setContentView(R.layout.about_dialog);
		dialog.setTitle(resources.getString(R.string.app_name));

		// Set Text
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(resources.getString(R.string.app_about_summary));
		
		// Set Image
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.sap_connect);
		
		dialog.show();
	}
}
