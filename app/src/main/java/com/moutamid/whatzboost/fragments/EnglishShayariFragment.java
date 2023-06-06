package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.adapters.CaptionAdapter;
import com.moutamid.whatzboost.adapters.ShayriAuthorAdapter;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentEnglishShayariBinding;
import com.moutamid.whatzboost.listners.ShayriClickListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class EnglishShayariFragment extends Fragment {
    FragmentEnglishShayariBinding binding;
    public EnglishShayariFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEnglishShayariBinding.inflate(getLayoutInflater(), container, false);

        Constants.initDialog(requireContext());
        Constants.showDialog();

        binding.authorLayout.setVisibility(View.VISIBLE);
        binding.titleLayout.setVisibility(View.GONE);
        binding.poetLayout.setVisibility(View.GONE);

        getAuthors();

        return binding.getRoot();
    }

    private void getAuthors() {
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(Constants.API_LINK);
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                Log.d("TAG", "compress: ERROR: " + e.toString());
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if ((input = in != null ? in.readLine() : null) == null) break;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            Log.d("HTMLDATA", "data: " + htmlData);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    try {
//                        Toast.makeText(context, htmlData, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = new JSONObject(htmlData);
                        Log.d("HTMLDATA", htmlData);
                        JSONArray arr = jsonObject.getJSONArray("authors");
                        String[] author = new String[arr.length()];
                        for (int i = 0; i < arr.length(); i++) {
                            author[i] = arr.getString(i);
                        }
                        ShayriAuthorAdapter adapter = new ShayriAuthorAdapter(requireContext(), author, listner);
                        Constants.dismissDialog();
                        Stash.put("SS", 1);
                        binding.authorRC.setHasFixedSize(false);
                        binding.authorRC.setAdapter(adapter);
                    } catch (JSONException error) {
                        Constants.dismissDialog();
                        Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    private void getTitles(String logo) {
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(Constants.titleLink(logo));
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                Log.d("TAG", "compress: ERROR: " + e.toString());
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if ((input = in != null ? in.readLine() : null) == null) break;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            Log.d("HTMLDATA", "data: " + htmlData);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    try {
//                        Toast.makeText(context, htmlData, Toast.LENGTH_SHORT).show();
                        JSONArray jsonObject = new JSONArray(htmlData);
                        Log.d("HTMLDATA", "Size: " + jsonObject.length());
                        String[] author = new String[jsonObject.length()];
                        for (int i = 0; i < jsonObject.length(); i++) {
                            author[i] = jsonObject.getJSONObject(i).getString("title");
                        }
                        ShayriAuthorAdapter adapter = new ShayriAuthorAdapter(requireContext(), author, listner);
                        Constants.dismissDialog();
                        Stash.put("SS", 2);
                        binding.titleRC.setHasFixedSize(false);
                        binding.titleRC.setAdapter(adapter);
                    } catch (JSONException error) {
                        Constants.dismissDialog();
                        Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    private void getPoetry(String logo) {
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(Constants.poetryLink(logo));
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                Log.d("TAG", "compress: ERROR: " + e.toString());
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if ((input = in != null ? in.readLine() : null) == null) break;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            Log.d("HTMLDATA", "data: " + htmlData);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    try {
//                        Toast.makeText(context, htmlData, Toast.LENGTH_SHORT).show();
                        JSONArray jsonObject = new JSONArray(htmlData);
                        Log.d("HTMLDATA", "Size: " + jsonObject.length());
                        String[] poetry = new String[jsonObject.length()];
                        for (int i = 0; i < jsonObject.length(); i++) {
                            JSONObject obj = jsonObject.getJSONObject(i);
                            String pp = "";
                            JSONArray lines = obj.getJSONArray("lines");
                            for (int j = 0; j < lines.length(); j++) {
                                pp += lines.getString(j) + "\n" ;
                            }
                            poetry[i] = pp;
                        }
                        CaptionAdapter adapter = new CaptionAdapter(requireContext(), poetry);
                        Constants.dismissDialog();
                        binding.poetRC.setHasFixedSize(false);
                        binding.poetRC.setAdapter(adapter);
                    } catch (JSONException error) {
                        Constants.dismissDialog();
                        Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    ShayriClickListner listner = new ShayriClickListner() {
        @Override
        public void onClick(String logo) {
            int o = Stash.getInt("SS", 1);
            if (o == 1){
                binding.authorLayout.setVisibility(View.GONE);
                binding.titleLayout.setVisibility(View.VISIBLE);
                binding.poetLayout.setVisibility(View.GONE);
                Constants.showDialog();
                Stash.put("AUTH", logo);
                getTitles(logo);
            } else if (o == 2) {
                binding.authorLayout.setVisibility(View.GONE);
                binding.titleLayout.setVisibility(View.GONE);
                binding.poetLayout.setVisibility(View.VISIBLE);
                Constants.showDialog();
                getPoetry(logo);
            }
        }
    };

}