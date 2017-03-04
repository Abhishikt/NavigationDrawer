package com.resultstrack.navigationdrawer1.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by abhishikt on 1/14/2017.
 */

public class ChildReg implements Parcelable{

    private String id;
    private String name;
    private String parentName;
    private String dateOfBirth;
    private int gender;
    private String image;
    private String location;
    private String state;
    private String district;
    private String block;
    private String villageName;
    private String pincode;
    private String anganwadiCode;
    private List<SurveyResponse> surveyResponses;

    public AsyncResponse delegate = null;

    public List<SurveyResponse> getSurveyResponses() {
        return surveyResponses;
    }

    public void setSurveyResponses(List<SurveyResponse> surveyResponses) {
        this.surveyResponses = surveyResponses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public ChildReg(){}

    public ChildReg(String id, String name, String parentName, String dateOfBirth, int gender, String image, String location, String state, String district, String block, String villageName, String pincode, String anganwadiCode) {
        this.id = id;
        this.name = name;
        this.parentName = parentName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.image=image;
        this.location = location;
        this.state = state;
        this.district = district;
        this.block = block;
        this.villageName = villageName;
        this.pincode = pincode;
        this.anganwadiCode = anganwadiCode;
        this.surveyResponses = null;
    }

    public void save() {
        try {
            String newId = UUID.randomUUID().toString();
            this.setId(newId);
            // Open database for Read / Write
            final SQLiteDatabase db = DBAdapter.open();

            ContentValues cVal = new ContentValues();
            cVal.put("id", getId());
            cVal.put("name", getName());
            cVal.put("parentName", getParentName());
            cVal.put("dateOfBirth", getDateOfBirth());
            cVal.put("gender", getGender());
            cVal.put("image", getImage());
            cVal.put("location", getLocation());
            cVal.put("state", getState());
            cVal.put("district", getDistrict());
            cVal.put("block", getBlock());
            cVal.put("villageName", getVillageName());
            cVal.put("pincode", getPincode());
            cVal.put("anganwadiCode", getAnganwadiCode());

            for (SurveyResponse response: surveyResponses) {
                response.setSurveyId(newId);
                response.save();
            }
            // Insert user values in database
            db.insert(DBAdapter.CHILD_TABLE, null, cVal);
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
                            URL url = new URL(RTContants.RTWEBSERVICE + "registerchild");
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection = RTContants.setConnectionRequestDefaults(urlConnection);

                            //JSON DATA OBJECT
                            JSONObject data = new JSONObject();
                            data.put("oChild", toJSON());

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
                                deleteChild();
                                delegate.processFinish("User profile saved.");
                                break;
                            case "Updated":
                                //delegate.processFinish("User profile updated.");
                            case "Exists":
                                // delegate.processFinish("User already exists. Choose another email.");
                                break;
                            case "Error":
                                //delegate.processFinish("Opps!! Error encountered while creating new user profile.");
                                break;
                            case "Offline":
                                delegate.processFinish("Saved offline.");
                                break;
                            default:
                                //delegate.processFinish("Opps!! nothing's working. Check internet connectivity.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.processFinish("Saved offline.");
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
        values.put("name", getName());
        values.put("parentName", getParentName());
        values.put("dateOfBirth", getDateOfBirth());
        values.put("gender", getGender());
        values.put("gender", getImage());
        values.put("location", getLocation());
        values.put("state", getState());
        values.put("district", getDistrict());
        values.put("block", getBlock());
        values.put("villageName", getVillageName());
        values.put("pincode", getPincode());
        values.put("anganwadiCode", getAnganwadiCode());

        // updating row
        return db.update(DBAdapter.CHILD_TABLE, values, "id" + " = ?",
                new String[] { String.valueOf(getId()) });
    }

    // Getting single contact
    public static ChildReg getChild(int _id) {

        // Open database for Read / Write
        final SQLiteDatabase db = DBAdapter.open();

        Cursor cursor = db.query(DBAdapter.CHILD_TABLE, new String[] { "id",
                        "name", "parentName", "dateOfBirth", "gender", "image", "location",
                        "state", "district", "block", "villageName", "pincode", "anganwadiCode"}, "id" + "=?",
                new String[] { String.valueOf(_id) }, null, null, null, null);


        if (cursor != null)
            cursor.moveToFirst();

        ChildReg data = new ChildReg(cursor.getString(0),
                cursor.getString(1),cursor.getString(2),cursor.getString(3),
                Integer.parseInt(cursor.getString(4)),cursor.getString(5),
                cursor.getString(6),cursor.getString(7),cursor.getString(8),
                cursor.getString(9),cursor.getString(10),cursor.getString(11),
                cursor.getString(12));
        // return user data
        return data;
    }

    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("Id", getId());
            jsonObject.put("ChildName", getName());
            jsonObject.put("ParentName", getParentName());
            jsonObject.put("DOB",getDateOfBirth());
            jsonObject.put("Gender",getGender());
            jsonObject.put("ImagePath",getImage());
            jsonObject.put("Location",getLocation());
            jsonObject.put("State",getState());
            jsonObject.put("District",getDistrict());
            jsonObject.put("Block",getBlock());
            jsonObject.put("VillageName",getVillageName());
            jsonObject.put("Pincode",getPincode());
            jsonObject.put("AnganwadiCode",getAnganwadiCode());
            jsonObject.put("CreatedBy","");
            jsonObject.put("CreatedDate", new Date().toString());

            JSONArray jsonArray = new JSONArray();
            for (SurveyResponse response:surveyResponses) {
               jsonArray.put(response.toJSON());
            }
            jsonObject.put("SurveyResponses",jsonArray);

            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return jsonObject;
        }
    }

    // Getting All User data
    public List<ChildReg> getAllChildren() {

        List<ChildReg> childRegList = new ArrayList<ChildReg>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBAdapter.CHILD_TABLE;
        // Open database for Read / Write
        final SQLiteDatabase db = DBAdapter.open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChildReg data = new ChildReg();
                data.setId(cursor.getString(0));
                data.setName(cursor.getString(1));
                data.setParentName(cursor.getString(2));
                data.setDateOfBirth(cursor.getString(3));
                data.setGender(Integer.parseInt(cursor.getString(4)));
                data.setImage(cursor.getString(5));
                data.setLocation(cursor.getString(6));
                data.setState(cursor.getString(7));
                data.setDistrict(cursor.getString(8));
                data.setBlock(cursor.getString(9));
                data.setVillageName(cursor.getString(10));
                data.setPincode(cursor.getString(11));
                data.setAnganwadiCode(cursor.getString(12));

                // Adding contact to list
                childRegList.add(data);
            } while (cursor.moveToNext());
        }
        // return user list
        return childRegList;
    }

    // Deleting single contact
    public void deleteChild() {
        final SQLiteDatabase db = DBAdapter.open();
        db.delete(DBAdapter.CHILD_TABLE, "id" + " = ?",
                new String[] { String.valueOf(this.getId()) });
        db.close();
    }

    // Getting dataCount
    public static int getChildrenCount() {
        final SQLiteDatabase db = DBAdapter.open();

        String countQuery = "SELECT  * FROM " + DBAdapter.CHILD_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(parentName);
        dest.writeString(dateOfBirth);
        dest.writeInt(gender);
        dest.writeString(image);
        dest.writeString(location);
        dest.writeString(state);
        dest.writeString(district);
        dest.writeString(block);
        dest.writeString(villageName);
        dest.writeString(pincode);
        dest.writeString(anganwadiCode);
        dest.writeList(surveyResponses);
    }

    public static final Parcelable.Creator<ChildReg> CREATOR
            = new Parcelable.Creator<ChildReg>() {
        public ChildReg createFromParcel(Parcel in) {
            return new ChildReg(in);
        }

        public ChildReg[] newArray(int size) {
            return new ChildReg[size];
        }
    };

    private ChildReg(Parcel in) {
        id = in.readString();
        name = in.readString();
        parentName = in.readString();
        dateOfBirth = in.readString();
        gender = in.readInt();
        image = in.readString();
        location = in.readString();
        state = in.readString();
        district = in.readString();
        block = in.readString();
        villageName = in.readString();
        pincode = in.readString();
        anganwadiCode = in.readString();
        in.readList(surveyResponses,SurveyResponse.class.getClassLoader());
    }
}
