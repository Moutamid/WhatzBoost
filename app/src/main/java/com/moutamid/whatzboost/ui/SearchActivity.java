package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fxn.stash.Stash;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.SearchAdapter;
import com.moutamid.whatzboost.adsense.Ads;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivitySearchBinding;
import com.moutamid.whatzboost.listners.SearchLister;
import com.moutamid.whatzboost.models.SearchModel;
import com.moutamid.whatzboost.whatsappsticker.StickerPack;
import com.moutamid.whatzboost.whatsappsticker.StickerPackDetailsActivity;
import com.moutamid.whatzboost.whatsappsticker.StickerPackListActivity;
import com.moutamid.whatzboost.whatsappsticker.StickerPackLoader;
import com.moutamid.whatzboost.whatsappsticker.StickerPackValidator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    ArrayList<SearchModel> list;
    SearchAdapter adapter;

    ProgressDialog progressDialog;

    String[] name = { "Deleted\nMessages", "Video\nSplitter", "Whatsapp\nWeb", "Open\nWA Profile", "Status\nSaver", "Text\nRepeater",
            "Fake\nProfile", "Fake\nStories", "Stickers\n ", "Caption\n ", "Poetry\n ", "Emotions\n ", "Text-to-Emoji",
            "Font Fun", "Qr\nScanner", "Qr\nGenerator", "Blank\nMessage"
    };   // "Insta\nReShare" , R.drawable.retweet
    int[] icons = {
            R.drawable.bin, R.drawable.split, R.drawable.whatsweb, R.drawable.chat, R.drawable.download, R.drawable.repeat,
            R.drawable.user, R.drawable.stories, R.drawable.sticker, R.drawable.closed_caption, R.drawable.poem, R.drawable.emotions,
            R.drawable.magic_hat, R.drawable.fonticons, R.drawable.barcode_scanner, R.drawable.qr_code, R.drawable.comment
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.adjustFontScale(getBaseContext(), getResources().getConfiguration());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        list = new ArrayList<>();
        Stash.put(Constants.RECENTS, false);
       // getList();

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getList() {
        for (int i = 0; i < name.length; i++) {
            SearchModel model = new SearchModel(icons[i], name[i]);
            list.add(model);
        }
        adapter = new SearchAdapter(SearchActivity.this, list, lister);
        binding.recycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    SearchLister lister = new SearchLister() {
        @Override
        public void click(String Name, boolean showAd, View dot, TextView view_counter) {
            switch (Name) {
                case "Deleted\nMessages":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, DeletedMessageActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, DeletedMessageActivity.class));
                    //finish();
                    break;
                case "Video\nSplitter":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, VideoSplitterActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, VideoSplitterActivity.class));
                   // finish();
                    break;
                case "Whatsapp\nWeb":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, WhatsWebActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, WhatsWebActivity.class));
                  //  finish();
                    break;
                case "Open\nWA Profile":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, DirectActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, DirectActivity.class));
                   // finish();
                    break;
                case "Status\nSaver":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, StatusSaverActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, StatusSaverActivity.class));
                   // finish();
                    break;
                case "Text\nRepeater":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, RepeaterActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, RepeaterActivity.class));
                  //  finish();
                    break;
                case "Fake\nProfile":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, MakeProfileActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, MakeProfileActivity.class));
                    //finish();
                    break;
                case "Fake\nStories":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, MakeStoryActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, MakeStoryActivity.class));
                   // finish();
                    break;
                case "Stickers\n ":
                    progressDialog.show();
                    Fresco.initialize(SearchActivity.this);
                    loadListAsyncTask = new LoadListAsyncTask(SearchActivity.this);
                    loadListAsyncTask.execute(new Void[0]);
                    break;
                case "Caption\n ":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, CaptionListActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, CaptionListActivity.class));
                    //finish();
                    break;
                case "Emotions\n ":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, EmotionsActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, EmotionsActivity.class));
                   // finish();
                    break;
                case "Poetry\n ":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, ShayariActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, ShayariActivity.class));
                   // finish();
                    break;
                case "Text-to-Emoji":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, TextToEmojiActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, TextToEmojiActivity.class));
                   // finish();
                    break;
                case "Qr\nGenerator":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, QrGeneratorActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, QrGeneratorActivity.class));
                   // finish();
                    break;
                case "Qr\nScanner":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, QrScannerActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, QrScannerActivity.class));
                   // finish();
                    break;
                case "Blank\nMessage":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, BlankMessageActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, BlankMessageActivity.class));
                   // finish();
                    break;
                case  "Font\nFun":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, FontFunActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, FontFunActivity.class));
                   // finish();
                    break;
                case "Insta\nReShare":
                    if (showAd){
                        dot.setVisibility(View.GONE);
                        view_counter.setVisibility(View.VISIBLE);
                        startCounter(view_counter, InstaReshareActivity.class);
                    } else startActivity(new Intent(SearchActivity.this, InstaReshareActivity.class));
                   // finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void startCounter(TextView view_counter, Class intent) {
        CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                view_counter.setText(String.valueOf((millisUntilFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                if (Stash.getBoolean(Constants.GUIDE_AD, true)) {
                    Constants.showAdGuide(SearchActivity.this, SearchActivity.this, intent);
                } else {
                    int rand = new Random().nextInt(101);
                    if (Stash.getBoolean(Ads.IS_ADMOB)) {
                        if (rand % 2 == 0) {
                            Ads.showInterstitial(SearchActivity.this, SearchActivity.this, intent);
                        } else {
                            Ads.showRewarded(SearchActivity.this, SearchActivity.this, intent);
                        }
                    } else {
                        if (rand % 2 == 0) {
                            Ads.showFacebookInters(SearchActivity.this, SearchActivity.this, intent);
                        } else {
                            Ads.showRewarded(SearchActivity.this, SearchActivity.this, intent);
                        }
                    }
                }
            }
        };
        countDownTimer.start();
    }

    public LoadListAsyncTask loadListAsyncTask;

    public class LoadListAsyncTask extends AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>> {
        private final WeakReference<Activity> contextWeakReference;

        LoadListAsyncTask(Activity mainActivity) {
            this.contextWeakReference = new WeakReference<>(mainActivity);
        }


        public Pair<String, ArrayList<StickerPack>> doInBackground(Void... voidArr) {
            try {
                Context context = (Context) this.contextWeakReference.get();
                if (context == null) {
                    return new Pair<>("could not fetch sticker packs", null);
                }
                ArrayList<StickerPack> fetchStickerPacks = StickerPackLoader.fetchStickerPacks(context);
                if (fetchStickerPacks.size() == 0) {
                    return new Pair<>("could not find any packs", null);
                }
                Iterator<StickerPack> it = fetchStickerPacks.iterator();
                while (it.hasNext()) {
                    StickerPackValidator.verifyStickerPackValidity(context, it.next());
                }
                return new Pair<>(null, fetchStickerPacks);
            } catch (Exception e) {
                Log.e("EntryActivity", "error fetching sticker packs", e);
                return new Pair<>(e.getMessage(), null);
            }
        }


        @Override
        public void onPostExecute(Pair<String, ArrayList<StickerPack>> pair) {
            FragmentActivity mainActivity = (FragmentActivity) this.contextWeakReference.get();
            if (mainActivity == null) {
                return;
            }
            if (pair.first != null) {
                showErrorMessagee((String) pair.first);
            } else {
                showStickerPackk((ArrayList) pair.second);
            }
        }
    }

    public void showErrorMessagee(String str) {
        progressDialog.dismiss();
        Log.e("Main activity", "error fetching sticker packs, " + str);
        Toast.makeText(this, "" + str, Toast.LENGTH_SHORT).show();
    }

    public void showStickerPackk(ArrayList<StickerPack> arrayList) {
        if (arrayList.size() > 1) {
            progressDialog.dismiss();
            Intent intent = new Intent(this, StickerPackListActivity.class);
            intent.putParcelableArrayListExtra(StickerPackListActivity.EXTRA_STICKER_PACK_LIST_DATA, arrayList);
            startActivity(intent);
            overridePendingTransition(0, 0);
            return;
        }
        progressDialog.dismiss();
        Intent intent2 = new Intent(this, StickerPackDetailsActivity.class);
        intent2.putExtra(StickerPackDetailsActivity.EXTRA_SHOW_UP_BUTTON, false);
        intent2.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_DATA, arrayList.get(0));
        startActivity(intent2);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        // startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}