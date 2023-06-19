package com.moutamid.whatzboost.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.LastMessageAdapter;
import com.moutamid.whatzboost.adapters.MediaAdapter;
import com.moutamid.whatzboost.adapters.WamrChatAdapter;
import com.moutamid.whatzboost.constants.MyApplication;
import com.moutamid.whatzboost.databinding.FragmentMessagesBinding;
import com.moutamid.whatzboost.listners.OnChatClickListener;
import com.moutamid.whatzboost.models.UnseenViewModel;
import com.moutamid.whatzboost.room.LastMessage;
import com.moutamid.whatzboost.room.Medias;
import com.moutamid.whatzboost.room.RoomDB;
import com.moutamid.whatzboost.room.WhatsappData;
import com.moutamid.whatzboost.services.Repository;
import com.moutamid.whatzboost.ui.ChatDetailActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

public class MessagesFragment extends Fragment {
    FragmentMessagesBinding binding;
    RoomDB roomDB;
    WhatsappData whatsappData;
    LastMessageAdapter adapter;
    List<WhatsappData> listOfData = new ArrayList<>();
    WamrChatAdapter chatAdapter;
    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessagesBinding.inflate(getLayoutInflater(), container, false);

        binding.recylerViewMediaRecover.setHasFixedSize(false);
        binding.recylerViewMediaRecover.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        roomDB = RoomDB.getInstance(requireContext());

      // getChat();

       getData();

        return binding.getRoot();
    }

    private void getData() {
        UnseenViewModel unseenViewModel = new ViewModelProvider(this).get(UnseenViewModel.class);
        unseenViewModel.getAllLastMessages().observe(getViewLifecycleOwner(), new Observer<List<LastMessage>>() {
            @Override
            public void onChanged(List<LastMessage> lastMessages) {
                setAdapter(lastMessages);
            }
        });

        adapter = new LastMessageAdapter(new LastMessageAdapter.OnMessageClickListener() {
            @Override
            public void onMessageClick(LastMessage hiddenMessage, int position) {
                ChatDetailActivity.start(MyApplication.getAppContext(), hiddenMessage.getMsgTitle(), hiddenMessage.getPack());
            }

            @Override
            public void onSelectionStarted() {
                Timber.i("onSelectionStarted");
                //viewDeleted.setVisibility(View.VISIBLE);
            }

            @Override
            public void updateCount(int selectedCount) {
                Timber.i("updateCount : %s", selectedCount);
            }

            @Override
            public void onSelectionFinished() {
                Timber.i("onSelectionFinished");
                //viewDeleted.setVisibility(View.GONE);
            }
        });

        binding.recylerViewMediaRecover.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recylerViewMediaRecover.setAdapter(adapter);

    }

    private void setAdapter(List<LastMessage> messages) {
        adapter.clearList();
        adapter.addAll(messages);
        binding.recylerViewMediaRecover.setAdapter(adapter);
        if (adapter.size() == 0) {
          //  notFound.setVisibility(View.VISIBLE);
            binding.recylerViewMediaRecover.setVisibility(View.GONE);
        } else {
          //  notFound.setVisibility(View.GONE);
            binding.recylerViewMediaRecover.setVisibility(View.VISIBLE);
        }
    }


    private void getChat() {
        whatsappData = new WhatsappData();

        roomDB.mainDao().getAll().observe(requireActivity(), new Observer<List<WhatsappData>>() {
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
            WhatsappData data = whatsappData;
            Intent intent = new Intent(requireContext(), ChatDetailActivity.class);
            intent.putExtra("name", data.getName());
            intent.putExtra("messages", data.getMessages());
            intent.putExtra("EXTRA_MESSAGE_PACK", "com.whatsapp");
            intent.putExtra("images", data.getImage());
            startActivity(intent);
        }

        @Override
        public void onChatLongItemClick(WhatsappData whatsappData) {

        }

    };

}