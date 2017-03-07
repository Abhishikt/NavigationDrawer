package com.resultstrack.navigationdrawer1.model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.resultstrack.navigationdrawer1.commonUtilities.AsyncResponse;
import com.resultstrack.navigationdrawer1.commonUtilities.RTContants;
import com.resultstrack.navigationdrawer1.commonUtilities.RTGlobal;

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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by abhishikt sk on 3/3/2017.
 */

public class OfflineData {

    private static int totalRecords=0;
    private static int syncedRecords=0;
    private static int failedRecords=0;

    public static AsyncResponse delegate=null;

    public static int getTotalRecords() {
        return totalRecords;
    }

    public static void setTotalRecords(int totalRecords) {
        OfflineData.totalRecords = totalRecords;
    }

    public static int getSyncedRecords() {
        return syncedRecords;
    }

    public static void setSyncedRecords(int syncedRecords) {
        OfflineData.syncedRecords = syncedRecords;
    }

    public static List<ChildReg> getOfflineChildRegistrationData()
    {
        List<ChildReg> lstChildReg = new ArrayList<ChildReg>();
        try{
            ChildReg oChildReg = new ChildReg();
            lstChildReg = oChildReg.getAllChildren();
            return lstChildReg;
        }catch (Exception e)
        {
            return lstChildReg;
        }
    }

    public static List<CommitteeMember> getOfflineCommitteeMemberData()
    {
        List<CommitteeMember> lstCommitteeMember = new ArrayList<CommitteeMember>();
        try{
            CommitteeMember oCommitteeMember = new CommitteeMember();
            lstCommitteeMember = oCommitteeMember.getAllMembers();
            return lstCommitteeMember;
        }catch (Exception e)
        {
            return lstCommitteeMember;
        }
    }

    public static void SyncOfflineData()
    {
        try {
            RTGlobal.setOffLineFlg(false);

            List<ChildReg> lstChildren = getOfflineChildRegistrationData();
            List<CommitteeMember> lstMember = getOfflineCommitteeMemberData();

            totalRecords = lstChildren.size() +lstMember.size();

            //Sync Registered Child
            for (final ChildReg child: lstChildren) {
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
                                data.put("oChild", child.toJSON());

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
                                    child.deleteChild();
                                    delegate.processFinish("REGCHILD");
                                    syncedRecords +=1;
                                    break;
                                case "Updated":
                                    child.deleteChild();
                                    syncedRecords +=1;
                                    break;
                                case "Exists":
                                    // delegate.processFinish("User already exists. Choose another email.");
                                    break;
                                case "Error":
                                    //delegate.processFinish("Opps!! Error encountered while creating new user profile.");
                                    failedRecords +=1;
                                    break;
                                case "Offline":
                                    failedRecords +=1;
                                    break;
                                default:
                                    //delegate.processFinish("Opps!! nothing's working. Check internet connectivity.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            delegate.processFinish("ERROR");
                            failedRecords +=1;
                        }
                    }
                }.execute();
            }
            //Sync Committee Members

            //Sync Minutes of Meeting

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
