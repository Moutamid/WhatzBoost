package com.moutamid.whatzboost;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityMainBinding;
import com.moutamid.whatzboost.fragments.ChatFragment;
import com.moutamid.whatzboost.fragments.FakeFragment;
import com.moutamid.whatzboost.fragments.FeelingFragment;
import com.moutamid.whatzboost.fragments.MagicFragment;
import com.moutamid.whatzboost.fragments.MainFragment;
import com.moutamid.whatzboost.fragments.OtherFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int cur = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Constants.checkApp(this);
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new MainFragment()).commit();
        } catch (Exception e) {

        }
        binding.mainCard.setOnClickListener(v -> {
            int i = 0;
            if (i != cur){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).replace(R.id.framelayout, new MainFragment()).addToBackStack(null).commit();
                cur = 0;
            }


            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.feelingCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.otherCard.setCardBackgroundColor(getResources().getColor(R.color.background));
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
                    ).replace(R.id.framelayout, new FakeFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new FakeFragment()).addToBackStack(null).commit();
                }
            cur = i;
            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.feelingCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.otherCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

        binding.feelingCard.setOnClickListener(v -> {
            int i = 2;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new FeelingFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new FeelingFragment()).addToBackStack(null).commit();
                }
            cur = i;

            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.feelingCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.otherCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

        binding.magicCard.setOnClickListener(v -> {
            int i = 3;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new MagicFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new MagicFragment()).addToBackStack(null).commit();
                }
            cur = i;

            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.feelingCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.otherCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

        binding.otherCard.setOnClickListener(v -> {
            int i = 4;
            if (i != cur)
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new OtherFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new OtherFragment()).addToBackStack(null).commit();
                }

            cur = i;

            binding.mainCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.fakeCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.feelingCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.magicCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.otherCard.setCardBackgroundColor(getResources().getColor(R.color.card));
        });

    }
}