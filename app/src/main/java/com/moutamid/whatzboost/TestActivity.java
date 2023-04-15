package com.moutamid.whatzboost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.whatzboost.databinding.ActivityTestBinding;
import com.moutamid.whatzboost.views.VerticalTabLayout;

public class TestActivity extends AppCompatActivity {
    ActivityTestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        VerticalTabLayout tabLayout = binding.tabLayout;
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));



    }
}