package com.fab.gpsprofile.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper {

    public static final String MYDATABASE_NAME = "PROFILE";
    public static final String MYDATABASE_TABLE = "LOCATION";
    public static final int MYDATABASE_VERSION = 1;
    public static final String ID = "ID";
    public static final String ADDRESS = "ADDRESS";
    public static final String MESSAGE = "MESSAGE";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String PROFILE = "PROFILE";
    public static final String TYPE = "TYPE";
    public static final String STATE = "STATE";
    public static final String PHONE = "PHONE";
    private static final String SCRIPT_CREATE_DATABASE =  "CREATE TABLE "+MYDATABASE_TABLE+" ( "+ID+" INT AUTO INCREMENT, "+ ADDRESS +" TEXT,"+ MESSAGE +" TEXT,"+ PROFILE +" TEXT,"+ LATITUDE +" TEXT,"+ LONGITUDE +" TEXT,"+ TYPE +" TEXT,"+ STATE +" TEXT ,"+ PHONE +" TEXT );";
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public DBHelper(Context c){
        context = c;
    }

/* public DataBaseHelper openToRead() throws android.database.SQLException {
  sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
  sqLiteDatabase = sqLiteHelper.getReadableDatabase();
  return this;
 }
*/

    public DBHelper openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public boolean insert(String address,String message,String profile, String latitude, String longitude, String type, String state, String phone){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, "");
        contentValues.put(ADDRESS, address);
        contentValues.put(MESSAGE, message);
        contentValues.put(PROFILE, profile);
        contentValues.put(LATITUDE, latitude);
        contentValues.put(LONGITUDE, longitude);
        contentValues.put(TYPE, type);
        contentValues.put(STATE, state);
        contentValues.put(PHONE, phone);
        return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues)>0;
    }

    public boolean delete(String name){
        return sqLiteDatabase.delete(MYDATABASE_TABLE, ADDRESS + "= '" + name + "'", null) > 0;
    }

    public int deleteAll(){
        return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
    }

    public int count(){
        String[] columns = new String[]{ADDRESS};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,null, null, null, null, null);
        return cursor.getCount();
    }

    public int updateState(int pos, String state){
        String add=displayAddress(pos);
        ContentValues cv = new ContentValues();
        cv.put(STATE,state); //These Fields should be your String values of actual column names
        return sqLiteDatabase.update(MYDATABASE_TABLE, cv, ADDRESS + "= ?", new String[] { String.valueOf(add) });
    }

    public String displayPhone(int pos){
        String[] columns = new String[]{PHONE};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,null, null, null, null, null);
        String result = "";
        int index_CONTENT = cursor.getColumnIndex(PHONE);
        cursor.moveToPosition(pos);
        result = cursor.getString(index_CONTENT);
        cursor.close();
        return result;
    }

    public String displayLatitude(int pos){
        String[] columns = new String[]{LATITUDE};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,null, null, null, null, null);
        String result = "";
        int index_CONTENT = cursor.getColumnIndex(LATITUDE);
        cursor.moveToPosition(pos);
        result = cursor.getString(index_CONTENT);
        cursor.close();
        return result;
    }

    public String displayLongitude(int pos){
        String[] columns = new String[]{LONGITUDE};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,null, null, null, null, null);
        String result = "";
        int index_CONTENT = cursor.getColumnIndex(LONGITUDE);
        cursor.moveToPosition(pos);
        result = cursor.getString(index_CONTENT);
        cursor.close();
        return result;
    }

    public String displayAddress(int pos){
        String[] columns = new String[]{ADDRESS};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,null, null, null, null, null);
        String result = "";
        int index_NAME = cursor.getColumnIndex(ADDRESS);
        cursor.moveToPosition(pos);
        result = cursor.getString(index_NAME);
        cursor.close();
        return result;
    }

    public String displayMessage(int pos){
        String[] columns = new String[]{MESSAGE};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,null, null, null, null, null);
        String result = "";
        int index_IMAGE = cursor.getColumnIndex(MESSAGE);
        cursor.moveToPosition(pos);
        result = cursor.getString(index_IMAGE);
        cursor.close();
        return result;
    }

    public String displayProfile(int pos){
        String[] columns = new String[]{PROFILE};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,null, null, null, null, null);
        String result = "";
        int index_IMAGE = cursor.getColumnIndex(PROFILE);
        cursor.moveToPosition(pos);
        result = cursor.getString(index_IMAGE);
        cursor.close();
        return result;
    }

    public String displayType(int pos){
        String[] columns = new String[]{TYPE};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,null, null, null, null, null);
        String result = "";
        int index_IMAGE = cursor.getColumnIndex(TYPE);
        cursor.moveToPosition(pos);
        result = cursor.getString(index_IMAGE);
        cursor.close();
        return result;
    }

    public String displayState(int pos){
        String[] columns = new String[]{STATE};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,null, null, null, null, null);
        String result = "";
        int index_IMAGE = cursor.getColumnIndex(STATE);
        cursor.moveToPosition(pos);
        result = cursor.getString(index_IMAGE);
        cursor.close();
        return result;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(SCRIPT_CREATE_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + MYDATABASE_TABLE);
            onCreate(db);
        }

    }
}