package com.moutamid.whatzboost.ui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.ActivityDirectBinding;

public class DirectActivity extends AppCompatActivity {
    ActivityDirectBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDirectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
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
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}