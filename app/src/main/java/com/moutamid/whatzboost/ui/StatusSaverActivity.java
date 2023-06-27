package com.moutamid.whatzboost.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityStatusSaverBinding;
import com.moutamid.whatzboost.fragments.FakeFragment;
import com.moutamid.whatzboost.fragments.MainFragment;
import com.moutamid.whatzboost.fragments.PhotoFragment;
import com.moutamid.whatzboost.fragments.VideoFragment;
import com.moutamid.whatzboost.models.SearchModel;
import com.moutamid.whatzboost.models.StatusItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class StatusSaverActivity extends AppCompatActivity {
    ActivityStatusSaverBinding binding;
    private Uri treeUriWA, uriWA;
    private static final String EXTERNAL_STORAGE_AUTHORITY_PROVIDER = "com.android.externalstorage.documents";
    private static final String ANDROID_DOC_ID_WA = "primary:Android/media/com.whatsapp/WhatsApp/Media/.Statuses";

    int cur = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusSaverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.download, "Status\nSaver");

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

        if (!Constants.checkIfGotAccess(this, treeUriWA)) {
            if (new File(Constants.SOURCE_FOLDER_WA_NEW).exists() || new File(Constants.SOURCE_FOLDER_WA_OLD).exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    openDirectory();
                }
            }
        } else {
            try {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new PhotoFragment()).commit();
            } catch (Exception e) {}
        }

        binding.photoCard.setOnClickListener(v -> {
            int i = 0;
            if (i != cur){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).replace(R.id.framelayout, new PhotoFragment()).commit();
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
                    ).replace(R.id.framelayout, new VideoFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new VideoFragment()).commit();
                }
            cur = i;
            binding.photoCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.videoCard.setCardBackgroundColor(getResources().getColor(R.color.card));
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

                        askpermision();

                        Stash.put(Constants.WaSavedRoute, directoryUri.toString());
                        try {
                            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new PhotoFragment()).commit();
                        } catch (Exception e) {}
                        initFolders();
                        getContentResolver().takePersistableUriPermission(directoryUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    }

                } else
                    Toast.makeText(this, "You didn't grant any permission", Toast.LENGTH_SHORT).show();
            });

    private void askpermision() {
        if (!Constants.isPermissionGranted(StatusSaverActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_AUDIO);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_VIDEO);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES);
                ActivityCompat.requestPermissions(StatusSaverActivity.this, Constants.permissions13, 1);
            } else {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(StatusSaverActivity.this, Constants.permissions, 1);
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
        if (Constants.isPermissionGranted(StatusSaverActivity.this)){
            initFolders();
        }
    }

    @Override
    public void onBackPressed() {
      //  startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}