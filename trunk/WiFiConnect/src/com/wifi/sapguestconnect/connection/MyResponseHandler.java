package com.wifi.sapguestconnect.connection;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;

import com.wifi.sapguestconnect.log.LogManager;

class MyResponseHandler extends BasicResponseHandler { // TODO refactor

	private int status = -1;
	
	public int getStatus() {
		LogManager.LogFunctionCall("MyResponseHandler", "getStatus()");
		
		return status;
	}
	
	@Override
	public String handleResponse(HttpResponse response)
			throws HttpResponseException, IOException {
		
		LogManager.LogFunctionCall("MyResponseHandler", "handleResponse()");
		
		status = response.getStatusLine().getStatusCode();
		return super.handleResponse(response);
	}

}
