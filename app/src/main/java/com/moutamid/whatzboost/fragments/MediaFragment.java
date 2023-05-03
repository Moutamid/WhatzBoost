package com.moutamid.whatzboost.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.MediaAdapter;
import com.moutamid.whatzboost.databinding.FragmentMediaBinding;
import com.moutamid.whatzboost.room.Medias;
import com.moutamid.whatzboost.room.RoomDB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaFragment extends Fragment {
    FragmentMediaBinding binding;

    RoomDB roomDB;
    MediaAdapter adapter;
    List<Medias> mediasList = new ArrayList<>();

    public MediaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMediaBinding.inflate(getLayoutInflater(), container, false);

        binding.recylerViewMediaRecover.setHasFixedSize(false);
        binding.recylerViewMediaRecover.setLayoutManager(new LinearLayoutManager(requireContext()));

        roomDB = RoomDB.getInstance(requireContext());

        getData();
        return binding.getRoot();
    }

    private void getData() {
        if (roomDB != null) {
            //  if (adapter != null) {
            if (mediasList == null) {
                mediasList = new ArrayList<>();
            }
            mediasList.clear();
            Log.d(TAG, "getData: " + mediasList.size());
            roomDB.mainDao().getAllMedia().observe(requireActivity(), new Observer<List<Medias>>() {
                @Override
                public void onChanged(List<Medias> medias) {
                    mediasList = medias;
                    if (mediasList.size() == 0) {
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("de_media", "getData: " + mediasList.size());
                    mediasList = filterList(mediasList);
                    adapter = null;
                    adapter = new MediaAdapter(mediasList, requireContext(), requireActivity());
                    binding.recylerViewMediaRecover.setAdapter(adapter);
                }
            });

        }
    }

    private List<Medias> filterList(List<Medias> list) {
        List<Medias> updateList = new ArrayList<>();
        for (Medias item : list) {

            File f = new File(item.getFile_path());
            if (f.exists()) {
                updateList.add(item);
            } else {
                //delete from db
                try {
                    roomDB.mainDao().deleteChat(item.getFile_path());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        this.mediasList.clear();
        return updateList;
    }

}