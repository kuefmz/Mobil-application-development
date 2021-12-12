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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String PARAMETER_DOG_TYPE="dogType";
    public final static String PARAMETER_DOG_IMAGE_URL="dogImageUrl";
    public final static String PARAMETER_DOG_INDEX="dogIndex";
    public final static String PARAMETER_FILENAME="dogFilename";

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
        DogsListAdapter dogsAdapter = new DogsListAdapter(results, MainActivity.this);
        lv.setAdapter(dogsAdapter);
        ((DogsListAdapter) lv.getAdapter()).addAll(results);

    }

    @Override
    public void onBackPressed()
    {
        NotificationTask.showNotification("Successful logout from Dog's app", "See You later!", MainActivity.this);

        Intent iNext = new Intent(MainActivity.this, Login.class);

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

        parameterUsername = i.getStringExtra(Login.PARAMETER_USERNAME);
        ((TextView) findViewById(R.id.txt_username)).setText("Welcome " + parameterUsername + "!");
        if(checkIfNetworkAccess()) {
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
            String filesDir = this.getFilesDir().toString();
            DownloadThread dTask = new DownloadThread(MainActivity.this,
                    randomDogs, filesDir);
            Thread th = new Thread(dTask);
            th.start();

            //Load more images button
            Button btnLoadMore = findViewById(R.id.btn_load_more);
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListView lv = findViewById(R.id.lst_planets);
                    lv.setVisibility(View.INVISIBLE);
                    ProgressBar pb = findViewById(R.id.loading_images);
                    pb.setVisibility(View.VISIBLE);

                    DownloadThread dTask = new DownloadThread(MainActivity.this,
                            randomDogs, filesDir);
                    Thread th = new Thread(dTask);
                    th.start();
                }
            });

        }else{
            //Load more images button
            Button btnLoadMore = findViewById(R.id.btn_load_more);
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

