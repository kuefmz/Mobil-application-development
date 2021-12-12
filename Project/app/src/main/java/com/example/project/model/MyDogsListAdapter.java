package com.example.project.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.project.DogsDataActivity;
import com.example.project.MainActivity;
import com.example.project.MyPicActivity;
import com.example.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MyDogsListAdapter extends BaseAdapter {
    private List<Dogs> data = new ArrayList<>();
    private Context mypics_ctx;

    public MyDogsListAdapter(List<Dogs> dogs, Context ctx) {
        data.addAll(dogs);
        mypics_ctx = (MyPicActivity)ctx;
    }
    public void addAll(List<Dogs> dogs) {
        data.clear();
        data.addAll(dogs);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater;
        inflater = (LayoutInflater) mypics_ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.dogs_row_layout , viewGroup,false );
        }

        ImageView imageView = view.findViewById(R.id.image_dog);
        int newWidth = 350;
        int newHeight = 315;

        Bitmap newbitMap = Bitmap.createScaledBitmap(data.get(i).getBmpImage(), newWidth, newHeight, true);
        imageView.setImageBitmap(newbitMap);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iNext = new Intent(mypics_ctx, DogsDataActivity.class);
                iNext.putExtra(MainActivity.PARAMETER_DOG_TYPE, data.get(i).getType());
                iNext.putExtra(MainActivity.PARAMETER_DOG_IMAGE_URL, data.get(i).getImageUrl());
                iNext.putExtra(MainActivity.PARAMETER_DOG_INDEX, Integer.toString(i));
                iNext.putExtra(MainActivity.PARAMETER_FILENAME, data.get(i).getFilename());

                mypics_ctx.startActivity(iNext);
            }
        });

        return view;
    }
}
