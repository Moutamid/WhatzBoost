package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityQrGeneratorBinding;
import com.moutamid.whatzboost.models.SearchModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class QrGeneratorActivity extends AppCompatActivity {
    ActivityQrGeneratorBinding binding;
    private Bitmap bitmap;
    private String savePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrGeneratorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.qr_code, "Qr\nGenerator");

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

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/QrCode/");
        savePath = file.getPath();

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.repeatbtn.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(QrGeneratorActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                if (!binding.message.getText().toString().isEmpty()){
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = Math.min(width, height);
                    smallerDimension = smallerDimension * 3 / 4;
                    // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
                    QRGEncoder qrgEncoder = new QRGEncoder(binding.message.getText().toString(), null, QRGContents.Type.TEXT, smallerDimension);
                    qrgEncoder.setColorBlack(getResources().getColor(R.color.card));
                    qrgEncoder.setColorWhite(getResources().getColor(R.color.icon));
                    // Getting QR-Code as Bitmap
                    bitmap = qrgEncoder.getBitmap();
                    // Setting Bitmap to ImageView
                    binding.image.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(this, "Add some message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.save.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                try {
                    boolean save = new QRGSaver().save(savePath, binding.message.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    String result = save ? "Image Saved" : "Image Not Saved";
                    Toast.makeText(QrGeneratorActivity.this, result, Toast.LENGTH_LONG).show();
                    binding.message.setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ActivityCompat.requestPermissions(QrGeneratorActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}