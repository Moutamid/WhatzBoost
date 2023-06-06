package com.moutamid.whatzboost.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.ActivityVideoSplitterBinding;
//import com.videotrimmer.library.utils.LogMessage;
//import com.videotrimmer.library.utils.TrimVideo;

public class VideoSplitterActivity extends AppCompatActivity {
    ActivityVideoSplitterBinding binding;
    private static final int REQUEST_SELECT_VIDEO = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoSplitterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        binding.split.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowSplitVideoActivity.class);
            intent.putExtra("all", true);
            startActivity(intent);
            finish();
        });

        binding.add.setOnClickListener(v -> {

            //Java



            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_SELECT_VIDEO);
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri selectedVideoUri = data.getData();
           // TrimVideo.activity(String.valueOf(selectedVideoUri))
////        .setCompressOption(new CompressOption()) //empty constructor for default compress option
//                    .setHideSeekBar(false)
//                    .setDestination("/storage/emulated/0/DCIM/WhatzBoost/")  //default output path /storage/emulated/0/DOWNLOADS
//                    .start(this);

//            String selectedVideoPath = getRealPathFromURI(selectedVideoUri);
//            // Use selectedVideoPath as needed
//            Intent intent = new Intent(VideoSplitterActivity.this, SplitVideoActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("Video_Uri", selectedVideoPath);
//            startActivity(intent);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}