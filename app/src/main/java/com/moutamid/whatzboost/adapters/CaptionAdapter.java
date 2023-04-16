package com.moutamid.whatzboost.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.whatzboost.R;

public class CaptionAdapter  extends RecyclerView.Adapter<CaptionAdapter.CaptionVH>{

    Context context;
    String[] captions;

    public CaptionAdapter(Context context, String[] captions) {
        this.context = context;
        this.captions = captions;
    }

    @NonNull
    @Override
    public CaptionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.caption_item_card, parent, false);
        return new CaptionVH(view);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(@NonNull CaptionVH holder, int position) {
        holder.caption.setText(captions[holder.getAbsoluteAdapterPosition()]);

        holder.copy.setOnClickListener(v -> {
            String str = captions[holder.getAbsoluteAdapterPosition()];
            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copied Text", str));
            Toast.makeText(context, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
        });

        holder.share.setOnClickListener(v -> {
            String str = captions[holder.getAbsoluteAdapterPosition()];
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setFlags(335544320);
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", str);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return captions.length;
    }

    public class CaptionVH extends RecyclerView.ViewHolder{
        TextView caption;
        Button share, copy;
        public CaptionVH(@NonNull View itemView) {
            super(itemView);
            caption = itemView.findViewById(R.id.caption);
            share = itemView.findViewById(R.id.shareBtn);
            copy = itemView.findViewById(R.id.copyBtn);
        }
    }

}
