package com.moutamid.whatzboost.fragments;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentMainBinding;
import com.moutamid.whatzboost.ui.DeletedMessageActivity;
import com.moutamid.whatzboost.ui.DirectActivity;
import com.moutamid.whatzboost.ui.RepeaterActivity;
import com.moutamid.whatzboost.ui.StatusSaverActivity;
import com.moutamid.whatzboost.ui.WhatsWebActivity;

public class MainFragment extends Fragment {
    FragmentMainBinding binding;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);

        binding.deleteMessage.setOnTouchListener((v, event) -> {
            int duration = 300;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                            "scaleX", 0.6f);
                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                            "scaleY", 0.6f);
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

                    new Handler().postDelayed(() -> {
                        if (!Constants.isPermissionGranted(requireContext())) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_AUDIO);
                                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_VIDEO);
                                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES);
                                ActivityCompat.requestPermissions(requireActivity(), Constants.permissions13, 1);
                            } else {
                                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                                ActivityCompat.requestPermissions(requireActivity(), Constants.permissions, 1);
                            }
                        } else {
                            if (!Constants.isNotificationServiceEnabled(requireContext())){
                                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                                startActivity(intent);
                            } else {
                                startActivity(new Intent(requireContext(), DeletedMessageActivity.class));
                                requireActivity().finish();
                            }

                        }
                    }, 300);

                    break;
            }


            return true;
        });
        binding.videoSplitter.setOnClickListener(v -> {

        });
        binding.whatsWeb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int duration = 300;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                                "scaleX", 0.6f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                                "scaleY", 0.6f);
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
                        new Handler().postDelayed(() -> {
                            startActivity(new Intent(requireContext(), WhatsWebActivity.class));
                            requireActivity().finish();
                        }, 300);

                        break;
                }
                return true;
            }
        });

        binding.directChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int duration = 300;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                                "scaleX", 0.6f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                                "scaleY", 0.6f);
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
                        new Handler().postDelayed(() -> {
                            startActivity(new Intent(requireContext(), DirectActivity.class));
                            requireActivity().finish();
                        }, 300);

                        break;
                }
                return true;
            }
        });

        binding.statusSaver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int duration = 300;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                                "scaleX", 0.6f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                                "scaleY", 0.6f);
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
                        new Handler().postDelayed(() -> {
                            startActivity(new Intent(requireContext(), StatusSaverActivity.class));
                            requireActivity().finish();
                        }, 300);

                        break;
                }
                return true;
            }
        });

        binding.repeater.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int duration = 300;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                                "scaleX", 0.6f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                                "scaleY", 0.6f);
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
                        new Handler().postDelayed(() -> {
                            startActivity(new Intent(requireContext(), RepeaterActivity.class));
                            requireActivity().finish();
                        }, 300);

                        break;
                }
                return true;
            }
        });

        return binding.getRoot();
    }
}