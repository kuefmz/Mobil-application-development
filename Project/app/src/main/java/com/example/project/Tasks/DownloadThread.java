package com.example.project.Tasks;

import android.content.Context;
import android.util.Log;

import com.example.project.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadThread implements Runnable {
    private Context ctx;
    private URL[] urls;

    public DownloadThread(Context ctx, URL... urls){
        this.ctx =ctx;
        this.urls = urls;
    }

    @Override
    public void run() {

        ((MainActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)ctx).prepareUIStartDownload();
            }
        });

        //NetUtil Part

        StringBuilder response = new StringBuilder();
        try {
            URLConnection conn = urls[0].openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            Log.d("Apple", response.toString());
            in.close();
            //return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //NetUtil part end


        String result = response.toString();
        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }


        ((MainActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)ctx).prepareUIFinishDownload(result);
            }
        });
    }

}
