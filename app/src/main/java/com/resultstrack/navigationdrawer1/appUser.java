package com.resultstrack.navigationdrawer1;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.resultstrack.navigationdrawer1.DBAdapter.DEBUG;
import static com.resultstrack.navigationdrawer1.DBAdapter.USER_TABLE;

/**
 * Created by abhishikt on 1/18/2017.
 */

public class appUser {

    private String id;
    private String sid; //Server Record Id
    private String fstname;
    private String lstname;
    private String email;
    private String paswrd;
    private String mobile;
    private int usrtyp;
    private String image;
    private String loc;
    private String state;
    private String district;
    private String block;
    private String villageName;
    private String anganwadiCode;
    private static final int MOBILE_USER = 4;

    public AsyncResponse delegate = null;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getSId() {
        return sid;
    }

    public String getFstname() { return fstname; }
    public void setFstname(String fstname) { this.fstname = fstname; }

    public String getLstname() { return lstname; }
    public void setLstname(String lstname) { this.lstname = lstname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPaswrd() { return paswrd; }
    public void setPaswrd(String paswrd) { this.paswrd = paswrd; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public int getUsrtyp() { return this.MOBILE_USER; }
    public void setUsrtyp(int usrtyp) { this.usrtyp = usrtyp; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getLoc() { return loc; }
    public void setLoc(String loc) { this.loc = loc; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }

    public String getVillageName() { return villageName; }
    public void setVillageName(String villageName) { this.villageName = villageName; }

    public String getAnganwadiCode() { return anganwadiCode; }
    public void setAnganwadiCode(String anganwadiCode) { this.anganwadiCode = anganwadiCode; }

    public appUser(){}

    public appUser(String id, String fstname, String lstname, String email, String paswrd,
                   String mobile, int usrtyp, String image, String loc, String state,
                   String district, String block, String villageName, String anganwadiCode) {
        this.id = id;
        this.fstname = fstname;
        this.lstname = lstname;
        this.email = email;
        this.paswrd = paswrd;
        this.mobile = mobile;
        this.usrtyp = usrtyp;
        this.image = image;
        this.loc = loc;
        this.state = state;
        this.district = district;
        this.block = block;
        this.villageName = villageName;
        this.anganwadiCode = anganwadiCode;
    }

    public appUser(String id, String _sid, String fstname, String lstname, String email, String paswrd,
                   String mobile, int usrtyp, String image, String loc, String state,
                   String district, String block, String villageName, String anganwadiCode) {
        this.id = id;
        this.sid = _sid;
        this.fstname = fstname;
        this.lstname = lstname;
        this.email = email;
        this.paswrd = paswrd;
        this.mobile = mobile;
        this.usrtyp = usrtyp;
        this.image = image;
        this.loc = loc;
        this.state = state;
        this.district = district;
        this.block = block;
        this.villageName = villageName;
        this.anganwadiCode = anganwadiCode;
    }

    public void save() {
        try {
            // Open database for Read / Write
            final SQLiteDatabase db = DBAdapter.open();

            ContentValues values = new ContentValues();
            values.put("id", UUID.randomUUID().toString());
            values.put("fstname", getFstname());
            values.put("lstname", getLstname());
            values.put("email", getEmail());
            values.put("paswrd", getPaswrd());
            values.put("mobile", getMobile());
            values.put("usrtyp", getUsrtyp());
            values.put("image", getImage());
            values.put("loc", getLoc());
            values.put("state", getState());
            values.put("district", getDistrict());
            values.put("block", getBlock());
            values.put("villageName", getVillageName());
            values.put("anganwadiCode", getAnganwadiCode());
            // Insert user values in database
            int newid = (int)db.insert(DBAdapter.USER_TABLE, null, values);
            db.close(); // Closing database connection
            // assign newly created id to object
            //setId(newid);

            //sync data to server
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params) {
                    //DataOutputStream doStream;
                    HttpURLConnection urlConnection = null;
                    //BufferedReader reader = null;
                    StringBuilder sBuilder = new StringBuilder();
                    JSONObject jUser = new JSONObject();
                    try {
                        boolean networkState = RTContants.socketCheck();
                        if(networkState==false){
                            jUser.put("d","Offline");
                            sBuilder.append(jUser.toString());
                            //return "";
                        }
                        else {
                            URL url = new URL(RTContants.RTWEBSERVICE + "saveuser"); //http://10.0.2.2:52011/ResutlsTrackService.asmx/getuser
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection = RTContants.setConnectionRequestDefaults(urlConnection);

                            //JSON DATA OBJECT
                            JSONObject data = new JSONObject();
                            data.put("p", toJSON());

                            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                            writer.write(data.toString());
                            writer.flush();
                            writer.close();

                            sBuilder = new StringBuilder();
                            int responseCode = urlConnection.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                BufferedReader bReader = new BufferedReader(new InputStreamReader(
                                        urlConnection.getInputStream(), "utf-8"));
                                String responseLine = null;
                                while ((responseLine = bReader.readLine()) != null) {
                                    sBuilder.append(responseLine);
                                }
                            } else {
                                sBuilder.append(urlConnection.getResponseMessage());
                            }
                        }
                        //return sBuilder.toString();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        sBuilder.append("{'d':'Error'}");
                    } catch (IOException e) {
                        e.printStackTrace();
                        sBuilder.append("{'d':'Error'}");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sBuilder.append("{'d':'Error'}");
                    }
                    return sBuilder.toString();
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    //TODO
                    try {
                        response = response.replace("(","").replace(");","");
                        JSONObject root = new JSONObject(response);
                        switch (root.getString("d")){
                            case "Inserted":
                                deleteUserData();
                                delegate.processFinish("User profile saved.");
                                break;
                            case "Updated":
                                delegate.processFinish("User profile updated.");
                            case "Exists":
                                delegate.processFinish("User already exists. Choose another email.");
                                break;
                            case "Error":
                                delegate.processFinish("Opps!! Error encountered while creating new user profile.");
                                break;
                            case "Offline":
                                delegate.processFinish("User profile saved offline.");
                                break;
                            default:
                                delegate.processFinish("Opps!! nothing's working. Check internet connectivity.");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
        catch (SQLException e){
            if(DBAdapter.DEBUG)
                Log.i(DBAdapter.LOG_TAG, "SQLException onSaveUser() exception");
        }catch (Exception e){
            if(DBAdapter.DEBUG)
                Log.i(DBAdapter.LOG_TAG, "Exception onSaveUser() exception");
        }
    }

    // Updating single data
    public int update() {

        final SQLiteDatabase db = DBAdapter.open();

        ContentValues values = new ContentValues();
        values.put("fstname", getFstname());
        values.put("lstname", getLstname());
        values.put("email", getEmail());
        values.put("paswrd", getPaswrd());
        values.put("mobile", getMobile());
        values.put("usrtyp", getUsrtyp());
        values.put("image", getImage());
        values.put("loc", getLoc());
        values.put("state", getState());
        values.put("district", getDistrict());
        values.put("block", getBlock());
        values.put("villageName", getVillageName());
        values.put("anganwadiCode", getAnganwadiCode());
        // updating row
        return db.update(USER_TABLE, values, "id" + " = ?",
                new String[] { String.valueOf(getId()) });
    }

    // Getting single contact
    public static appUser getUser(String email) {

        try {
            // Open database for Read / Write
            final SQLiteDatabase db = DBAdapter.open();

            Cursor cursor = db.query(USER_TABLE, new String[]{"id",
                            "fstname", "lstname", "email", "paswrd", "mobile",
                            "usrtyp", "image", "loc", "state", "district", "block",
                            "villageName", "anganwadiCode"}, "email" + "=?",
                    new String[]{String.valueOf(email)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            appUser data = new appUser(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9),
                    cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13));

            // return user data
            return data;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public JSONObject toJSON(){

        JSONObject jsonObject = new JSONObject();
        try {
            if(getId()!="") {
                jsonObject.put("Id", this.getId());
            }
            jsonObject.put("FirstName", this.getFstname());
            jsonObject.put("LastName", this.getLstname());
            jsonObject.put("UserEmail", this.getEmail());
            jsonObject.put("Password", this.getPaswrd());
            jsonObject.put("PhoneNumber", this.getMobile());
            jsonObject.put("UserType", this.getUsrtyp());
            jsonObject.put("Imagepath", this.getImage());
            jsonObject.put("Location", this.getLoc());
            jsonObject.put("State", this.getState());
            jsonObject.put("District", this.getDistrict());
            jsonObject.put("Block", this.getBlock());
            jsonObject.put("VillageName", this.getVillageName());
            jsonObject.put("AnganwadiCode", this.getAnganwadiCode());
            jsonObject.put("Active", 0);
            jsonObject.put("Pincode", "");
            jsonObject.put("OrganisationId", RTGlobal.getOrganizationId());
            jsonObject.put("CreatedBy","");
            jsonObject.put("CreatedDate", ""); //new Date().toString()
            jsonObject.put("ApprovedBy", "");
            jsonObject.put("ApprovedDate", "");
            jsonObject.put("ErrMsg", "");

            return jsonObject;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            String errMsg = e.getMessage();
            Log.i(DBAdapter.LOG_TAG, "SQLException onSaveUser() exception :" + e.getMessage());
            System.out.println(errMsg);
            return jsonObject;
        }catch (Exception e){
            String errMsg = e.getMessage();
            Log.i(DBAdapter.LOG_TAG, "SQLException onSaveUser() exception :" + e.getMessage());
            return null;
        }
    }

    // Getting All User data
    public List<appUser> getAllUserData() {

        List<appUser> userList = new ArrayList<appUser>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE;
        // Open database for Read / Write
        final SQLiteDatabase db = DBAdapter.open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                appUser data = new appUser(cursor.getString(0),
                        cursor.getString(1),cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),Integer.parseInt(cursor.getString(6)),
                        cursor.getString(7),cursor.getString(8),cursor.getString(9),
                        cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13));
                // Adding contact to list
                userList.add(data);
            } while (cursor.moveToNext());
        }
        // return user list
        return userList;
    }

    // Deleting single contact
    public void deleteUserData(appUser data) {
        final SQLiteDatabase db = DBAdapter.open();
        db.delete(USER_TABLE, "id" + " = ?",
                new String[] { String.valueOf(data.getId()) });
        db.close();
    }

    // Deleting single contact
    public void deleteUserData() {
        final SQLiteDatabase db = DBAdapter.open();
        db.delete(USER_TABLE, "id" + " = ?",
                new String[] { String.valueOf(this.getId()) });
        db.close();
    }

    // Getting dataCount
    public static int getUserDataCount() {
        final SQLiteDatabase db = DBAdapter.open();

        String countQuery = "SELECT  * FROM " + USER_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void doLogin (final String email, String password) {

        //sync data to server
        new AsyncTask<String, String, String>(){
            @Override
            protected String doInBackground(String... params) {
                //DataOutputStream doStream;
                HttpURLConnection urlConnection = null;
                //BufferedReader reader = null;
                StringBuilder sBuilder = null;
                try {
                    boolean networkState= RTContants.socketCheck();
                    sBuilder = new StringBuilder();

                    if(networkState==false){
                        appUser oUser =  getUser(email);
                        if(oUser!=null) {
                            String strUser = oUser.toJSON().toString();
                            sBuilder.append(strUser);
                        }else{
                            sBuilder.append("No User Found");
                        }
                        //return "Not Connected";
                    }
                    else {
                        URL url = new URL(RTContants.RTWEBSERVICE + "getuser");
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection = RTContants.setConnectionRequestDefaults(urlConnection);

                        //JSON DATA OBJECT
                        JSONObject data = new JSONObject();
                        data.put("p", email);

                        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                        writer.write(data.toString());
                        writer.flush();
                        writer.close();

                        int responseCode = urlConnection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader bReader = new BufferedReader(new InputStreamReader(
                                    urlConnection.getInputStream(), "utf-8"));
                            String responseLine = null;
                            while ((responseLine = bReader.readLine()) != null) {
                                sBuilder.append(responseLine);
                            }
                        } else {
                            sBuilder.append(urlConnection.getResponseMessage());
                        }
                    }
                    return sBuilder.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return sBuilder.toString();
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                //TODO
                try {
                    response = response.replace("(","").replace(");","");
                    JSONObject root = new JSONObject(response);
                    JSONObject jUser=null;
                    if(root.has("d"))
                        jUser = new JSONArray(root.getString("d")).getJSONObject(0);
                    else
                       jUser =root;

                    appUser user = new appUser(jUser.getString("Id"),
                            jUser.getString("FirstName"),jUser.getString("LastName"),
                            jUser.getString("UserEmail"),jUser.getString("Password"),
                            jUser.getString("PhoneNumber"),Integer.parseInt(jUser.getString("UserType")),
                            jUser.getString("Imagepath"),jUser.getString("Location"),
                            jUser.getString("State"),jUser.getString("District"),
                            jUser.getString("Block"),jUser.getString("VillageName"),
                            jUser.getString("AnganwadiCode"));

                    delegate.processFinish(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                    delegate.processFinish(null);
                }
            }
        }.execute();
    }

    private class SyncData extends AsyncTask<String,String, String>{

        @Override
        protected String doInBackground(String... params) {
            DataOutputStream doStream;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder sBuilder = null;
            try {
                boolean networkState=socketCheck("10.0.2.2", 52011, 1000);
                if(networkState==false){return "Not Connected";}
                URL url = new URL(RTContants.RTWEBSERVICE + "getuser"); //http://10.0.2.2:52011/ResutlsTrackService.asmx/getuser
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod(RTContants.HTTP_METHOD_GET);
                urlConnection.setRequestProperty("Host",RTContants.HTTP_HOST);
                urlConnection.setRequestProperty("Content-Type","application/json; charset=utf-8");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();

                //JSON DATA OBJECT
                JSONObject data = new JSONObject();
                data.put("p", "salil@gmail.com");

                //get output stream done writing
                /*doStream = new DataOutputStream(urlConnection.getOutputStream());
                doStream.writeBytes(URLEncoder.encode(jdata.toString(),"utf-8"));
                doStream.flush ();
                doStream.close ();*/
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(data.toString());
                writer.flush();
                writer.close();

                sBuilder = new StringBuilder();
                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String responseLine=null;
                    while((responseLine=bReader.readLine())!=null){
                        sBuilder.append(responseLine + "\n");
                    }
                }else{
                    sBuilder.append(urlConnection.getResponseMessage());
                }
                return sBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sBuilder.toString();
        }

        @Override
        protected void onPostExecute(String appUsers) {
            super.onPostExecute(appUsers);
            //TODO
            String str = appUsers;
        }

        private boolean socketCheck(String host, int port, int timeout) {
            try {
                long start = System.currentTimeMillis();
                Socket socket = new Socket();
                InetSocketAddress addr = new InetSocketAddress(host, port);
                Log.d("NetworkUtil", "InetSocketAddress time taken: " + (System.currentTimeMillis() - start));

                start = System.currentTimeMillis();
                socket.connect(addr, timeout);
                Log.d("NetworkUtil", "connect time taken: " + (System.currentTimeMillis() - start));
                socket.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
