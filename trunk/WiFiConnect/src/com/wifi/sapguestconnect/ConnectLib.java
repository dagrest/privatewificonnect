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

import android.content.Context;
import android.database.SQLException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class ConnectLib {
	private String bssID = "00:0b:86:5c:b2:42"; // SAP WiFi network ID
    private final String MY_DATABASE_TABLE = "DataTable";
	private Context context;
    private LoginData loginData = new LoginData();
    private boolean isLoginDataChanged = false;
    
	public boolean isLoginDataChanged() {
		return isLoginDataChanged;
	}

	public LoginData getLoginData(){
		return loginData;
	}
	
	public void setLoginDataChanged(boolean isLoginDataChanged) {
		this.isLoginDataChanged = isLoginDataChanged;
	}

	private WifiManager wm = null;

    public ConnectLib(final Context context, final WifiManager wm){
    	this.context = context;
    	this.wm = wm;
    }
    
    public boolean isLoginDataExist(){
		if(loginData.getUser() != null && loginData.getPass() != null && loginData.getBssID() != null &&
		   loginData.getUser() != "" && loginData.getPass() != "" && loginData.getBssID() != ""){
			return true;
		}
		return false;
	}

	public void SaveLoginData(final String user, final String pass, final String netID){
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
		loginData.setBssID(netID);

		long res = myDbHelper.saveLoginInformation(MY_DATABASE_TABLE, loginData.getUser(), loginData.getPass(), loginData.getBssID());
		loginData = myDbHelper.getLoginData(MY_DATABASE_TABLE);
		
		isLoginDataChanged = false;
		myDbHelper.close();
	}

	public void LoadLoginData(){
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
		if(loginData.getBssID() == null || loginData.getBssID().equals("")){
			loginData.setBssID(bssID);
		}
		
//		// create and save login information in database
//		if(loginData == null){
//			long res = myDbHelper.saveLoginInformation(MY_DATABASE_TABLE, user, pass, bssID);
//			loginData = myDbHelper.getLoginData(MY_DATABASE_TABLE);
//		}
		
		myDbHelper.close();
	}

	public boolean isLoggedInToSAP(){
        if(ifWifiEnabled() == true){
            String connUrl = "https://www.google.com";
            
	        HttpGet getMethod=new HttpGet(connUrl);
	        
	        try {
	        	MyResponseHandler tmpHandler = new MyResponseHandler();
	        	ResponseHandler<String> responseHandler=tmpHandler;
	        	HttpClient httpclient = new DefaultHttpClient();
	        	httpclient.execute(getMethod, responseHandler);
	        	return tmpHandler.getStatus() == 200;
	        }
	        catch (Throwable t) {
	        	String errorMessage = t.getMessage();
	        }
        }
		return false;
	}
	
	public errorMessages loginToSAPWiFi(){
		boolean isLoginSucceeded = false;
        if(ifWifiEnabled() == true){
            String macAddress = getMacAddress();
            String ipAddress = getIPAddress();
            // "https://wlan.sap.com/cgi-bin/login?cmd=login&mac=00:18:de:14:20:91&ip=192.168.143.135&essid=SAP-Guest&url=http%3A%2F%2Fwww%2Egoogle%2Ecom%2F";
            String connUrl = "https://wlan.sap.com/cgi-bin/login?cmd=login&mac=" + macAddress + "&ip=" + ipAddress + 
                             "&essid=SAP-Guest&url=http://www.google.com";

            if(getBSSID().equals(loginData.getBssID())){
    	        HttpsURLConnection httpsConnection = openConnectionToHTTPS(connUrl);
    	        logInToWiFi(httpsConnection);
    	        if(isLoggedIn(httpsConnection)){
    	        	//TODO show message login succeeded
    	        	isLoginSucceeded = true;
    	        	//setValue(statusText, "Logged in successfully.");
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
            	//TODO show message that it is not SAP WiFi currently connected
            	isLoginSucceeded = false;
            	//setValue(statusText, "Logged in successfully.");
            	return errorMessages.NOT_SAP_WIFI;
            }
        }
        else{
        	//TODO show message that WiFi turned off
        	//setValue(statusText, "WiFI is turned off.");
        	return errorMessages.WIFI_TURNED_OFF;
        }
	}
	
	public HttpsURLConnection openConnectionToHTTPS(String connUrl)
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
			String errorMessage = e.getMessage();
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
	
	public String getMacAddress() {
		String mac = null;
		if (wm != null) {
			WifiInfo wi = wm.getConnectionInfo();
			mac = wi.getMacAddress();
		}

		return mac;
	}
	
	public String getBSSID() {
		String bssid = null;

		if (wm != null) {
			WifiInfo wi = wm.getConnectionInfo();
			bssid = wi.getBSSID();
		}

		return bssid;
	}
	
	boolean ifWifiEnabled() {
		boolean isEnabled = false;

		if (wm != null) {
			isEnabled = wm.isWifiEnabled();
		}

		return isEnabled;
	}
	
	public String getIPAddress() {
		String strIP = null;
		int ip = -1;

		if (wm != null) {
			WifiInfo wi = wm.getConnectionInfo();
			ip = wi.getIpAddress();
			String binIP = Integer.toBinaryString(ip);
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
