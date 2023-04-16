package com.moutamid.whatzboost.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int duration = 300;
                switch (event.getAction()) {
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
                        Intent intent = new Intent(context, CaptionsActivity.class);
                        intent.putExtra(Constants.NAME, logos[holder.getAdapterPosition()]);
                        intent.putExtra(Constants.POS, holder.getAdapterPosition());
                        context.startActivity(intent);

                        break;
                }
                return true;
            }
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