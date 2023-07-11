package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.constants.MyBrowser;
import com.moutamid.whatzboost.databinding.ActivityWhatsWebBinding;
import com.moutamid.whatzboost.models.SearchModel;

import java.util.ArrayList;
import java.util.Collections;

import im.delight.android.webview.AdvancedWebView;

public class WhatsWebActivity extends AppCompatActivity implements AdvancedWebView.Listener {
    ActivityWhatsWebBinding binding;


    String selectedSearchEngine = "https://www.google.com/search?q=";
    boolean caching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.adjustFontScale(WhatsWebActivity.this);
        binding = ActivityWhatsWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.whatsweb, "WA\nWeb");

        Bundle params = new Bundle();
        params.putString(Constants.Tool_Name, "WA Web");
        params.putString(Constants.Type, "TOOL");
        Constants.firebaseAnalytics(this).logEvent(Constants.Most_Used_Tool, params);

        if (recents.size() == 0) {
            recents.add(model);
            Stash.put(Constants.RECENTS_LIST, recents);
        } else {
            boolean check = false;
            Collections.reverse(recents);
            int size = recents.size() > 6 ? 6 : recents.size();
            for (int i = 0; i < size; i++) {
                if (!recents.get(i).getName().equals(model.getName())) {
                    check = true;
                } else {
                    check = false;
                    break;
                }
            }
            if (check) {
                recents.add(model);
                Stash.put(Constants.RECENTS_LIST, recents);
            }
        }

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        String url = "https://web.whatsapp.com/";

        binding.web.setListener(this, this);
        binding.web.setMixedContentAllowed(true);
        enableSettingsOfWebView(binding.web);
        binding.web.setDesktopMode(true);
//        setDesktopMode(binding.web, true);

        binding.web.loadUrl(url);
        binding.web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);

                if (progress == 100) {
                    binding.progress.setVisibility(View.GONE);
                }
            }
        });


    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void enableSettingsOfWebView(AdvancedWebView webView) {
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setThirdPartyCookiesEnabled(true);
        webView.setCookiesEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    public void setDesktopMode(AdvancedWebView webview, boolean enabled) {
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

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        binding.web.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

}