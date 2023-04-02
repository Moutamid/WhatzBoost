package com.moutamid.whatzboost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startActivity(new Intent(this, MainActivity.class));
        finish();
//        new Handler().postDelayed(() -> {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }, 5000);

    }
}