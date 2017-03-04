package com.resultstrack.navigationdrawer1.commonUtilities;

import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by abhishikt on 1/30/2017.
 */

public class FileUpload extends AsyncTask<Void,Void,String> {

    private byte[] byteArray;
    private String FileName;
    public AsyncResponse delegate = null;

    public FileUpload(byte[] bytes, String fileName){
        this.byteArray = bytes;
        this.FileName = fileName;
    }

    public FileUpload(byte[] bytes, String fileName, AsyncResponse _delegate){
        this.byteArray = bytes;
        this.FileName = fileName;
        this.delegate = _delegate;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        try {
            response = response.replace("(","").replace(");","");
            JSONObject root = new JSONObject(response);
            delegate.processFinish(root.getString("d"));
        } catch (Exception e) {
            e.printStackTrace();
            delegate.processFinish("ERROR");
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        //DataOutputStream doStream;
        HttpURLConnection urlConnection = null;
        //BufferedReader reader = null;
        StringBuilder sBuilder = null;
        try {
            boolean networkState= RTContants.socketCheck();
            if(networkState==false){return "Not Connected";}
            URL url = new URL(RTContants.RTWEBSERVICE + "UploadFileAsString"); //http://10.0.2.2:52011/ResutlsTrackService.asmx/getuser
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection = RTContants.setConnectionRequestDefaults(urlConnection);

            //JSON DATA OBJECT
            JSONObject data = new JSONObject();
            data.put("myBase64String", Base64.encodeToString(byteArray,Base64.DEFAULT));
            data.put("fileName",FileName);

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
                    sBuilder.append(responseLine);
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
}
