package com.moutamid.whatzboost.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.listners.ShayriClickListner;
import com.moutamid.whatzboost.ui.CaptionsActivity;

import java.util.ArrayList;
import java.util.Collection;

public class ShayriAuthorAdapter extends RecyclerView.Adapter<ShayriAuthorAdapter.CaptionsListVH> implements Filterable {

    Context context;
    ArrayList<String> logos, logosAll;
    ShayriClickListner listner;

    public ShayriAuthorAdapter(Context context,  ArrayList<String> logos, ShayriClickListner listner) {
        this.context = context;
        this.logos = logos;
        this.logosAll = new ArrayList<>(logos);
        this.listner = listner;
    }

    @NonNull
    @Override
    public CaptionsListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.captions_item_list, parent, false);
        return new CaptionsListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaptionsListVH holder, int position) {
        holder.captions.setText(logos.get(holder.getAdapterPosition()));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.poppins);
        holder.captions.setTypeface(typeface);

        holder.itemView.setOnClickListener(v -> listner.onClick(logos.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return logos.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<String> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterList.addAll(logosAll);
            } else {
                for (String listModel : logosAll){
                    if (listModel.toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filterList.add(listModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        //run on Ui thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            logos.clear();
            logos.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class CaptionsListVH extends RecyclerView.ViewHolder{
        TextView captions;
        public CaptionsListVH(@NonNull View itemView) {
            super(itemView);
            captions = itemView.findViewById(R.id.caption);
        }
    }
}