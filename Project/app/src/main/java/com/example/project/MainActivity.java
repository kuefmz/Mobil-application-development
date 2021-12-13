package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Tasks.DownloadThread;
import com.example.project.Tasks.LoadLocalThread;
import com.example.project.Tasks.NotificationTask;
import com.example.project.model.Dogs;
import com.example.project.model.DogsListAdapter;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String PARAMETER_DOG_TYPE="dogType";
    public final static String PARAMETER_DOG_IMAGE_URL="dogImageUrl";
    public final static String PARAMETER_DOG_INDEX="dogIndex";
    public final static String PARAMETER_FILENAME="dogFilename";
    public final static String PARAMETER_REMOVABLE="removable";

    private List<Dogs> dogs;
    private String parameterUsername;

    private boolean checkIfNetworkAccess(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }

    public void prepareUIStartDownload(){
        ProgressBar pb = findViewById(R.id.loading_images);
        ListView lv = findViewById(R.id.lst_planets);
        lv.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        Button btnLoadMore = findViewById(R.id.btn_load_more);
        btnLoadMore.setEnabled(false);
    }

    public void prepareUIFinishDownload(List<Dogs> results){
        ProgressBar pb = findViewById(R.id.loading_images);
        pb.setVisibility(View.INVISIBLE);
        ListView lv = findViewById(R.id.lst_planets);
        lv.setVisibility(View.VISIBLE);
        Button btnLoadMode = findViewById(R.id.btn_load_more);
        btnLoadMode.setEnabled(true);
        dogs = results;
        DogsListAdapter dogsAdapter = new DogsListAdapter(results, MainActivity.this, false);
        lv.setAdapter(dogsAdapter);
        ((DogsListAdapter) lv.getAdapter()).addAll(results);

    }

    private void logoutUser() {
        try (FileWriter file = new FileWriter(MainActivity.this.getFilesDir().toString() + "/mydogs.json")) {
            file.write("{}");
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        NotificationTask.showNotification("Successful logout from Dog's app", "See You later!", MainActivity.this);

        Intent iNext = new Intent(MainActivity.this, Login.class);

        logoutUser();
        SharedPreferences sp = getSharedPreferences("Login", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Username", null);
        editor.commit();
        startActivity(iNext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Intent i = this.getIntent();
        URL[] randomDogs = new URL[10];
        try {
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Load more images button
        Button btnLoadMore = findViewById(R.id.btn_load_more);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfNetworkAccess()) {
                    ListView lv = findViewById(R.id.lst_planets);
                    btnLoadMore.setText("Load more images");
                    lv.setVisibility(View.INVISIBLE);
                    ProgressBar pb = findViewById(R.id.loading_images);
                    pb.setVisibility(View.VISIBLE);

                    DownloadThread dTask = new DownloadThread(MainActivity.this,
                            randomDogs, MainActivity.this.getFilesDir().toString());
                    Thread th = new Thread(dTask);
                    th.start();
                } else {
                    btnLoadMore.setText("Offline");
                }
            }
        });

        parameterUsername = i.getStringExtra(Login.PARAMETER_USERNAME);
        ((TextView) findViewById(R.id.txt_username)).setText("Welcome " + parameterUsername + "!");
        if(checkIfNetworkAccess()) {
            DownloadThread dTask = new DownloadThread(MainActivity.this,
                    randomDogs, this.getFilesDir().toString());
            Thread th = new Thread(dTask);
            th.start();
        }else{
            btnLoadMore.setText("Offline");
            LoadLocalThread dTask = new LoadLocalThread(MainActivity.this, this.getFilesDir().toString());
            Thread th = new Thread(dTask);
            th.start();
        }



        //LogoutButton
        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iNext = new Intent(MainActivity.this, Login.class);

                SharedPreferences sp = getSharedPreferences("Login", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Username", null);
                editor.commit();

                logoutUser();

                NotificationTask.showNotification("Successful logout from Dog's app", "See You later!", MainActivity.this);
                startActivity(iNext);
            }
        });

        //Go to My Pic Buttom
        Button btnMypic = findViewById(R.id.btn_mypic);
        btnMypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNext = new Intent(MainActivity.this, MyPicActivity.class);
                startActivity(iNext);
            }
        });

    }

}

