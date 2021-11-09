package com.example.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.project.Tasks.NetUtil;

import org.w3c.dom.Text;

import java.net.URL;
import java.net.URLConnection;

public class DogsDataActivity extends AppCompatActivity {

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
        setContentView(R.layout.dogsdata_page);

        Intent i = this.getIntent();
        String dogType = i.getStringExtra(MainActivity.PARAMETER_DOG_TYPE);
        String dogImageUrl = i.getStringExtra(MainActivity.PARAMETER_DOG_IMAGE_URL);
        String username = i.getStringExtra(Login.PARAMETER_USERNAME);

        showNotification("Congratulations!", "Do You like " + dogType + " dogs?");g

        //setImage
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bmpDog = BitmapFactory.decodeFile(this.getFilesDir() + "/dogDataImage.png", options);
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





    }
}
