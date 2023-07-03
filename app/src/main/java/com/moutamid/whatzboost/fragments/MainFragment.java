package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentMainBinding;
import com.moutamid.whatzboost.databinding.ViewAdIndicatorBinding;
import com.moutamid.whatzboost.ui.CaptionListActivity;
import com.moutamid.whatzboost.ui.EmotionsActivity;
import com.moutamid.whatzboost.ui.InstaReshareActivity;
import com.moutamid.whatzboost.ui.QrGeneratorActivity;
import com.moutamid.whatzboost.ui.QrScannerActivity;
import com.moutamid.whatzboost.ui.ShayariActivity;

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

        ViewAdIndicatorBinding[] views = {
                binding.viewGenerator, binding.viewScanner, binding.viewCaptions, binding.viewPoetry
        };

        int randomNumber = new Random().nextInt(views.length);
        ViewAdIndicatorBinding[] randomViews = Constants.pickRandomViews(views, randomNumber);

        for (ViewAdIndicatorBinding randomView : randomViews) {
            randomView.getRoot().setVisibility(View.VISIBLE);
        }

        boolean viewGenerator = binding.viewGenerator.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewScanner = binding.viewScanner.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewPoetry = binding.viewPoetry.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewCaptions = binding.viewCaptions.getRoot().getVisibility() == View.VISIBLE ? true : false;

        binding.captions.setOnTouchListener(Constants.customOnTouchListner(CaptionListActivity.class, requireContext(), requireActivity(), viewCaptions, binding.viewCaptions.view, binding.viewCaptions.counter));
        binding.shayari.setOnTouchListener(Constants.customOnTouchListner(ShayariActivity.class, requireContext(), requireActivity(), viewPoetry, binding.viewPoetry.view, binding.viewPoetry.counter));
        binding.qrGen.setOnTouchListener(Constants.customOnTouchListner(QrGeneratorActivity.class, requireContext(), requireActivity(), viewGenerator, binding.viewGenerator.view, binding.viewGenerator.counter));
        binding.qrScan.setOnTouchListener(Constants.customOnTouchListner(QrScannerActivity.class, requireContext(), requireActivity(), viewScanner, binding.viewScanner.view, binding.viewScanner.counter));
        // binding.reshare.setOnTouchListener(Constants.customOnTouchListner(InstaReshareActivity.class, requireContext(), requireActivity(), false, binding.viewGenerator.counter));

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        binding.viewGenerator.counter.setText("3");
        binding.viewGenerator.counter.setVisibility(View.GONE);
        binding.viewGenerator.view.setVisibility(View.VISIBLE);

        binding.viewScanner.counter.setText("3");
        binding.viewScanner.counter.setVisibility(View.GONE);
        binding.viewScanner.view.setVisibility(View.VISIBLE);

        binding.viewPoetry.counter.setText("3");
        binding.viewPoetry.counter.setVisibility(View.GONE);
        binding.viewPoetry.view.setVisibility(View.VISIBLE);

        binding.viewCaptions.counter.setText("3");
        binding.viewCaptions.counter.setVisibility(View.GONE);
        binding.viewCaptions.view.setVisibility(View.VISIBLE);
    }

}