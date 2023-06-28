package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Constants.adjustFontScale(getBaseContext(), getResources().getConfiguration());
    }
}