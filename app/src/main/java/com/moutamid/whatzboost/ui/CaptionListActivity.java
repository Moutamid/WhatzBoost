package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.CaptionsListAdapter;
import com.moutamid.whatzboost.databinding.ActivityCaptionListBinding;

public class CaptionListActivity extends AppCompatActivity {
    ActivityCaptionListBinding binding;
    String[] logos = new String[]{"Best", "Clever", "Cool", "Cute", "Fitness", "Funny", "Life", "Love", "Motivation", "Sad", "Selfie", "Song"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCaptionListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.captionRC.setLayoutManager(new GridLayoutManager(this, 2));
        binding.captionRC.setHasFixedSize(false);
        binding.captionRC.setAdapter(new CaptionsListAdapter(this, logos));

    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}