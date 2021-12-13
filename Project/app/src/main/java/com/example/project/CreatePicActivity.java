package com.example.project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project.Tasks.GetStringFromFileTask;
import com.example.project.Tasks.SaveDogsInfoTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class CreatePicActivity extends Activity {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap myDogBm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createpic_page);

        Intent i = this.getIntent();
        String path = i.getStringExtra("path");


        this.imageView = (ImageView) this.findViewById(R.id.myimage);
        Button btnCreate = findViewById(R.id.btn_create_pic);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        Button btnSave = findViewById(R.id.btn_save_mypic);
        btnSave.setEnabled(false);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dogtype = ((EditText) findViewById(R.id.txt_mytype_input)).getText().toString();
                //Log.d("apple", path);
                File file = new File(path, "mydogs.json");
                String content = GetStringFromFileTask.getStringFromFile(file);

                JSONObject stats = null;
                try {
                    stats = new JSONObject(content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("apple", stats.toString());
                //int index = stats.length();
                ArrayList<Integer> keys = new ArrayList<>();
                for (Iterator<String> it = stats.keys(); it.hasNext(); ) {
                    String key = it.next();
                    keys.add(Integer.parseInt(key));
                }
                int index = keys.size() == 0? 1 : Collections.max(keys) + 1;
                String filename = path + "/mydog" + Integer.toString(index) + ".png";

                try {
                    stats.put(Integer.toString(index), dogtype +";" + filename);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try (FileWriter myfile = new FileWriter( path + "/mydogs.json")) {
                    myfile.write(stats.toString());
                    myfile.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                SaveDogsInfoTask.saveBitMapToFile(myDogBm, filename);

                myDogBm = null;
                imageView = null;

                Intent iNext = new Intent(CreatePicActivity.this, MyPicActivity.class);
                startActivity(iNext);
            }
        });

        //Back to mypics
        Button btnBackToMypics = findViewById(R.id.btn_back_to_mypics);
        btnBackToMypics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNext = new Intent(CreatePicActivity.this, MyPicActivity.class);
                iNext.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            myDogBm = (Bitmap) data.getExtras().get("data");
            Button btnSave = findViewById(R.id.btn_save_mypic);
            btnSave.setEnabled(true);
            imageView.setImageBitmap(myDogBm);
        }
    }
}

