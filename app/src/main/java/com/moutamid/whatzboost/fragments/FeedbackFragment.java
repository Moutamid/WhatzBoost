package com.moutamid.whatzboost.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.moutamid.whatzboost.BuildConfig;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.databinding.FragmentFeedbackBinding;
import com.moutamid.whatzboost.notification.Data;
import com.moutamid.whatzboost.notification.NotiModel;

import java.util.ArrayList;

public class FeedbackFragment extends Fragment {
    FragmentFeedbackBinding binding;
    ArrayAdapter<String> toolsAdapter;
    public FeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedbackBinding.inflate(getLayoutInflater(), container, false);

        ArrayList<NotiModel> list = Data.getData();
        ArrayList<String> toolsList = new ArrayList<>();
        for (NotiModel model : list){
            toolsList.add(model.title);
        }

        toolsAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, toolsList);
        binding.toolsList.setAdapter(toolsAdapter);

        binding.send.setOnClickListener(v -> {
            if (valid()){

                String appVersion = BuildConfig.VERSION_NAME;
                String androidVersion = Build.VERSION.RELEASE;
                String deviceName = Build.MODEL;

                String subject = "Feedback for WhatzBoost";
                String text = "App Version: " + appVersion + "\n"
                        + "Android Version: " + androidVersion + "\n"
                        + "Device Name: " + deviceName + "\n" + "Tool Name : " + binding.category.getEditText().getText().toString() + "\n\n"
                        + "Feedback Message : \n" + binding.message.getEditText().getText().toString() ;

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
              //  intent.setDataAndType(Uri.parse("mailto:"), "message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hello@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                   // Toast.makeText(requireContext(), "No app found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    private boolean valid() {
        if (binding.message.getEditText().getText().toString().isEmpty()){
            binding.message.getEditText().setError("Message Box is required!");
            return false;
        }
        if (binding.category.getEditText().getText().toString().isEmpty()){
            binding.category.getEditText().setError("Message Box is required!");
            return false;
        }
        return true;
    }
}