package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.CaptionAdapter;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentHindiShayariBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class HindiShayariFragment extends Fragment {
    FragmentHindiShayariBinding binding;
    public HindiShayariFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHindiShayariBinding.inflate(getLayoutInflater(), container, false);

        try {
//                        Toast.makeText(context, htmlData, Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            Log.d("HTMLDATA", "Size: " + jsonObject.length());
            JSONArray array = jsonObject.getJSONArray("shayris");
            String[] poetry = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject shayriText = array.getJSONObject(i);
                poetry[i] = shayriText.getString("shayriText");
            }
            CaptionAdapter adapter = new CaptionAdapter(requireContext(), poetry);
            Constants.dismissDialog();
            binding.poetRC.setHasFixedSize(false);
            binding.poetRC.setAdapter(adapter);
        } catch (JSONException error) {
            Constants.dismissDialog();
            Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
        }

        return binding.getRoot();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = requireActivity().getAssets().open("shayri.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}