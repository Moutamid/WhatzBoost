package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.hbb20.CountryCodePicker;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentToolsSettingsBinding;

public class ToolsSettingsFragment extends Fragment {
    FragmentToolsSettingsBinding binding;
    public ToolsSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentToolsSettingsBinding.inflate(getLayoutInflater(), container, false);
        String countryCode = Stash.getString(Constants.DefaultCountry);

        if (!countryCode.isEmpty()){
            binding.countryPick.setCountryForNameCode(countryCode);
        }

        if (Stash.getInt(Constants.Duration, 29000) == 29000){
            binding.radio1.setChecked(true);
            binding.radio1.setTextColor(getResources().getColor(R.color.white));
        } else {
            binding.radio2.setChecked(true);
            binding.radio2.setTextColor(getResources().getColor(R.color.white));
        }

        binding.radio1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                Stash.put(Constants.Duration, 29000);
                binding.radio1.setTextColor(getResources().getColor(R.color.white));
                binding.radio2.setTextColor(getResources().getColor(R.color.text_off));
            }
        });
        binding.radio2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                Stash.put(Constants.Duration, 15000);
                binding.radio1.setTextColor(getResources().getColor(R.color.text_off));
                binding.radio2.setTextColor(getResources().getColor(R.color.white));
            }
        });

        binding.countryPick.setOnCountryChangeListener(() -> Stash.put(Constants.DefaultCountry, binding.countryPick.getSelectedCountryNameCode()));

        return binding.getRoot();
    }
}