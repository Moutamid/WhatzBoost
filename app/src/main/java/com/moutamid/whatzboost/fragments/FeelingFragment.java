package com.moutamid.whatzboost.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentFeelingBinding;
import com.moutamid.whatzboost.ui.CaptionListActivity;
import com.moutamid.whatzboost.ui.EmotionsActivity;
import com.moutamid.whatzboost.ui.ShayariActivity;

public class FeelingFragment extends Fragment {
    public FragmentFeelingBinding binding;
    static ProgressDialog progressDialog;
    public FeelingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeelingBinding.inflate(getLayoutInflater(), container, false);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        boolean viewProfile = false;

//        binding.captions.setOnTouchListener(Constants.customOnTouchListner(CaptionListActivity.class, requireContext(), requireActivity(), viewProfile, binding.viewProfile.view, binding.viewProfile.counter));
//
//        binding.emotions.setOnTouchListener(Constants.customOnTouchListner(EmotionsActivity.class, requireContext(), requireActivity(), viewProfile, binding.viewProfile.view, binding.viewProfile.counter));
//        binding.shayari.setOnTouchListener(Constants.customOnTouchListner(ShayariActivity.class, requireContext(), requireActivity(), viewProfile, binding.viewGenerator.view, binding.viewGenerator.counter));

        return binding.getRoot();
    }

}