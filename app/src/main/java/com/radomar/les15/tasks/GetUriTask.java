package com.radomar.les15.tasks;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radomar on 27.08.2015.
 */
public class GetUriTask extends AsyncTask<String, Void, ArrayList<String>> {


    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> list = new ArrayList<>();
        try {
            for (int i = 0; i < 30; i++ ) {
                String randUrl = getRandUrl_UrlConn(params[0]);
                list.add(randUrl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }


    private String getRandUrl_UrlConn(String _url) throws IOException {
        URL url = new URL(_url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");

        int respCode = conn.getResponseCode();
        if (respCode == 200) {
            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }

        return null;
    }
}

