package com.wifi.sapguestconnect;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;

public class MyResponseHandler extends BasicResponseHandler {

	private int status = -1;
	
	public int getStatus() {
		return status;
	}
	
	@Override
	public String handleResponse(HttpResponse response)
			throws HttpResponseException, IOException {
		status = response.getStatusLine().getStatusCode();
		return super.handleResponse(response);
	}

}
