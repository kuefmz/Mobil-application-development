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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadLocalThread implements Runnable {
    private Context ctx;
    private String path;

    public LoadLocalThread(Context ctx, String path) {
        this.ctx = ctx;
        this.path = path;
    }

    @Override
    public void run() {
        ((MainActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)ctx).prepareUIStartDownload();
            }
        });

        ArrayList<Dogs> resultPlanets = new ArrayList<Dogs>();

        for(int i = 0; i < 10; i++){
            File file = new File(path, "dog" + Integer.toString(i) + ".json");
            String fileContent =GetStringFromFileTask.getStringFromFile(file);
            JSONObject stats = null;
            try {
                stats = new JSONObject(fileContent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            String picPath = path + "/dog" + Integer.toString(i) + ".png";
            Bitmap bmpDog = BitmapFactory.decodeFile(picPath, options);

            Dogs p = new Dogs();
            try {
                p.setType(stats.getString("type"));
                p.setImageUrl(stats.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            p.setBmpImage(bmpDog);
            resultPlanets.add(p);
        }

        ((MainActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)ctx).prepareUIFinishDownload(resultPlanets);
            }
        });

    }
}
