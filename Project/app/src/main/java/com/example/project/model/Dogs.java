package com.example.project.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Dogs {
    @SerializedName("message")
    private String imageUrl;

    @SerializedName("status")
    private String status;

    private String typeOfDog;
    private Bitmap bmpImage;

    private String filename;

    public void setBmpImage(Bitmap bmpImage) {
            this.bmpImage = bmpImage;
    }
    public Bitmap getBmpImage() {
        return bmpImage;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImageUrl() { return imageUrl; }


    public void setType(String type) { this.typeOfDog = type; }
    public String getType() { return this.typeOfDog; }

    public void setFilename(String filename) { this.filename = filename; }
    public String getFilename() { return this.filename; }


    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

