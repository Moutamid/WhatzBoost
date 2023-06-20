package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;

import java.util.Locale;

public class FakeProfileActivity extends AppCompatActivity {
    TextView num1, num2, name, status, statusDate, block, create, lastSeen;
    ImageView profileImage, backbtn;
    String personName, mobile, personStatus, persStatusDate, lastseen;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_profile);

        num1 = findViewById(R.id.num);
        num2 = findViewById(R.id.numb);
        name = findViewById(R.id.name);
        lastSeen = findViewById(R.id.lastSeen);
        profileImage = findViewById(R.id.profileImage);
        status = findViewById(R.id.status);
        statusDate = findViewById(R.id.statusdate);
        create = findViewById(R.id.create);
        block = findViewById(R.id.block);
        backbtn = findViewById(R.id.backbtn);

        personName = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");
        lastseen = getIntent().getStringExtra("lastSeen");
        personStatus = getIntent().getStringExtra("status");
        persStatusDate = getIntent().getStringExtra("statusDate");
        imageUri = getIntent().getParcelableExtra("ImageURI");

        num1.setText(mobile);
        num2.setText(mobile);
        name.setText(personName);
        profileImage.setImageURI(imageUri);
        status.setText(personStatus);
        statusDate.setText(persStatusDate);
        create.setText("Create group with " + personName);
        block.setText("Block " + personName);

        if (lastseen.toLowerCase(Locale.ROOT).contains("online")){
            lastSeen.setText("Online");
            lastSeen.setTextColor(getResources().getColor(R.color.primary));
        } else if (lastseen.toLowerCase(Locale.ROOT).contains("typing")){
            lastSeen.setText("Typing...");
            lastSeen.setTextColor(getResources().getColor(R.color.primary));
        } else {
            lastSeen.setText(lastseen);
        }

        backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    @Override
    public void onBackPressed() {
       // startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}