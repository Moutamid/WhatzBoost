package com.moutamid.whatzboost.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.MediaAdapter;
import com.moutamid.whatzboost.adapters.WamrChatAdapter;
import com.moutamid.whatzboost.databinding.FragmentMessagesBinding;
import com.moutamid.whatzboost.listners.OnChatClickListener;
import com.moutamid.whatzboost.room.Medias;
import com.moutamid.whatzboost.room.RoomDB;
import com.moutamid.whatzboost.room.WhatsappData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessagesFragment extends Fragment {
    FragmentMessagesBinding binding;
    RoomDB roomDB;
    WhatsappData whatsappData;
    List<WhatsappData> listOfData = new ArrayList<>();
    WamrChatAdapter chatAdapter;
    List<Medias> mediasList = new ArrayList<>();
    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessagesBinding.inflate(getLayoutInflater(), container, false);

        binding.recylerViewMediaRecover.setHasFixedSize(false);
        binding.recylerViewMediaRecover.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        roomDB = RoomDB.getInstance(requireContext());

        return binding.getRoot();
    }

    private void getChat() {
        whatsappData = new WhatsappData();

        roomDB.mainDao().getAll().observe(this, new Observer<List<WhatsappData>>() {
            @Override
            public void onChanged(List<WhatsappData> whatsappData) {
                Log.d("de_d", "onChanged: " + whatsappData.size());
                listOfData = whatsappData;
                Collections.reverse(listOfData);
//                if (listOfData.size() > 0) {
//                    binding.noChatFounds.setVisibility(View.GONE);
//                } else
//                    binding.noChatFounds.setVisibility(View.VISIBLE);
                //
                chatAdapter = new WamrChatAdapter(listOfData, requireActivity(), onChatClickListener);
                binding.recylerViewMediaRecover.setVisibility(View.VISIBLE);
                binding.recylerViewMediaRecover.setAdapter(chatAdapter);
            }
        });


    }

    OnChatClickListener onChatClickListener = new OnChatClickListener() {
        @Override
        public void onChatItemClick(WhatsappData whatsappData) {
//            WhatsappData data = whatsappData;
//            Intent intent = new Intent(requireContext(), ChatDetailActivity.class);
//            intent.putExtra("name", data.getName());
//            intent.putExtra("messages", data.getMessages());
//            intent.putExtra("images", data.getImage());
//            startActivity(intent);
        }

        @Override
        public void onChatLongItemClick(WhatsappData whatsappData) {

        }

    };

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