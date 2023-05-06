package com.moutamid.whatzboost.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

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
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivitySplitVideoBinding;
import com.moutamid.whatzboost.videosplitter.RangePlaySeekBar;
import com.moutamid.whatzboost.videosplitter.RangeSeekBar;
import com.moutamid.whatzboost.videosplitter.SplitBaseAdapter;
import com.moutamid.whatzboost.videosplitter.StaticMethods;
import com.moutamid.whatzboost.videosplitter.UtilCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class SplitVideoActivity extends AppCompatActivity {
    public static SplitVideoActivity context = null;
    static final boolean r = true;
    public static String videoPath;
    public boolean CompleteNotificationCreated = false;
    public int LIST_COLUMN_SIZE = 4;
    public int MP_DURATION;
    boolean a = r;
    ViewGroup viewGroup;
    RangeSeekBar<Integer> rangeSeekBar;
    RangePlaySeekBar<Integer> rangePlaySeekBar;
    TextView left_pointer;
    TextView mid_pointer;
    TextView right_pointer;
    TextView textsizeValue;
    public boolean isInFront = r;
    TextView textcompressSize;
    TextView textCompressPercentage;
    TextView Filename;
    boolean m = false;
    public int maxtime;
    public int mintime;
    PowerManager.WakeLock wakeLock;
    SplitBaseAdapter splitBaseAdapter;
    ArrayList<String> stringArrayList;
    public ProgressDialog prgDialog;
    Spinner spinner;
    private final StateObserver stateObserver = new StateObserver();
    public int seekduration;
    public int seekend;
    public int seekstart;

    public VideoView addcutsvideoview;
    public int totalVideoDuration = 0;
    public int type = 0;
    private String u;
    private String destination;
    public ImageView videoPlayBtn;
    private static final String TAG = "SplitVideoActivity";
    private boolean isPurchased;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_split_video);

        findViewById(R.id.backbtn).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });


        findViewById(R.id.split).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (addcutsvideoview.isPlaying()) {
                        videoPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        addcutsvideoview.pause();
                    }
                } catch (Exception ignored) {
                }
                executeCompressCommand();
            }
        });

        if (r ) {
            this.m = false;
            this.a = r;
            getPermission();
            copyCreate();
            context = this;
            return;
        }
       // throw new AssertionError();

    }

    @SuppressLint({"HandlerLeak"})
    public class StateObserver extends Handler {
        private boolean b = false;
        private final Runnable c = new Runnable() {
            public void run() {
                StateObserver.this.a();
            }
        };

        public StateObserver() {
        }


        public void a() {
            if (!this.b) {
                this.b = SplitVideoActivity.r;
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message message) {
            this.b = false;
            SplitVideoActivity.this.rangePlaySeekBar.setSelectedMaxValue(Integer.valueOf(SplitVideoActivity.this.addcutsvideoview.getCurrentPosition()));
            if (!SplitVideoActivity.this.addcutsvideoview.isPlaying() || SplitVideoActivity.this.addcutsvideoview.getCurrentPosition() >= SplitVideoActivity.this.rangeSeekBar.getSelectedMaxValue().intValue()) {
                if (SplitVideoActivity.this.addcutsvideoview.isPlaying()) {
                    SplitVideoActivity.this.addcutsvideoview.pause();
                    SplitVideoActivity.this.videoPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    SplitVideoActivity.this.addcutsvideoview.seekTo(SplitVideoActivity.this.rangeSeekBar.getSelectedMinValue().intValue());
                    SplitVideoActivity.this.rangePlaySeekBar.setSelectedMinValue(SplitVideoActivity.this.rangeSeekBar.getSelectedMinValue());
                    SplitVideoActivity.this.rangePlaySeekBar.setVisibility(View.INVISIBLE);
                }
                if (!SplitVideoActivity.this.addcutsvideoview.isPlaying()) {
                    SplitVideoActivity.this.videoPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    SplitVideoActivity.this.rangePlaySeekBar.setVisibility(View.INVISIBLE);
                    return;
                }
                return;
            }
            SplitVideoActivity.this.rangePlaySeekBar.setVisibility(View.VISIBLE);
            postDelayed(this.c, 50);
        }
    }

    public void intentToPreviewActivity() {
    }

    private void getPermission() {
        if (!Constants.isPermissionGranted(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_AUDIO);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_VIDEO);
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES);
                ActivityCompat.requestPermissions(this, Constants.permissions13, 1);
            } else {
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(this, Constants.permissions, 1);
            }
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    public void copyCreate() {
        this.stringArrayList = new ArrayList<>();
        // this.stringArrayList.add("10 Sec");
        //  this.stringArrayList.add("20 Sec");
        this.stringArrayList.add("30 Sec");
        //this.stringArrayList.add("40 Sec");
        // this.stringArrayList.add("50 Sec");
        //  this.stringArrayList.add("60 Sec");
        this.isInFront = r;
        this.LIST_COLUMN_SIZE = b() / 100;
        this.totalVideoDuration = 0;
        this.wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(6, "VideoMerge");
        if (!this.wakeLock.isHeld()) {
            this.wakeLock.acquire();
        }
        this.u = getIntent().getStringExtra("Video_Uri");
        videoPath = this.u;
        this.Filename = findViewById(R.id.Filename);
        this.addcutsvideoview = findViewById(R.id.addcutsvideoview);
        this.videoPlayBtn = findViewById(R.id.videoplaybtn);
        this.textsizeValue = findViewById(R.id.textsizeValue);
        this.textCompressPercentage = findViewById(R.id.textCompressPercentage);
        this.textcompressSize = findViewById(R.id.textcompressSize);
        this.left_pointer = findViewById(R.id.left_pointer);
        this.mid_pointer = findViewById(R.id.mid_pointer);
        this.right_pointer = findViewById(R.id.right_pointer);
        this.Filename.setText(new File(videoPath).getName());
        a(videoPath);
        a(2);
        runOnUiThread(() -> SplitVideoActivity.this.prgDialog = ProgressDialog.show(SplitVideoActivity.this, "", "Loading...", SplitVideoActivity.r));
        VideoSeekBar();
        this.spinner = findViewById(R.id.sp_convert);
        this.splitBaseAdapter = new SplitBaseAdapter(this, this.stringArrayList, 0);
        this.spinner.setAdapter(this.splitBaseAdapter);
        this.spinner.setSelection(0);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).toString() == "10 Sec") {
                    SplitVideoActivity.this.a(1);
                } else if (adapterView.getItemAtPosition(i).toString() == "20 Sec") {
                    SplitVideoActivity.this.a(2);
                } else if (adapterView.getItemAtPosition(i).toString() == "30 Sec") {
                    SplitVideoActivity.this.a(3);
                } else if (adapterView.getItemAtPosition(i).toString() == "40 Sec") {
                    SplitVideoActivity.this.a(4);
                } else if (adapterView.getItemAtPosition(i).toString() == "50 Sec") {
                    SplitVideoActivity.this.a(5);
                } else if (adapterView.getItemAtPosition(i).toString() == "60 Sec") {
                    SplitVideoActivity.this.a(6);
                }
            }
        });
    }
    public void executeCompressCommand() {
        String[] strArr = new String[0];
        this.seekstart = this.rangeSeekBar.getSelectedMinValue().intValue() / 1000;
        this.seekend = this.rangeSeekBar.getSelectedMaxValue().intValue() / 1000;
        this.seekduration = this.seekend - this.seekstart;
        StringBuilder sb = new StringBuilder();
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        //      sb.append(CreateFolder(this));
        //  } else {
        sb.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
        //  }
        sb.append("/");
        sb.append(getResources().getString(R.string.VideoSplitter));
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        String name = new File(videoPath).getName();
        StringBuilder sb2 = new StringBuilder();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            sb2.append(CreateFolder(this));
//        } else {
        sb2.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
//        }
        sb2.append("/");
        sb2.append(getResources().getString(R.string.VideoSplitter));
        sb2.append("/");
        sb2.append(name.substring(0, name.lastIndexOf(".")));
        sb2.append("-part%2d");
        sb2.append(videoPath.substring(videoPath.lastIndexOf(".")));
        this.destination = sb2.toString();
        if (this.stringArrayList.get(this.spinner.getSelectedItemPosition()) == "10 Sec") {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(this.seekstart);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(this.seekduration);
            strArr = new String[]{"-y", "-ss", sb3.toString(), "-t", sb4.toString(), "-i", videoPath, "-acodec", "copy", "-f", "segment", "-segment_time", "10", "-vcodec", "copy", "-reset_timestamps", "1", "-map", "0", "-preset", "ultrafast", this.destination};
        } else if (this.stringArrayList.get(this.spinner.getSelectedItemPosition()) == "20 Sec") {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("");
            sb5.append(this.seekstart);
            StringBuilder sb6 = new StringBuilder();
            sb6.append("");
            sb6.append(this.seekduration);
            strArr = new String[]{"-y", "-ss", sb5.toString(), "-t", sb6.toString(), "-i", videoPath, "-acodec", "copy", "-f", "segment", "-segment_time", "20", "-vcodec", "copy", "-reset_timestamps", "1", "-map", "0", "-preset", "ultrafast", this.destination};
        } else if (this.stringArrayList.get(this.spinner.getSelectedItemPosition()) == "30 Sec") {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("");
            sb7.append(this.seekstart);
            StringBuilder sb8 = new StringBuilder();
            sb8.append("");
            sb8.append(this.seekduration);
            strArr = new String[]{"-y", "-ss", sb7.toString(), "-t", sb8.toString(), "-i", videoPath, "-acodec", "copy", "-f", "segment", "-segment_time", "30", "-vcodec", "copy", "-reset_timestamps", "1", "-map", "0", "-preset", "ultrafast", this.destination};
        } else if (this.stringArrayList.get(this.spinner.getSelectedItemPosition()) == "40 Sec") {
            StringBuilder sb9 = new StringBuilder();
            sb9.append("");
            sb9.append(this.seekstart);
            StringBuilder sb10 = new StringBuilder();
            sb10.append("");
            sb10.append(this.seekduration);
            strArr = new String[]{"-y", "-ss", sb9.toString(), "-t", sb10.toString(), "-i", videoPath, "-acodec", "copy", "-f", "segment", "-segment_time", "40", "-vcodec", "copy", "-reset_timestamps", "1", "-map", "0", "-preset", "ultrafast", this.destination};
        } else if (this.stringArrayList.get(this.spinner.getSelectedItemPosition()) == "50 Sec") {
            StringBuilder sb11 = new StringBuilder();
            sb11.append("");
            sb11.append(this.seekstart);
            StringBuilder sb12 = new StringBuilder();
            sb12.append("");
            sb12.append(this.seekduration);
            strArr = new String[]{"-y", "-ss", sb11.toString(), "-t", sb12.toString(), "-i", videoPath, "-acodec", "copy", "-f", "segment", "-segment_time", "50", "-vcodec", "copy", "-reset_timestamps", "1", "-map", "0", "-preset", "ultrafast", this.destination};
        } else if (this.stringArrayList.get(this.spinner.getSelectedItemPosition()) == "60 Sec") {
            StringBuilder sb13 = new StringBuilder();
            sb13.append("");
            sb13.append(this.seekstart);
            StringBuilder sb14 = new StringBuilder();
            sb14.append("");
            sb14.append(this.seekduration);
            strArr = new String[]{"-y", "-ss", sb13.toString(), "-t", sb14.toString(), "-i", videoPath, "-acodec", "copy", "-f", "segment", "-segment_time", "60", "-vcodec", "copy", "-reset_timestamps", "1", "-map", "0", "-preset", "ultrafast", this.destination};
        }
        a(strArr, this.destination);
    }

    private void a(String[] strArr, final String str) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        String ffmpegCommand = UtilCommand.main(strArr);
//        FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {
//
//            @Override
//            public void apply(final long executionId, final int returnCode) {
//                Log.d("TAG", String.format("FFmpeg process exited with rc %d.", returnCode));
//
//                Log.d("TAG", "FFmpeg process output:");
//
//                Config.printLastCommandOutput(Log.INFO);
//
//                progressDialog.dismiss();
//                if (returnCode == RETURN_CODE_SUCCESS) {
//                    progressDialog.dismiss();
//                    Toasty.success(SplitVideoActivity.this,"Video Splitted Successfully!",Toasty.LENGTH_SHORT).show();
//                    StringBuilder sb = new StringBuilder();
//                   /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
//                        sb.append(CreateFolder(getBaseContext()));
//                     else*/
//                    sb.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
//                    sb.append("/");
//                    sb.append(SplitVideoActivity.this.getResources().getString(R.string.VideoSplitter));
//                    sb.append("/");
//                    File[] listFiles = new File(sb.toString()).listFiles();
//                    ArrayList<String> listofpath = new ArrayList();
//                    for (File file : listFiles) {
//                        if (file.isFile()) {
//                            listofpath.add(file.getPath());
//                            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//                            intent.setData(Uri.fromFile(file));
//                            SplitVideoActivity.this.getApplicationContext().sendBroadcast(intent);
//                        }
//                    }
//                    Intent intent = new Intent(SplitVideoActivity.this,ShowSplitVideoActivity.class);
//                    intent.putStringArrayListExtra("split",listofpath);
//                    intent.putExtra("all",false);
//                    Log.d("de_path", "apply: "+ new File(videoPath).getName());
//                    intent.putExtra("name",(new File(videoPath).getName()));
//                    SplitVideoActivity.this.refreshGallery(str);
//                    startActivity(intent);
//                    finish();
//                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.d("ffmpegfailure", str);
//                    try {
//                        new File(str).delete();
//                        SplitVideoActivity.this.deleteFromGallery(str);
//                        Toasty.error(SplitVideoActivity.this, "Error Creating Video", 0).show();
//                    } catch (Throwable th) {
//                        th.printStackTrace();
//                    }
//                } else {
//                    Log.d("ffmpegfailure", str);
//                    try {
//                        new File(str).delete();
//                        SplitVideoActivity.this.deleteFromGallery(str);
//                        Toasty.error(SplitVideoActivity.this, "Error Creating Video", 0).show();
//                    } catch (Throwable th) {
//                        th.printStackTrace();
//                    }
//                }
//
//
//            }
//        });
        getWindow().clearFlags(16);
    }


    public void a(int i2) {
        this.type = i2;
        TextView textView = this.textcompressSize;
        StringBuilder sb = new StringBuilder();
        sb.append(StaticMethods.ExpectedOutputSize(videoPath, timeInSecond(this.mintime, this.maxtime), this.type));
        sb.append("");
        textView.setText(sb.toString());
        TextView textView2 = this.textCompressPercentage;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("-");
        sb2.append(StaticMethods.SelectedCompressPercentage(videoPath, timeInSecond(this.mintime, this.maxtime), this.type));
        sb2.append("%");
        textView2.setText(sb2.toString());
    }

    private void a(String str) {
        TextView textView = this.textsizeValue;
        StringBuilder sb = new StringBuilder();
        sb.append("Size :- ");
        sb.append(StaticMethods.sizeInMBbyFilepath(str));
        textView.setText(sb.toString());
    }

    public int timeInSecond(int i2, int i3) {
        return (i3 - i2) / 1000;
    }

    public void VideoSeekBar() {
        this.addcutsvideoview.setVideoURI(Uri.parse(videoPath));
        this.addcutsvideoview.setOnPreparedListener(mediaPlayer -> {
            if (SplitVideoActivity.this.a) {
                SplitVideoActivity.this.mintime = 0;
                SplitVideoActivity.this.maxtime = mediaPlayer.getDuration();
                SplitVideoActivity.this.MP_DURATION = mediaPlayer.getDuration();
                SplitVideoActivity.this.seekLayout(0, SplitVideoActivity.this.MP_DURATION);
                SplitVideoActivity.this.addcutsvideoview.start();
                SplitVideoActivity.this.addcutsvideoview.pause();
                SplitVideoActivity.this.addcutsvideoview.seekTo(300);
                return;
            }
            SplitVideoActivity.this.seekLayout(SplitVideoActivity.this.mintime, SplitVideoActivity.this.maxtime);
            SplitVideoActivity.this.addcutsvideoview.start();
            SplitVideoActivity.this.addcutsvideoview.pause();
            SplitVideoActivity.this.addcutsvideoview.seekTo(SplitVideoActivity.this.mintime);
        });
        this.addcutsvideoview.setOnErrorListener((mediaPlayer, i, i2) -> {
            SplitVideoActivity.this.prgDialog.dismiss();
            return false;
        });
        this.videoPlayBtn.setOnClickListener(view -> SplitVideoActivity.this.a());
    }


    public void a() {
        if (this.addcutsvideoview.isPlaying()) {
            this.addcutsvideoview.pause();
            this.addcutsvideoview.seekTo(this.rangeSeekBar.getSelectedMinValue().intValue());
            this.videoPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            this.rangePlaySeekBar.setVisibility(View.INVISIBLE);
            return;
        }
        this.addcutsvideoview.seekTo(this.rangeSeekBar.getSelectedMinValue().intValue());
        this.addcutsvideoview.start();
        this.rangePlaySeekBar.setSelectedMaxValue(Integer.valueOf(this.addcutsvideoview.getCurrentPosition()));
        this.stateObserver.a();
        this.videoPlayBtn.setImageResource(R.drawable.ic_baseline_pause_24);
        this.rangePlaySeekBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        this.totalVideoDuration = 0;
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        super.onDestroy();
    }

    public void seekLayout(int i2, int i3) {
        TextView textView = this.left_pointer;
        StringBuilder sb = new StringBuilder();
        sb.append(getTimeForTrackFormat(i2));
        sb.append("");
        textView.setText(sb.toString());
        TextView textView2 = this.right_pointer;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getTimeForTrackFormat(i3));
        sb2.append("");
        textView2.setText(sb2.toString());
        TextView textView3 = this.mid_pointer;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getTimeForTrackFormat(i3 - i2));
        sb3.append("");
        textView3.setText(sb3.toString());
        if (this.type != 9) {
            TextView textView4 = this.textcompressSize;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(StaticMethods.ExpectedOutputSize(videoPath, timeInSecond(i2, i3), this.type));
            sb4.append("");
            textView4.setText(sb4.toString());
            TextView textView5 = this.textCompressPercentage;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("-");
            sb5.append(StaticMethods.SelectedCompressPercentage(videoPath, timeInSecond(i2, i3), this.type));
            sb5.append("%");
            textView5.setText(sb5.toString());
        }
        if (this.viewGroup != null) {
            this.viewGroup.removeAllViews();
            this.viewGroup = null;
            this.rangeSeekBar = null;
            this.rangePlaySeekBar = null;
        }
        this.viewGroup = findViewById(R.id.seekLayout);
        this.rangeSeekBar = new RangeSeekBar<>(Integer.valueOf(0), Integer.valueOf(this.MP_DURATION), this);
        this.rangePlaySeekBar = new RangePlaySeekBar<>(Integer.valueOf(0), Integer.valueOf(this.MP_DURATION), this);
        this.rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {

            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> rangeSeekBar, Integer num, Integer num2, boolean z) {
                if (SplitVideoActivity.this.addcutsvideoview.isPlaying()) {
                    SplitVideoActivity.this.addcutsvideoview.pause();
                    SplitVideoActivity.this.videoPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
                if (SplitVideoActivity.this.maxtime == num2.intValue()) {
                    if (num2.intValue() - num.intValue() <= 1000) {
                        num = Integer.valueOf(num2.intValue() + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED);
                    }
                    SplitVideoActivity.this.addcutsvideoview.seekTo(num.intValue());
                } else if (SplitVideoActivity.this.mintime == num.intValue()) {
                    if (num2.intValue() - num.intValue() <= 1000) {
                        num2 = Integer.valueOf(num.intValue() + 1000);
                    }
                    SplitVideoActivity.this.addcutsvideoview.seekTo(num.intValue());
                }
                SplitVideoActivity.this.rangeSeekBar.setSelectedMaxValue(num2);
                SplitVideoActivity.this.rangeSeekBar.setSelectedMinValue(num);
                SplitVideoActivity.this.left_pointer.setText(SplitVideoActivity.getTimeForTrackFormat(num.intValue()));
                SplitVideoActivity.this.right_pointer.setText(SplitVideoActivity.getTimeForTrackFormat(num2.intValue()));
                SplitVideoActivity.this.mid_pointer.setText(SplitVideoActivity.getTimeForTrackFormat(num2.intValue() - num.intValue()));
                if (SplitVideoActivity.this.type != 9) {
                    TextView textView = SplitVideoActivity.this.textcompressSize;
                    StringBuilder sb = new StringBuilder();
                    sb.append(StaticMethods.ExpectedOutputSize(SplitVideoActivity.videoPath, SplitVideoActivity.this.timeInSecond(num.intValue(), num2.intValue()), SplitVideoActivity.this.type));
                    sb.append("");
                    textView.setText(sb.toString());
                    TextView textView2 = SplitVideoActivity.this.textCompressPercentage;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("-");
                    sb2.append(StaticMethods.SelectedCompressPercentage(SplitVideoActivity.videoPath, SplitVideoActivity.this.timeInSecond(num.intValue(), SplitVideoActivity.this.maxtime), SplitVideoActivity.this.type));
                    sb2.append("%");
                    textView2.setText(sb2.toString());
                }
                SplitVideoActivity.this.rangePlaySeekBar.setSelectedMinValue(num);
                SplitVideoActivity.this.rangePlaySeekBar.setSelectedMaxValue(num2);
                SplitVideoActivity.this.mintime = num.intValue();
                SplitVideoActivity.this.maxtime = num2.intValue();
            }
        });
        this.viewGroup.addView(this.rangeSeekBar);
        this.viewGroup.addView(this.rangePlaySeekBar);
        this.rangeSeekBar.setSelectedMinValue(Integer.valueOf(i2));
        this.rangeSeekBar.setSelectedMaxValue(Integer.valueOf(i3));
        this.rangePlaySeekBar.setSelectedMinValue(Integer.valueOf(i2));
        this.rangePlaySeekBar.setSelectedMaxValue(Integer.valueOf(i3));
        this.rangePlaySeekBar.setEnabled(false);
        this.rangePlaySeekBar.setVisibility(View.INVISIBLE);
        this.prgDialog.dismiss();
    }

    private int b() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i2 = displayMetrics.heightPixels;
        int i3 = displayMetrics.widthPixels;
        return i3 <= i2 ? i3 : i2;
    }

    public static String getTimeForTrackFormat(int i2) {
        long j2 = i2;
        return String.format("%02d:%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toHours(j2)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j2) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(j2))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2))));
    }


    @Override
    public void onStart() {
        super.onStart();
        if (this.CompleteNotificationCreated) {
            intentToPreviewActivity();
            ((NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE)).cancelAll();
        }
        this.CompleteNotificationCreated = false;
    }

    public void onPause() {
        super.onPause();
        this.a = false;
        try {
            if (this.addcutsvideoview.isPlaying()) {
                this.videoPlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                this.addcutsvideoview.pause();
            }
        } catch (Exception unused) {
        }
        this.isInFront = false;
        if (this.rangePlaySeekBar != null && this.rangePlaySeekBar.getVisibility() == View.VISIBLE) {
            this.rangePlaySeekBar.setVisibility(View.INVISIBLE);
        }
    }


    public void onRestart() {
        super.onRestart();
    }


    public void d() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Device not supported").setMessage("FFmpeg is not supported on your device").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SplitVideoActivity.this.finish();
            }
        }).create().show();
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                new File(str).delete();
                refreshGallery(str);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        query.close();
    }

    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        sendBroadcast(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.isInFront = r;
        this.m = false;
        this.u = getIntent().getStringExtra("Video_Uri");
        videoPath = this.u;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}