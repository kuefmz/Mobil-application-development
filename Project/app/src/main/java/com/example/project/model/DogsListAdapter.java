package com.example.project.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.project.DogsDataActivity;
import com.example.project.MainActivity;
import com.example.project.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DogsListAdapter extends BaseAdapter {
    private List<Dogs> data = new ArrayList<>();
    private Context main_ctx;

    public DogsListAdapter(List<Dogs> dogs, Context ctx) {
        data.addAll(dogs);
        main_ctx = (MainActivity)ctx;
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
        inflater = (LayoutInflater) main_ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.dogs_row_layout , viewGroup,false );
        }

        ImageView imageView = view.findViewById(R.id.image_dog);
        imageView.setImageBitmap(data.get(i).getBmpImage());
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iNext = new Intent(main_ctx, DogsDataActivity.class);
                iNext.putExtra(MainActivity.PARAMETER_DOG_TYPE, data.get(i).getType());
                iNext.putExtra(MainActivity.PARAMETER_DOG_IMAGE_URL, data.get(i).getImageUrl());

                try (FileOutputStream out = new FileOutputStream(main_ctx.getFilesDir() + "/dogDataImage.png")) {
                    data.get(i).getBmpImage().compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                main_ctx.startActivity(iNext);
            }
        });

        return view;
    }
}
