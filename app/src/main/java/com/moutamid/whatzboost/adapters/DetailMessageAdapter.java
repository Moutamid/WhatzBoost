package com.moutamid.whatzboost.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.room.HiddenMessage;
import com.moutamid.whatzboost.services.ImageUtils;
import com.moutamid.whatzboost.services.Repository;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class DetailMessageAdapter extends RecyclerView.Adapter<DetailMessageAdapter.BaseViewHolder> {

    private static final int TYPE_AD = 0;
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;
    private static final int TYPE_DELETED = 3;
    private static final int TYPE_DELETED_MEDIA = 4;
    private Calendar date = Calendar.getInstance(Locale.US);
    private int selectedCount = 0;
    private List<HiddenMessage> hiddenDetailedMessages;
    private OnItemClickListener onItemClickListener;
    private SimpleDateFormat format1 = new SimpleDateFormat("hh:mm a", Locale.US);
    private boolean isSelecting = false;
    Context context;

    public DetailMessageAdapter(Context context) {
        this(new ArrayList<>());
        this.context = context;
    }

    public DetailMessageAdapter(List<HiddenMessage> hiddenDetailedMessages) {
        this.hiddenDetailedMessages = hiddenDetailedMessages;
    }

    @Override
    public int getItemViewType(int position) {
        HiddenMessage hiddenMessage = hiddenDetailedMessages.get(position);
        if (hiddenMessage == null)
            return TYPE_AD;
        if (hiddenMessage.isDeleted())
            if (hiddenMessage.fileExists()) {
                return TYPE_DELETED_MEDIA;
            } else {
                return TYPE_DELETED;
            }
        if (hiddenMessage.isSent())
            return TYPE_SENT;
        return TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {

            case TYPE_DELETED:
                layoutId = R.layout.layout_msg_deleted;
                break;
            case TYPE_DELETED_MEDIA:
                layoutId = R.layout.layout_msg_deleted_media;
                return new DeletedMediaViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(layoutId, parent, false));
            case TYPE_SENT:
                layoutId = R.layout.layout_msg_sent;
                break;
            default:
            case TYPE_RECEIVED:
                layoutId = R.layout.layout_msg_received;
                break;
        }
        return new DetailMessageViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bindView(hiddenDetailedMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return hiddenDetailedMessages.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addNewMessage(HiddenMessage hiddenMessage) {
        hiddenDetailedMessages.add(0, hiddenMessage);
        notifyItemInserted(0);
    }

    public void updateItem(long id) {
        for (int i = 0; i < hiddenDetailedMessages.size(); i++) {
            HiddenMessage hiddenMessage = hiddenDetailedMessages.get(i);
            if (hiddenMessage.getId() == id) {
                hiddenMessage.setDeleted(true);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void addAll(List<HiddenMessage> hiddenMessages) {
        hiddenDetailedMessages.clear();
        hiddenDetailedMessages.addAll(hiddenMessages);
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (int index = 0; index < hiddenDetailedMessages.size(); index++) {
            HiddenMessage me = hiddenDetailedMessages.get(index);
            if (!me.isSelected()) {
                me.setSelected(true);
            }
        }
        selectedCount = hiddenDetailedMessages.size();
        updateCount();
        notifyDataSetChanged();
    }

    public void clearAll() {
        for (int index = 0; index < hiddenDetailedMessages.size(); index++) {
            HiddenMessage me = hiddenDetailedMessages.get(index);
            if (me.isSelected()) {
                me.setSelected(false);
            }
        }
        isSelecting = false;
        selectedCount = 0;
        notifyDataSetChanged();
    }

    public void deleteSelected() {
        Iterator<HiddenMessage> iterator = hiddenDetailedMessages.iterator();
        while (iterator.hasNext()) {
            HiddenMessage hiddenMessage = iterator.next();
            if (hiddenMessage.isSelected()) {
                Repository.INSTANCE.remove(hiddenMessage);
                iterator.remove();
            }
        }
        isSelecting = false;
        selectedCount = 0;
        notifyDataSetChanged();
    }

    private void toggleSelection(HiddenMessage hiddenMessage, int position) {
        hiddenMessage.setSelected(!hiddenMessage.isSelected());
        notifyItemChanged(position);
        if (hiddenMessage.isSelected()) {
            if (selectedCount == 0) {
                isSelecting = true;
                onItemClickListener.onSelectionStarted();
            }
            selectedCount++;
        } else {
            selectedCount--;
            if (selectedCount == 0) {
                isSelecting = false;
                onItemClickListener.onSelectionFinished();
            }
        }
        updateCount();

    }

    private void updateCount() {
        if (onItemClickListener != null)
            onItemClickListener.updateCount(selectedCount);
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public interface OnItemClickListener {
        void onSelectionStarted();

        void updateCount(int selectedCount);

        void onClick(int position);

        void onSelectionFinished();
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        View viewSelected;
        TextView tvSender;
        TextView tvTime;

        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            viewSelected = itemView.findViewById(R.id.viewSelected);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvTime = itemView.findViewById(R.id.tvTime);
        }

        @CallSuper
        void bindView(@NotNull final HiddenMessage hiddenMessage) {
            if (hiddenMessage.isSelected())
                viewSelected.setVisibility(View.VISIBLE);
            else
                viewSelected.setVisibility(View.INVISIBLE);

            if (hiddenMessage.isGroup()) {
                tvSender.setVisibility(View.VISIBLE);
                tvSender.setText(hiddenMessage.getSenderName());
            } else {
                tvSender.setVisibility(View.GONE);
            }
            date.setTimeInMillis(hiddenMessage.getTime());
            tvTime.setText(format1.format(date.getTime()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSelecting)
                        toggleSelection(hiddenMessage, getAdapterPosition());
                    else {
                        if (hiddenMessage.getMsgContent().contains("\uD83C\uDFA5") || hiddenMessage.getMsgContent().contains("\uD83D\uDCF7")) {
                            //inters Ad
//                            Intent intent = new Intent(context, DeletedMediaActivity.class);
//                            context.startActivity(intent);
                        } else if (hiddenMessage.getMsgContent().contains("\uD83C\uDFA4")) {
//                            Intent intent = new Intent(context, VoiceActivity.class);
//                            context.startActivity(intent);
                        } else {
                          copyText(hiddenMessage);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(v -> {
                copyText(hiddenMessage);
              //  toggleSelection(hiddenMessage, getAdapterPosition());
                return true;
            });
        }
    }

    private void copyText(@NonNull HiddenMessage hiddenMessage) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("message", hiddenMessage.getMsgContent());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "copied", Toast.LENGTH_SHORT).show();
    }

    class DeletedMediaViewHolder extends BaseViewHolder {

        private ImageView ivMedia;
        private ImageView ivPlay;
        private TextView tvDuration;

        DeletedMediaViewHolder(@NotNull View itemView) {
            super(itemView);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            ivPlay = itemView.findViewById(R.id.ivPlay);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }

        @Override
        void bindView(@NotNull HiddenMessage hiddenMessage) {
            super.bindView(hiddenMessage);
            ImageUtils.load(hiddenMessage.getFilePath(), ivMedia);
            if (Constants.isVideoFile(hiddenMessage.getFilePath())) {
                ivPlay.setVisibility(View.VISIBLE);
                tvDuration.setVisibility(View.VISIBLE);
                tvDuration.setText(Constants.getDuration(new File(hiddenMessage.getFilePath())));
            } else {
                tvDuration.setVisibility(View.GONE);
                ivPlay.setVisibility(View.GONE);
            }
        }
    }

    public class DetailMessageViewHolder extends BaseViewHolder {

        private TextView textViewDetailContent;

        DetailMessageViewHolder(View itemView) {
            super(itemView);
            textViewDetailContent = itemView.findViewById(R.id.textViewDetailContent);
        }

        @Override
        void bindView(@NotNull final HiddenMessage hiddenMessage) {
            super.bindView(hiddenMessage);
            textViewDetailContent.setText(hiddenMessage.getMsgContent());
        }
    }
}
