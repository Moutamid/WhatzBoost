package com.moutamid.whatzboost.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.listners.OnChatClickListener;
import com.moutamid.whatzboost.room.WhatsappData;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WamrChatAdapter extends RecyclerView.Adapter<WamrChatAdapter.ChatHolder> {

    private List<WhatsappData> dataList;
    private Activity context;
    OnChatClickListener onChatClickListener;

    public WamrChatAdapter(List<WhatsappData> dataList, Activity context, OnChatClickListener onChatClickListener) {
        this.dataList = dataList;
        this.context = context;
        this.onChatClickListener = onChatClickListener;

    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        File imgFile = new File(dataList.get(position).getImage());
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        holder.senderName.setText(dataList.get(position).getName());
        holder.circleImageView.setImageBitmap(myBitmap);
        Log.d("HACK", "onBindViewHolder: POS" + position);
        String message = dataList.get(position).getMessages();
        if (message != null) {
            Log.d("Hack", "onBindViewHolder:msg " + message);
            String[] messageArray = message.split(",");
            holder.message.setText(messageArray[messageArray.length - 1].split("##")[0]);
        }


        holder.itemView.setFocusable(false);
        holder.itemView.setOnClickListener(view -> {
            onChatClickListener.onChatItemClick(dataList.get(position));

        });
        holder.itemView.setOnLongClickListener(view -> {
            onChatClickListener.onChatLongItemClick(dataList.get(position));
            return true;
        });


    }
    public void setDataList(List<WhatsappData> WhatsappData){
        this.dataList = WhatsappData;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        TextView senderName;
        CircleImageView circleImageView;
        TextView message;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.textview_name);
            message = itemView.findViewById(R.id.textview_message);
            circleImageView = itemView.findViewById(R.id.profile_image);
        }
    }
}
