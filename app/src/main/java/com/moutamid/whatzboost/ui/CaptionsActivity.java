package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.CaptionAdapter;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityCaptionsBinding;

public class CaptionsActivity extends AppCompatActivity {
    ActivityCaptionsBinding binding;
    String[] captions;
    String name;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.adjustFontScale(CaptionsActivity.this);
        binding = ActivityCaptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        name = getIntent().getStringExtra(Constants.NAME);
        position = getIntent().getIntExtra(Constants.POS, 0);

        binding.captionTitle.setText(name);

        binding.captionRC.setLayoutManager(new GridLayoutManager(this,1));
        binding.captionRC.setHasFixedSize(false);

        if (position == 0) {
            captions = getResources().getStringArray(R.array.s_best);
        } else if (position == 1) {
            captions = getResources().getStringArray(R.array.s_clever);
        } else if (position == 2) {
            captions = getResources().getStringArray(R.array.s_cool);
        } else if (position == 3) {
            captions = getResources().getStringArray(R.array.s_cute);
        } else if (position == 4) {
            captions = getResources().getStringArray(R.array.s_fitness);
        } else if (position == 5) {
            captions = getResources().getStringArray(R.array.s_funny);
        } else if (position == 6) {
            captions = getResources().getStringArray(R.array.s_life);
        } else if (position == 7) {
            captions = getResources().getStringArray(R.array.s_love);
        } else if (position == 8) {
            captions = getResources().getStringArray(R.array.s_motivation);
        } else if (position == 9) {
            captions = getResources().getStringArray(R.array.s_sad);
        } else if (position == 10) {
            captions = getResources().getStringArray(R.array.s_selfie);
        } else if (position == 11) {
            captions = getResources().getStringArray(R.array.s_song);
        }

        binding.captionRC.setAdapter(new CaptionAdapter(this, captions));

    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}