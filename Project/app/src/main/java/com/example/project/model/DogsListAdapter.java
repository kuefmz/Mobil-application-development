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

import java.util.ArrayList;
import java.util.List;

public class DogsListAdapter extends BaseAdapter {
    private List<Dogs> data = new ArrayList<>();
    private Context ctx;
    private Boolean removable = false;

    public DogsListAdapter(List<Dogs> dogs, Context ctx, Boolean removable) {
        data.addAll(dogs);
        this.ctx = ctx;
        this.removable = removable;
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
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.dogs_row_layout , viewGroup,false );
        }

        final int newWidth = 400;
        final int newHeight = 315;

        Bitmap newbitMap = Bitmap.createScaledBitmap(data.get(i).getBmpImage(), newWidth, newHeight, true);

        ImageView imageView = view.findViewById(R.id.image_dog);
        imageView.setImageBitmap(newbitMap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iNext = new Intent(ctx, DogsDataActivity.class);
                iNext.putExtra(MainActivity.PARAMETER_DOG_TYPE, data.get(i).getType());
                iNext.putExtra(MainActivity.PARAMETER_DOG_IMAGE_URL, data.get(i).getImageUrl());
                iNext.putExtra(MainActivity.PARAMETER_DOG_INDEX, Integer.toString(i));
                iNext.putExtra(MainActivity.PARAMETER_FILENAME, data.get(i).getFilename());
                iNext.putExtra(MainActivity.PARAMETER_REMOVABLE, removable.toString());
                ctx.startActivity(iNext);
            }
        });

        return view;
    }
}
