package com.resultstrack.navigationdrawer1.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.resultstrack.navigationdrawer1.commonUtilities.AsyncResponse;
import com.resultstrack.navigationdrawer1.commonUtilities.RTContants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by abhishikt on 3/5/2017.
 */

public class LocalSettings {

    private String id;
    private String property;
    private String type;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalSettings() {
    }

    public LocalSettings(String property, String type, String value) {
        this.property = property;
        this.type = type;
        this.value = value;
    }

    public void save() {
        try {
            List<LocalSettings> lstSettings = getLocalSettings(this.getProperty());
            if(lstSettings.size()==0) {
                String newId = UUID.randomUUID().toString();
                this.setId(newId);
                // Open database for Read / Write
                final SQLiteDatabase db = DBAdapter.open();

                ContentValues cVal = new ContentValues();
                cVal.put("id", getId());
                cVal.put("property", getProperty());
                cVal.put("type", getType());
                cVal.put("value", getValue());

                // Insert user values in database
                db.insert(DBAdapter.LOCAL_SETTINGS, null, cVal);
                db.close(); // Closing database connection
            }
            else{
                this.setId(lstSettings.get(0).getId());
                this.update();
            }
        }
        catch (Exception e){
            if(DBAdapter.DEBUG)
                Log.i(DBAdapter.LOG_TAG, "Exception onSaveLocalSettings() exception");
        }
    }

    // Updating single data
    private int update() {

        final SQLiteDatabase db = DBAdapter.open();

        ContentValues values = new ContentValues();
        values.put("property", getProperty());
        values.put("type", getType());
        values.put("value", getValue());

        // updating row
        return db.update(DBAdapter.LOCAL_SETTINGS, values, "id" + " = ?",
                new String[] { String.valueOf(getId()) });
    }

    public List<LocalSettings> getLocalSettings() {

        List<LocalSettings> lstLocalSettings = new ArrayList<LocalSettings>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBAdapter.LOCAL_SETTINGS;
        // Open database for Read / Write
        final SQLiteDatabase db = DBAdapter.open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LocalSettings data = new LocalSettings();
                data.setId(cursor.getString(0));
                data.setProperty(cursor.getString(1));
                data.setType(cursor.getString(2));
                data.setValue(cursor.getString(3));

                // Adding contact to list
                lstLocalSettings.add(data);
            } while (cursor.moveToNext());
        }
        // return user list
        return lstLocalSettings;
    }

    public List<LocalSettings> getLocalSettings(String property) {

        List<LocalSettings> lstLocalSettings = new ArrayList<LocalSettings>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + DBAdapter.LOCAL_SETTINGS;
        // Open database for Read / Write
        final SQLiteDatabase db = DBAdapter.open();

        //Cursor cursor = db.rawQuery ( selectQuery, null );
        Cursor cursor = db.query (DBAdapter.LOCAL_SETTINGS,new String[] { "id","property","type","value"}, "property"+"=?",
                new String[]{property},null,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LocalSettings data = new LocalSettings();
                data.setId(cursor.getString(0));
                data.setProperty(cursor.getString(1));
                data.setType(cursor.getString(2));
                data.setValue(cursor.getString(3));

                // Adding contact to list
                lstLocalSettings.add(data);
            } while (cursor.moveToNext());
        }
        // return user list
        return lstLocalSettings;
    }

    public void deleteSetting() {
        final SQLiteDatabase db = DBAdapter.open();
        db.delete(DBAdapter.LOCAL_SETTINGS, "id" + " = ?",
                new String[] { String.valueOf(this.getId()) });
        db.close();
    }


}
