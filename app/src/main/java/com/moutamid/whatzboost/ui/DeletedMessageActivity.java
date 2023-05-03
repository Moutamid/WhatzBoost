package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.ActivityDeletedMessageBinding;
import com.moutamid.whatzboost.fragments.MediaFragment;
import com.moutamid.whatzboost.fragments.MessagesFragment;
import com.moutamid.whatzboost.fragments.PhotoFragment;
import com.moutamid.whatzboost.fragments.VideoFragment;

public class DeletedMessageActivity extends AppCompatActivity {
    ActivityDeletedMessageBinding binding;
    int cur = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeletedMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new MessagesFragment()).commit();

        binding.photoCard.setOnClickListener(v -> {
            int i = 0;
            if (i != cur){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).replace(R.id.framelayout, new MessagesFragment()).addToBackStack(null).commit();
                cur = 0;
            }


            binding.photoCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.videoCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

        binding.videoCard.setOnClickListener(v -> {
            int i = 1;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new MediaFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new MediaFragment()).addToBackStack(null).commit();
                }
            cur = i;
            binding.photoCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.videoCard.setCardBackgroundColor(getResources().getColor(R.color.card));
        });

        binding.error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeletedMessageActivity.this);
                builder.create();
                builder.setMessage("Trouble to recover media and chats? Try to restart the Notification service will help you to recover chats and media.");
                builder.setPositiveButton("Restart Service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAllowNotificaionService();
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    private void showAllowNotificaionService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Allow Notification Service");
        //builder.setIcon(R.drawable.logo_with_text);
        builder.create();
        builder.setCancelable(false);
        builder.setMessage("App needs Notification listener permission to recover deleted chats and medial. Without this permission app would not work properly.")
        ;
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //onBackPressed();
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}