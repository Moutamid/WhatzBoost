package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentOtherBinding;
import com.moutamid.whatzboost.ui.BlankMessageActivity;
import com.moutamid.whatzboost.ui.InstaReshareActivity;
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
        boolean viewProfile = false;

        binding.qrGen.setOnTouchListener(Constants.customOnTouchListner(QrGeneratorActivity.class, requireContext(), requireActivity(), viewProfile));
        binding.qrScan.setOnTouchListener(Constants.customOnTouchListner(QrScannerActivity.class, requireContext(), requireActivity(), viewProfile));
        binding.reshare.setOnTouchListener(Constants.customOnTouchListner(InstaReshareActivity.class, requireContext(), requireActivity(), viewProfile));
        binding.blank.setOnTouchListener(Constants.customOnTouchListner(BlankMessageActivity.class, requireContext(), requireActivity(), viewProfile));

        return binding.getRoot();
    }
}