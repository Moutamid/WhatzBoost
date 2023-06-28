package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.constants.MyBrowser;
import com.moutamid.whatzboost.databinding.ActivityWhatsWebBinding;
import com.moutamid.whatzboost.models.SearchModel;

import java.util.ArrayList;
import java.util.Collections;

public class WhatsWebActivity extends AppCompatActivity {
    ActivityWhatsWebBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWhatsWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.adjustFontScale(getBaseContext(), getResources().getConfiguration());

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.whatsweb, "Whatsapp\nWeb");

        if (recents.size() == 0){
            recents.add(model);
            Stash.put(Constants.RECENTS_LIST, recents);
        } else {
            boolean check = false;
            Collections.reverse(recents);
            int size = recents.size() > 6 ? 6 : recents.size();
            for (int i=0; i<size; i++){
                if (!recents.get(i).getName().equals(model.getName())){
                    check = true;
                } else {
                    check = false;
                    break;
                }
            }
            if (check){
                recents.add(model);
                Stash.put(Constants.RECENTS_LIST, recents);
            }
        }

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        String url = "https://web.whatsapp.com/";

        binding.web.setWebViewClient(new MyBrowser());
        binding.web.getSettings().setLoadsImagesAutomatically(true);
        binding.web.getSettings().setJavaScriptEnabled(true);
        binding.web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        setDesktopMode(binding.web, true);
        binding.web.loadUrl(url);

    }

    public void setDesktopMode(WebView webview, boolean enabled) {
        String newUserAgent = webview.getSettings().getUserAgentString();
        if (enabled) {
            try {
                String ua = webview.getSettings().getUserAgentString();
                String androidOSString = webview.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webview.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newUserAgent = null;
        }

        webview.getSettings().setUserAgentString(newUserAgent);
        webview.getSettings().setUseWideViewPort(enabled);
        webview.getSettings().setLoadWithOverviewMode(enabled);
        webview.reload();
    }

    @Override
    public void onBackPressed() {
        // startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}