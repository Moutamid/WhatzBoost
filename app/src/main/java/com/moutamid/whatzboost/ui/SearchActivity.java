package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.SearchAdapter;
import com.moutamid.whatzboost.databinding.ActivitySearchBinding;
import com.moutamid.whatzboost.fragments.FeelingFragment;
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

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    ArrayList<SearchModel> list;
    SearchAdapter adapter;

    ProgressDialog progressDialog;

    String[] name = { "Deleted Messages", "Video Splitter", "Whatsapp Web", "Direct Chat", "Status Saver", "Text Repeater",
            "Make Profile", "Make Stories", "Stickers", "Caption", "Shayari", "Emotions", "Text to Emoji",
            "Qr Generator", "Qr Scanner", "Blank Message", "Font Fun", "Insta ReShare"
    };
    int[] icons = {
            R.drawable.bin, R.drawable.split, R.drawable.whatsweb, R.drawable.chat, R.drawable.download, R.drawable.repeat,
            R.drawable.user, R.drawable.stories, R.drawable.sticker, R.drawable.closed_caption, R.drawable.happy, R.drawable.emotions,
            R.drawable.magic_hat, R.drawable.qr_code, R.drawable.barcode_scanner, R.drawable.comment, R.drawable.fonticons, R.drawable.retweet,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        list = new ArrayList<>();

        getList();

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

    SearchLister lister = new SearchLister() {
        @Override
        public void click(String Name) {
            switch (Name) {
                case "Deleted Messages":
                    startActivity(new Intent(SearchActivity.this, DeletedMessageActivity.class));
                    //finish();
                    break;
                case "Video Splitter":
                    startActivity(new Intent(SearchActivity.this, VideoSplitterActivity.class));
                   // finish();
                    break;
                case "Whatsapp Web":
                    startActivity(new Intent(SearchActivity.this, WhatsWebActivity.class));
                  //  finish();
                    break;
                case "Direct Chat":
                    startActivity(new Intent(SearchActivity.this, DirectActivity.class));
                   // finish();
                    break;
                case "Status Saver":
                    startActivity(new Intent(SearchActivity.this, StatusSaverActivity.class));
                   // finish();
                    break;
                case "Text Repeater":
                    startActivity(new Intent(SearchActivity.this, RepeaterActivity.class));
                  //  finish();
                    break;
                case "Make Profile":
                    startActivity(new Intent(SearchActivity.this, MakeProfileActivity.class));
                    //finish();
                    break;
                case "Make Stories":
                    startActivity(new Intent(SearchActivity.this, MakeStoryActivity.class));
                   // finish();
                    break;
                case "Stickers":
                    progressDialog.show();
                    Fresco.initialize(SearchActivity.this);
                    loadListAsyncTask = new LoadListAsyncTask(SearchActivity.this);
                    loadListAsyncTask.execute(new Void[0]);
                    break;
                case "Caption":
                    startActivity(new Intent(SearchActivity.this, CaptionListActivity.class));
                    //finish();
                    break;
                case "Emotions":
                    startActivity(new Intent(SearchActivity.this, EmotionsActivity.class));
                   // finish();
                    break;
                case "Text to Emoji":
                    startActivity(new Intent(SearchActivity.this, TextToEmojiActivity.class));
                   // finish();
                    break;
                case "Qr Generator":
                    startActivity(new Intent(SearchActivity.this, QrGeneratorActivity.class));
                   // finish();
                    break;
                case "Qr Scanner":
                    startActivity(new Intent(SearchActivity.this, QrScannerActivity.class));
                   // finish();
                    break;
                case "Blank Message":
                    startActivity(new Intent(SearchActivity.this, BlankMessageActivity.class));
                   // finish();
                    break;
                case  "Font Fun":
                    startActivity(new Intent(SearchActivity.this, FontFunActivity.class));
                   // finish();
                    break;
                case "Insta ReShare":
                    startActivity(new Intent(SearchActivity.this, InstaReshareActivity.class));
                   // finish();
                    break;
                default:
                    break;
            }
        }
    };

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