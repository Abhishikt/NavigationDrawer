package com.resultstrack.navigationdrawer1.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.resultstrack.navigationdrawer1.commonUtilities.AsyncResponse;
import com.resultstrack.navigationdrawer1.commonUtilities.RTContants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by abhishikt on 1/14/2017.
 */

public class CommitteeMember {
    private String id;
    private String fstnm;
    private String lstnm;
    private String designation;
    private String title;
    private String email;
    private String phone;
    private String address;
    private String image;
    private String location;
    private String state;
    private String district;
    private String block;
    private String villageName;
    private String pincode;
    private String anganwadiCode;
    private static final int memberCount = 9;
    //private static final String CMEMBERS="cm.json";
    private ArrayList<CommitteeMember> CommitteeMembers = new ArrayList<CommitteeMember>();

    public AsyncResponse delegate = null;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getFstnm() { return fstnm; }
    public void setFstnm(String fstnm) { this.fstnm = fstnm; }

    public String getLstnm() { return lstnm; }
    public void setLstnm(String lstnm) { this.lstnm = lstnm; }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBlock() {
        return block;
    }
    public void setBlock(String block) {
        this.block = block;
    }

    public String getVillageName() {
        return villageName;
    }
    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getPincode() {
        return pincode;
    }
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAnganwadiCode() {
        return anganwadiCode;
    }
    public void setAnganwadiCode(String anganwadiCode) {
        this.anganwadiCode = anganwadiCode;
    }

    public static int getMemberCount() {
        return memberCount;
    }

    public CommitteeMember(){}

    public CommitteeMember(String id, String fstnm, String lstnm, String designation, String title,
                           String email, String phone, String address, String image, String location,
                           String state, String district, String block, String villageName,
                           String pincode, String anganwadiCode) {
        this.id = id;
        this.fstnm = fstnm;
        this.lstnm = lstnm;
        this.designation = designation;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.location = location;
        this.state = state;
        this.district = district;
        this.block = block;
        this.villageName = villageName;
        this.pincode = pincode;
        this.anganwadiCode = anganwadiCode;
    }

    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("Id", getId());
            jsonObject.put("MemberName", getFstnm()+" "+getLstnm());
            jsonObject.put("Designation", getDesignation());
            jsonObject.put("Title",getTitle());
            jsonObject.put("Email",getEmail());
            jsonObject.put("PhoneNo",getPhone());
            jsonObject.put("Address",getAddress());
            jsonObject.put("ImagePath",getImage());
            jsonObject.put("Location",getLocation());
            jsonObject.put("State",getState());
            jsonObject.put("District",getDistrict());
            jsonObject.put("Block",getBlock());
            jsonObject.put("VillageName",getVillageName());
            jsonObject.put("Pincode",getPincode());
            jsonObject.put("AnganwadiCode",getAnganwadiCode());
            jsonObject.put("CreatedBy","");
            jsonObject.put("CreatedDate", DateFormat.getDateTimeInstance().format(new Date()));

            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return jsonObject;
        }
    }

    public ArrayList<CommitteeMember> getCommitteeMembers() {
        JSONArray jsonArray=new JSONArray();

        return CommitteeMembers;
    }

    public void save() {
        try {
            // Open database for Read / Write
            final SQLiteDatabase db = DBAdapter.open();
        /*String name = sqlEscapeString(uData.getName());
        String email = sqlEscapeString(uData.getEmail());*/
            this.setId(UUID.randomUUID().toString());

            ContentValues cVal = new ContentValues();
            cVal.put("id", getId());
            cVal.put("fstnm", getFstnm());
            cVal.put("lstnm", getLstnm());
            cVal.put("designation", getDesignation());
            cVal.put("title", getTitle());
            cVal.put("email", getEmail());
            cVal.put("phone", getPhone());
            cVal.put("address", getAddress());
            cVal.put("image", getImage());
            cVal.put("location", getLocation());
            cVal.put("state", getState());
            cVal.put("district", getDistrict());
            cVal.put("block", getBlock());
            cVal.put("villageName", getVillageName());
            cVal.put("pincode", getPincode());
            cVal.put("anganwadiCode", getAnganwadiCode());
            // Insert user values in database
            db.insert(DBAdapter.MEMBER_TABLE, null, cVal);
            db.close(); // Closing database connection

            //sync data to server
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params) {
                    //DataOutputStream doStream;
                    HttpURLConnection urlConnection = null;
                    //BufferedReader reader = null;
                    StringBuilder sBuilder = null;
                    try {
                        boolean networkState = RTContants.socketCheck();
                        sBuilder = new StringBuilder();
                        if(networkState==false){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("d","Offline");
                            sBuilder.append(jsonObject.toString());
                            //return "Not Connected";
                        }
                        else {
                            URL url = new URL(RTContants.RTWEBSERVICE + "registermember");
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection = RTContants.setConnectionRequestDefaults(urlConnection);

                            //JSON DATA OBJECT
                            JSONObject data = new JSONObject();
                            data.put("oMember", toJSON());

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
                        switch (root.getString("d")){
                            case "Inserted":
                                deleteCommitteeMember();
                                delegate.processFinish("User profile saved.");
                                break;
                            case "Updated":
                                //delegate.processFinish("User profile updated.");
                            case "Exists":
                                // delegate.processFinish("User already exists. Choose another email.");
                                break;
                            case "Error":
                                delegate.processFinish("Opps!! Error encountered while creating member profile.");
                                break;
                            case "Offline":
                                delegate.processFinish("Saved offline.");
                                break;
                            default:
                                //delegate.processFinish("Opps!! nothing's working. Check internet connectivity.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
        catch (Exception e){
            if(DBAdapter.DEBUG)
                Log.i(DBAdapter.LOG_TAG, "Exception onSaveChildRegistration() exception");
        }
    }

    // Updating single data
    public int update() {

        final SQLiteDatabase db = DBAdapter.open();

        ContentValues values = new ContentValues();
        values.put("fstnm", getFstnm());
        values.put("lstnm", getLstnm());
        values.put("designation", getDesignation());
        values.put("title", getTitle());
        values.put("email", getEmail());
        values.put("phone", getPhone());
        values.put("address", getAddress());
        values.put("image", getImage());
        values.put("location", getLocation());
        values.put("state", getState());
        values.put("district", getDistrict());
        values.put("block", getBlock());
        values.put("villageName", getVillageName());
        values.put("pincode", getPincode());
        values.put("anganwadiCode", getAnganwadiCode());

        // updating row
        return db.update(DBAdapter.MEMBER_TABLE, values, "id" + " = ?",
                new String[] { String.valueOf(getId()) });
    }

    // Getting single contact
    public static CommitteeMember getCommitteeMember(int _id) {

        // Open database for Read / Write
        final SQLiteDatabase db = DBAdapter.open();

        Cursor cursor = db.query(DBAdapter.MEMBER_TABLE, new String[] { "id",
                        "fstnm","lstnm","designation","title","email","phone","address",
                        "image","location","state","district","block","villageName","pincode",
                        "anganwadiCode" }, "id" + "=?",
                new String[] { String.valueOf(_id) }, null, null, null, null);


        if (cursor != null)
            cursor.moveToFirst();

        CommitteeMember data = new CommitteeMember(cursor.getString(0),
                cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),
                cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),
                cursor.getString(13),cursor.getString(14),cursor.getString(15));

        // return user data
        return data;
    }

    // Getting All User data
    public List<CommitteeMember> getAllMembers() {

        List<CommitteeMember> memberList = new ArrayList<CommitteeMember>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBAdapter.MEMBER_TABLE;
        // Open database for Read / Write
        final SQLiteDatabase db = DBAdapter.open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CommitteeMember data = new CommitteeMember();
                data.setId(cursor.getString(0));
                data.setFstnm(cursor.getString(1));
                data.setLstnm(cursor.getString(2));
                data.setDesignation(cursor.getString(3));
                data.setTitle(cursor.getString(4));
                data.setEmail(cursor.getString(5));
                data.setPhone(cursor.getString(6));
                data.setAddress(cursor.getString(7));
                data.setImage(cursor.getString(8));
                data.setLocation(cursor.getString(9));
                data.setState(cursor.getString(10));
                data.setDistrict(cursor.getString(11));
                data.setBlock(cursor.getString(12));
                data.setVillageName(cursor.getString(13));
                data.setPincode(cursor.getString(14));
                data.setAnganwadiCode(cursor.getString(15));

                // Adding contact to list
                memberList.add(data);
            } while (cursor.moveToNext());
        }
        // return user list
        return memberList;
    }

    // Deleting single contact
    public void deleteCommitteeMember(CommitteeMember data) {
        final SQLiteDatabase db = DBAdapter.open();
        db.delete(DBAdapter.MEMBER_TABLE, "id" + " = ?",
                new String[] { String.valueOf(data.getId()) });
        db.close();
    }

    // Deleting single contact
    public void deleteCommitteeMember() {
        final SQLiteDatabase db = DBAdapter.open();
        db.delete(DBAdapter.MEMBER_TABLE, "id" + " = ?",
                new String[] { String.valueOf(this.getId()) });
        db.close();
    }

    // Getting dataCount
    public static int getUserDataCount() {
        final SQLiteDatabase db = DBAdapter.open();

        String countQuery = "SELECT  * FROM " + DBAdapter.MEMBER_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
