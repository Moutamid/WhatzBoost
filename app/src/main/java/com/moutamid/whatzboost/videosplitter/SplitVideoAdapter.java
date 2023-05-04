package com.moutamid.whatzboost.videosplitter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.whatzboost.R;

import java.io.File;
import java.util.ArrayList;

public class SplitVideoAdapter extends RecyclerView.Adapter<SplitVideoAdapter.SplitVideoHolder> {
    private ArrayList<String> pathList;
    Context context;

    public SplitVideoAdapter(ArrayList<String> pathList, Context context) {
        this.pathList = pathList;
        this.context = context;
    }

    @NonNull
    @Override
    public SplitVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SplitVideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_split, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SplitVideoHolder holder, int position) {
        holder.bind(pathList.get(position));
       holder.name.setText(  (new File(pathList.get(position)).getName()));
       holder.name.setSelected(true);
        holder.repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    //  shareIntent.setPackage("com.whatsapp");
                    shareIntent.setType("video/mp4");
                    shareIntent.putExtra(Intent.EXTRA_STREAM,
                            FileProvider.getUriForFile(context,
                                    context.getApplicationContext().getPackageName() + ".provider", new File(pathList.get(position))));
                    ;
                    context.startActivity(Intent.createChooser(shareIntent, "Share Files"));
                    //showInterstitialAds();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    public void updateList(ArrayList<String> splitVideoPathList) {
        this.pathList.addAll(splitVideoPathList);
        notifyDataSetChanged();
    }

    public class SplitVideoHolder extends RecyclerView.ViewHolder {
        private ImageView thumb_Video;
        private TextView repost;
        TextView name;

        public SplitVideoHolder(@NonNull View itemView) {
            super(itemView);
            thumb_Video = itemView.findViewById(R.id.video_thumb);
            repost = itemView.findViewById(R.id.repost);
            name = itemView.findViewById(R.id.tv_name);
        }

        public void bind(String path) {
            Glide.with(itemView.getContext()).load(path).into(thumb_Video);

        }
    }
}
