package com.resultstrack.navigationdrawer1.commonUtilities;

import android.os.AsyncTask;
import android.os.Debug;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by abhishikt on 1/21/2017.
 */

public final class HttpService extends AsyncTask<URL, String, String> {

    private static final String SERVICE_URL = "http://beta.resultstrack.in/survey/"; //"http://10.0.2.2:2011/ResultsTrackService.asmx/";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final String ENCODING_UTF_8 = "UTF-8"; //"UTF-8"

    @Override
    protected String doInBackground(URL... params) {
        return null;
    }

    public static boolean sendJsonData(JSONObject json, String operation, String param) {
        DataOutputStream doStream;
        //DataInputStream diStream;
        HttpURLConnection urlConnection = null;
        StringBuilder sBuilder;
        try {
            //Create Connection
            urlConnection = (HttpURLConnection)new URL(SERVICE_URL + operation).openConnection();
            //urlConnection.setDoInput(true);
            //urlConnection.setDoOutput(true);
            //urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod(METHOD_POST);
            urlConnection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
            //urlConnection.setChunkedStreamingMode(0);
            //urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            //urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
            //urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();

            //JSON DATA OBJECT
            JSONObject data = new JSONObject();
            data.put(param, json.toString());

            //get output stream done writing
            doStream = new DataOutputStream(urlConnection.getOutputStream());
            doStream.writeBytes(URLEncoder.encode(data.toString(),ENCODING_UTF_8));
            doStream.flush ();
            doStream.close ();

            sBuilder = new StringBuilder();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bReader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),ENCODING_UTF_8));
                String responseLine=null;
                while((responseLine=bReader.readLine())!=null){
                    sBuilder.append(responseLine + "\n");
                }
                return true;
            }else{
                String response = urlConnection.getResponseMessage();
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }catch (Exception e){
            System.console().printf(e.getMessage());
            return false;
        }finally {
            if(urlConnection!=null)
            urlConnection.disconnect();
        }
    }
}
