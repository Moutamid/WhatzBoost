package com.moutamid.whatzboost.adapters;

import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.models.StatusItem;

import java.io.File;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusVH> {
    private final Context context;
    private final List<StatusItem> items;

    public StatusAdapter(Context context, List<StatusItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public StatusVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.status_item, parent, false);
        return new StatusVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusVH holder, int position) {
        StatusItem item = items.get(holder.getAbsoluteAdapterPosition());

        if (item.isDownloaded()){
            holder.saveBtn.setVisibility(View.GONE);
            holder.downloadedBtn.setVisibility(View.VISIBLE);
        }

        if (item.isImage()){
            holder.video_layout.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(item.getFileUri())
                .centerCrop()
                .into(holder.itemImage);

        holder.saveBtn.setOnClickListener(v -> {
            if (download(item.getFileUri().toString(), item.getFileName(), holder.getAbsoluteAdapterPosition())){
                Toast.makeText(context, "Status Saved!", Toast.LENGTH_SHORT).show();
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
            } else {
                Toast.makeText(context, "Status is not saved try again!", Toast.LENGTH_SHORT).show();
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
            }
        });

        holder.shareBtn.setOnClickListener(v -> {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Uri imageUri = null;
            if (item.getFilePath() != null) {
                imageUri = FileProvider.getUriForFile(context, "com.moutamid.whatzboost.provider", item.getFile());
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            if (SDK_INT >= Build.VERSION_CODES.R)
                intent.putExtra(Intent.EXTRA_STREAM, item.getFileUri());
            else intent.putExtra(Intent.EXTRA_STREAM, imageUri);

            context.startActivity(Intent.createChooser(intent, "Share with"));
        });

    }

    private boolean download(String videoPath, String fileName, int position) {
        boolean isDownload = false;

        if (Constants.checkFolder()) {
            File source = new File(videoPath);
            File target = new File(Constants.SAVED_FOLDER + fileName);

            if (target.exists())
                Toast.makeText(context, "Already Saved", Toast.LENGTH_SHORT).show();
            else {
                isDownload = Constants.copyFileInSavedDir(context, videoPath, fileName);
                notifyItemChanged(position);
            }
        } else Toast.makeText(context, "File not save please try again!", Toast.LENGTH_SHORT).show();

        return isDownload;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class StatusVH extends RecyclerView.ViewHolder{
        ImageView itemImage;
        Button saveBtn, shareBtn, downloadedBtn;
        RelativeLayout video_layout;
        public StatusVH(@NonNull View itemView) {
            super(itemView);
            saveBtn = itemView.findViewById(R.id.saveBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            downloadedBtn = itemView.findViewById(R.id.downloadedBtn);
            itemImage = itemView.findViewById(R.id.itemImage);
            video_layout = itemView.findViewById(R.id.video_layout);
        }
    }

}
