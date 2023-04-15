package com.moutamid.whatzboost.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.FragmentOtherBinding;
import com.moutamid.whatzboost.ui.QrGeneratorActivity;
import com.moutamid.whatzboost.ui.QrScannerActivity;


public class OtherFragment extends Fragment {
    FragmentOtherBinding binding;
    public OtherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOtherBinding.inflate(getLayoutInflater(), container, false);

        binding.qrGen.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), QrGeneratorActivity.class));
            requireActivity().finish();
        });

        binding.qrScan.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), QrScannerActivity.class));
            requireActivity().finish();
        });

        return binding.getRoot();
    }
}