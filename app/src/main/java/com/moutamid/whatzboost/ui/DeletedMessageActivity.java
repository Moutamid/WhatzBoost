package com.moutamid.whatzboost.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityDeletedMessageBinding;
import com.moutamid.whatzboost.fragments.MediaFragment;
import com.moutamid.whatzboost.fragments.MessagesFragment;
import com.moutamid.whatzboost.fragments.PhotoFragment;
import com.moutamid.whatzboost.fragments.VideoFragment;
import com.moutamid.whatzboost.models.SearchModel;

import java.util.ArrayList;
import java.util.Collections;

public class DeletedMessageActivity extends AppCompatActivity {
    ActivityDeletedMessageBinding binding;
    int cur = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.adjustFontScale(DeletedMessageActivity.this);

        binding = ActivityDeletedMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.bin, "Deleted\nMessages");

        Bundle params = new Bundle();
        params.putString(Constants.Tool_Name, "Deleted Messages");
        params.putString(Constants.Type, "TOOL");
        Constants.firebaseAnalytics(this).logEvent(Constants.Most_Used_Tool, params);

        if (!Constants.isPermissionGranted(DeletedMessageActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_AUDIO);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_VIDEO);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES);
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS);
                String[] per = new String[Constants.permissions13.length + 2];
                System.arraycopy(Constants.permissions13, 0, per, 0, Constants.permissions13.length+1);
                per[Constants.permissions13.length+1] = Manifest.permission.POST_NOTIFICATIONS;
                ActivityCompat.requestPermissions(DeletedMessageActivity.this, per, 1);
            } else {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(DeletedMessageActivity.this, Constants.permissions, 1);
            }
        }

        if (Stash.getBoolean("ISDELETED", true)){
            showDeleteDialog();
        }

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

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new MessagesFragment()).commit();

        binding.photoCard.setOnClickListener(v -> {
            int i = 0;
            if (i != cur){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).replace(R.id.framelayout, new MessagesFragment()).commit();
                cur = 0;
            }

            binding.messageText.setTextColor(getResources().getColor(R.color.white));
            binding.mediaText.setTextColor(getResources().getColor(R.color.text_off));

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
                    ).replace(R.id.framelayout, new MediaFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new MediaFragment()).commit();
                }
            cur = i;

            binding.messageText.setTextColor(getResources().getColor(R.color.text_off));
            binding.mediaText.setTextColor(getResources().getColor(R.color.white));

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

    private void showDeleteDialog() {
        Stash.put("ISDELETED", false);
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_intro_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        dialog.show();

        Button ok = dialog.findViewById(R.id.ok);

        ok.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }
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
        // startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}