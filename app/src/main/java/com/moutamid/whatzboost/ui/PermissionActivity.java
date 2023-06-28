package com.moutamid.whatzboost.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.SplashScreenActivity;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityPermissionBinding;
import com.moutamid.whatzboost.fragments.PhotoFragment;
import com.moutamid.whatzboost.models.StatusItem;

import java.io.File;

public class PermissionActivity extends AppCompatActivity {
    ActivityPermissionBinding binding;
    private Uri treeUriWA, uriWA;
    private static final String EXTERNAL_STORAGE_AUTHORITY_PROVIDER = "com.android.externalstorage.documents";
    private static final String ANDROID_DOC_ID_WA = "primary:Android/media/com.whatsapp/WhatsApp/Media/.Statuses";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Stash.put("PERMS", 1);

        if (Constants.isPermissionGranted(PermissionActivity.this)) {
            binding.mediaLayout.setVisibility(View.GONE);
            binding.storyLayout.setVisibility(View.VISIBLE);
        }  if (!Constants.checkIfGotAccess(this, treeUriWA)) {
            binding.mediaLayout.setVisibility(View.GONE);
            binding.storyLayout.setVisibility(View.GONE);
            binding.notificationLayout.setVisibility(View.VISIBLE);
        }

        if (new File(Constants.SOURCE_FOLDER_WA_NEW).exists()) {
            uriWA = DocumentsContract.buildDocumentUri(
                    EXTERNAL_STORAGE_AUTHORITY_PROVIDER,
                    ANDROID_DOC_ID_WA
            );

            treeUriWA = DocumentsContract.buildTreeDocumentUri(
                    EXTERNAL_STORAGE_AUTHORITY_PROVIDER,
                    ANDROID_DOC_ID_WA
            );
        }

        binding.allowMedia.setOnClickListener(v -> {
            if (!Constants.isPermissionGranted(PermissionActivity.this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_AUDIO);
                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_VIDEO);
                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES);
                    shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA);
                    ActivityCompat.requestPermissions(PermissionActivity.this, Constants.permissions13, 1);
                } else {
                    shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                    ActivityCompat.requestPermissions(PermissionActivity.this, Constants.permissions, 1);
                }
            }
        });

        binding.allowStory.setOnClickListener(v -> {
            if (!Constants.checkIfGotAccess(this, treeUriWA)) {
                if (new File(Constants.SOURCE_FOLDER_WA_NEW).exists() || new File(Constants.SOURCE_FOLDER_WA_OLD).exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        openDirectory();
                    }
                }
            }
        });

        binding.allowNotification.setOnClickListener(v -> {
            if (!Constants.isNotificationServiceEnabled(this)) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });

    }

    private void initFolders() {
        File sourceDirectoryOld = new File(Constants.SOURCE_FOLDER_WA_OLD);
        getItems(sourceDirectoryOld);
    }

    private void getItems(File sourceFolder) {
        if (sourceFolder.exists()) {
            File[] sourceFiles = sourceFolder.listFiles();
            if (sourceFiles != null) getWAFiles(sourceFiles);
        }
    }

    private void getWAFiles(Object[] objects) {
        Constants.allWAImageItems.clear();
        Constants.allWAVideoItems.clear();

        // Arrays.sort(objects, (o1, o2) -> Long.compare(((File) o2).lastModified(), ((File) o1).lastModified()));

        for (File file : (File[]) objects) {
            if (!file.getPath().contains(".nomedia")) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".png")) {
                    Constants.allWAImageItems.add(new StatusItem(file, file.getName(), file.getAbsolutePath(), Uri.fromFile(file), file.lastModified(), true));
                } else if (file.getPath().endsWith(".mp4") || file.getPath().endsWith(".3gp")) {
                    Constants.allWAVideoItems.add(new StatusItem(file, file.getName(), file.getAbsolutePath(), Uri.fromFile(file), file.lastModified(), false));
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void openDirectory() {
        Intent intent = getPrimaryVolume().createOpenDocumentTreeIntent();
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriWA);
        handleIntentActivityResult.launch(intent);
    }

    ActivityResultLauncher<Intent> handleIntentActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Uri directoryUri = result.getData().getData();
                        if (!directoryUri.toString().contains(".Statuses")) {
                            Log.d("myMsg", directoryUri.toString());
                            Toast.makeText(this, "You didn't grant permission to the correct folder", Toast.LENGTH_SHORT).show();
                            return;
                        }

                       // askpermision();

                        Stash.put(Constants.WaSavedRoute, directoryUri.toString());
                        binding.mediaLayout.setVisibility(View.GONE);
                        binding.storyLayout.setVisibility(View.GONE);
                        binding.notificationLayout.setVisibility(View.VISIBLE);
                        initFolders();
                        getContentResolver().takePersistableUriPermission(directoryUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    }

                } else
                    Toast.makeText(this, "You didn't grant any permission", Toast.LENGTH_SHORT).show();
            });

    private void askpermision() {
        if (!Constants.isPermissionGranted(PermissionActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_AUDIO);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_VIDEO);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES);
                ActivityCompat.requestPermissions(PermissionActivity.this, Constants.permissions13, 1);
            } else {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(PermissionActivity.this, Constants.permissions, 1);
            }
        }
    }

    private StorageVolume getPrimaryVolume() {
        StorageManager sm = (StorageManager) getSystemService(STORAGE_SERVICE);
        return sm.getPrimaryStorageVolume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.isPermissionGranted(PermissionActivity.this)){
            initFolders();
        }
        if (Constants.isNotificationServiceEnabled(PermissionActivity.this)){
            startActivity(new Intent(PermissionActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            binding.mediaLayout.setVisibility(View.GONE);
            binding.storyLayout.setVisibility(View.VISIBLE);
            Stash.put("PERMS", 2);
        }
    }
}