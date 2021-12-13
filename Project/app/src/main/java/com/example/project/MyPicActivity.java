package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Tasks.LoadMyPicsThread;
import com.example.project.model.Dogs;
import com.example.project.model.DogsListAdapter;

import java.util.List;

public class MyPicActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public void prepareUIFinishDownload(List<Dogs> results){
        ListView lv = findViewById(R.id.lst_mydogs);
        lv.setVisibility(View.VISIBLE);
        DogsListAdapter dogsAdapter = new DogsListAdapter(results, MyPicActivity.this, true);
        lv.setAdapter(dogsAdapter);
        ((DogsListAdapter) lv.getAdapter()).addAll(results);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypic_page);

        Intent i = this.getIntent();

        //Add new pic button
        Button btnCreate = findViewById(R.id.btn_addpic);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNext = new Intent(MyPicActivity.this, CreatePicActivity.class);
                iNext.putExtra("path", MyPicActivity.this.getFilesDir().toString());
                startActivity(iNext);
            }
        });

        //Back to main
        Button btnBackToMain = findViewById(R.id.btn_back_to_main);
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNext = new Intent(MyPicActivity.this, MainActivity.class);
                iNext.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });

        LoadMyPicsThread dTask = new LoadMyPicsThread(MyPicActivity.this);
        Thread th = new Thread(dTask);
        th.start();


    }

    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.mypic_page);

        Intent i = this.getIntent();

        //Add new pic button
        Button btnCreate = findViewById(R.id.btn_addpic);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNext = new Intent(MyPicActivity.this, CreatePicActivity.class);
                iNext.putExtra("path", MyPicActivity.this.getFilesDir().toString());
                startActivity(iNext);
            }
        });

        //Back to main
        Button btnBackToMain = findViewById(R.id.btn_back_to_main);
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNext = new Intent(MyPicActivity.this, MainActivity.class);
                iNext.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(iNext);
                finish();
            }
        });

        LoadMyPicsThread dTask = new LoadMyPicsThread(MyPicActivity.this);
        Thread th = new Thread(dTask);
        th.start();
    }

}

