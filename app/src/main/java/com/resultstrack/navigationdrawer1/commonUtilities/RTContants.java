package com.resultstrack.navigationdrawer1.commonUtilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ProtocolException;

import android.app.Activity;

/**
 * Created by abhishikt on 1/24/2017.
 */

public final class RTContants extends Activity {

    //LOCAL
    public final static String HTTP_HOST = "192.168.0.100";  //"10.0.2.2";
    public final static int HTTP_HOST_PORT = 52011;
    public final static String HTTP_HOST_SERVER_ROOT = "http://" + HTTP_HOST + ":" + HTTP_HOST_PORT;
    //LIVE
    /*public final static String HTTP_HOST = "services.resultstrack.in";  //"10.0.2.2";
    public final static String HTTP_HOST_SERVER_ROOT = "http://" + HTTP_HOST ;*/
    public final static String RTWEBSERVICE = HTTP_HOST_SERVER_ROOT + "/ResultsTrackMService.asmx/";
    public final static String HTTP_METHOD_POST = "POST";
    public final static String HTTP_METHOD_GET = "GET";
    public final static String REQUEST_CONTENT_TYPE = "application/json; charset=utf-8";

    public static HttpURLConnection setConnectionRequestDefaults(HttpURLConnection connection){

        try {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(RTContants.HTTP_METHOD_GET);
            connection.setRequestProperty("Host",RTContants.HTTP_HOST);
            connection.setRequestProperty("Content-Type",RTContants.REQUEST_CONTENT_TYPE);
            connection.setRequestProperty("Accept","application/json");
            //urlConnection.setConnectTimeout(5000);
            connection.connect();
            return connection;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*Must be called within AsyncTask Method*/
    public static boolean socketCheck() {
        try {
            /*long start = System.currentTimeMillis();
            Socket socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress(HTTP_HOST, 80);
            Log.d("NetworkUtil", "InetSocketAddress time taken: " + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            socket.connect(addr, 5000);
            Log.d("NetworkUtil", "connect time taken: " + (System.currentTimeMillis() - start));
            socket.close();
            return true;*/
            return isInternetAvailable();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private static boolean isInternetAvailable() {
        try {
            if(RTGlobal.isOffLineFlg()){return false;}else {
                InetAddress ipAddr = InetAddress.getByName(HTTP_HOST); //You can replace it with your name
                return !ipAddr.equals("");
            }

        } catch (Exception e) {
            return false;
        }

    }

}
