package com.example.project.Tasks;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class SaveDogsInfoTask {

    static public void saveDogStatsToFile(String type, String url, String filename){
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", type);
            obj.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try (FileWriter file = new FileWriter(filename)) {
            file.write(obj.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void saveBitMapToFile(Bitmap bitmap, String filename){
        try (FileOutputStream out = new FileOutputStream(filename)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
