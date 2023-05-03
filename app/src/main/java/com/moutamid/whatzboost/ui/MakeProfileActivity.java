package com.moutamid.whatzboost.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.ActivityMakeProfileBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MakeProfileActivity extends AppCompatActivity {
    ActivityMakeProfileBinding binding;
    private Uri ImageURI;
    final Calendar calendar = Calendar.getInstance();
    String date = "date";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeProfileBinding.inflate(getLayoutInflater());
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

        DatePickerDialog.OnDateSetListener datePick = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        binding.send.setOnClickListener(v -> {
            Intent i = new Intent(MakeProfileActivity.this, FakeProfileActivity.class);
            i.putExtra("name", binding.name.getText().toString());
            i.putExtra("lastSeen", binding.lastSeen.getText().toString());
            i.putExtra("status", binding.status.getText().toString());
            i.putExtra("statusDate", date);
            i.putExtra("mobile", binding.number.getText().toString());
            i.putExtra("ImageURI", ImageURI);
            startActivity(i);
        });

        binding.date.setOnClickListener(v -> {
            new DatePickerDialog(this, datePick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

    }

    private void updateLabel() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
        date = dateFormat.format(calendar.getTime());
        binding.date.setText(dateFormat.format(calendar.getTime()));
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