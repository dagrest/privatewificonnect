package com.wifi.sapguestconnect;

public class LoginData {
	private String user;
	private String pass;
	private String ssID;
	
	public LoginData(){
		user = "";
		pass = "";
		ssID = "";
	}
	
	public LoginData(String user, String pass, String bssID){
		this.user = user;
		this.pass = pass;
		this.ssID = bssID;
	}	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getSSID() {
		return ssID;
	}
	public void setSSID(String bssID) {
		this.ssID = bssID;
	}
}
