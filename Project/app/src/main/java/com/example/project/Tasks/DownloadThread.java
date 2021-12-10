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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DownloadThread implements Runnable {
    private Context ctx;
    private URL[] urls;
    private String path;

    public DownloadThread(Context ctx, URL[] urls, String path) {
        this.ctx = ctx;
        this.urls = urls;
        this.path = path;
    }

    public static String removeLastChar(String str) {
        return removeLastChars(str, 1);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }

    private void saveDogStatsToFile(int index, String type, String url){
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", type);
            obj.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try (FileWriter file = new FileWriter(path + "/dog" + Integer.toString(index) +".json")) {
            file.write(obj.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveBitMapToFile(int index, Bitmap bitmap){
        try (FileOutputStream out = new FileOutputStream(path + "/dog" + Integer.toString(index) + ".png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (IOException e) {
            e.printStackTrace();
        }
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

        for (int i = 0; i< resultPlanets.size(); i++) {
            Dogs p = resultPlanets.get(i);
            Bitmap bmpPlanet = null;
            try {
                bmpPlanet = NetUtil.readBitmapUrl(new URL(p.getImageUrl()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bmpPlanet != null) {
                String[] type = p.getImageUrl().split("/");
                p.setType(type[4]);
                p.setBmpImage(bmpPlanet);
                saveDogStatsToFile(i, p.getType(), p.getImageUrl());
                saveBitMapToFile(i, bmpPlanet);
            }
        }



        ((MainActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)ctx).prepareUIFinishDownload(resultPlanets);
            }
        });

    }
}
