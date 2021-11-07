package com.example.project;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Intent i = this.getIntent();
        String parameterUsername = i.getStringExtra(Login.PARAMETER_USERNAME);
        ((TextView) findViewById(R.id.txt_username)).setText("Welcome " + parameterUsername + "!");


    }
}

