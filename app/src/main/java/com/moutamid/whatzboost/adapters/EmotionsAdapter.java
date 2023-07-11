package com.moutamid.whatzboost.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.whatzboost.R;

public class EmotionsAdapter extends RecyclerView.Adapter<EmotionsAdapter.MyViewHolder> {
    private String[] ascii;
    private Context context;

    public EmotionsAdapter(String[] ascii, Context context) {
        this.ascii = ascii;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listfacis, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.TextFaces.setText(this.ascii[holder.getAbsoluteAdapterPosition()]);

        /*holder.WhatsApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String FACE = ascii[holder.getAbsoluteAdapterPosition()];
                Intent whatsappIntent = new Intent("android.intent.action.SEND");
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra("android.intent.extra.TEXT", FACE);
                try {
                    context.startActivity(whatsappIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        //Share button clicked event
        holder.Share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String FACE = ascii[holder.getAbsoluteAdapterPosition()];
                Intent whatsappIntent = new Intent("android.intent.action.SEND");
                whatsappIntent.setType("text/plain");
                whatsappIntent.putExtra("android.intent.extra.TEXT", FACE);
                try {
                    context.startActivity(whatsappIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Some problems", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (this.ascii == null) ? 0 : this.ascii.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView Share;
        TextView TextFaces;
//        ImageView WhatsApp;

        MyViewHolder(View itemView) {
            super(itemView);
            this.TextFaces = itemView.findViewById(R.id.txt);
//            this.WhatsApp = itemView.findViewById(R.id.whats);
            this.Share = itemView.findViewById(R.id.share);
        }
    }

}
