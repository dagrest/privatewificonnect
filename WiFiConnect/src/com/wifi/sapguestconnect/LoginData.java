package com.wifi.sapguestconnect;

public class LoginData {
	private String user;
	private String pass;
	private String bssID;
	
	public LoginData(){
		user = null;
		pass = null;
		bssID = null;
	}
	
	public LoginData(String user, String pass, String bssID){
		this.user = user;
		this.pass = pass;
		this.bssID = bssID;
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
	public String getBssID() {
		return bssID;
	}
	public void setBssID(String bssID) {
		this.bssID = bssID;
	}
}
