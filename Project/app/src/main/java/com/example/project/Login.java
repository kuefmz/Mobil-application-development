package com.example.project;


import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Login extends AppCompatActivity {

    public final static String PARAMETER_USERNAME="username";

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
        PendingIntent pi = getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        SharedPreferences sp1 = this.getSharedPreferences("Login",0);
        String uname = sp1.getString("Username", null);

        if (uname == null) {

            Button btnNext = findViewById(R.id.btn_login);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = ((EditText) findViewById(R.id.txt_name_input)).getText().toString();

                    SharedPreferences sp = getSharedPreferences("Login", 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Username", username);
                    editor.commit();

                    showNotification("Successful login to Dog's app", "Welcome " + username + "!");

                    Intent iNext = new Intent(Login.this, MainActivity.class);
                    iNext.putExtra(PARAMETER_USERNAME, username);

                    startActivity(iNext);
                }
            });
        }
        else {
            String username = uname;
            Intent iNext = new Intent(Login.this, MainActivity.class);
            iNext.putExtra(PARAMETER_USERNAME, username);

            showNotification("Successful login to Dog's app", "Welcome " + username + "!");
            startActivity(iNext);
        }

        Intent i = this.getIntent();

    }
}