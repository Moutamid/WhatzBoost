package com.moutamid.whatzboost;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.adsense.Ads;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityMainBinding;
import com.moutamid.whatzboost.fragments.FakeFragment;
import com.moutamid.whatzboost.fragments.MagicFragment;
import com.moutamid.whatzboost.fragments.MainFragment;
import com.moutamid.whatzboost.fragments.RecentsFragment;
import com.moutamid.whatzboost.notification.Data;
import com.moutamid.whatzboost.notification.NotiModel;
import com.moutamid.whatzboost.notification.NotificationScheduler;
import com.moutamid.whatzboost.notification.RestartBootReceiiver;
import com.moutamid.whatzboost.ui.SearchActivity;
import com.moutamid.whatzboost.ui.SettingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int cur = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.adjustFontScale(MainActivity.this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
        getApi();

        if (Stash.getBoolean("assosiate", true)){
            show();
        }

        new Thread(() -> {

            ArrayList<NotiModel> linesList = new ArrayList<>();

            linesList.addAll(Data.getData());

            Collections.shuffle(linesList);

            if (Stash.getBoolean(Constants.IS_FIRST_TIME, true)) {
                Stash.put(Constants.IS_FIRST_TIME, false);
                Stash.put(Constants.DATA, linesList);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.set(Calendar.HOUR_OF_DAY, 4);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                if (Calendar.getInstance().after(calendar)) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                NotificationScheduler.scheduleNotification(MainActivity.this, calendar);

                ComponentName receiver = new ComponentName(MainActivity.this, RestartBootReceiiver.class);
                PackageManager pm = getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }).start();

//        PackageManager pm = getPackageManager();
//        pm.setComponentEnabledSetting(new ComponentName(this, NLService.class),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//        pm.setComponentEnabledSetting(new ComponentName(this, NLService.class),
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new FakeFragment()).commit();
        } catch (Exception e) {

        }

        binding.settings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });

        binding.recentsCard.setOnClickListener(v -> {
            int i = 0;
            if (i != cur){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).replace(R.id.framelayout, new RecentsFragment()).commit();
                cur = 0;
            }

            binding.textFake.setTextColor(getResources().getColor(R.color.text_off));
            binding.textMagic.setTextColor(getResources().getColor(R.color.text_off));
            binding.textMain.setTextColor(getResources().getColor(R.color.text_off));
            binding.recents.setTextColor(getResources().getColor(R.color.white));

            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.recentsCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });
        binding.mainCard.setOnClickListener(v -> {
            int i = 3;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new MainFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new MainFragment()).commit();
                }
            cur = i;


            binding.textFake.setTextColor(getResources().getColor(R.color.text_off));
            binding.textMagic.setTextColor(getResources().getColor(R.color.text_off));
            binding.textMain.setTextColor(getResources().getColor(R.color.white));
            binding.recents.setTextColor(getResources().getColor(R.color.text_off));

            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.recentsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });
        binding.fakeCard.setOnClickListener(v -> {
            int i = 1;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new FakeFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new FakeFragment()).commit();
                }
            cur = i;


            binding.textFake.setTextColor(getResources().getColor(R.color.white));
            binding.textMagic.setTextColor(getResources().getColor(R.color.text_off));
            binding.textMain.setTextColor(getResources().getColor(R.color.text_off));
            binding.recents.setTextColor(getResources().getColor(R.color.text_off));

            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.recentsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });
        binding.magicCard.setOnClickListener(v -> {
            int i = 2;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new MagicFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new MagicFragment()).commit();
                }
            cur = i;


            binding.textFake.setTextColor(getResources().getColor(R.color.text_off));
            binding.textMagic.setTextColor(getResources().getColor(R.color.white));
            binding.textMain.setTextColor(getResources().getColor(R.color.text_off));
            binding.recents.setTextColor(getResources().getColor(R.color.text_off));

            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.recentsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.card));
        });

        binding.searchBadge.setOnTouchListener(Constants.customOnTouchListner(SearchActivity.class, this, this, false, null, null));

    }

    private void show() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.assosiation_dialg);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        Button cancel = dialog.findViewById(R.id.ok);

        cancel.setOnClickListener(v -> {
            Stash.put("assosiate", false);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (cur == 1){
            finish();
        }
        if (cur == 0) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    R.anim.slide_out,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_in  // popExit
            ).replace(R.id.framelayout, new FakeFragment()).commit();
            cur = 1;
            binding.textFake.setTextColor(getResources().getColor(R.color.white));
            binding.textMagic.setTextColor(getResources().getColor(R.color.text_off));
            binding.textMain.setTextColor(getResources().getColor(R.color.text_off));
            binding.recents.setTextColor(getResources().getColor(R.color.text_off));
            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.recentsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        }

        if (cur == 2){
            getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit
            ).replace(R.id.framelayout, new FakeFragment()).commit();
            cur = 1;
            binding.textFake.setTextColor(getResources().getColor(R.color.white));
            binding.textMagic.setTextColor(getResources().getColor(R.color.text_off));
            binding.textMain.setTextColor(getResources().getColor(R.color.text_off));
            binding.recents.setTextColor(getResources().getColor(R.color.text_off));
            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.recentsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        }

        if (cur == 3){
            getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit
            ).replace(R.id.framelayout, new MagicFragment()).commit();
            cur = 2;
            binding.textFake.setTextColor(getResources().getColor(R.color.text_off));
            binding.textMagic.setTextColor(getResources().getColor(R.color.white));
            binding.textMain.setTextColor(getResources().getColor(R.color.text_off));
            binding.recents.setTextColor(getResources().getColor(R.color.text_off));
            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.recentsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.card));
        }

    }

    public void getApi(){
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(Ads.api);
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject obj = new JSONObject(htmlData);
                boolean isAdmob = obj.getBoolean("isAdmob");
                int version_num = obj.getInt("version_num");
                int interstitial_ratio = obj.getInt("interstitial_ratio");
                int rewarded_ratio = obj.getInt("rewarded_ratio");
                int dots_frequency = obj.getInt("dots_frequency");
                boolean force_update = obj.getBoolean("force_update");
                String admob_rewarded_video = obj.getString("admob_rewarded_video");
                String admob_interstitial = obj.getString("admob_interstitial");
                String facebook_rewarded_video = obj.getString("facebook_rewarded_video");
                String facebook_interstitial = obj.getString("facebook_interstitial");

                Log.d(Ads.TAG, admob_rewarded_video);

                Stash.put(Ads.IS_ADMOB, isAdmob);
                Stash.put(Ads.ADMOB_INTERSTITIAL, admob_interstitial);
                Stash.put(Ads.ADMOB_REWARDED_VIDEO, admob_rewarded_video);
                Stash.put(Ads.FACEBOOK_INTERSTITIAL, facebook_interstitial);
                Stash.put(Ads.FACEBOOK_REWARDED_VIDEO, facebook_rewarded_video);
                Stash.put(Ads.DOTS_FREQUENCY, dots_frequency);
                Stash.put(Ads.REWARDED_RATIO, rewarded_ratio);
                Stash.put(Ads.INTERSTITIAL_RATIO, interstitial_ratio);

               runOnUiThread(() -> {

                   if (isAdmob){
                       Ads.calledIniti(MainActivity.this);
                       Ads.loadIntersAD(MainActivity.this);
                       Ads.loadRewardedAD(MainActivity.this);
                   } else {
                       Ads.facebookInititalize(MainActivity.this);
                       Ads.loadFacebookRewarded();
                       Ads.loadFacebookIntersAd();
                   }

                   try {
                       PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0);
                       int version = pInfo.versionCode;
                       if (version_num != version){
                           showDialog(force_update);
                       }
                   } catch (PackageManager.NameNotFoundException e) {
                       e.printStackTrace();
                   }
               });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Stash.getBoolean(Ads.IS_ADMOB, true)){
            Ads.calledIniti(this);
            Ads.loadIntersAD(this);
            Ads.loadRewardedAD(this);
        } else {
            Ads.facebookInititalize(this);
            Ads.loadFacebookIntersAd();
            Ads.loadFacebookRewarded();
        }
    }

    private void showDialog(boolean force_update) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(!force_update);

        Button cancel = dialog.findViewById(R.id.cancel);
        Button update = dialog.findViewById(R.id.update);

        if (force_update){
            cancel.setVisibility(View.GONE);
        }

        cancel.setOnClickListener(v -> dialog.dismiss());
        update.setOnClickListener(v -> {
            String appPackageName = getPackageName(); // Get your app's package name
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException e) {
                // Play Store app is not installed, open the web browser
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        });
        dialog.show();
    }

}