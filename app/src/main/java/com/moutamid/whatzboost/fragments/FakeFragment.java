package com.moutamid.whatzboost.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.FragmentFakeBinding;
import com.moutamid.whatzboost.ui.MakeProfileActivity;
import com.moutamid.whatzboost.ui.MakeStoryActivity;
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

        binding.profile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int duration = 300;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                                "scaleX", 0.8f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                                "scaleY", 0.8f);
                        scaleDownX.setDuration(duration);
                        scaleDownY.setDuration(duration);

                        AnimatorSet scaleDown = new AnimatorSet();
                        scaleDown.play(scaleDownX).with(scaleDownY);

                        scaleDown.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(
                                v, "scaleX", 1f);
                        ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(
                                v, "scaleY", 1f);
                        scaleDownX2.setDuration(duration);
                        scaleDownY2.setDuration(duration);

                        AnimatorSet scaleDown2 = new AnimatorSet();
                        scaleDown2.play(scaleDownX2).with(scaleDownY2);

                        scaleDown2.start();
                        startActivity(new Intent(requireContext(), MakeProfileActivity.class));
                        requireActivity().finish();

                        break;
                }
                return true;
            }
        });

        binding.story.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int duration = 300;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                                "scaleX", 0.8f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                                "scaleY", 0.8f);
                        scaleDownX.setDuration(duration);
                        scaleDownY.setDuration(duration);

                        AnimatorSet scaleDown = new AnimatorSet();
                        scaleDown.play(scaleDownX).with(scaleDownY);

                        scaleDown.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(
                                v, "scaleX", 1f);
                        ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(
                                v, "scaleY", 1f);
                        scaleDownX2.setDuration(duration);
                        scaleDownY2.setDuration(duration);

                        AnimatorSet scaleDown2 = new AnimatorSet();
                        scaleDown2.play(scaleDownX2).with(scaleDownY2);

                        scaleDown2.start();
                        startActivity(new Intent(requireContext(), MakeStoryActivity.class));
                        requireActivity().finish();

                        break;
                }
                return true;
            }
        });

        return binding.getRoot();
    }
}