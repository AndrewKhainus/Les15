package com.radomar.les15.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Radomar on 27.08.2015
 */
public class DownloadImageTask  extends AsyncTask<URL, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(URL... params) {
        return downloadImage(params[0]);
    }

    private Bitmap downloadImage(URL url) {
        Bitmap bmImg = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            int respCode = conn.getResponseCode();
            if (respCode == 200) {
                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);
                is.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmImg;
    }

}
