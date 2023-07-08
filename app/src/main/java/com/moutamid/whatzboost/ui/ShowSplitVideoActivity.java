package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.SplitVideoAdapter;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityShowSplitVideoBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ShowSplitVideoActivity extends AppCompatActivity {
    ActivityShowSplitVideoBinding binding;
    private static final String TAG = "ShowSplitVideoActivity";

    public static final int DEFAULT_COLUMN_SIZE = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
     private SplitVideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.adjustFontScale(ShowSplitVideoActivity.this);
        binding = ActivityShowSplitVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        initViews();

    }

    private void initViews() {
        binding.recycler.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ArrayList<String> list = Stash.getArrayList(Constants.RECENTS_SAVED_VIDEOS, String.class);
        binding.recycler.setLayoutManager(gridLayoutManager);
        adapter = new SplitVideoAdapter(this,list);
        binding.recycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
      //  startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}