package com.moutamid.whatzboost.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.ui.SplitVideoActivity;
import com.moutamid.whatzboost.ui.VideoSplitterActivity;

import java.util.ArrayList;

public class SplitVideoAdapter extends RecyclerView.Adapter<SplitVideoAdapter.SplitVH>{
    Context context;
    ArrayList<String> list;

    public SplitVideoAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SplitVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SplitVH(LayoutInflater.from(context).inflate(R.layout.recent_videos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SplitVH holder, int position) {
        String videoPath = list.get(holder.getAbsoluteAdapterPosition());
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
        holder.itemImage.setImageBitmap(thumbnail);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SplitVideoActivity.class);
            intent.putExtra("path", videoPath);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SplitVH extends RecyclerView.ViewHolder{
        ImageView itemImage;
        public SplitVH(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
    
}
