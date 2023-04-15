package com.moutamid.whatzboost.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.FragmentMagicBinding;
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

        binding.textMagic.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), TextToEmojiActivity.class));
            requireActivity().finish();
        });

        return binding.getRoot();
    }
}