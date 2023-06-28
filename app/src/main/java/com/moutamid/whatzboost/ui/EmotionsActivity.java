package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ActivityEmotionsBinding;
import com.moutamid.whatzboost.fragments.AngryFragment;
import com.moutamid.whatzboost.fragments.HappyFragment;
import com.moutamid.whatzboost.fragments.OtherEmoFragment;
import com.moutamid.whatzboost.fragments.PhotoFragment;
import com.moutamid.whatzboost.fragments.VideoFragment;
import com.moutamid.whatzboost.models.SearchModel;

import java.util.ArrayList;
import java.util.Collections;

public class EmotionsActivity extends AppCompatActivity {
    ActivityEmotionsBinding binding;
    int cur = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmotionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new AngryFragment()).commit();


        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.emotions, "Emotions\n ");

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

        binding.angryCard.setOnClickListener(v -> {
            int i = 0;
            if (i != cur){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).replace(R.id.framelayout, new AngryFragment()).commit();
                cur = 0;
            }


            binding.angryCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.happyCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.otherCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

        binding.happyCard.setOnClickListener(v -> {
            int i = 1;
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new HappyFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new HappyFragment()).commit();
                }
            cur = i;
            binding.angryCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.happyCard.setCardBackgroundColor(getResources().getColor(R.color.card));
            binding.otherCard.setCardBackgroundColor(getResources().getColor(R.color.background));
        });

        binding.otherCard.setOnClickListener(v -> {
            int i = 2;
                if (i < cur) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.framelayout, new OtherEmoFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_out,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_in  // popExit
                    ).replace(R.id.framelayout, new OtherEmoFragment()).commit();
                }
            cur = i;
            binding.angryCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.happyCard.setCardBackgroundColor(getResources().getColor(R.color.background));
            binding.otherCard.setCardBackgroundColor(getResources().getColor(R.color.card));
        });

    }

    @Override
    public void onBackPressed() {
       // startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}