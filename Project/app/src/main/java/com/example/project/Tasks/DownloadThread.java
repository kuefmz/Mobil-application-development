package com.example.project.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.project.MainActivity;
import com.example.project.Tasks.NetUtil;
import com.example.project.model.Dogs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DownloadThread implements Runnable {
    private Context ctx;
    private URL[] urls;

    public DownloadThread(Context ctx, URL[] urls) {
        this.ctx = ctx;
        this.urls = urls;
    }

    public static String removeLastChar(String str) {
        return removeLastChars(str, 1);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }

    @Override
    public void run() {
        ((MainActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)ctx).prepareUIStartDownload();
            }
        });

        String result = "[";
        for(URL u : urls){
            result = result.toString() + NetUtil.getTextFromURL(u) + ",";
        }
        result = removeLastChar(result);
        result = result + "]";

        GsonBuilder gsb = new GsonBuilder();
        Gson gson = gsb.create();

        List<Dogs> resultPlanets = Arrays.asList(gson.fromJson(result, Dogs[].class));

        for (Dogs p : resultPlanets) {
            Bitmap bmpPlanet = null;
            try {
                bmpPlanet = NetUtil.readBitmapUrl(new URL(p.getImageUrl()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String imageUrl = p.getImageUrl();
            String [] type = imageUrl.split("/");
            p.setType(type[4]);
            p.setBmpImage(bmpPlanet);
        }

        ((MainActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)ctx).prepareUIFinishDownload(resultPlanets);
            }
        });

    }
}
