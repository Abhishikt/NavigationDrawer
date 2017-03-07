package com.resultstrack.navigationdrawer1.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by abhishikt sk on 2/1/2017.
 */

public class SurveyResponse implements Parcelable{

    private String id;
    private String surveyId;
    private String organizationId;
    private String ques_id;
    private int ques_typ;
    private String response;
    private String response_dt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getQues_id() {
        return ques_id;
    }

    public void setQues_id(String ques_id) {
        this.ques_id = ques_id;
    }

    public int getQues_typ() {
        return ques_typ;
    }

    public void setQues_typ(int ques_typ) {
        this.ques_typ = ques_typ;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponseDate() {
        return response_dt;
    }

    public void setResponseDate(String response_dt) {
        this.response_dt = response_dt;
    }

    public SurveyResponse(){}

    public SurveyResponse(String id, String surveyId, String organizationId, String ques_id, int ques_typ, String response, String response_dt) {
        this.id = id;
        this.surveyId = surveyId;
        this.organizationId = organizationId;
        this.ques_id = ques_id;
        this.ques_typ = ques_typ;
        this.response = response;
        this.response_dt = response_dt;
    }

    public List<SurveyResponse> getSurveyResponseList(String surveyId){
        List<SurveyResponse> lstResponse = new ArrayList<SurveyResponse>();
        try{
            // Select All Query
            String selectQuery = "SELECT  * FROM " + DBAdapter.SURVEY_RESPONSE_TABLE;
            // Open database for Read / Write
            final SQLiteDatabase db = DBAdapter.open();
            //Cursor cursor = db.rawQuery ( selectQuery, null );
            Cursor cursor = db.query (DBAdapter.SURVEY_RESPONSE_TABLE,new String[] {
                    "id","surveyId","organizationId","ques_id","ques_typ","response","response_dt"}, "surveyId"+"=?",
                    new String[]{surveyId},null,null,null,null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    SurveyResponse data = new SurveyResponse();
                    data.setId(cursor.getString(0));
                    data.setSurveyId(cursor.getString(1));
                    data.setOrganizationId(cursor.getString(2));
                    data.setQues_id(cursor.getString(3));
                    data.setQues_typ(Integer.parseInt(cursor.getString(4)));
                    data.setResponse(cursor.getString(5));
                    data.setResponseDate(cursor.getString(6));
                    // Adding contact to list
                    lstResponse.add(data);
                } while (cursor.moveToNext());
            }
            // return user list
            return lstResponse;

        }catch (Exception e){
            e.printStackTrace();
            return lstResponse;
        }
    }

    public void save() {
        try {
            this.setId(UUID.randomUUID().toString());
            // Open database for Read / Write
            final SQLiteDatabase sqlDb = DBAdapter.open();

            ContentValues cVal = new ContentValues();
            cVal.put("id", getId());
            cVal.put("surveyId", getSurveyId());
            cVal.put("organizationId", getOrganizationId());
            cVal.put("ques_id", getQues_id());
            cVal.put("ques_typ", getQues_typ());
            cVal.put("response", getResponse());
            cVal.put("response_dt", getResponseDate());
            // Insert user values in database
            sqlDb.insert(DBAdapter.SURVEY_RESPONSE_TABLE, null, cVal);
            //sqlDb.close(); // Closing database connection
        }
        catch (Exception e){
            if(DBAdapter.DEBUG)
                Log.i(DBAdapter.LOG_TAG, "Exception onSaveSurveyResposne() exception");
        }
    }

    public JSONObject toJSON(){

        JSONObject jsonObject = new JSONObject();
        try {
            if(getId()!="") {
                jsonObject.put("Id", this.getId());
            }
            jsonObject.put("SurveyId", this.getSurveyId());
            jsonObject.put("OrganizationId", this.getOrganizationId());
            jsonObject.put("QuestionId", this.getQues_id());
            jsonObject.put("QuestionType", this.getQues_typ());
            jsonObject.put("Response", this.getResponse());
            jsonObject.put("ResponseDate", DateFormat.getDateTimeInstance().format(new Date()));

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

    // Deleting single contact
    public void deleteSurveyResponse(String surveyId) {
        final SQLiteDatabase db = DBAdapter.open();
        db.delete(DBAdapter.SURVEY_RESPONSE_TABLE, "surveyId" + " = ?",
                new String[] { String.valueOf(surveyId) });
        db.close();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(surveyId);
        dest.writeString(organizationId);
        dest.writeString(ques_id);
        dest.writeInt(ques_typ);
        dest.writeString(response);
        dest.writeString(response_dt);
        //dest.writeParcelable(surveyResponses);
    }

    public static final Parcelable.Creator<SurveyResponse> CREATOR
            = new Parcelable.Creator<SurveyResponse>() {
        public SurveyResponse createFromParcel(Parcel in) {
            return new SurveyResponse(in);
        }

        public SurveyResponse[] newArray(int size) {
            return new SurveyResponse[size];
        }
    };

    private SurveyResponse(Parcel in) {
        id = in.readString();
        surveyId = in.readString();
        organizationId = in.readString();
        ques_id = in.readString();
        ques_typ = in.readInt();
        response = in.readString();
        response_dt = in.readString();
    }
}
