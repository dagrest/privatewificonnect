package test.application.com.by.david.agrest;

import android.view.View;
import android.view.View.OnClickListener;

public class StopServiceOnClickListener implements OnClickListener{
	MainTest mainTest = null;
	
	StopServiceOnClickListener(MainTest mainTest){
		this.mainTest = mainTest;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//finish();
		mainTest.finish();
	}

}
