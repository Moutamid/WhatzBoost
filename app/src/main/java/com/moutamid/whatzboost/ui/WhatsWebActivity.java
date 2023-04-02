package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.MyBrowser;
import com.moutamid.whatzboost.databinding.ActivityWhatsWebBinding;

public class WhatsWebActivity extends AppCompatActivity {
    ActivityWhatsWebBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWhatsWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String url = "https://web.whatsapp.com/";

        binding.web.setWebViewClient(new MyBrowser());
        binding.web.getSettings().setLoadsImagesAutomatically(true);
        binding.web.getSettings().setJavaScriptEnabled(true);
        binding.web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.web.loadUrl(url);

    }
}