package com.moutamid.whatzboost.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.whatzboost.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private String[] timeArray;
    private List<String> messagesList;


    public MessageAdapter(Activity context, List<String> dataList, String[] timeArray) {
        this.timeArray = timeArray;
        this.messagesList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_message, parent,
                false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       try {
           holder.textViewTime.setText(timeArray[position]);
       }catch (Exception e){
           e.printStackTrace();
           Log.d("de_not", "arraylent: " +timeArray.length );
           holder.textViewTime.setText(timeArray[timeArray.length-1]);
       }

        Log.d("de_msg", "onBindViewHolder: actual " + messagesList.get(position).toString());

        holder.textViewMessage.setText(messagesList.get(position).split("##")[0]);
        if(messagesList.get(position).contains("##true")){
            holder.linearLayout.setBackgroundColor(holder.itemView.getResources().getColor(R.color.delete_color));
        }else{
            holder.itemView.setBackground(holder.itemView.getResources().getDrawable(R.drawable.text));
        }

        holder.itemView.setFocusable(false);


    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView textViewTime;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textview_message);
            textViewTime = itemView.findViewById(R.id.time);
            linearLayout = itemView.findViewById(R.id.msg_root_linear);
        }
    }

}
