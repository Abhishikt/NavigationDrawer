package com.resultstrack.plantation.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.resultstrack.plantation.commonUtilities.AsyncResponse;
import com.resultstrack.plantation.commonUtilities.RTContants;
import com.resultstrack.plantation.commonUtilities.RTGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public static int getOfflineSurveyResponse(Context ctx)
    {
        int count=0;
        try {
            File dir = ctx.getDir(RTGlobal.get_appUser().getEmail(), Context.MODE_PRIVATE);
            File[] files = dir.listFiles();
            count = files.length;
        }catch (Exception e)
        {
            Log.e("File Error",e.getMessage());
            //count = 0;
        }
        return count;
    }

    public static int getOfflinePlantRegistration(Context ctx)
    {
        int count=0;
        try {
            File dir = ctx.getDir(RTGlobal.get_appUser().getEmail(), Context.MODE_PRIVATE);
            //File childDir = new File(dir,"Plants");
            if(dir.exists()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    if (file.isFile())
                        count++;    //count = (files!=null)?files.length:0;
                }
            }else
                count = 0;
        }catch (Exception e)
        {
            Log.e("File Error",e.getMessage());
            count = 0;
        }
        return count;
    }

    public static void SyncOfflineSurveyData(Context ctx)
    {
        try{

            File dir = ctx.getDir(RTGlobal.get_appUser().getEmail(), Context.MODE_PRIVATE);
            String dirName = dir.getName();
            File[] files = dir.listFiles();
            if(files.length>0) {
                for (File file : files) {
                    if (file.isFile()) {
                        //String json = null;
                        String fname = dirName + "/" + file.getName();
                        InputStream inputStream = new FileInputStream(file);//ctx.openFileInput(fname);
                        int size = inputStream.available();
                        byte[] buffer = new byte[size];
                        int no_bytes_read = inputStream.read(buffer);
                        inputStream.close();
                        //json = new String(buffer, "UTF-8");
                        //JSONArray data = new JSONArray(json);
                        JSONArray data = new JSONArray(new String(buffer, "UTF-8"));
                        sendJsonResponse(data, file);
                    }
                }
            }
        }catch (Exception e){
            Log.e("File Error",e.getMessage());
        }
    }

    public static void SyncOfflinePlantData(Context ctx)
    {
        try{

            File dir = ctx.getDir(RTGlobal.get_appUser().getEmail(),Context.MODE_PRIVATE);
            String dirName = dir.getName();
            File[] files = dir.listFiles();
            if(files.length>0) {
                for (File file : files) {
                    if (file.isFile()) {
                        //String json = null;
                        String fname = dirName + "/" + file.getName();
                        InputStream inputStream = new FileInputStream(file);//ctx.openFileInput(fname);
                        int size = inputStream.available();
                        byte[] buffer = new byte[size];
                        int no_bytes_read = inputStream.read(buffer);
                        inputStream.close();
                        //json = new String(buffer, "UTF-8");
                        //JSONArray data = new JSONArray(json);
                        //JSONObject data = new JSONObject(json);
                        JSONObject data = new JSONObject(new String(buffer, "UTF-8"));
                        sendPlantJson(data, file);
                    }
                }
            }
        }catch (Exception e){
            Log.e("File Error",e.getMessage());
        }
    }

    private static void sendJsonResponse(final JSONArray _response,final File file)
    {
        try {
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
                        if(networkState){
                            URL url = new URL(RTContants.RTWEBSERVICE + "savesurveyresponse");
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection = RTContants.setConnectionRequestDefaults(urlConnection);

                            JSONObject data = new JSONObject();
                            data.put("s",_response);

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
                        }else{
                            sBuilder.append("Offline");
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
                            case "success":
                                file.delete();
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
                                break;
                            default:
                                //delegate.processFinish("Opps!! nothing's working. Check internet connectivity.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        } catch (Exception ex) {
            Log.e("Response Exception:", ex.getMessage());
        }
    }

    private static void sendPlantJson(final JSONObject _response,final File file)
    {
        try {
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
                        if(networkState){
                            URL url = new URL(RTContants.RTWEBSERVICE + "registerplant");
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection = RTContants.setConnectionRequestDefaults(urlConnection);

                            JSONObject data = new JSONObject();
                            data.put("oPlant",_response);

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
                        }else{
                            sBuilder.append("Offline");
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
                            case "success":
                            case "Inserted":
                            case "Updated":
                            case "Exists":
                                file.delete();
                                break;
                            case "Error":
                            case "Offline":
                                break;
                            default:
                                //delegate.processFinish("Opps!! nothing's working. Check internet connectivity.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        } catch (Exception ex) {
            Log.e("Response Exception:", ex.getMessage());
        }
    }
}
