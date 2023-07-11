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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adsense.Ads;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.ViewAdIndicatorBinding;
import com.moutamid.whatzboost.listners.SearchLister;
import com.moutamid.whatzboost.models.SearchModel;
import com.moutamid.whatzboost.ui.TextToEmojiActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchVH> implements Filterable {

    Context context;
    ArrayList<SearchModel> list;
    ArrayList<SearchModel> listAll;
    SearchLister searchLister;
    private Set<Integer> randomIndices;

    public SearchAdapter(Context context, ArrayList<SearchModel> list, SearchLister searchLister) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
        this.searchLister = searchLister;
        this.randomIndices = generateRandomIndices(list.size());
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

        if (randomIndices.contains(position)) {
            holder.tool_view.setVisibility(View.VISIBLE);
        } else {
            holder.tool_view.setVisibility(View.GONE);
        }

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

                    new Handler().postDelayed(() -> {
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
                            "scaleX", 0.8f);
                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                            "scaleY", 0.8f);
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
                        boolean showAd = holder.tool_view.getVisibility() == View.VISIBLE;
                        searchLister.click(list.get(holder.getAbsoluteAdapterPosition()).getName(), showAd, holder.dot, holder.view_counter);
                    }, 300);


                    break;
            }
            return true;
        });

    }

    private Set<Integer> generateRandomIndices(int dataSize) {
        Set<Integer> indices = new HashSet<>();
        Random random = new Random();
//        Toast.makeText(context, "s " + s, Toast.LENGTH_SHORT).show();
        int s = Stash.getInt(Ads.DOTS_FREQUENCY, 4);
        boolean showRandomOnly = (s == 1 || s == 0);
        if (!showRandomOnly) {
            for (int i = 0; i < dataSize; i++) {
                int randomIndex = random.nextInt(dataSize - i);
                int freq = random.nextInt(s);
                if (freq == 1 || freq == 0) {
                    indices.add(random.nextInt(dataSize));
                }
            }
        } else {
            for (int i = 0; i < dataSize; i++) {
                indices.add(i);
            }
        }
//            if (!showRandomOnly) {
//                while ((indices.size() < Math.min(freq, dataSize))) {
//                    indices.add(random.nextInt(dataSize));
//                }
//            } else {
//                for (int i = 0; i < dataSize; i++) {
//                    indices.add(i);
//                }
//            }
        return indices;
    }

    @Override
    public int getItemCount() {
        if (Stash.getBoolean(Constants.RECENTS, false)) {
            return list.size() > 6 ? 6 : list.size();
        }
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
            if (charSequence.toString().isEmpty()) {
                filterList.addAll(listAll);
            } else {
                for (SearchModel listModel : listAll) {
                    if (listModel.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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

        View tool_view, dot;
        TextView view_counter;

        public SearchVH(@NonNull View itemView) {
            super(itemView);

            tool = itemView.findViewById(R.id.tool);
            toolName = itemView.findViewById(R.id.tool_name);
            toolImage = itemView.findViewById(R.id.tool_icon);
            tool_view = itemView.findViewById(R.id.tool_view);
            dot = tool_view.findViewById(R.id.view);
            view_counter = tool_view.findViewById(R.id.counter);

        }
    }

}
