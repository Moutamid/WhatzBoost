package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityFontFunBinding;
import com.moutamid.whatzboost.fontfun.Characters;
import com.moutamid.whatzboost.fontfun.Font;
import com.moutamid.whatzboost.fontfun.FontAdapter;
import com.moutamid.whatzboost.models.SearchModel;

import java.util.ArrayList;
import java.util.Collections;

public class FontFunActivity extends AppCompatActivity {
    ActivityFontFunBinding binding;
    ArrayList<Font> fonts;
    String font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.adjustFontScale(FontFunActivity.this);
        binding = ActivityFontFunBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.fonticons, "Font\nFun");

        Bundle params = new Bundle();
        params.putString(Constants.Tool_Name, "Font Fun");
        params.putString(Constants.Type, "TOOL");
        Constants.firebaseAnalytics(this).logEvent(Constants.Most_Used_Tool, params);

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

        fonts = new ArrayList<>();

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                makeStylishOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.recyler.setHasFixedSize(false);
        binding.recyler.setLayoutManager(new LinearLayoutManager(this));

    }

    public void makeStylishOf(CharSequence charSequence) {
        char[] charArray = charSequence.toString().toLowerCase().toCharArray();
        String[] strArr = new String[44];
        for (int i = 0; i < 44; i++) {
            strArr[i] = applyStyle(charArray, Characters.strings[i]);
        }
        styleTheFont(strArr);
    }

    private String applyStyle(char[] cArr, String[] strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < cArr.length; i++) {
            if (cArr[i] - 'a' < 0 || cArr[i] - 'a' > 25) {
                stringBuffer.append(cArr[i]);
            } else {
                stringBuffer.append(strArr[cArr[i] - 'a']);
            }
        }
        return stringBuffer.toString();
    }

    private void styleTheFont(String[] strArr) {
        fonts.clear();
        font = binding.number.getText().toString().trim();
        if (!font.isEmpty()) {
            for (int i = 0; i < 44; i++) {
                Font font = new Font();
                font.fontText = strArr[i];
                fonts.add(font);
            }
            binding.recyler.setAdapter(new FontAdapter(this, fonts, (adapterView, view, i, j) -> {}));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}