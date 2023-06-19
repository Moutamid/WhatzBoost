package com.moutamid.whatzboost.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.MyApplication;
import com.moutamid.whatzboost.room.LastMessage;
import com.moutamid.whatzboost.services.ImageUtils;
import com.moutamid.whatzboost.services.Repository;
import com.moutamid.whatzboost.services.StorageUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LastMessageAdapter extends RecyclerView.Adapter<LastMessageAdapter.LastMessageViewHolder> {

    private static final String TAG = LastMessageAdapter.class.getSimpleName();
    private static int currentSelectedIndex = -1;
    private OnMessageClickListener listener;

    private List<LastMessage> hiddenMessages;
    private boolean isSelecting = false;
    private int selectedCount = 0;

    public LastMessageAdapter(OnMessageClickListener listener) {
        setHasStableIds(true);
        this.listener = listener;
        this.hiddenMessages = new ArrayList<>();
    }

    @NonNull
    @Override
    public LastMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LastMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_holder, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull LastMessageViewHolder holder, final int position) {
        // set the data in items
        holder.bindView(hiddenMessages.get(position), listener);
    }

    @Override
    public long getItemId(int position) {
        return hiddenMessages.get(position).getMsgTitle().hashCode();
    }

    @Override
    public int getItemCount() {
        return hiddenMessages.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;

        LastMessage hiddenMessage = getItemAt(pos);

        hiddenMessage.setSelected(!hiddenMessage.isSelected());

        if (hiddenMessage.isSelected()) {
            if (selectedCount == 0) {
                isSelecting = true;
                listener.onSelectionStarted();
            }
            selectedCount++;
        } else {
            selectedCount--;
            if (selectedCount == 0) {
                isSelecting = false;
                listener.onSelectionFinished();
            }
        }
        updateCount();
        notifyItemChanged(pos);
    }

    private void updateCount() {
        if (listener != null)
            listener.updateCount(selectedCount);
    }

    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            hiddenMessages.get(i).select();
        }
        selectedCount = hiddenMessages.size();
        updateCount();
        notifyDataSetChanged();

    }

    public void clearSelections() {
        for (int i = 0; i < hiddenMessages.size(); i++) {
            hiddenMessages.get(i).unSelect();
        }
        selectedCount = 0;
        updateCount();
        listener.onSelectionFinished();
        isSelecting = false;
        notifyDataSetChanged();
    }

    public void addAll(List<LastMessage> messageList) {
        hiddenMessages.addAll(messageList);
    }

    public void deleteSelected() {
        for (int i = 0; i < hiddenMessages.size(); i++) {
            LastMessage lastMessage = hiddenMessages.get(i);
            if (lastMessage.isSelected()) {
                String title = lastMessage.getMsgTitle();
                lastMessage.setSelected(false);
                Repository.INSTANCE.deleteAllMessages(title);
            }
        }
        clearSelections();
    }

    public int size() {
        return hiddenMessages.size();
    }

    public void clearList() {
        hiddenMessages.clear();
        notifyDataSetChanged();
    }

    public LastMessage getItemAt(Integer position) {
        return hiddenMessages.get(position);
    }

    public interface OnMessageClickListener {
        void onMessageClick(@NonNull LastMessage hiddenMessage, int position);

        default void onSelectionStarted() {

        }

        default void updateCount(int selectedCount) {

        }

        default void onSelectionFinished() {

        }
    }

    public class LastMessageViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        private TextView textViewTitle;
        private TextView textViewContent;
        private CircleImageView imageViewUser;
        private LastMessage hiddenMessage;
        private OnMessageClickListener listener;

        LastMessageViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            textViewTitle = itemView.findViewById(R.id.textview_name);
            imageViewUser = itemView.findViewById(R.id.profile_image);
            textViewContent = itemView.findViewById(R.id.textview_message);
        }

        void bindView(@NotNull final LastMessage hiddenMessage,
                      final OnMessageClickListener listener) {
            this.listener = listener;
            this.hiddenMessage = hiddenMessage;
            int badgeCount = hiddenMessage.getUnread();
//            if (badgeCount > 0) {
//                String count = (badgeCount > 99) ? "99+" : String.valueOf(badgeCount);
//                tvCount.setText(count);
//                tvCount.setVisibility(VISIBLE);
//            } else tvCount.setVisibility(GONE);
//            if (hiddenMessage.isSelected()) {
//             //   viewSelected.setVisibility(VISIBLE);
//            } else viewSelected.setVisibility(GONE);
            textViewTitle.setText(hiddenMessage.getMsgTitle());
            textViewContent.setText(hiddenMessage.getMsgContent());
            File profile = StorageUtils.getImage(hiddenMessage.getMsgTitle());
            if (profile != null)
                ImageUtils.loadWithoutCache(profile, imageViewUser);
            else ImageUtils.load(R.drawable.ic_user, imageViewUser);
           // textViewTime.setText(TimeAgo.using(hiddenMessage.getTime()));
            applyClickEvents(getAdapterPosition());
        }

        private void applyClickEvents(final int position) {
            itemView.setOnClickListener(view -> {
                if (isSelecting)
                    toggleSelection(position);
                else {
                    listener.onMessageClick(hiddenMessage, position);
                }
            });

            itemView.setOnLongClickListener(view -> {
                toggleSelection(position);
                vibrate();
                return true;
            });
        }
    }

    public void vibrate() {
        Vibrator v = (Vibrator) MyApplication.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(70);
        }
    }
}