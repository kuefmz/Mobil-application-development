package com.example.project;

import static android.app.NotificationManager.*;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.project.Tasks.NetUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DogsDataActivity extends AppCompatActivity {

    public static final int PERMISSION_WRITE = 0;

    void showNotification(String title, String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("MY_APP",
                "DOGS",
                IMPORTANCE_DEFAULT);
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
        setContentView(R.layout.dogsdata_page);

        final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;

        Intent i = this.getIntent();
        String dogType = i.getStringExtra(MainActivity.PARAMETER_DOG_TYPE);
        String dogImageUrl = i.getStringExtra(MainActivity.PARAMETER_DOG_IMAGE_URL);

        showNotification("Congratulations!", "Do You like " + dogType + " dogs?");

        //setImage
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        String picPath = this.getFilesDir() + "/dogDataImage.png";
        Bitmap bmpDog = BitmapFactory.decodeFile(picPath, options);
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
                //Uri uri = Uri.parse("content://media/external/images/media");
                //String provider = "com.android.providers.media.MediaProvider";

                //grantUriPermission(provider, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //grantUriPermission(provider, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //grantUriPermission(provider, uri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                String path = MediaStore.Images.Media.insertImage(cr, bmpDog,"title", "description");
                Uri imageUri = Uri.parse(path);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                //shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //shareIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //shareIntent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent,"Dog image"));


            }
        });

    }
}
