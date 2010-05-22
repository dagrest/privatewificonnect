package test.application.com.by.david.agrest;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

public class StartServiceOnClickListener implements OnClickListener {
	private String fileName = null;
	private Context context = null;
	
	public StartServiceOnClickListener(Context context, String fileName){
		super();
		this.fileName = fileName;
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		FileUtils fileUtils = new FileUtils(context, fileName);
		fileUtils.createFile();
		fileUtils.appendToFile(">>> time <<<");
		int i = 0;
	}

}
