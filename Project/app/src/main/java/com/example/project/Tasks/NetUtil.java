package com.example.project.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.chromium.base.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NetUtil {
    public static String getTextFromURL(URL url) {
        StringBuilder response = new StringBuilder();
        try {
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static Bitmap readBitmapUrl(URL wsUrl) {
        Bitmap result = null;
        try{
            URLConnection conn = wsUrl.openConnection();
            result = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

