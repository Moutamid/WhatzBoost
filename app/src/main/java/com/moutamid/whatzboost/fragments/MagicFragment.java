package com.moutamid.whatzboost.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentMagicBinding;
import com.moutamid.whatzboost.ui.CaptionListActivity;
import com.moutamid.whatzboost.ui.EmotionsActivity;
import com.moutamid.whatzboost.ui.FontFunActivity;
import com.moutamid.whatzboost.ui.QrScannerActivity;
import com.moutamid.whatzboost.ui.ShayariActivity;
import com.moutamid.whatzboost.ui.TextToEmojiActivity;

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

        binding.textMagic.setOnTouchListener(Constants.customOnTouchListner(TextToEmojiActivity.class, requireContext(), requireActivity()));
        binding.fontFun.setOnTouchListener(Constants.customOnTouchListner(FontFunActivity.class, requireContext(), requireActivity()));
        binding.captions.setOnTouchListener(Constants.customOnTouchListner(CaptionListActivity.class, requireContext(), requireActivity()));
        binding.emotions.setOnTouchListener(Constants.customOnTouchListner(EmotionsActivity.class, requireContext(), requireActivity()));
        binding.shayari.setOnTouchListener(Constants.customOnTouchListner(ShayariActivity.class, requireContext(), requireActivity()));

        return binding.getRoot();
    }
}