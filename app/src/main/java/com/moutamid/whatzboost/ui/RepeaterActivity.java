package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityRepeaterBinding;
import com.moutamid.whatzboost.models.SearchModel;

import java.util.ArrayList;
import java.util.Collections;

public class RepeaterActivity extends AppCompatActivity {
    ActivityRepeaterBinding binding;
    String s = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.adjustFontScale(RepeaterActivity.this);
        binding = ActivityRepeaterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.repeat, "Text\nRepeater");

        Bundle params = new Bundle();
        params.putString(Constants.Tool_Name, "Text Repeater");
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



        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.repeatBtn.setOnClickListener(v -> {
            if (binding.repeat.getText().toString().isEmpty() || binding.message.getText().toString().isEmpty()){
                Toast.makeText(this, "Please Enter The Required Data", Toast.LENGTH_SHORT).show();
            } else {
                if (binding.newLineSwitch.isChecked()) {
                    int t = Integer.parseInt(binding.repeat.getText().toString());
                    for (int i = 0; i < t; i++) {
                        s = s + binding.message.getText().toString() + "\n";
                    }
                    binding.result.setText(s);
                } else {
                    int t = Integer.parseInt(binding.repeat.getText().toString());
                    for (int i = 0; i < t; i++) {
                        s = s + binding.message.getText().toString() + " ";
                    }
                    binding.result.setText(s);
                }
            }
        });

        binding.copy.setOnClickListener(v -> {
            int sdk = android.os.Build.VERSION.SDK_INT;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            if (binding.result.getText().toString().equals("Your Result will be here...")){
                Toast.makeText(this, "Nothing To Copy", Toast.LENGTH_SHORT).show();
            } else {
                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    clipboard.setText(binding.result.getText().toString());
                    Toast.makeText(this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                } else {
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Blank Message", binding.result.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.share.setOnClickListener(v -> {
            if (binding.result.getText().toString().equals("Your Result will be here...")){
                Toast.makeText(this, "Please repeat some text first", Toast.LENGTH_SHORT).show();
            } else {
                Intent whatsappIntent = new Intent("android.intent.action.SEND");
                whatsappIntent.setType("text/plain");
                whatsappIntent.putExtra("android.intent.extra.TEXT", binding.result.getText().toString());
                try {
                    startActivity(whatsappIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(RepeaterActivity.this, "Some problems", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.delete.setOnClickListener(v -> {
            binding.repeat.setText("");
            binding.message.setText("");
            binding.result.setText("Your Result will be here...");
            s = "";
        });

    }

    @Override
    public void onBackPressed() {
       // startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}