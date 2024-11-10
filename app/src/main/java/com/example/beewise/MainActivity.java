package com.example.beewise;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000;
    private VideoView welcomeVideo;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeVideo = findViewById(R.id.videoView);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        welcomeVideo.setVideoURI(videoUri);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(welcomeVideo);
        welcomeVideo.setMediaController(mediaController);
        welcomeVideo.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(loginIntent);
                finish();

            }
        }, SPLASH_TIME_OUT);

    }
}