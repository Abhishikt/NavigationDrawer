package com.resultstrack.navigationdrawer1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abhishikt on 1/28/2017.
 */

public class DownloadImage extends AsyncTask<Void, Void, Bitmap> {

    private String imageUrl;
    private ImageView imageView;
    private Context context;

    //public AsyncResponse delegate = null;
    // you may separate this or combined to caller class.
    /*public interface AsyncResponse {
        void ImageDownloaded(String output);
    }*/
    /*public DownloadImage(AsyncResponse _delegate){
        this.delegate = _delegate;
    }*/

    public DownloadImage(Context _context, String _imageUrl, ImageView _imageView){
        this.context = _context;
        this.imageUrl = _imageUrl;
        this.imageView = _imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        FileOutputStream fos;
        Bitmap myBitmap = null;
        try {
            String filename=imageUrl.substring(imageUrl.lastIndexOf("/")+1);
            //Check for image locally
            File file = new File(filename);
            if(file.exists()) {
                FileInputStream fis = context.openFileInput(filename);
                myBitmap = BitmapFactory.decodeStream(fis);
                fis.close();
            }else {
                String ImagePath = RTContants.HTTP_HOST_SERVER_ROOT + imageUrl;
                URL urlConnection = new URL(ImagePath);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
                fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }
}
