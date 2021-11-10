package com.example.project;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.project.Tasks.DownloadThread;
import com.example.project.model.Dogs;
import com.example.project.model.DogsListAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String PARAMETER_DOG_TYPE="dogType";
    public final static String PARAMETER_DOG_IMAGE_URL="dogImageUrl";
    private List<Dogs> dogs;
    private String parameterUsername;

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
        dogs = results;

        DogsListAdapter dogsAdapter = new DogsListAdapter(results, MainActivity.this);
        lv.setAdapter(dogsAdapter);
        ((DogsListAdapter) lv.getAdapter()).addAll(results);

    }

    void showNotification(String title, String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("MY_APP",
                "DOGS",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("NOTIFICATION FROM DOGS APP");
        mNotificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "MY_APP")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), Login.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Intent i = this.getIntent();
        URL[] randomDogs = new URL[10];

        parameterUsername = i.getStringExtra(Login.PARAMETER_USERNAME);
        ((TextView) findViewById(R.id.txt_username)).setText("Welcome " + parameterUsername + "!");

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

        DownloadThread dTask = new DownloadThread(MainActivity.this,
                randomDogs);
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
                        randomDogs);
                Thread th = new Thread(dTask);
                th.start();
            }
        });


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

                showNotification("Successful logout from Dog's app", "See You later!");
                startActivity(iNext);
            }
        });

    }

}

