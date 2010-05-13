package com.wifi.sapguestconnect;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.wifi.sapguestconnect/databases/";
 
    private static String DB_NAME = "WiFiLoginDB";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
    public boolean isTableExist(String table){
    	boolean isTableExist = false;
    	String sqlIsTableExistStmt = "SELECT name FROM sqlite_master WHERE name='" + table + "' AND type='table'";
    	
    	try { 
    		Cursor result = myDataBase.rawQuery(sqlIsTableExistStmt, null);
    		if(result.getCount() > 0){
    			isTableExist = true;
    		}
//    		result.moveToFirst();
//    		while(!result.isAfterLast()){
//    			int columnsCount = result.getColumnCount();
//        		result.moveToNext();
//    		}
    	}
    	catch (SQLException e) {
    		//TODO error message to log
    		String errorMessage = e.getMessage();
    	}
    	return isTableExist;
    }
    
    public long saveLoginInformation(String table, String user, String pass, String bssID) {
    	
    	String sqlDropTable = "DROP TABLE IF EXISTS " + table;
    	String sqlCreateStmt = "CREATE TABLE " + table + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, pass TEXT, bssid TEXT);";

    	if(isTableExist(table) == true){
    		System.out.println("Table deleted.");
	    	try{
	    		myDataBase.execSQL(sqlDropTable);
	    	}
	    	catch(SQLException e){
	    		//TODO error message to log
	    		String errorMessage = e.getMessage();
	    		int i = 0;
	    	}
    	}
    	if(isTableExist(table) == false){
	    	try{
	    		if(myDataBase.isReadOnly() == false & myDataBase.isOpen() == true){
	    			myDataBase.execSQL(sqlCreateStmt);
	    		}
	    	}
	    	catch(SQLException e){
	    		//TODO error message to log
	    		String errorMessage = e.getMessage();
	    		int i = 0;
	    	}
    	}

    	ContentValues initialValues = new ContentValues();
        initialValues.put("user", user);
        initialValues.put("pass", pass);
        initialValues.put("bssID", bssID);
       
        long queryResult = 0;
        try {
        	queryResult = myDataBase.insert(table, "title", initialValues);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String errorMessage = e.getMessage();
			int i = 0;
		}
        
        return queryResult;
    }

    public LoginData getLoginData(String table){
    	String user = null;
    	String pass = null;
    	String bssID = null;
    	
    	String sqlIsTableExistStmt = "SELECT * FROM " + table;// + " WHERE _id='5'";
    	
    	try { 
    		Cursor result = myDataBase.rawQuery(sqlIsTableExistStmt, null);
    		if(result.getCount() > 0){
	    		result.moveToFirst();
	    		while(!result.isAfterLast()){
	    			user = result.getString(1);
	    			pass = result.getString(2);
	    			bssID = result.getString(3);
	        		result.moveToNext();
	    		}
    		}
    		else{
    			return null;
    		}
    	}
    	catch (SQLException e) {
    		//TODO error message to log
    		String errorMessage = e.getMessage();
    		int i = 1;
    	}
    	    	
    	return new LoginData(user, pass, bssID);
    }
    
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
		Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'createDataBase' before 'checkDataBase' ...");
    	boolean dbExist = checkDataBase();
		Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'createDataBase' after 'checkDataBase' ...");
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
        	SQLiteDatabase db = this.getWritableDatabase();
        	
//        	try {
// 
//    			copyDataBase();
// 
//    		} catch (IOException e) {
// 
//        		throw new Error("Error copying database");
// 
//        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'checkDataBase' before 'openDatabase' ...");
    		//checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    		checkDB = SQLiteDatabase.openOrCreateDatabase(myPath, null);
    		Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'checkDataBase' after 'openDatabase' ...");

    	}catch(SQLiteException e){
 
    		//database does't exist yet.
    		String sss = e.getMessage();
    		int i = 0;
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = null;
		try {
			myInput = myContext.getAssets().open(DB_NAME);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			//e.printStackTrace();
			int i = 0;
		}
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	//return myDataBase;
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
//	@Override
//	public void onCreate(SQLiteDatabase db) {
// 
//	}
 
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// 
//	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
 
}