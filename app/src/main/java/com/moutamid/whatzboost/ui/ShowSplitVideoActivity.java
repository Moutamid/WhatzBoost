package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.ActivityShowSplitVideoBinding;
import com.moutamid.whatzboost.videosplitter.SplitVideoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ShowSplitVideoActivity extends AppCompatActivity {
    ActivityShowSplitVideoBinding binding;
    private static final String TAG = "ShowSplitVideoActivity";

    public static final int DEFAULT_COLUMN_SIZE = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
    private SplitVideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowSplitVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        initViews();
        boolean all = getIntent().getBooleanExtra("all",false);
        ArrayList<String> filePaths = getIntent().getStringArrayListExtra("split");
        if(all)
            getFiles();
        else{
            adapter.updateList(fileByName(filePaths, getIntent().getStringExtra("name")));
        }


    }

    private ArrayList<String> fileByName(ArrayList<java.lang.String> list, java.lang.String name) {
        Log.d(TAG, "fileByName: " + name);
        String rsck = name.replace(".mp4", "-");
        Log.d(TAG, "fileByName: rsk " + rsck);
        ArrayList<java.lang.String> fileter = new ArrayList<>();
        for (java.lang.String path : list) {
            Log.d(TAG, "fileByName: " + (new File(path)).getName() + " name: " + name);
            if ((new File(path)).getName().startsWith(rsck)) {
                fileter.add(path);
            }
        }
        return fileter;
    }

    private void initViews() {
        binding.recycler.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter != null) {
                    switch (adapter.getItemViewType(position)) {
                        case DEFAULT_COLUMN_SIZE:
                            return 1;
                        case FULL_WIDTH_COLUMN:
                            return 2;
                        default:
                            return -1;
                    }
                }
                return -1;
            }
        });
        binding.recycler.setLayoutManager(gridLayoutManager);
        adapter = new SplitVideoAdapter(new ArrayList<>(),this);
        binding.recycler.setAdapter(adapter);
    }

    private void getFiles() {
        ArrayList<String> splitVideoPathList = new ArrayList<>();
        new Thread(() -> {
            File fileList = new File(getImageFilePathWhatsapp());
            File[] statusFiles;
            statusFiles = fileList.listFiles();
            splitVideoPathList.clear();

            if (statusFiles != null && statusFiles.length > 0) {
                Arrays.sort(statusFiles);

                for (File file : statusFiles) {
                    splitVideoPathList.add(file.getPath());
                    Log.d(TAG, "getImagesFromFolder: image whatsapp: " + file.getName());
                }
                runOnUiThread(() -> {
                    adapter.updateList(splitVideoPathList);
                });
            }
        }).start();
    }

    private String getImageFilePathWhatsapp() {
        StringBuilder sb = new StringBuilder();
        //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        //      sb.append(CreateFolder(this));
        // } else {
        sb.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
        // }
        sb.append("/");
        sb.append(getResources().getString(R.string.VideoSplitter));
        return sb.toString();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}