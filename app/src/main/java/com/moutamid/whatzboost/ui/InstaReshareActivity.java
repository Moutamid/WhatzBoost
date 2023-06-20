package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.ActivityInstaReshareBinding;

public class InstaReshareActivity extends AppCompatActivity {
    ActivityInstaReshareBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstaReshareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}