package com.example.project;

import static android.app.NotificationManager.*;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

    void shareImage(Context ctx, Bitmap bitmap, String text, String pathofBmp){
        pathofBmp = MediaStore.Images.Media.insertImage(ctx.getContentResolver(),bitmap,"Title",null);
                //MediaStore.Images.Media.insertImage(getContentResolver(),
                //        bitmap,"title", null);
        Uri uri = Uri.parse(pathofBmp);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Star App");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "hello hello"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dogsdata_page);

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
                //shareImage(DogsDataActivity.this, bmpDog, "", dogImageUrl);

            }
        });

    }
}
