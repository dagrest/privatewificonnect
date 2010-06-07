package com.wifi.sapguestconnect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.wifi.sapguestconnect.ErrorMessages.errorMessages;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class ConnectHelper {
	
    private final String MY_DATABASE_TABLE = "DataTable";
	private Context context;
    private LoginData loginData = new LoginData();
    private boolean isLoginDataChanged = false;
	ProgressDialog progressDialog = null;

	boolean isLoginDataChanged() {
		return isLoginDataChanged;
	}

	LoginData getLoginData(){
		return loginData;
	}
	
	void setLoginDataChanged(boolean isLoginDataChanged) {
		this.isLoginDataChanged = isLoginDataChanged;
	}

	private WifiManager wm = null;

    ConnectHelper(final Context context, final WifiManager wm){
    	this.context = context;
    	this.wm = wm;
    }
    
    boolean isLoginDataExist(final String user, final String pass, final String ssid){
		if(loginData.getUser() != null && loginData.getPass() != null && loginData.getSSID() != null &&
		   loginData.getUser() != "" && loginData.getPass() != "" && loginData.getSSID() != "" &&
		   loginData.getUser().equals(user) && 
		   loginData.getPass().equals(pass) && 
		   loginData.getSSID().equals(ssid)){
			return true;
		}
		return false;
	}

    boolean isConnectedToCorrectWiFi(final String ssID) {
    	return loginData.getSSID().compareToIgnoreCase(ssID) == 0;
    }
    
	void saveLoginData(final String user, final String pass, final String netID){
		DataBaseHelper myDbHelper = new DataBaseHelper(context);
		try {
				myDbHelper.createDataBase();
		} catch (IOException ioe) {
			//TODO show message login failed
			throw new Error("Unable to create database");
		}
		try {
			myDbHelper.openDataBase();
		} catch (SQLException sqle) {
			//TODO show message login failed
			throw sqle;
		}
		
		loginData.setUser(user);
		loginData.setPass(pass);
		loginData.setSSID(netID);

		myDbHelper.saveLoginInformation(MY_DATABASE_TABLE, loginData.getUser(), loginData.getPass(), loginData.getSSID());
		loginData = myDbHelper.getLoginData(MY_DATABASE_TABLE);
		
		isLoginDataChanged = false;
		myDbHelper.close();
	}

	void LoadLoginData(){
		DataBaseHelper myDbHelper = new DataBaseHelper(context);
		try {
				Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'LoadLoginData' before 'createDataBase' ...");
				myDbHelper.createDataBase();
				Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'LoadLoginData' after 'createDataBase' ...");
		} catch (IOException ioe) {
			//TODO show message login failed
			throw new Error("Unable to create database");
		}
		try {
			Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'LoadLoginData' before 'openDataBase' ...");
			myDbHelper.openDataBase();
			Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'LoadLoginData' after 'openDataBase' ...");
		} catch (SQLException sqle) {
			//TODO show message login failed
			throw sqle;
		}
		
		loginData = myDbHelper.getLoginData(MY_DATABASE_TABLE);
		String ssid = loginData.getSSID();
		if(ssid != null && ssid.length() > 0){
			loginData.setSSID(ssid);
		}
		
//		// create and save login information in database
//		if(loginData == null){
//			long res = myDbHelper.saveLoginInformation(MY_DATABASE_TABLE, user, pass, bssID);
//			loginData = myDbHelper.getLoginData(MY_DATABASE_TABLE);
//		}
		
		myDbHelper.close();
	}

//	void ProgressBarDialog(Context context, boolean toShowDialog){
//		ProgressDialog progressDialog = null;
//		if(toShowDialog == true){
//			progressDialog = new ProgressDialog(context);
//			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			progressDialog.setMessage("Please wait...");
//			progressDialog.setCancelable(false);
//		}
//		else{
//			progressDialog.dismiss();
//		}
//	}
	
	boolean isLoggedInToSAPWithProgress(){
		boolean isLoggedIn = false;

		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Connecting ...");
		progressDialog.setTitle("Please wait...");
		progressDialog.setIndeterminate(true);
		progressDialog.show();
		//progressDialog = ProgressDialog.show(context, "Please wait...", "Connecting ...", true, true);
		
		new Thread() {
            public void run() {
                try{
                    // Do some Fake-Work
		        	sleep(5000);
		        }
	            catch (Exception e) 
	            { 
	            	
	            }
	            // Dismiss the Dialog
	            progressDialog.dismiss();
            }
		}.start();
		
		return isLoggedIn;
	}
	
	boolean isLoggedInToSAP(){
		boolean isLoggedInToSAP = false; 
		if(ifWifiEnabled() == true){
			String connUrl = "https://www.google.com";
            
	        HttpGet getMethod=new HttpGet(connUrl);
	        
	        try {
	        	MyResponseHandler tmpHandler = new MyResponseHandler();
	        	ResponseHandler<String> responseHandler=tmpHandler;
	        	HttpClient httpclient = new DefaultHttpClient();
	        	httpclient.execute(getMethod, responseHandler);
	        	isLoggedInToSAP = (tmpHandler.getStatus() == 200);
	        }
	        catch (Throwable t) {
	        	//TODO save error message to log
	        	String errorMessage = t.getMessage();
	        }
        }
		return isLoggedInToSAP;
	}
	
	errorMessages loginToSAPWiFi(){
        if(ifWifiEnabled() == true){
            String macAddress = getMacAddress();
            String ipAddress = getIPAddress();
            // "https://wlan.sap.com/cgi-bin/login?cmd=login&mac=00:18:de:14:20:91&ip=192.168.143.135&essid=SAP-Guest&url=http%3A%2F%2Fwww%2Egoogle%2Ecom%2F";
            String connUrl = "https://wlan.sap.com/cgi-bin/login?cmd=login&mac=" + macAddress + "&ip=" + ipAddress + 
                             "&essid=SAP-Guest&url=http://www.google.com";

            if(getSSID().equals(loginData.getSSID())){
    	        HttpsURLConnection httpsConnection = openConnectionToHTTPS(connUrl);
    	        logInToWiFi(httpsConnection);
    	        if(isLoggedIn(httpsConnection)){
    	        	//show message login succeeded
    	        	return errorMessages.SUCCESS;
    	        }
    	        else{
    	        	//TODO show message login failed
    	        	//statusText.setTextColor(0xFF000000);
    	        	//setValue(statusText, "Login FAILED.");
    	        	return errorMessages.FAILED;
    	        }
            }
            else{
            	//show message that it is not SAP WiFi currently connected
            	return errorMessages.NOT_CORRECT_WIFI;
            }
        }
        else{
        	//TODO show message that WiFi turned off
        	//setValue(statusText, "WiFI is turned off.");
        	return errorMessages.WIFI_TURNED_OFF;
        }
	}
	
	 HttpsURLConnection openConnectionToHTTPS(String connUrl)
	{
		HttpsURLConnection httpsConnection = null;
		try {
			// Trust every server - dont check for any certificate 
	        // Create a trust manager that does not validate certificate chains 
			TrustManager[] trustAllCerts = new TrustManager[] { 
				new X509TrustManager(){
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
	                        return new java.security.cert.X509Certificate[] {}; 
	                } 
	 
	                public void checkClientTrusted(X509Certificate[] chain, 
	                                String authType) throws CertificateException { 
	                } 
	 
	                public void checkServerTrusted(X509Certificate[] chain, 
	                                String authType) throws CertificateException { 
	                } 
				}
			}; 

			URL url = new URL(connUrl);
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			// http://stackoverflow.com/questions/995514/https-connection-android
			sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());
			
			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext
					.getSocketFactory());
			httpsConnection = (HttpsURLConnection) url.openConnection();
			/*
			POST /cgi-bin/login HTTP/1.1
			Accept: *//*
			Referer: https://wlan.sap.com/cgi-bin/login?cmd=login&mac=00:18:de:14:20:91&ip=192.168.143.135&essid=SAP-Guest&url=http%3A%2F%2Fwww%2Egoogle%2Ecom%2F
			Accept-Language: en-gb
			User-Agent: Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)
			Content-Type: application/x-www-form-urlencoded
			Accept-Encoding: gzip, deflate
			Host: wlan.sap.com
			Content-Length: 60
			Connection: Keep-Alive
			Cache-Control: no-cache
			Cookie: CPsession=http%3A%2F%2Fwww%2Egoogle%2Ecom%2F%26ip%3D192%2E168%2E143%2E135
			
			BODY:
			user=USER&password=PASSWORD&cmd=authenticate&Login=Log+In
			*/
			httpsConnection.setRequestMethod("POST");
			httpsConnection.setDoInput(true);
			httpsConnection.setDoOutput(true);
			httpsConnection.setRequestProperty("Accept", "*/*");
			httpsConnection.setRequestProperty("Accept-Language", "en-gb");
			httpsConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpsConnection.setRequestProperty("Host", "wlan.sap.com");
			httpsConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		}
		catch(Exception e)
		{
			//String errorMessage = e.getMessage();
			//TODO write to Android LOG
		}
		return httpsConnection;
	}
	//http://androidforums.com/android-applications/60650-send-data-https-android-application.html
	//http://www.anddev.org/viewtopic.php?p=40035
	//http://www.softwarepassion.com/android-series-get-post-and-multipart-post-requests/
	//http://stackoverflow.com/questions/995514/https-connection-android

	private void logInToWiFi(HttpsURLConnection httpsConnection){
        // add url form parameters
		OutputStream ostream = null;
		try {
			ostream = httpsConnection.getOutputStream();
			byte[] b = ("user=" + loginData.getUser() + "&password=" + loginData.getPass() + "&cmd=authenticate&Login=Log+In").getBytes();
			ostream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
			//TODO write to Android LOG
		} finally {
			if (ostream != null) {
				try {
					ostream.flush();
					ostream.close();
				} catch (IOException e) {
					e.printStackTrace();
					//TODO write to Android LOG
				}
			}
		}
	}
	
	private boolean isLoggedIn(HttpsURLConnection httpsConnection){
		boolean isLoggedIn = false;
		Object contents;
		try {
			int responseCode = httpsConnection.getResponseCode();
			String u = httpsConnection.getContentType();
			contents = httpsConnection.getContent();
			if (contents != null) {
				InputStream is = (InputStream) contents;
				StringBuffer buf = new StringBuffer();
				int c;
				while ((c = is.read()) != -1) {
					buf.append((char) c);
				}
				String response = buf.toString();
				if(response.contains("User Authenticated")){
					// Logged in successfully
					isLoggedIn = true;
				}
			}
			httpsConnection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO write to Android LOG
		}
		return isLoggedIn;
	}
	
	String getMacAddress() {
		String mac = null;
		if (wm != null) {
			WifiInfo wi = wm.getConnectionInfo();
			mac = wi.getMacAddress();
		}

		return mac;
	}
	
	String getSSID() {
		String ssid = null;

		if (wm != null) {
			WifiInfo wi = wm.getConnectionInfo();
			ssid = wi.getSSID();
		}

		return ssid;
	}
	
	boolean ifWifiEnabled() {
		boolean isEnabled = false;

		if (wm != null) {
			isEnabled = wm.isWifiEnabled();
		}

		return isEnabled;
	}
	
	String getIPAddress() {
		String strIP = null;
		int ip = -1;

		if (wm != null) {
			WifiInfo wi = wm.getConnectionInfo();
			ip = wi.getIpAddress();
			StringBuilder binIP = new StringBuilder(Integer.toBinaryString(ip));
			int len = binIP.length();
			for(int i = 0 ; i < 32 - len; ++i) {
				binIP.insert(0, '0');
			}
			
			StringBuilder stringIP = new StringBuilder();
			
			for (int i = 3; i >= 0; --i) {
				stringIP.append(Integer.toString(Integer.parseInt(binIP.substring(i * 8, i * 8 + 8), 2)));
				if (i > 0) {
					stringIP.append('.');
				}
			}
			strIP = stringIP.toString();
		}
		return strIP;
	}
}
