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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.Tasks.NotificationTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Login extends AppCompatActivity {

    public final static String PARAMETER_USERNAME="username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        SharedPreferences sp1 = this.getSharedPreferences("Login",0);
        //Log.d("apple", Login.this.getFilesDir().toString());
        String uname = sp1.getString("Username", null);

        //Create jsom for my pics

        File f = new File(Login.this.getFilesDir().toString() + "/mydogs.json");
        if(!f.exists()) {
            try (FileWriter file = new FileWriter(Login.this.getFilesDir().toString() + "/mydogs.json")) {

                file.write("{}");
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //If the user is logged out
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

                    NotificationTask.showNotification("Successful login to Dog's app", "Welcome " + username + "!", Login.this);

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

            NotificationTask.showNotification("Successful login to Dog's app", "Welcome " + username + "!", Login.this);
            startActivity(iNext);
        }

        Intent i = this.getIntent();

    }
}