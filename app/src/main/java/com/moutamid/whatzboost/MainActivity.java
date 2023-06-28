package com.moutamid.whatzboost;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Toast;

import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityMainBinding;
import com.moutamid.whatzboost.fragments.ChatFragment;
import com.moutamid.whatzboost.fragments.FakeFragment;
import com.moutamid.whatzboost.fragments.FeelingFragment;
import com.moutamid.whatzboost.fragments.MagicFragment;
import com.moutamid.whatzboost.fragments.MainFragment;
import com.moutamid.whatzboost.fragments.OtherFragment;
import com.moutamid.whatzboost.fragments.RecentsFragment;
import com.moutamid.whatzboost.services.NLService;
import com.moutamid.whatzboost.ui.MakeProfileActivity;
import com.moutamid.whatzboost.ui.SearchActivity;
import com.moutamid.whatzboost.ui.SettingActivity;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int cur = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

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

            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.recentsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.card));
        });

        binding.searchBadge.setOnTouchListener(Constants.customOnTouchListner(SearchActivity.class, this, this));

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
            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.recentsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.card));
        }

    }
}