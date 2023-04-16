package com.moutamid.whatzboost.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.ui.CaptionsActivity;

public class CaptionsListAdapter extends RecyclerView.Adapter<CaptionsListAdapter.CaptionsListVH> {

    Context context;
    String[] logos;

    public CaptionsListAdapter(Context context, String[] logos) {
        this.context = context;
        this.logos = logos;
    }

    @NonNull
    @Override
    public CaptionsListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.captions_item_list, parent, false);
        return new CaptionsListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaptionsListVH holder, int position) {
        holder.captions.setText(logos[holder.getAdapterPosition()]);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CaptionsActivity.class);
            intent.putExtra(Constants.NAME, logos[holder.getAdapterPosition()]);
            intent.putExtra(Constants.POS, holder.getAdapterPosition());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return logos.length;
    }

    public class CaptionsListVH extends RecyclerView.ViewHolder{
        TextView captions;
        public CaptionsListVH(@NonNull View itemView) {
            super(itemView);
            captions = itemView.findViewById(R.id.caption);
        }
    }
}