package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.ActivityEmotionsBinding;

public class EmotionsActivity extends AppCompatActivity {
    ActivityEmotionsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmotionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}