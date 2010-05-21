package test.application.com.by.david.agrest;

//import com.wifi.sapguestconnect.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainTest extends Activity {
	
	private TextView messageText;
	private Button buttonStartService;
	private Button buttonStopService;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Lock screen orientation to vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 

        messageText = (TextView)findViewById(R.id.MessageText);
        buttonStartService = (Button)findViewById(R.id.StartService);
        buttonStopService = (Button)findViewById(R.id.StopService);
        
        buttonStartService.setOnClickListener(new StartServiceOnClickListener());
        buttonStopService.setOnClickListener(new StopServiceOnClickListener(this));
        
        messageText.setText("This is initial text... And another initial text...");
    }
}