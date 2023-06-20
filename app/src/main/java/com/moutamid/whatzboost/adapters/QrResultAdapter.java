package com.moutamid.whatzboost.adapters;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.whatzboost.R;

import java.util.ArrayList;

public class QrResultAdapter extends RecyclerView.Adapter<QrResultAdapter.QRVH> {
    Context context;
    ArrayList<String> list;

    public QrResultAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public QRVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QRVH(LayoutInflater.from(context).inflate(R.layout.result_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QRVH holder, int position) {
        String text = list.get(holder.getAbsoluteAdapterPosition());
        holder.result.setText(text);

        holder.copy.setOnClickListener(v -> {
            String str = list.get(holder.getAbsoluteAdapterPosition());
            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copied Text", str));
            Toast.makeText(context, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QRVH extends RecyclerView.ViewHolder{
        TextView result;
        ImageView copy;

        public QRVH(@NonNull View itemView) {
            super(itemView);
            result = itemView.findViewById(R.id.result);
            copy = itemView.findViewById(R.id.copy);
        }
    }

}
