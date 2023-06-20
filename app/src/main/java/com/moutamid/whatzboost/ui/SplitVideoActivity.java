package com.moutamid.whatzboost.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.moutamid.whatzboost.BuildConfig;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivitySplitVideoBinding;
import com.moutamid.whatzboost.videosplitter.SplitVideo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SplitVideoActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    ActivitySplitVideoBinding binding;
    String path;
    Context context;
    private int splitFileCount = 0;
    private int stopPosition = 0;
    int start = -1;
    int end = -1;
    private static final String SEPARATOR = "/";
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplitVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        path = getIntent().getStringExtra("path");
        MediaController controller = new MediaController(this);
        end = 29000;

        binding.videoView.setOnPreparedListener(mp -> {
            int duration = binding.videoView.getDuration() / 1000;
            int splitTime;
            if(end==29000){
                splitTime = 29;
            }else{
                splitTime=15;
            }
            splitFileCount = duration / splitTime + 1;
        });
        controller.setAnchorView(binding.videoView);
        controller.setMediaPlayer(binding.videoView);
        binding.videoView.setMediaController(controller);
        binding.videoView.setVideoPath(path);
        binding.videoView.setOnCompletionListener(this);
        binding.videoView.requestFocus();
        binding.videoView.start();

        binding.split.setOnClickListener(v -> {
            new SplitVideo().execute();
        });


    }


    @Override
    public void onBackPressed() {
       // startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            stopPosition = binding.videoView.getCurrentPosition(); //stopPosition is an int
            binding.videoView.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            if (stopPosition == binding.videoView.getDuration())
                stopPosition = 0;
            binding.videoView.seekTo(stopPosition);
            binding.videoView.start(); //Or use resume() if it doesn't work. I'm not sure
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        binding.videoView.start();
    }

    private void splitVideo() {

        final File folder = new File(
                context.getExternalFilesDir(""), "SuperTech Uploader"
        );
        final String SPLIT_VIDEO = "Split Video";
        final File splitVideoFolder = new File(folder, SPLIT_VIDEO);

        ArrayList<String> videoFiles = new ArrayList<>();
        for (int i = 0; i < splitFileCount; i++) {
            String outputPath = splitVideoFolder.getPath() + SEPARATOR + "video" + i + ".mp4";
            videoFiles.add(splitVideoFolder.getPath() + SEPARATOR + "video" + i + ".mp4");

            File pa = new File(outputPath);
            pa.mkdir();

           Log.i("TAg1111", "outputPath : " + outputPath);
           Log.i("TAg1111", "root : " + pa.mkdir());
           Log.i("TAg1111", "root : " + pa.getPath());

            try {
                Log.i("TAg1111", "start : " + start);
                Log.i("TAg1111", "end : " + end);
                Log.i("TAg1111", "path : " + path);
                genVideoUsingMuxer(path, outputPath, start, end, true, true);
                Log.i("TAg1111", "video saved : " + i);
                start = end;
                end = end + 29000;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAg1111", "Catch : " + e.getMessage());
            }
        }
        saveVideos(videoFiles);
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;

    @SuppressLint("WrongConstant")
    public void genVideoUsingMuxer(String srcPath, String dstPath, int startMs, int endMs, boolean useAudio, boolean useVideo) throws IOException {
        // Set up MediaExtractor to read from the source.
        MediaExtractor extractor = new MediaExtractor();
        Log.d("BUGCHECK123", "1");
        extractor.setDataSource(srcPath);
        Log.d("BUGCHECK123", "2");
        int trackCount = extractor.getTrackCount();
        Log.d("BUGCHECK123", "3");
        // Set up MediaMuxer for the destination.
        MediaMuxer muxer = null;
        try {
            muxer = new MediaMuxer(dstPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (Exception e){
            Log.d("BUGCHECK123", e.getMessage());
        }
        Log.d("BUGCHECK123", "4");
        // Set up the tracks and retrieve the max buffer size for selected
        // tracks.
        HashMap<Integer, Integer> indexMap = new HashMap<>(trackCount);
        Log.d("BUGCHECK123", "5");
        int bufferSize = -1;
        for (int i = 0; i < trackCount; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            Log.d("BUGCHECK123", "6");
            String mime = format.getString(MediaFormat.KEY_MIME);
            Log.d("BUGCHECK123", "7");
            boolean selectCurrentTrack = false;
            if (mime.startsWith("audio/") && useAudio) {
                selectCurrentTrack = true;
            } else if (mime.startsWith("video/") && useVideo) {
                selectCurrentTrack = true;
            }
            if (selectCurrentTrack) {
                Log.d("BUGCHECK123", "8 IFFF");
                extractor.selectTrack(i);
                int dstIndex = muxer.addTrack(format);
                Log.d("BUGCHECK123", "8.444 mux");
                indexMap.put(i, dstIndex);
                if (format.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
                    Log.d("BUGCHECK123", "8 8 IFFF");
                    int newSize = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                    bufferSize = Math.max(newSize, bufferSize);
                } else {
                    Log.d("BUGCHECK123", "8 Elseee");
                }
            } else {
                Log.d("BUGCHECK123", "9 Elseee");
            }
        }
        if (bufferSize < 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }
        Log.d("BUGCHECK123", "10");
        // Set up the orientation and starting time for extractor.
        MediaMetadataRetriever retrieverSrc = new MediaMetadataRetriever();
        Log.d("BUGCHECK123", "11");
        retrieverSrc.setDataSource(srcPath);
        Log.d("BUGCHECK123", "12");
        String degreesString = retrieverSrc.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        Log.d("BUGCHECK123", "13");
        if (degreesString != null) {
            int degrees = Integer.parseInt(degreesString);
            if (degrees >= 0) {
                muxer.setOrientationHint(degrees);
            }
        }
        if (startMs > 0) {
            extractor.seekTo(startMs * 1000L, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        }
        // Copy the samples from MediaExtractor to MediaMuxer. We will loop
        // for copying each sample and stop when we get to the end of the source
        // file or exceed the end time of the trimming.
        int offset = 0;
        int trackIndex = -1;
        ByteBuffer dstBuf = ByteBuffer.allocate(bufferSize);
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        muxer.start();
        while (true) {
            bufferInfo.offset = offset;
            bufferInfo.size = extractor.readSampleData(dstBuf, offset);
            if (bufferInfo.size < 0) {
                bufferInfo.size = 0;
                break;
            } else {
                bufferInfo.presentationTimeUs = extractor.getSampleTime();
                if (endMs > 0 && bufferInfo.presentationTimeUs > (endMs * 1000L)) {
                    break;
                } else {
                    bufferInfo.flags = extractor.getSampleFlags(); //
                    trackIndex = extractor.getSampleTrackIndex();
                    muxer.writeSampleData(indexMap.get(trackIndex), dstBuf, bufferInfo);
                    extractor.advance();
                }
            }
        }

        try {
            muxer.stop();
        } catch (Throwable e) {
            Log.d("TAg1111", "Muxer : " + e.getMessage());
            e.printStackTrace();
        }

        muxer.release();
    }


    private void saveVideos(ArrayList<String> videoFiles) {

        Log.d("TAg1111", "Size : " + videoFiles.size());
        Log.d("TAg1111", "file : " + videoFiles.get(0));

        runOnUiThread(() -> {
            new AlertDialog.Builder(SplitVideoActivity.this)
                    .setPositiveButton("Share", (dialog, which) -> {
                        ArrayList<Uri> uriArrayList = new ArrayList<>();
                        for (String videoPath : videoFiles) {
                            Uri uri;
                            // only for gingerbread and newer versions
                            uri = FileProvider.getUriForFile(getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider", new File(videoPath));
                            uriArrayList.add(uri);
                        }
                        Log.d("TAg1111", "Size : " + uriArrayList.size());
                        Log.d("TAg1111", "file : " + uriArrayList.get(0));
                        dialog.dismiss();
                        // Intent videoShare = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        Intent videoShare = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        videoShare.setPackage("com.whatsapp");
                        videoShare.setType("video/*");
                        videoShare.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
                        videoShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(videoShare, "Share Videos"));
                    })
                    .setNegativeButton("Save", (dialog, which) -> {
                        ArrayList<Uri> uriArrayList = new ArrayList<>();
                        for (String videoPath : videoFiles) {
                            Uri uri;
                            // only for gingerbread and newer versions
                            uri = FileProvider.getUriForFile(getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider", new File(videoPath));
                            uriArrayList.add(uri);
                        }
                        try {
                            saveFilesToDownloads(uriArrayList, getContentResolver());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("TAg1111", e.getMessage());
                            Toast.makeText(this, "File not saved", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }).setTitle("Video Split Completed").setMessage("Do you want to share it now to whatsapp or save the videos?")
                    .show();
        });
    }

    class SplitVideo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(SplitVideoActivity.this);
            progress.setTitle("Splitting the video");
            progress.setMessage("Please wait...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            splitVideo();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissProgressDialog();
        }
    }

    private void dismissProgressDialog() {
        try {
            if (progress != null && progress.isShowing())
                progress.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFilesToDownloads(ArrayList<Uri> uris, ContentResolver contentResolver) throws IOException {
        for (Uri uri : uris) {
            String displayName = getDisplayNameFromUri(uri, contentResolver);
            InputStream inputStream = contentResolver.openInputStream(uri);
            File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/WhatzBoost Videos");
            File outputFile = new File(downloadsDirectory, displayName);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {
                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } finally {
                outputStream.close();
                inputStream.close();
            }
        }
    }

    private String getDisplayNameFromUri(Uri uri, ContentResolver contentResolver) {
        String displayName = null;
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        try (Cursor cursor = contentResolver.query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                displayName = cursor.getString(columnIndex);
            }
        }
        return displayName;
    }


}