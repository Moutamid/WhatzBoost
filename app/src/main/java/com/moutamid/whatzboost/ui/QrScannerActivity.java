package com.moutamid.whatzboost.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.fxn.stash.Stash;
import com.google.zxing.Result;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.QrResultAdapter;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityQrScannerBinding;
import com.moutamid.whatzboost.models.SearchModel;

import java.util.ArrayList;
import java.util.Collections;

public class QrScannerActivity extends AppCompatActivity {
    ActivityQrScannerBinding binding;
    private CodeScanner mCodeScanner;
    QrResultAdapter adapter;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.barcode_scanner, "Qr\nScanner");

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

        data = new ArrayList<>();
        data = Stash.getArrayList("QrResult", String.class);

        binding.recyler.setHasFixedSize(false);
        binding.recyler.setLayoutManager(new LinearLayoutManager(this));

        getData();

        mCodeScanner = new CodeScanner(this, binding.scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data.size() > 0){
                    if (data.get(0).equals(result.getText())){
                        Toast.makeText(QrScannerActivity.this, "Already scanned", Toast.LENGTH_SHORT).show();
                    } else {
                        data.add(result.getText());
                        Stash.put("QrResult", data);
                        getData();
                    }
                    mCodeScanner.stopPreview();
                    mCodeScanner.startPreview();
                } else {
                    data.add(result.getText());
                    Stash.put("QrResult", data);
                    mCodeScanner.stopPreview();
                    mCodeScanner.startPreview();
                    getData();
                }
            }
        }));

        binding.scan.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(QrScannerActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                mCodeScanner.startPreview();
            }
        });

    }

    private void getData() {
        Collections.reverse(data);
        adapter = new QrResultAdapter(this, data);
        binding.recyler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(QrScannerActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.stopPreview();
    }

    @Override
    public void onBackPressed() {
        // startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}