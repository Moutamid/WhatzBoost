package com.moutamid.whatzboost.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.FragmentMainBinding;
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

        binding.deleteMessage.setOnClickListener(v -> {

        });
        binding.videoSplitter.setOnClickListener(v -> {

        });
        binding.whatsWeb.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), WhatsWebActivity.class));
        });
        binding.directChat.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), DirectActivity.class));
        });
        binding.statusSaver.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), StatusSaverActivity.class));
        });

        binding.repeater.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), RepeaterActivity.class));
        });

        return binding.getRoot();
    }
}