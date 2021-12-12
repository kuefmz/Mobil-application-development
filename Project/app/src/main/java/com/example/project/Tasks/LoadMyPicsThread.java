package com.example.project.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.project.MainActivity;
import com.example.project.MyPicActivity;
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
import java.util.Iterator;
import java.util.List;

public class LoadMyPicsThread implements Runnable {
    private Context ctx;

    public LoadMyPicsThread(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {

        ArrayList<Dogs> resultPlanets = new ArrayList<Dogs>();

        File file = new File(ctx.getFilesDir().toString(), "mydogs.json");
        String fileContent = GetStringFromFileTask.getStringFromFile(file);
        JSONObject stats = null;
        try {
            stats = new JSONObject(fileContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Iterator<String> it = stats.keys(); it.hasNext(); ) {
            String key = it.next();
            String filename = null;
            String type = null;
            try {
                String val = stats.getString(key);
                String[] vals = val.split(";");
                type = vals[0];
                filename = vals[1];
            } catch (JSONException e) {
                e.printStackTrace();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmpDog = BitmapFactory.decodeFile(filename, options);

            Dogs p = new Dogs();
            p.setType(type);
            p.setImageUrl(filename);
            p.setBmpImage(bmpDog);
            p.setFilename(filename);
            resultPlanets.add(p);
        }

        ((MyPicActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MyPicActivity)ctx).prepareUIFinishDownload(resultPlanets);
            }
        });
    }
}
