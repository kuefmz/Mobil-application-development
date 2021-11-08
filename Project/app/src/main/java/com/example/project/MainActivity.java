package com.example.project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Tasks.DownloadThread;
import com.example.project.model.Dogs;
import com.example.project.model.DogsListAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public void prepareUIStartDownload(){
        ProgressBar pb = findViewById(R.id.loading_images);
        pb.setVisibility(View.VISIBLE);
        Button btnLoadMode = findViewById(R.id.btn_load_more);
        btnLoadMode.setEnabled(false);
    }

    public void prepareUIFinishDownload(List<Dogs> results){
        ProgressBar pb = findViewById(R.id.loading_images);
        pb.setVisibility(View.INVISIBLE);
        ListView lv = findViewById(R.id.lst_planets);
        lv.setVisibility(View.VISIBLE);
        Button btnLoadMode = findViewById(R.id.btn_load_more);
        btnLoadMode.setEnabled(true);

        DogsListAdapter dogsAdapter = new DogsListAdapter(results, MainActivity.this);
        lv.setAdapter(dogsAdapter);
        ((DogsListAdapter) lv.getAdapter()).addAll(results);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Intent i = this.getIntent();
        String parameterUsername = i.getStringExtra(Login.PARAMETER_USERNAME);
        ((TextView) findViewById(R.id.txt_username)).setText("Welcome " + parameterUsername + "!");

        URL[] randomDogs = new URL[10];

        try{
            randomDogs[0] = (new URL("https://dog.ceo/api/breeds/image/random"));
            randomDogs[1] = (new URL("https://dog.ceo/api/breeds/image/random"));
            randomDogs[2] = (new URL("https://dog.ceo/api/breeds/image/random"));
            randomDogs[3] = (new URL("https://dog.ceo/api/breeds/image/random"));
            randomDogs[4] = (new URL("https://dog.ceo/api/breeds/image/random"));
            randomDogs[5] = (new URL("https://dog.ceo/api/breeds/image/random"));
            randomDogs[6] = (new URL("https://dog.ceo/api/breeds/image/random"));
            randomDogs[7] = (new URL("https://dog.ceo/api/breeds/image/random"));
            randomDogs[8] = (new URL("https://dog.ceo/api/breeds/image/random"));
            randomDogs[9] = (new URL("https://dog.ceo/api/breeds/image/random"));
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        for(URL u : randomDogs){
            Log.d("Dog", String.valueOf(u));

        }

        DownloadThread dTask = new DownloadThread(MainActivity.this,
                randomDogs);
        Thread th=new Thread(dTask);
        th.start();

        Button btnLoadMore = findViewById(R.id.btn_load_more);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView lv = findViewById(R.id.lst_planets);
                lv.setVisibility(View.INVISIBLE);
                ProgressBar pb = findViewById(R.id.loading_images);
                pb.setVisibility(View.VISIBLE);

                DownloadThread dTask = new DownloadThread(MainActivity.this,
                        randomDogs);
                Thread th=new Thread(dTask);
                th.start();
            }
        });

    }
}

