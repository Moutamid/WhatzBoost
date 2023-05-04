package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.SearchAdapter;
import com.moutamid.whatzboost.databinding.ActivitySearchBinding;
import com.moutamid.whatzboost.listners.SearchLister;
import com.moutamid.whatzboost.models.SearchModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    ArrayList<SearchModel> list;
    SearchAdapter adapter;

    String[] name = { "Deleted Messages", "Video Splitter", "Whatsapp Web", "Direct Chat", "Status Saver", "Text Repeater",
            "Make Profile", "Make Stories", "Stickers", "Caption", "Shayari", "Emotions", "Text to Emoji",
            "Qr Generator", "Qr Scanner", "Message Reply", "Search Profile"
    };
    int[] icons = {
        R.drawable.bin, R.drawable.split, R.drawable.whatsweb, R.drawable.chat, R.drawable.download, R.drawable.repeat,
        R.drawable.user, R.drawable.stories, R.drawable.sticker, R.drawable.closed_caption, R.drawable.happy, R.drawable.emotions,
        R.drawable.magic_hat, R.drawable.qr_code, R.drawable.barcode_scanner, R.drawable.send, R.drawable.round_search_24
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
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
                    finish();
                    break;
                case "Video Splitter":
                    startActivity(new Intent(SearchActivity.this, VideoSplitterActivity.class));
                    finish();
                    break;
                case "Whatsapp Web":
                    startActivity(new Intent(SearchActivity.this, WhatsWebActivity.class));
                    finish();
                    break;
                case "Direct Chat":
                    startActivity(new Intent(SearchActivity.this, DirectActivity.class));
                    finish();
                    break;
                case "Status Saver":
                    startActivity(new Intent(SearchActivity.this, StatusSaverActivity.class));
                    finish();
                    break;
                case "Text Repeater":
                    startActivity(new Intent(SearchActivity.this, RepeaterActivity.class));
                    finish();
                    break;
                case "Make Profile":
                    startActivity(new Intent(SearchActivity.this, MakeProfileActivity.class));
                    finish();
                    break;
                case "Make Stories":
                    startActivity(new Intent(SearchActivity.this, MakeStoryActivity.class));
                    finish();
                    break;
                case "Stickers":
                    Toast.makeText(SearchActivity.this, "Stickers", Toast.LENGTH_SHORT).show();
                    break;
                case "Caption":
                    startActivity(new Intent(SearchActivity.this, CaptionListActivity.class));
                    finish();
                    break;
                case "Emotions":
                    startActivity(new Intent(SearchActivity.this, EmotionsActivity.class));
                    finish();
                    break;
                case "Text to Emoji":
                    startActivity(new Intent(SearchActivity.this, TextToEmojiActivity.class));
                    finish();
                    break;
                case "Qr Generator":
                    startActivity(new Intent(SearchActivity.this, QrGeneratorActivity.class));
                    finish();
                    break;
                case "Qr Scanner":
                    startActivity(new Intent(SearchActivity.this, QrScannerActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}