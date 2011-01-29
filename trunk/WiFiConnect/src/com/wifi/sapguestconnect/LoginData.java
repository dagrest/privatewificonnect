package com.wifi.sapguestconnect;

import com.wifi.sapguestconnect.log.LogManager;

public class LoginData {
	private String user;
	private String pass;
	private String ssID;
	
	public LoginData(){
		LogManager.LogFunctionCall("LoginData", "C'tor()");
		user = "";
		pass = "";
		ssID = "";
	}
	
	public LoginData(String user, String pass, String bssID){
		LogManager.LogFunctionCall("LoginData", "C'tor("+user+", *****, " + bssID + ")");
		this.user = user;
		this.pass = pass;
		this.ssID = bssID;
	}	
	
	public String getUser() {
		LogManager.LogFunctionCall("LoginData", "getUser()");
		return user;
	}
	public void setUser(String user) {
		LogManager.LogFunctionCall("LoginData", "setUser()");
		this.user = user;
	}
	public String getPass() {
		LogManager.LogFunctionCall("LoginData", "getPass()");
		return pass;
	}
	public void setPass(String pass) {
		LogManager.LogFunctionCall("LoginData", "setPass()");
		this.pass = pass;
	}
	public String getSSID() {
		LogManager.LogFunctionCall("LoginData", "getSSID()");
		return ssID;
	}
	public void setSSID(String bssID) {
		LogManager.LogFunctionCall("LoginData", "setSSID()");
		this.ssID = bssID;
	}
}
