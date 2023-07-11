package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

        String url = "https://web.whatsapp.com/%F0%9F%8C%90/en";

        binding.web.setListener(this, this);
        binding.web.setMixedContentAllowed(true);
        binding.web.setDesktopMode(true);
//        setDesktopMode(binding.web, true);

        enableSettingsOfWebView(binding.web);
        binding.web.loadUrl(url);

        binding.web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);

                if (progress == 100) {
                    new Handler().postDelayed(() -> binding.progress.setVisibility(View.GONE), 4000);
                }
            }
        });


    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void enableSettingsOfWebView(AdvancedWebView webView) {
        String agent = webView.getSettings().getUserAgentString();
        Log.d("Hello", agent);
        String newUserAgent = "";
        try {
            String androidOSString = webView.getSettings().getUserAgentString().substring(agent.indexOf("("), agent.indexOf(")") + 1);
            newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
        } catch (Exception e) {
            e.printStackTrace();
            newUserAgent = null;
        }

//        webView.getSettings().setUserAgentString(newUserAgent);
//        String a = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"; // Chrome/57.0.2987.132
        String a = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0";
        webView.getSettings().setUserAgentString(a);
        Log.d("Hello", a);
        Log.d("Hello", "New User Agent : "+ newUserAgent);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setThirdPartyCookiesEnabled(false);
        webView.setCookiesEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setBlockNetworkLoads(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.reload();
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