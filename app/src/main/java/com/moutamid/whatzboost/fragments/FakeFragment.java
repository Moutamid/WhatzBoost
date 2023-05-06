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
import com.moutamid.whatzboost.databinding.FragmentFakeBinding;
import com.moutamid.whatzboost.ui.MakeProfileActivity;
import com.moutamid.whatzboost.ui.MakeStoryActivity;
import com.moutamid.whatzboost.ui.TextToEmojiActivity;
import com.moutamid.whatzboost.ui.VideoSplitterActivity;
import com.moutamid.whatzboost.ui.WhatsWebActivity;

public class FakeFragment extends Fragment {
    FragmentFakeBinding binding;

    public FakeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFakeBinding.inflate(getLayoutInflater(), container, false);

        binding.profile.setOnTouchListener(Constants.customOnTouchListner(MakeProfileActivity.class, requireContext(), requireActivity()));
        binding.story.setOnTouchListener(Constants.customOnTouchListner(MakeStoryActivity.class, requireContext(), requireActivity()));

        return binding.getRoot();
    }
}