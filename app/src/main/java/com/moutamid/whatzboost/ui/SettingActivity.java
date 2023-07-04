package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivitySettingBinding;
import com.moutamid.whatzboost.fragments.FeedbackFragment;
import com.moutamid.whatzboost.fragments.MediaFragment;
import com.moutamid.whatzboost.fragments.MessagesFragment;
import com.moutamid.whatzboost.fragments.PolicyFragment;
import com.moutamid.whatzboost.fragments.ToolsSettingsFragment;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    int cur = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.adjustFontScale(getBaseContext(), getResources().getConfiguration());

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ToolsSettingsFragment()).commit();

        binding.toolsCard.setOnClickListener(v -> {
            int i = 0;
            if (i != cur){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).replace(R.id.framelayout, new ToolsSettingsFragment()).commit();
                cur = 0;
            }

            binding.toolsText.setTextColor(getResources().getColor(R.color.white));
            binding.policyText.setTextColor(getResources().getColor(R.color.text_off));
            binding.feedbackText.setTextColor(getResources().getColor(R.color.text_off));

            binding.toolsCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.policyCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.feedbackCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

        binding.policyCard.setOnClickListener(v -> {
            int i = 1;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new PolicyFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new PolicyFragment()).commit();
                }
            cur = i;

            binding.toolsText.setTextColor(getResources().getColor(R.color.text_off));
            binding.policyText.setTextColor(getResources().getColor(R.color.white));
            binding.feedbackText.setTextColor(getResources().getColor(R.color.text_off));

            binding.toolsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.policyCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.feedbackCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

        binding.feedbackCard.setOnClickListener(v -> {
            int i = 2;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new FeedbackFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new FeedbackFragment()).commit();
                }
            cur = i;

            binding.toolsText.setTextColor(getResources().getColor(R.color.text_off));
            binding.policyText.setTextColor(getResources().getColor(R.color.text_off));
            binding.feedbackText.setTextColor(getResources().getColor(R.color.white));

            binding.toolsCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.policyCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.feedbackCard.setCardBackgroundColor(getResources().getColor(R.color.card));
        });

    }

    @Override
    public void onBackPressed() {
        // startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}