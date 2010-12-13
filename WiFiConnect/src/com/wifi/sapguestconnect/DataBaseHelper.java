package com.wifi.sapguestconnect;

import java.io.IOException;
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
    private LogHelper logHelper;
    private boolean isLogEnabled;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
        logHelper = LogHelper.getLog();
        isLogEnabled = logHelper.isLogEnabled();
    }	
 
    public boolean isTableExist(String table){
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> isTableExist() started.");
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
        	logHelper.toLog(isLogEnabled, "EXCEPTION: DataBaseHelper -> isTableExist(): " + e.getMessage());
    	}
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> isTableExist() ended.");
    	return isTableExist;
    }
    
    public long saveLoginInformation(String table, String user, String pass, String bssID) {
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> saveLoginInformation() started.");
   	
    	String sqlDropTable = "DROP TABLE IF EXISTS " + table;
    	String sqlCreateStmt = "CREATE TABLE " + table + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, pass TEXT, bssid TEXT);";

    	if(isTableExist(table) == true){
    		System.out.println("Table deleted.");
	    	try{
	    		myDataBase.execSQL(sqlDropTable);
	    	}
	    	catch(SQLException e){
	        	logHelper.toLog(isLogEnabled, "EXCEPTION: DataBaseHelper -> saveLoginInformation() [sqlDropTable]: " + e.getMessage());
	    	}
    	}
    	if(isTableExist(table) == false){
	    	try{
	    		if(myDataBase.isReadOnly() == false & myDataBase.isOpen() == true){
	    			myDataBase.execSQL(sqlCreateStmt);
	    		}
	    	}
	    	catch(SQLException e){
	        	logHelper.toLog(isLogEnabled, "EXCEPTION: DataBaseHelper -> saveLoginInformation() [sqlCreateStmt]: " + e.getMessage());
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
			logHelper.toLog(isLogEnabled, "EXCEPTION: DataBaseHelper -> saveLoginInformation() [insert]: " + e.getMessage());
		}

		logHelper.toLog(isLogEnabled, "DataBaseHelper -> saveLoginInformation() ended.");
        
        return queryResult;
    }

    public LoginData getLoginData(String table){
    	String user = null;
    	String pass = null;
    	String bssID = null;
    	
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> getLoginData() started.");
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
    		logHelper.toLog(isLogEnabled, "EXCEPTION: DataBaseHelper -> getLoginData(): " + e.getMessage());
    	}
    	    	
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> getLoginData() ended.");
    	return new LoginData(user, pass, bssID);
    }
    
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> createDataBase() started.");
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
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> createDataBase() ended.");
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
    	
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> checkDataBase() started.");
    	
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'checkDataBase' before 'openDatabase' ...");
    		//checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    		checkDB = SQLiteDatabase.openOrCreateDatabase(myPath, null);
    		Log.e("WiFiConnect", ">>>>WiFiConnect>>>> 'checkDataBase' after 'openDatabase' ...");

    	}catch(SQLiteException e){
    		//database does't exist yet.
    		logHelper.toLog(isLogEnabled, "EXCEPTION: DataBaseHelper -> checkDataBase(): " + e.getMessage());
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> checkDataBase() ended.");
    	
    	return checkDB != null ? true : false;
    }
 
//    /**
//     * Copies your database from your local assets-folder to the just created empty database in the
//     * system folder, from where it can be accessed and handled.
//     * This is done by transfering bytestream.
//     * */
//    private void copyDataBase() throws IOException{
//    	
//    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> copyDataBase() started.");
//
//    	//Open your local db as the input stream
//    	InputStream myInput = null;
//		try {
//			myInput = myContext.getAssets().open(DB_NAME);
//		} catch (Exception e) {
//			logHelper.toLog(isLogEnabled, "EXCEPTION: DataBaseHelper -> copyDataBase(): " + e.getMessage());
//		}
// 
//    	// Path to the just created empty db
//    	String outFileName = DB_PATH + DB_NAME;
// 
//    	//Open the empty db as the output stream
//    	OutputStream myOutput = new FileOutputStream(outFileName);
// 
//    	//transfer bytes from the inputfile to the outputfile
//    	byte[] buffer = new byte[1024];
//    	int length;
//    	while ((length = myInput.read(buffer))>0){
//    		myOutput.write(buffer, 0, length);
//    	}
// 
//    	//Close the streams
//    	myOutput.flush();
//    	myOutput.close();
//    	myInput.close();
// 
//    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> copyDataBase() ended.");
//
//    }
 
    public void openDataBase() throws SQLException{
 
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> openDataBase() started.");
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	//return myDataBase;
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> openDataBase() ended.");
   }
 
    @Override
	public synchronized void close() {
    	logHelper.toLog(isLogEnabled, "DataBaseHelper -> close() started.");
   	    if(myDataBase != null)
   		    myDataBase.close();
   	    super.close();
       	logHelper.toLog(isLogEnabled, "DataBaseHelper -> close() ended.");
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