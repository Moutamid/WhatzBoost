package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.R;
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
import java.util.ArrayList;
import java.util.Arrays;

public class EnglishShayariFragment extends Fragment {
    FragmentEnglishShayariBinding binding;
    ShayriAuthorAdapter adapter, titleAdapter;
    public EnglishShayariFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEnglishShayariBinding.inflate(getLayoutInflater(), container, false);

        Constants.initDialog(requireContext());

        binding.authorLayout.setVisibility(View.VISIBLE);
        binding.titleLayout.setVisibility(View.GONE);
        binding.poetLayout.setVisibility(View.GONE);

        ArrayList<String>author = Stash.getArrayList(Constants.Authors, String.class);
        if (author.size() > 0){
            adapter = new ShayriAuthorAdapter(requireContext(), author, listner);
            Stash.put("SS", 1);
            binding.authorRC.setHasFixedSize(false);
            binding.authorRC.setAdapter(adapter);
        } else {
            getAuthors();
        }


        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.authorLayout.getVisibility() == View.VISIBLE){
                        adapter.getFilter().filter(s);
                } else if (binding.titleLayout.getVisibility() == View.VISIBLE){
                        if (titleAdapter != null){
                            titleAdapter.getFilter().filter(s);
                        }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button press
                // Change the visibility of the view
                int o = Stash.getInt("SS", 1);
                if (o == 2){
                    binding.authorLayout.setVisibility(View.GONE);
                    binding.titleLayout.setVisibility(View.VISIBLE);
                    binding.poetLayout.setVisibility(View.GONE);
                    binding.search.setText("");
                    binding.search.setHint("Search Author Titles");
                } else if (o == 1) {
                    binding.authorLayout.setVisibility(View.VISIBLE);
                    binding.titleLayout.setVisibility(View.GONE);
                    binding.poetLayout.setVisibility(View.GONE);
                    binding.search.setText("");
                    binding.search.setHint("Search Author");
                }
            }
        });
    }

    private void getAuthors() {
        Constants.showDialog();
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
                        ArrayList<String>author = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            author.add(arr.getString(i));
                        }
                        Stash.put(Constants.Authors, author);
                        adapter = new ShayriAuthorAdapter(requireContext(), author, listner);
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
        Constants.showDialog();
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
                        ArrayList<String>author = new ArrayList<>();
                        for (int i = 0; i < jsonObject.length(); i++) {
                            author.add(jsonObject.getJSONObject(i).getString("title"));
                        }
                        Stash.put(Constants.formatName(logo), author);
                        titleAdapter = new ShayriAuthorAdapter(requireContext(), author, listner);
                        Constants.dismissDialog();
                        Stash.put("SS", 2);
                        binding.titleRC.setHasFixedSize(false);
                        binding.titleRC.setAdapter(titleAdapter);
                    } catch (JSONException error) {
                        Constants.dismissDialog();
                        Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    private void getPoetry(String logo) {
        Constants.showDialog();
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

                        ArrayList<String> poetryList = new ArrayList<>(Arrays.asList(poetry));
                        String auth = Stash.getString("AUTH");
                        String name = Constants.formatName(auth) + Constants.formatName(logo);
                        Stash.put(name, poetryList);
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
                binding.search.setText("");
                binding.titles.setText(logo);
                binding.search.setHint("Search Author Titles");
                Stash.put("AUTH", logo);
                ArrayList<String> titles = Stash.getArrayList(Constants.formatName(logo), String.class);
                if (titles.size() > 0){
                    getTitlesLocal(titles);
                } else {
                    getTitles(logo);
                }
            } else if (o == 2) {
                binding.authorLayout.setVisibility(View.GONE);
                binding.titleLayout.setVisibility(View.GONE);
                binding.poetLayout.setVisibility(View.VISIBLE);
                binding.search.setText("");
                binding.poet.setText(logo);
                String auth = Stash.getString("AUTH");
                String name = Constants.formatName(auth) + Constants.formatName(logo);
                ArrayList<String> poetry = Stash.getArrayList(name, String.class);
                if (poetry.size() > 0){
                    getPoetryLocal(poetry);
                } else {
                    getPoetry(logo);
                }
            }
        }
    };

    private void getPoetryLocal(ArrayList<String> poetry) {
        String[] stringArray = new String[poetry.size()];
        poetry.toArray(stringArray);
        CaptionAdapter adapter = new CaptionAdapter(requireContext(), stringArray);
        binding.poetRC.setHasFixedSize(false);
        binding.poetRC.setAdapter(adapter);
    }

    private void getTitlesLocal(ArrayList<String> titles) {
        titleAdapter = new ShayriAuthorAdapter(requireContext(), titles, listner);
        Stash.put("SS", 2);
        binding.titleRC.setHasFixedSize(false);
        binding.titleRC.setAdapter(titleAdapter);
    }

}