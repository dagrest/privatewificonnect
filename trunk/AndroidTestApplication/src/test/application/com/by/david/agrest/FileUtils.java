package test.application.com.by.david.agrest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.provider.OpenableColumns;
import test.application.com.by.david.agrest.ErrorMessages.errorMessages;

//http://androidgps.blogspot.com/2008/09/writing-to-sd-card-in-android.html
//http://www.anddev.org/viewtopic.php?p=13574

public class FileUtils {
	Context context = null;
	private errorMessages result = errorMessages.FAILED;
	private String fileName = null;

	public FileUtils(Context context, String fileName){
		this.context = context;
		this.fileName = fileName;
	}
	
	public errorMessages createFile(){
		if(fileName == null){
			return result = errorMessages.FILE_NAME_IS_EMPTY;
		}
		errorMessages result = isMediaAvailabe();
		if(result == errorMessages.SUCCESS){
			FileOutputStream fos;
			try {
				fos = context.openFileOutput(fileName, context.MODE_WORLD_WRITEABLE);
				fos.write("".getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public errorMessages appendToFile(String message){
		if(fileName == null){
			return result = errorMessages.FILE_NAME_IS_EMPTY;
		}
		errorMessages result = isMediaAvailabe();
		if(result == errorMessages.SUCCESS){
			FileOutputStream fos;
			try {
				fos = context.openFileOutput(fileName, context.MODE_APPEND);
				fos.write(message.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO write exception to LOG
				//e.printStackTrace();
				result = errorMessages.FILE_NOT_FOUND_EXCEPTION;
			} catch (IOException e) {
				// TODO write exception to LOG
				//e.printStackTrace();
				result = errorMessages.IO_EXCEPTION;
			}
		}
		return result;
	}
	
	public errorMessages isMediaAvailabe(){
		//boolean mExternalStorageAvailable = false;
		//boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    //mExternalStorageAvailable = mExternalStorageWriteable = true;
		    result = errorMessages.SUCCESS;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    //mExternalStorageAvailable = true;
		    //mExternalStorageWriteable = false;
		    result = errorMessages.MEDIA_MOUNTED_READ_ONLY;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    //mExternalStorageAvailable = mExternalStorageWriteable = false;
		    result = errorMessages.EXTERNAL_STORAGE_UNAVAILABLE;
		}
		
		return result;
	}
}
