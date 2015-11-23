package com.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by Rajareddy on 20/11/15.
 */
public class MyReceiver extends BroadcastReceiver
{

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();

        mContext = context;

            new DownloadFile().execute("http://t3.gstatic.com/images?q=tbn:ANd9GcQs0EPegqi56Alq4vCgC_lVDbZvJtk51RhER7AyDEVA3nUkzjMVK-yDHY3V-w");
    }

    private class DownloadFile extends AsyncTask<String,Integer,Long>
    {

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            Log.i("Dow", "onPostExecute : " + aLong);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Long doInBackground(String... url) {
            int count;

            for(int i=0;i<1000000;i++) {
                Bitmap bitmap = getBitmapFromURL(url[0]);
                SaveImage(bitmap);
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i("Dow", "onProgressUpdate : "+values[0]);
        }
    }

    private void SaveImage(Bitmap finalBitmap)
    {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromURL(String src)
    {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
