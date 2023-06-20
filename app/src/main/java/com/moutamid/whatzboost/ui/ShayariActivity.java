package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.common.util.UriUtil;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.ActivityShayariBinding;
import com.moutamid.whatzboost.fragments.EnglishShayariFragment;
import com.moutamid.whatzboost.fragments.HindiShayariFragment;
import com.moutamid.whatzboost.fragments.MediaFragment;
import com.moutamid.whatzboost.fragments.MessagesFragment;
import com.moutamid.whatzboost.fragments.UrduShayariFragment;

import javax.microedition.khronos.egl.EGL;

public class ShayariActivity extends AppCompatActivity {
    ActivityShayariBinding binding;
    int cur = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShayariBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new EnglishShayariFragment()).commit();

        binding.englishCard.setOnClickListener(v -> {
            int i = 0;
            if (i != cur){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).replace(R.id.framelayout, new EnglishShayariFragment()).addToBackStack(null).commit();
                cur = 0;
            }


            binding.englishCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.hindiCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.urduCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

        binding.hindiCard.setOnClickListener(v -> {
            int i = 1;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new HindiShayariFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new HindiShayariFragment()).addToBackStack(null).commit();
                }
            cur = i;
            binding.englishCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.urduCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.hindiCard.setCardBackgroundColor(getResources().getColor(R.color.card));
        });

        binding.urduCard.setOnClickListener(v -> {
            int i = 2;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new UrduShayariFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new UrduShayariFragment()).addToBackStack(null).commit();
                }
            cur = i;
            binding.englishCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.urduCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.hindiCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}