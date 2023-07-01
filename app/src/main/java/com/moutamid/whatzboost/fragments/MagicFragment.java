package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.adsense.Ads;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMagicBinding.inflate(getLayoutInflater(), container, false);

        Ads.calledIniti(requireContext());
        Ads.loadIntersAD(requireContext());
        Ads.loadRewardedAD(requireContext());

        ViewAdIndicatorBinding[] views = {
                binding.viewCaptions, binding.viewEmoji, binding.viewEmotions, binding.viewFont,
                binding.viewPoetry, binding.viewRepeater
        };

        int randomNumber = new Random().nextInt(views.length);
        ViewAdIndicatorBinding[] randomViews = Constants.pickRandomViews(views, randomNumber);

        for (int i =0; i< randomViews.length; i++){
            View includedLayout = randomViews[i].getRoot();
            includedLayout.setVisibility(View.VISIBLE);
        }


        boolean viewCaptions = binding.viewCaptions.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewEmoji = binding.viewEmoji.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewEmotions = binding.viewEmotions.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewFont = binding.viewFont.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewPoetry = binding.viewPoetry.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewRepeater = binding.viewRepeater.getRoot().getVisibility() == View.VISIBLE ? true : false;

        binding.repeater.setOnTouchListener(Constants.customOnTouchListner(RepeaterActivity.class, requireContext(), requireActivity(), viewRepeater));
        binding.textMagic.setOnTouchListener(Constants.customOnTouchListner(TextToEmojiActivity.class, requireContext(), requireActivity(), viewEmoji));
        binding.fontFun.setOnTouchListener(Constants.customOnTouchListner(FontFunActivity.class, requireContext(), requireActivity(), viewFont));
        binding.captions.setOnTouchListener(Constants.customOnTouchListner(CaptionListActivity.class, requireContext(), requireActivity(), viewCaptions));
        binding.emotions.setOnTouchListener(Constants.customOnTouchListner(EmotionsActivity.class, requireContext(), requireActivity(), viewEmotions));
        binding.shayari.setOnTouchListener(Constants.customOnTouchListner(ShayariActivity.class, requireContext(), requireActivity(), viewPoetry));

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Ads.calledIniti(requireContext());
        Ads.loadIntersAD(requireContext());
        Ads.loadRewardedAD(requireContext());
    }

}