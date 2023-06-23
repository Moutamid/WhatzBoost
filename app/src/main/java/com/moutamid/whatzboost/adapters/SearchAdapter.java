package com.moutamid.whatzboost.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.listners.SearchLister;
import com.moutamid.whatzboost.models.SearchModel;
import com.moutamid.whatzboost.ui.TextToEmojiActivity;

import java.util.ArrayList;
import java.util.Collection;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchVH> implements Filterable {

    Context context;
    ArrayList<SearchModel> list;
    ArrayList<SearchModel> listAll;

    SearchLister searchLister;

    public SearchAdapter(Context context, ArrayList<SearchModel> list, SearchLister searchLister) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
        this.searchLister = searchLister;
    }

    @NonNull
    @Override
    public SearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchVH(LayoutInflater.from(context).inflate(R.layout.search_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVH holder, int position) {
        SearchModel model = list.get(holder.getAbsoluteAdapterPosition());

        holder.toolName.setText(model.getName());
        holder.toolImage.setImageResource(model.getIcon());

        holder.tool.setOnTouchListener((v, event) -> {
            int duration = 300;
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    ObjectAnimator scaleDownXX = ObjectAnimator.ofFloat(v,
                            "scaleX", 0.6f);
                    ObjectAnimator scaleDownYY = ObjectAnimator.ofFloat(v,
                            "scaleY", 0.6f);
                    scaleDownXX.setDuration(duration);
                    scaleDownYY.setDuration(duration);

                    AnimatorSet scaleDownn = new AnimatorSet();
                    scaleDownn.play(scaleDownXX).with(scaleDownYY);

                    scaleDownn.start();

                    new Handler().postDelayed(()-> {
                        ObjectAnimator scaleDownX3 = ObjectAnimator.ofFloat(v,
                                "scaleX", 1f);
                        ObjectAnimator scaleDownY3 = ObjectAnimator.ofFloat(v,
                                "scaleY", 1f);
                        scaleDownX3.setDuration(duration);
                        scaleDownY3.setDuration(duration);

                        AnimatorSet scaleDown3 = new AnimatorSet();
                        scaleDown3.play(scaleDownX3).with(scaleDownY3);

                        scaleDown3.start();
                    }, 300);
                    break;

                case MotionEvent.ACTION_DOWN:
                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                            "scaleX", 0.6f);
                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                            "scaleY", 0.6f);
                    scaleDownX.setDuration(duration);
                    scaleDownY.setDuration(duration);

                    AnimatorSet scaleDown = new AnimatorSet();
                    scaleDown.play(scaleDownX).with(scaleDownY);

                    scaleDown.start();
                    break;

                case MotionEvent.ACTION_UP:
                    ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(
                            v, "scaleX", 1f);
                    ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(
                            v, "scaleY", 1f);
                    scaleDownX2.setDuration(duration);
                    scaleDownY2.setDuration(duration);

                    AnimatorSet scaleDown2 = new AnimatorSet();
                    scaleDown2.play(scaleDownX2).with(scaleDownY2);

                    scaleDown2.start();
                    new Handler().postDelayed(() -> {
                        searchLister.click(list.get(holder.getAbsoluteAdapterPosition()).getName());
                    }, 300);


                    break;
            }
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<SearchModel> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterList.addAll(listAll);
            } else {
                for (SearchModel listModel : listAll){
                    if (listModel.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
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
            list.clear();
            list.addAll((Collection<? extends SearchModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class SearchVH extends RecyclerView.ViewHolder {
        MaterialCardView tool;
        TextView toolName;
        ImageView toolImage;

        public SearchVH(@NonNull View itemView) {
            super(itemView);

            tool = itemView.findViewById(R.id.tool);
            toolName = itemView.findViewById(R.id.tool_name);
            toolImage = itemView.findViewById(R.id.tool_icon);

        }
    }

}
