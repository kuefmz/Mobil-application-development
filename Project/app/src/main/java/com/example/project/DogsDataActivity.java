package com.example.project;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Tasks.NotificationTask;

import java.util.ArrayList;
import java.util.List;

public class DogsDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dogsdata_page);

        final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;

        Intent i = this.getIntent();
        String dogType = i.getStringExtra(MainActivity.PARAMETER_DOG_TYPE);
        String dogImageUrl = i.getStringExtra(MainActivity.PARAMETER_DOG_IMAGE_URL);
        String dogIndex = i.getStringExtra(MainActivity.PARAMETER_DOG_INDEX);
        String filename = i.getStringExtra(MainActivity.PARAMETER_FILENAME);

        NotificationTask.showNotification("Congratulations!", "Do You like " + dogType + " dogs?", DogsDataActivity.this);

        //setImage
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bmpDog = BitmapFactory.decodeFile(filename, options);
        ImageView dogImage = findViewById(R.id.img_dog);
        dogImage.setImageBitmap(bmpDog);

        //setDogType
        ((TextView) findViewById(R.id.txt_set_type)).setText(dogType);

        //setImageUrl
        ((TextView) findViewById(R.id.txt_set_url)).setText(dogImageUrl);

        //set back button
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iNext = new Intent(DogsDataActivity.this, MainActivity.class);
                iNext.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });

        //set share type button
        Button btnShareType = findViewById(R.id.btn_share_type);
        btnShareType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "The dog that I just checked has " + dogType + " type. I like it!");
                sendIntent.setType("text/pain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Dog type");
                startActivity(shareIntent);
            }
        });



        //set share image button
        Button btnSharePic = findViewById(R.id.btn_share_pic);
        btnSharePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

                    List<String> permissions = new ArrayList<String>();
                    if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    if (!permissions.isEmpty()) {
                        requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
                    }
                }

                ContentResolver cr = getContentResolver();
                String path = MediaStore.Images.Media.insertImage(cr, bmpDog,"title", "description");
                Uri imageUri = Uri.parse(path);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent,"Dog image"));


            }
        });

    }
}
