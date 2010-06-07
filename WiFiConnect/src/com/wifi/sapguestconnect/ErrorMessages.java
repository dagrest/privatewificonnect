package com.wifi.sapguestconnect;

public class ErrorMessages {
	public static enum errorMessages {NOT_CONNECTED, ALREADY_CONNECTED, SUCCESS, FAILED, WIFI_TURNED_OFF, NOT_CORRECT_WIFI}
	
	public static int toInt(errorMessages e){
		int value = 0;
		switch (e) {
			case NOT_CONNECTED: 	value = 0;
									break;
	    	case ALREADY_CONNECTED: value = 1;
	    							break;
		    case SUCCESS: 			value = 2;
		                   			break;
		    case FAILED: 			value = 3;		
		    						break;
		    case WIFI_TURNED_OFF: 	value = 4;
		                    		break;
		    case NOT_CORRECT_WIFI: 	value = 5;
    								break;
		}
		return value;
	}
}
