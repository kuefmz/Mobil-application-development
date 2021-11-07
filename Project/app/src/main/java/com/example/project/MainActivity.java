package com.example.project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Tasks.DownloadThread;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public void prepareUIStartDownload(){
        ProgressBar pb = findViewById(R.id.loading_images);
        pb.setVisibility(View.VISIBLE);
    }

    public void prepareUIFinishDownload(String results){
        ProgressBar pb = findViewById(R.id.loading_images);
        pb.setVisibility(View.INVISIBLE);
        //here comes the image showing part.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Intent i = this.getIntent();
        String parameterUsername = i.getStringExtra(Login.PARAMETER_USERNAME);
        ((TextView) findViewById(R.id.txt_username)).setText("Welcome " + parameterUsername + "!");

        URL wsUrl = null;
        try{
            wsUrl = new URL("https://dog.ceo/api/breeds/image/random");
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        DownloadThread dTask = new DownloadThread(MainActivity.this, new URL[]{wsUrl});
        Thread th=new Thread(dTask);
        th.start();

    }
}

