package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;

public class FakeStoryActivity extends AppCompatActivity {

    String name, lastSeen, caption;
    Uri profile, story;

    TextView personName, last, cap;
    ImageView imageView, personImag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_story);
        Constants.adjustFontScale(getBaseContext(), getResources().getConfiguration());

        personName = findViewById(R.id.personName);
        last = findViewById(R.id.time);
        cap = findViewById(R.id.cap);
        imageView = findViewById(R.id.storyImage);
        personImag = findViewById(R.id.profileImage);

        name = getIntent().getStringExtra("Name");
        lastSeen = getIntent().getStringExtra("LastSeen");
        caption = getIntent().getStringExtra("caption");
        profile = getIntent().getParcelableExtra("profile");
        story = getIntent().getParcelableExtra("story");

        personName.setText(name);
        last.setText(lastSeen);
        cap.setText(caption);
        imageView.setImageURI(story);
        personImag.setImageURI(profile);

    }

    @Override
    public void onBackPressed() {
      //  startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}