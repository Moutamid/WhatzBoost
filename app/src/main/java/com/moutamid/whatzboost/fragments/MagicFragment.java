package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentMagicBinding;
import com.moutamid.whatzboost.databinding.ViewAdIndicatorBinding;
import com.moutamid.whatzboost.ui.CaptionListActivity;
import com.moutamid.whatzboost.ui.EmotionsActivity;
import com.moutamid.whatzboost.ui.FontFunActivity;
import com.moutamid.whatzboost.ui.RepeaterActivity;
import com.moutamid.whatzboost.ui.ShayariActivity;
import com.moutamid.whatzboost.ui.TextToEmojiActivity;

import java.util.Random;

public class MagicFragment extends Fragment {
    FragmentMagicBinding binding;

    public MagicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMagicBinding.inflate(getLayoutInflater(), container, false);


        ViewAdIndicatorBinding[] views = {
                binding.viewEmoji, binding.viewEmotions, binding.viewFont, binding.viewRepeater
        };

        int randomNumber = new Random().nextInt(views.length);
        ViewAdIndicatorBinding[] randomViews = Constants.pickRandomViews(views, randomNumber);

        for (ViewAdIndicatorBinding randomView : randomViews) {
            randomView.getRoot().setVisibility(View.VISIBLE);
        }


        boolean viewEmoji = binding.viewEmoji.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewEmotions = binding.viewEmotions.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewFont = binding.viewFont.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewRepeater = binding.viewRepeater.getRoot().getVisibility() == View.VISIBLE ? true : false;

        binding.repeater.setOnTouchListener(Constants.customOnTouchListner(RepeaterActivity.class, requireContext(), requireActivity(), viewRepeater, binding.viewRepeater.view, binding.viewRepeater.counter));
        binding.textMagic.setOnTouchListener(Constants.customOnTouchListner(TextToEmojiActivity.class, requireContext(), requireActivity(), viewEmoji, binding.viewEmoji.view, binding.viewEmoji.counter));
        binding.fontFun.setOnTouchListener(Constants.customOnTouchListner(FontFunActivity.class, requireContext(), requireActivity(), viewFont, binding.viewFont.view, binding.viewFont.counter));
        binding.emotions.setOnTouchListener(Constants.customOnTouchListner(EmotionsActivity.class, requireContext(), requireActivity(), viewEmotions, binding.viewEmotions.view, binding.viewEmotions.counter));

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        binding.viewEmoji.counter.setText("3");
        binding.viewEmoji.counter.setVisibility(View.GONE);
        binding.viewEmoji.view.setVisibility(View.VISIBLE);

        binding.viewEmotions.counter.setText("3");
        binding.viewEmotions.counter.setVisibility(View.GONE);
        binding.viewEmotions.view.setVisibility(View.VISIBLE);

        binding.viewFont.counter.setText("3");
        binding.viewFont.counter.setVisibility(View.GONE);
        binding.viewFont.view.setVisibility(View.VISIBLE);

        binding.viewRepeater.counter.setText("3");
        binding.viewRepeater.counter.setVisibility(View.GONE);
        binding.viewRepeater.view.setVisibility(View.VISIBLE);
    }

}