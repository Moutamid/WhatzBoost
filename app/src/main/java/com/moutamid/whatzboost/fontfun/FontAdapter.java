package com.moutamid.whatzboost.fontfun;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.whatzboost.R;

import java.util.ArrayList;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontHolder> {
    Context context;
    ArrayList<Font> dataSet;
    AdapterView.OnItemClickListener onItemClickListener;
    SharedPreferences preferences;

    public FontAdapter(Context context2, ArrayList<Font> arrayList, AdapterView.OnItemClickListener onItemClickListener2) {
        this.dataSet = arrayList;
        this.onItemClickListener = onItemClickListener2;
        this.context = context2;
    }

    @NonNull
    public FontHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(viewGroup.getContext());
        return new FontHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.caption_item_card, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final FontHolder fontHolder, int i) {
        fontHolder.caption.setText(this.dataSet.get(fontHolder.getAdapterPosition()).fontText);

        fontHolder.copy.setOnClickListener(view -> {
            ((ClipboardManager) fontHolder.itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("stylish text", fontHolder.caption.getText()));
            Toast.makeText(fontHolder.itemView.getContext(), "Text Copied", Toast.LENGTH_SHORT).show();
        });
        fontHolder.share.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setFlags(335544320);
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", fontHolder.caption.getText());
            context.startActivity(intent);
        });
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public class FontHolder extends RecyclerView.ViewHolder {
        TextView caption;
        Button share, copy;

        public FontHolder(@NonNull View view) {
            super(view);
            caption = itemView.findViewById(R.id.caption);
            share = itemView.findViewById(R.id.shareBtn);
            copy = itemView.findViewById(R.id.copyBtn);
        }
    }
}
