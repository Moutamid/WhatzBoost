package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.MyBrowser;
import com.moutamid.whatzboost.databinding.FragmentPolicyBinding;

public class PolicyFragment extends Fragment {
    FragmentPolicyBinding binding;
    public PolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentPolicyBinding.inflate(getLayoutInflater(), container, false);

        String url = "https://google.com/";

        binding.web.setWebViewClient(new MyBrowser());
        binding.web.getSettings().setLoadsImagesAutomatically(true);
        binding.web.getSettings().setJavaScriptEnabled(true);
        binding.web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.web.loadUrl(url);

        return binding.getRoot();

    }
}