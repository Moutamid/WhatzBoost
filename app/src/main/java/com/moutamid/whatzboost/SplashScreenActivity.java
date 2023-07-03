package com.moutamid.whatzboost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.ui.DeletedMessageActivity;
import com.moutamid.whatzboost.ui.PermissionActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Constants.adjustFontScale(getBaseContext(), getResources().getConfiguration());
//        startActivity(new Intent(this, TestActivity.class));
//        finish();
        new Handler().postDelayed(() -> {
            if (!Constants.isPermissionGranted(SplashScreenActivity.this) && !Constants.isNotificationServiceEnabled(SplashScreenActivity.this)) {
                Dialog dialog = new Dialog(SplashScreenActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.permission_dialg);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();

                Button cancel = dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(v -> {
                    dialog.dismiss();
                    finish();
                });

                Button grant = dialog.findViewById(R.id.grant);
                grant.setOnClickListener(v -> {
                    startActivity(new Intent(SplashScreenActivity.this, PermissionActivity.class));
                    finish();
                });

            } else {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);

    }
}