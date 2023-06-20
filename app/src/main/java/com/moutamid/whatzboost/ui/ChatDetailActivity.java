package com.moutamid.whatzboost.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.DetailMessageAdapter;
import com.moutamid.whatzboost.adapters.MessageAdapter;
import com.moutamid.whatzboost.databinding.ActivityChatDetailBinding;
import com.moutamid.whatzboost.room.HiddenMessage;
import com.moutamid.whatzboost.room.RoomDB;
import com.moutamid.whatzboost.services.NLService;
import com.moutamid.whatzboost.services.NotificationListener;
import com.moutamid.whatzboost.services.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;

    List<String> messageList;
    RoomDB roomDB;
    String name = "";
    String messages = "";
    String imagePath;
    public static final String PACK_NONE = "";
    public static String pack = PACK_NONE;
    String time;
    String TAG = "le_chdetail";

    public static final String EXTRA_MESSAGE_TITLE = "EXTRA_MESSAGE_TITLE";
    public static final String EXTRA_MESSAGE_PACK = "EXTRA_MESSAGE_PACK";
    public static final String EXTRA_MESSAGE_SHOW = "EXTRA_MESSAGE_SHOW";
    public static final String CHAT_NONE = "";
    public static String title = CHAT_NONE;
    DetailMessageAdapter adapter;
   // MessageAdapter adapter;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    public static Intent getStartIntent(Context context, String msgTitle, String pack, boolean isFromService) {
        Intent intent = new Intent(context, ChatDetailActivity.class);
        intent.putExtra(EXTRA_MESSAGE_TITLE, msgTitle);
        intent.putExtra(EXTRA_MESSAGE_PACK, pack);
        intent.putExtra(EXTRA_MESSAGE_SHOW, isFromService);
        return intent;
    }

    public static void start(Context context, String msgTitle, String pack) {
        Intent intent = new Intent(context, ChatDetailActivity.class);
        intent.putExtra(EXTRA_MESSAGE_TITLE, msgTitle);
        intent.putExtra(EXTRA_MESSAGE_PACK, pack);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private String getExtraPack() {
        return getIntent().getStringExtra(EXTRA_MESSAGE_PACK);
    }

    private void registerObserver(String title, String pack) {
        Repository.INSTANCE.getAllSocialMessageLive(title, pack).observe(this, this::setData);
    }

    private String getExtraTitle() {
        return getIntent().getStringExtra(EXTRA_MESSAGE_TITLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        title = getExtraTitle();
        pack = getExtraPack();

        binding.send.setOnClickListener(v -> {
            String reply = binding.message.getText().toString();
            if (reply.isEmpty())
                return;
            if (NotificationListener.reply(this, title, reply, pack)) {
                binding.message.setText("");
            } else {
                Toast.makeText(this, "Can't send message", Toast.LENGTH_SHORT).show();
            }
        });

        name = getIntent().getStringExtra("name");
        imagePath = getIntent().getStringExtra("images");

        binding.person.setText(title);

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

//        try {
//            roomDB = RoomDB.getInstance(this);
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    messages = roomDB.mainDao().getAllMessages(name);
//                    Log.d(TAG, "onCreate: " + messages );
//                    if (messages == null) {
//                        messages = "No Chat History found, ):";
//                    }
//                    String[] messageArray = messages.split(",");
//                    messageList = new ArrayList<>(Arrays.asList(messageArray));
//                    time = roomDB.mainDao().getAllTime(name);
//                    String[] timeArray = time.split(",");
//                    adapter = new MessageAdapter(ChatDetailActivity.this, messageList, timeArray);
//                    binding.recycler.setAdapter(adapter);
//                    binding.recycler.scrollToPosition(messageList.size() - 1);
//                    setListeners(messageList);
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

         registerObserver(title, pack);

        adapter = new DetailMessageAdapter(this);
        binding.recycler.setAdapter(adapter);
        setAdapterListener();

    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void setData(List<HiddenMessage> hiddenMessages) {
        Collections.reverse(hiddenMessages);
        adapter.addAll(hiddenMessages);
    }

    private void setAdapterListener() {
        adapter.setOnItemClickListener(new DetailMessageAdapter.OnItemClickListener() {
            @Override
            public void onSelectionStarted() {

            }

            @Override
            public void updateCount(int selectedCount) {

            }

            @Override
            public void onClick(int position) {

            }

            @Override
            public void onSelectionFinished() {

            }
        });
    }

    private void setListeners(List <String> messageArrayList) {
        binding.recycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom)
                    if (messageArrayList.size() == 0) {
                        binding.recycler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.recycler.smoothScrollToPosition(messageArrayList.size());
                            }
                        }, 100);
                    } else {
                        binding.recycler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.recycler.smoothScrollToPosition(messageArrayList.size() - 1);
                            }
                        }, 100);
                    }
            }
        });

    }

}