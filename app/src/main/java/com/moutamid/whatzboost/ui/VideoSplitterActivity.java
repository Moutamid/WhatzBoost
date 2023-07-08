package com.moutamid.whatzboost.ui;

import static com.moutamid.whatzboost.constants.Constants.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityVideoSplitterBinding;
import com.moutamid.whatzboost.models.SearchModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
//import com.videotrimmer.library.utils.LogMessage;
//import com.videotrimmer.library.utils.TrimVideo;

public class VideoSplitterActivity extends AppCompatActivity {
    ActivityVideoSplitterBinding binding;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 32;
    private static final int REQUEST_LOAD_VIDEOS = 100;
    public static int REQUEST_DOWNLOADS = 4;
    private ArrayList<String> videoPaths = new ArrayList<>();
    public static int REQUEST_FILES = 5;

    private static final String AUDIO = "audio";
    public File folder;
    public File audioFolder;
    private String SPLIT_VIDEO = "Split Video";
    public File splitVideoFolder;
    public File Statusbackupfolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.adjustFontScale(VideoSplitterActivity.this);
        binding = ActivityVideoSplitterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.split, "Video\nSplitter");

        Bundle params = new Bundle();
        params.putString(Constants.Tool_Name, "Video Splitter");
        params.putString(Constants.Type, "TOOL");
        Constants.firebaseAnalytics(this).logEvent(Constants.Most_Used_Tool, params);

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

        folder = new File(
                getExternalFilesDir(""), "SuperTech Uploader"
        );
        audioFolder = new File(folder, AUDIO);
        splitVideoFolder = new File(folder, SPLIT_VIDEO);
        Statusbackupfolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/SuperTech Status/Media/Statuses");

        mkDirs();

        binding.backbtn.setOnClickListener(v -> {
           onBackPressed();
        });

        binding.split.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowSplitVideoActivity.class);
            intent.putExtra("all", true);
            startActivity(intent);
          //  finish();
        });

        binding.add.setOnClickListener(v -> {
            if (isRorAbove()) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_LOAD_VIDEOS);
            } else {
                upload();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    public static boolean isRorAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    public void mkDirs() {
        if (folder.mkdirs())
            Log.i(TAG, "Folder created");
        if (audioFolder.mkdirs())
            Log.i(TAG, "Audio folder created");
        if (splitVideoFolder.mkdirs())
            Log.i(TAG, "Split Video folder created");
        if (Statusbackupfolder.mkdirs())
            Log.i(TAG, "Folder created");
    }

    private void requestReadPermission(int id) {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                id);
    }

    private boolean hasReadPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void upload() {
        String str8 = "Please select Video";
        String str9 = "MP4";
        FilePickerBuilder.getInstance().setMaxCount(1).setSelectedFiles(this.videoPaths)
                .addFileSupport(str9, new String[]{".mp4", ".mkv"})
                .enableVideoPicker(true).enableImagePicker(false)
                .enableCameraSupport(false).setActivityTitle(str8)
                .setActivityTheme(R.style.LibAppTheme)
                .enableDocSupport(false)
                .enableSelectAll(false)
                .pickPhoto(this, REQUEST_TAKE_GALLERY_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                FilePickerBuilder.getInstance().setMaxCount(1).setSelectedFiles(this.videoPaths)
                        .addFileSupport("MP4", new String[]{".mp4", ".mkv"})
                        .enableVideoPicker(true)
                        .enableImagePicker(false).enableCameraSupport(false)
                        .setActivityTitle("Please select device Video")
                        .setActivityTheme(R.style.LibAppTheme)
                        .enableDocSupport(false)
                        .enableSelectAll(false)
                        .pickPhoto(this, REQUEST_TAKE_GALLERY_VIDEO);
                try {
                    this.videoPaths = new ArrayList<>();
                    this.videoPaths = (ArrayList) data.getSerializableExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                    ArrayList<String> recent = Stash.getArrayList(Constants.RECENTS_SAVED_VIDEOS, String.class);
                    if (recent.size() == 0) {
                        recent.addAll(videoPaths);
                        Stash.put(Constants.RECENTS_SAVED_VIDEOS, recent);
                    } else {
                        boolean check = false;
                        Collections.reverse(recent);
                        for (int i=0; i<recent.size(); i++) {
                            if (!recent.get(i).equals(videoPaths.get(0))) {
                                check = true;
                            } else {
                                check = false;
                                break;
                            }
                        }
                        if (check) {
                            recent.addAll(videoPaths);
                            Stash.put(Constants.RECENTS_SAVED_VIDEOS, recent);
                        }
                    }
                    Intent intent2 = new Intent(VideoSplitterActivity.this, SplitVideoActivity.class);
                    intent2.putExtra("path", (String) this.videoPaths.get(0));
                    startActivity(intent2);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please retry to select video...!", Toast.LENGTH_LONG).show();
                }

            }
            else if (requestCode == REQUEST_LOAD_VIDEOS) {
                Uri selectedVideo = null;
                selectedVideo = data.getData();
                Log.i("TAG", "image load");
                if (selectedVideo == null) {
                    Toast.makeText(VideoSplitterActivity.this, "Image not supported", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Intent intent = new Intent(VideoSplitterActivity.this, SplitVideoActivity.class);
                        String filePath = getPath(getApplicationContext(), selectedVideo);
                        ArrayList<String> recent = Stash.getArrayList(Constants.RECENTS_SAVED_VIDEOS, String.class);
                        if (recent.size() == 0){
                            recent.add(filePath);
                            Stash.put(Constants.RECENTS_SAVED_VIDEOS, recent);
                        } else {
                            boolean check = false;
                            Collections.reverse(recent);
                            for (int i=0; i<recent.size(); i++){
                                if (!recent.get(i).equals(filePath)){
                                    check = true;
                                } else {
                                    check = false;
                                    break;
                                }
                            }
                            if (check){
                                recent.add(filePath);
                                Stash.put(Constants.RECENTS_SAVED_VIDEOS, recent);
                            }
                        }
                        intent.putExtra("path", filePath);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Please retry to select video...!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = true;

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void showPermissionDeniedDialog(int id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String message = "You have denied permission. Read storage permission needed to recover Media into device storage.";
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Allow", (dialogInterface, i) -> requestReadPermission(id));

        alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialogBuilder.show();
    }

    private void showNeverAskDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String message = "You have selected never ask permission. Allow permission manually from settings.";
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Settings", (dialogInterface, i) -> openSettings(VideoSplitterActivity.this));
        alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialogBuilder.show();
    }

    public static void openSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_DOWNLOADS) {
            if (!hasReadPermission()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showPermissionDeniedDialog(REQUEST_DOWNLOADS);
                } else {
                    showNeverAskDialog();
                }
            }  //                startActivity(new Intent(this, StatusMainActivity.class));

        } else if (requestCode == REQUEST_FILES) {
            if (!hasReadPermission()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showPermissionDeniedDialog(REQUEST_DOWNLOADS);
                } else {
                    showNeverAskDialog();
                }
            } else {
                if (isRorAbove()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    VideoSplitterActivity.this.startActivityForResult(intent, REQUEST_LOAD_VIDEOS);
                } else {
                    upload();
                }
            }
        }
    }

}