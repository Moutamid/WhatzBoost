package com.moutamid.whatzboost.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.ActivityMakeStoryBinding;

public class MakeStoryActivity extends AppCompatActivity {
    ActivityMakeStoryBinding binding;

    private Uri ImageURI;
    private Uri storyImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        binding.selectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, ""), 1);
        });

        binding.send.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, ""), 2);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1){
                try{
                    if (resultCode == RESULT_OK && data != null) {
                        ImageURI = data.getData();
                        binding.profileImage.setImageURI(data.getData());
                    } else {
                        Toast.makeText(this, "Please Select Images", Toast.LENGTH_SHORT).show();
                    }
                }  catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                try{
                    if (resultCode == RESULT_OK && data != null) {
                        storyImage = data.getData();
                        Intent i = new Intent(MakeStoryActivity.this, FakeStoryActivity.class);
                        i.putExtra("Name", binding.name.getText().toString());
                        i.putExtra("LastSeen", binding.lastSeen.getText().toString());
                        i.putExtra("caption", binding.status.getText().toString());
                        i.putExtra("profile", ImageURI);
                        i.putExtra("story", storyImage);
                        startActivity(i);
                    } else {
                        Toast.makeText(this, "Please Select Images", Toast.LENGTH_SHORT).show();
                    }
                }  catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}