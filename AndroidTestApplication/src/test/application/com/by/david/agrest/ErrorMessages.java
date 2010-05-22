package test.application.com.by.david.agrest;

public class ErrorMessages {
	private static String errorMessageString = null;
	public static enum errorMessages {
			SUCCESS, 
			FAILED, 
			MEDIA_MOUNTED_READ_ONLY, 
			EXTERNAL_STORAGE_UNAVAILABLE, 
			FILE_NAME_IS_EMPTY, 
			FILE_NOT_FOUND_EXCEPTION,
			IO_EXCEPTION
	}
	
	public static String getMessage(errorMessages errorMsg){
		if(errorMsg == errorMessages.FAILED){
			errorMessageString = "Operation Failed.";
		}
		else if(errorMsg == errorMessages.FAILED){
			errorMessageString = "Operation Succeeded.";
		}
		else if(errorMsg == errorMessages.MEDIA_MOUNTED_READ_ONLY){
			errorMessageString = "External storage (media) mounted as read only.";
		}
		else if(errorMsg == errorMessages.EXTERNAL_STORAGE_UNAVAILABLE){
			errorMessageString = "External storage (media) unavailable.";
		}
		else if(errorMsg == errorMessages.FILE_NAME_IS_EMPTY){
			errorMessageString = "File name is empty.";
		}
		else if(errorMsg == errorMessages.FILE_NOT_FOUND_EXCEPTION){
			errorMessageString = "File not found.";
		}
		else if(errorMsg == errorMessages.IO_EXCEPTION){
			errorMessageString = "IO Exception.";
		}
//		else if(){
//		
//	}
		return errorMessageString;
	}
}
