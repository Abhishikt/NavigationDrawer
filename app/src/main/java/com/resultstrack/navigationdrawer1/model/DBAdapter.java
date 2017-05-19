package com.resultstrack.navigationdrawer1.model;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by abhishikt on 1/18/2017.
 */

public class DBAdapter {
    /******** if debug is set true then it will show all Logcat message *******/
    public static final boolean DEBUG = true;
    /******************** Logcat TAG ************/
    public static final String LOG_TAG = "DBAdapter";
    /******************** Table Fields ************/
    public static final String KEY_ID = "_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";

    /******************** Database Name ************/
    public static final String DATABASE_NAME = "resultstrackDb";
    /**** Database Version (Increase one if want to also upgrade your database) ***/
    public static final int DATABASE_VERSION = 7;// started at 1
    //    /** Table names */
    public static final String USER_TABLE = "tbl_user";
    public static final String CHILD_TABLE = "tbl_children";
    public static final String MEMBER_TABLE = "tbl_members";
    public static final String MOM_TABLE = "tbl_mom";
    public static final String SURVEY_RESPONSE_TABLE = "tbl_surveyResponse";
    public static final String LOCAL_SETTINGS = "tbl_settings";

    /******* Set all table with comma seperated like USER_TABLE,ABC_TABLE *******/
    private static final String[ ] ALL_TABLES = { USER_TABLE, CHILD_TABLE, MEMBER_TABLE, MOM_TABLE, SURVEY_RESPONSE_TABLE, LOCAL_SETTINGS };

    /** Create table syntax */
    private static final String USER_CREATE = "create table if not exists" +
            " tbl_user ( " +
            "id text primary key not null, " +
            "fstname text not null, " +
            "lstname text, " +
            "email text not null, " +
            "paswrd text not null, " +
            "mobile text not null, " +
            "usrtyp integer not null, " +
            "image text, " +
            "loc text, " +
            "state text, " +
            "district text, " +
            "block text, " +
            "villageName text, " +
            "anganwadiCode text " +
            ");";

    private static final String CHILD_CREATE="create table if not exists" +
            " tbl_children ( " +
            "id text primary key not null, " +
            "name text not null, " +
            "parentName text, " +
            "dateOfBirth text, " +
            "gender int, " +
            "isMissing int, " +
            "image text," +
            "location text, " +
            "state text, " +
            "district text, " +
            "block text, " +
            "villageName text, " +
            "pincode text, " +
            "anganwadiCode text " +
            ");";

    private static final String MEMBER_CREATE="create table if not exists" +
            " tbl_members ( " +
            "id text primary key not null, " +
            "fstnm text not null, " +
            "lstnm text not null, " +
            "designation text not null, " +
            "title text not null, " +
            "email text not null, " +
            "phone text not null, " +
            "address text not null, " +
            "image text, " +
            "location text, " +
            "state text, " +
            "district text, " +
            "block text, " +
            "villageName text, " +
            "pincode text, " +
            "anganwadiCode text " +
            ");";

    private static final String MOM_CREATE="create table if not exists" +
            " tbl_mom ( " +
            "id text primary key not null, " +
            "title text not null, " +
            "subject text not null, " +
            "momdt text not null, " +
            "place text not null, " +
            "image text, " +
            "summary text, " +
            "location text, " +
            "state text, " +
            "district text, " +
            "block text, " +
            "villageName text, " +
            "pincode text, " +
            "anganwadiCode text " +
            ");";

    private static final String SURVEY_RESPONSE_CREATE="create table if not exists" +
            " tbl_surveyResponse ( " +
            "id text primary key not null, " +
            "surveyId text not null, " +
            "organizationId text not null, " +
            "ques_id text not null, " +
            "ques_typ int not null, " +
            "response text," +
            "response_dt text" +
            ");";

    //LOCAL_SETTINGS
    private static final String LOCAL_SETTINGS_CREATE="create table if not exists" +
            " tbl_settings ( " +
            "id text primary key not null, " +
            "property text not null, " +
            "type text not null, " +
            "value text not null, " +
            "userId text not null" +
            ");";
    /******************** Used to open database in syncronized way ************/
    private static DBAdapter.DataBaseHelper DBHelper = null;

    protected DBAdapter() {
    }
    /*********** Initialize database *************/
    public static void init(Context context) {
        if (DBHelper == null) {
            if (DEBUG){
                Log.i("DBAdapter", context.toString());
                File outFile= context.getDatabasePath("resultstrackDb");
                Log.i("DBAdapter-Path", outFile.getPath().toString());
            }
            DBHelper = new DataBaseHelper(context);
        }
    }

    /********** Main Database creation INNER class ********/
    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory)null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (DEBUG)
                Log.i(LOG_TAG, "New Database created.");
            try {
                db.execSQL(USER_CREATE);
                db.execSQL(CHILD_CREATE);
                db.execSQL(MEMBER_CREATE);
                db.execSQL(MOM_CREATE);
                db.execSQL(SURVEY_RESPONSE_CREATE);
                db.execSQL(LOCAL_SETTINGS_CREATE);

            } catch (Exception exception) {
                if (DEBUG)
                    Log.i(LOG_TAG, "Exception onCreate() exception");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (DEBUG)
                Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
                        + "to" + newVersion + "...");

            for (String table : ALL_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }
            onCreate(db);
        }

    } // Inner class closed


    /***** Open database for insert,update,delete in syncronized manner *****/
    public static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }


    /****************** General functions*******************/


    /********** Escape string for single quotes (Insert,Update) *******/
    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace(", );
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }


    /********* UnEscape string for single quotes (show data) *******/
    private static String sqlUnEscapeString(String aString) {

        String aReturn = "";

        if (null != aString) {
            aReturn = aString.replace("\'", "'");
        }

        return aReturn;
    }


    /********************************************************************/

    /********* User data functons *********/

    /*public static void addUserData(UserData uData) {
        // Open database for Read / Write
        final SQLiteDatabase db = open();
        String name = sqlEscapeString(uData.getName());
        String email = sqlEscapeString(uData.getEmail());
        ContentValues cVal = new ContentValues();
        cVal.put(KEY_USER_NAME, name);
        cVal.put(KEY_USER_EMAIL, email);
        // Insert user values in database
        db.insert(USER_TABLE, null, cVal);
        db.close(); // Closing database connection
    }


    // Updating single data
    public static int updateUserData(UserData data) {

        final SQLiteDatabase db = open();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, data.getName());
        values.put(KEY_USER_EMAIL, data.getEmail());

        // updating row
        return db.update(USER_TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
    }

    // Getting single contact
    public static UserData getUserData(int id) {

        // Open database for Read / Write
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(USER_TABLE, new String[] { KEY_ID,
                        KEY_USER_NAME, KEY_USER_EMAIL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);


        if (cursor != null)
            cursor.moveToFirst();

        UserData data = new UserData(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        // return user data
        return data;
    }

    // Getting All User data
    public static List<UserData> getAllUserData() {

        List<UserData> contactList = new ArrayList<UserData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserData data = new UserData();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setName(cursor.getString(1));
                data.setEmail(cursor.getString(2));

                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }

        // return user list
        return contactList;
    }



    // Deleting single contact
    public static void deleteUserData(UserData data) {
        final SQLiteDatabase db = open();
        db.delete(USER_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        db.close();
    }

    // Getting dataCount

    public static int getUserDataCount() {

        final SQLiteDatabase db = open();

        String countQuery = "SELECT  * FROM " + USER_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }*/
}
