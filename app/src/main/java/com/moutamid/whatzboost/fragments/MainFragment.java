package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.adsense.Ads;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentMainBinding;
import com.moutamid.whatzboost.databinding.ViewAdIndicatorBinding;
import com.moutamid.whatzboost.ui.InstaReshareActivity;
import com.moutamid.whatzboost.ui.QrGeneratorActivity;
import com.moutamid.whatzboost.ui.QrScannerActivity;

import java.util.Random;

public class MainFragment extends Fragment {
    FragmentMainBinding binding;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);

        Ads.calledIniti(requireContext());
        Ads.loadIntersAD(requireContext());
        Ads.loadRewardedAD(requireContext());

        ViewAdIndicatorBinding[] views = {
                binding.viewGenerator, binding.viewScanner
        };

        int randomNumber = new Random().nextInt(views.length);
        ViewAdIndicatorBinding[] randomViews = Constants.pickRandomViews(views, randomNumber);

        for (int i =0; i< randomViews.length; i++){
            View includedLayout = randomViews[i].getRoot();
            includedLayout.setVisibility(View.VISIBLE);
        }

        boolean viewGenerator = binding.viewGenerator.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewScanner = binding.viewScanner.getRoot().getVisibility() == View.VISIBLE ? true : false;

        binding.qrGen.setOnTouchListener(Constants.customOnTouchListner(QrGeneratorActivity.class, requireContext(), requireActivity(), viewGenerator));
        binding.qrScan.setOnTouchListener(Constants.customOnTouchListner(QrScannerActivity.class, requireContext(), requireActivity(), viewScanner));
        binding.reshare.setOnTouchListener(Constants.customOnTouchListner(InstaReshareActivity.class, requireContext(), requireActivity(), false));

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