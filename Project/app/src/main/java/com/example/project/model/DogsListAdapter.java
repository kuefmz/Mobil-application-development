package com.example.project.model;

import static androidx.core.content.ContextCompat.getSystemService;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.MainActivity;
import com.example.project.R;

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

        ImageView imageView = view.findViewById(R.id.image_planet);
        imageView.setImageBitmap(data.get(i).getBmpImage());

        return view;
    }
}
