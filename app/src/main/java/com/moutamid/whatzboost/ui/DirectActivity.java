package com.moutamid.whatzboost.ui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityDirectBinding;
import com.moutamid.whatzboost.models.SearchModel;

import java.util.ArrayList;
import java.util.Collections;

public class DirectActivity extends AppCompatActivity {
    ActivityDirectBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDirectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.adjustFontScale(getBaseContext(), getResources().getConfiguration());

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.chat, "Open\nWA Profile");
        String countryCode = Stash.getString(Constants.DefaultCountry);
        if (!countryCode.isEmpty()){
            binding.countryPick.setCountryForNameCode(countryCode);
        }

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

        binding.send.setOnClickListener(v -> {
            if (binding.number.getText().toString().isEmpty()){
                Toast.makeText(this, "Please add a number", Toast.LENGTH_SHORT).show();
            } else {
                String number = binding.countryPick.getSelectedCountryCode().toString() + binding.number.getText().toString();
                String message = binding.message.getText().toString();
                openWhatsApp(number, message);
            }
        });

    }

    private void openWhatsApp(String smsNumber, String text) {
        Log.d(TAG, "openWhatsApp: smsNumber: " + smsNumber);
        Log.d(TAG, "openWhatsApp: text: " + text);

        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                        smsNumber, text))));
    }

    @Override
    public void onBackPressed() {
        // startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}