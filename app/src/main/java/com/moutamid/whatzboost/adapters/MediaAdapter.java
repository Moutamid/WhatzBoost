package com.moutamid.whatzboost.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.FilseEnum;
import com.moutamid.whatzboost.room.Medias;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaHolder> {

    List<Medias> medias = new ArrayList<>();
    int IMAGE_VIEW = 0;
    int VOICE_NOTES = 1;
    int VIDEO_VIEW = 2;
    int DOC_VIEW = 3;

    SimpleExoPlayer player;
    MediaHolder playingHolder;
    Context context;
    SeekBarUpdater seekBarUpdater;
    Activity activity;

    public MediaAdapter(List<Medias> medias, Context context, Activity activity) {
        Collections.reverse(medias);
        this.medias.clear();
        this.medias = medias;
        this.context = context;
        seekBarUpdater = new SeekBarUpdater();
        this.activity = activity;
    }

    public void setMedias(List<Medias> medias) {
        medias.clear();
        Collections.reverse(medias);
        this.medias = medias;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MediaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_media, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaHolder holder, int pos) {
        Medias val = medias.get(holder.getAbsoluteAdapterPosition());
        String type = val.getType();
      //  Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
        this.playingHolder = holder;
        if (type.equals(FilseEnum.AUDIO.name())) {
            holder.aud_const.setVisibility(View.VISIBLE);
            holder.audi_file_name.setText(val.getName());
            holder.audio_time.setText(val.getTime());
            playAudio(holder, val, holder.getAbsoluteAdapterPosition());

        } else if (type.equals(FilseEnum.IMAGE.name())) {
            holder.img_constriant.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(val.getFile_path())
                    .centerCrop()
                    .into(holder.item_img_view);
            holder.image_file_name_txt.setText(val.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //show image
                    try {
                        Log.d("de_me", "PATH : "+val.getFile_path());
                        Log.d("de_me", "TYPE : "+val.getType());
                        Log.d("de_me", "ENUM : "+FilseEnum.IMAGE.name());
                      /*  Intent intent = new Intent(context, ViewStatusAct.class);
                        intent.putExtra("title",val.getName());
                        intent.putExtra("path",val.getFile_path());
                        context.startActivity(intent);*/
                       openImageorVideo(medias.get(holder.getAbsoluteAdapterPosition()));

                    }catch (Exception e){
                        // openImageorVideo(val);
                    }

                    //openImageorVideo(val);


                }
            });

        } else if (type.equals(FilseEnum.VIDEO.name())) {
            holder.video_const.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).post(() -> {
                Glide.with(holder.itemView.getContext())
                        .asBitmap()
                        .load(Uri.fromFile(new File(val.getFile_path())))
                        .centerCrop()
                        .thumbnail(0.1f)
                        .into(holder.item_video_view);
            });

            /*Picasso.get().load(Uri.parse(val.getFile_path()))

                    .into(holder.item_video_view);*/

            holder.video_file_name.setText(val.getName());
            holder.video_play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("de_check", "onClick: ");
                    openImageorVideo(val);
                   // showInterstitialAds();
                }
            });

        } else if (type.equals(FilseEnum.VOICE_NOTES.name())) {
            holder.aud_const.setVisibility(View.VISIBLE);
            holder.audi_file_name.setText(val.getName());
            holder.audio_time.setText(val.getTime());
            playAudio(holder, val, holder.getAbsoluteAdapterPosition());
        } else {
            holder.doc_const.setVisibility(View.VISIBLE);
            holder.doc_file_name.setText(val.getName());
            holder.doc_time.setText(val.getTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    open_file(val);
                //  showInterstitialAds();
                }
            });
        }

    }
    public void openImageorVideo(Medias val) {
        try {
            Uri uri = Uri.parse(val.getFile_path());
            Log.d("de_me", "openImageorVideo: " + uri.toString());
           // File a = new File(String.valueOf(Uri.parse(val.getFile_path())));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String mime;
            if (val.getType().equals(FilseEnum.IMAGE.name()))
                mime = "image/*";
            else
                mime = "video/*";
            Log.d("de_me", "MEME: " + mime);
            intent.setDataAndType(uri, mime);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);

            /*MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            if (mimeTypeMap.hasExtension(
                    MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {

                mime = mimeTypeMap.getMimeTypeFromExtension(
                        MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

            }*/
//           try {
//                Log.e("de_me", "Mime  " + mime);
//                intent.setDataAndType(uri, mime);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                context.startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//                try {
//                    intent.setDataAndType(uri, "*/*");
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    context.startActivity(intent);
//                } catch (Exception es) {
//                    Toast.makeText(context, "Couldn't find app that open this file ", Toast.LENGTH_SHORT).show();
//                }
//
//            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public void open_file(Medias val) {
        try {

            File a = new File(String.valueOf(Uri.parse(val.getFile_path())));

            Uri uri = Uri.parse(val.getFile_path());
            Intent intent = new Intent(Intent.ACTION_VIEW);

            String mime = "*/*";
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            if (mimeTypeMap.hasExtension(
                    MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {

                mime = mimeTypeMap.getMimeTypeFromExtension(
                        MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

            }
            try {
                Log.e("Mime", mime);
                intent.setDataAndType(uri, mime);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                try {
                    intent.setDataAndType(uri, "*/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);
                } catch (Exception es) {
                    Toast.makeText(context, "Couldn't find app that open this file ", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    boolean playing = false;
    int currentPosPlaying = -1;

    private void playItem(MediaHolder holder, Medias val, int pos) {
        player = new SimpleExoPlayer.Builder(context).build();
        MediaItem item = MediaItem.fromUri(val.getFile_path());
        player.setMediaItem(item);
        player.prepare();
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null & fromUser)
                    player.seekTo(progress * 1000L);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        player.play();
    }

    private void playAudio(MediaHolder holder, Medias val, int pos) {
        try {

            player = new SimpleExoPlayer.Builder(context).build();
            MediaItem item = MediaItem.fromUri(val.getFile_path());
            player.setMediaItem(item);
            player.prepare();
            holder.aud_duration_.setText(String.valueOf(player.getDuration()));
            holder.playerView.setPlayer(player);
            holder.playerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                   try {
                       holder.playerView.getPlayer().stop();
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                }
            });

        } catch (
                Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onViewRecycled(@NonNull MediaHolder holder) {
        super.onViewRecycled(holder);

    }



    @Override
    public int getItemCount() {
        return medias.size();
//        return medias.size();
    }

    private void updateNonPlayingView(MediaHolder holder) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                holder.seekBar.removeCallbacks(seekBarUpdater);
                holder.seekBar.setEnabled(false);
                holder.seekBar.setProgress(0);
                holder.audio_play_btn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
        });

    }

    private void updatePlayingView(MediaHolder holder) {
        try {
            if (playingHolder == null) {
                this.playingHolder = holder;
            }
            playingHolder.seekBar.setMax((int) player.getDuration());
            playingHolder.seekBar.setProgress((int) player.getCurrentPosition());
            playingHolder.seekBar.setEnabled(true);
            if (player.isPlaying()) {
                playingHolder.seekBar.postDelayed(seekBarUpdater, 100);
                playingHolder.audio_play_btn.setImageResource(R.drawable.ic_baseline_pause_24);
            } else {
                playingHolder.seekBar.removeCallbacks(seekBarUpdater);
                playingHolder.audio_play_btn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SeekBarUpdater implements Runnable {
        @Override
        public void run() {
            if (null != playingHolder && null != player) {
                playingHolder.seekBar.setProgress((int) player.getCurrentPosition());
                playingHolder.seekBar.postDelayed(this, 100);
            }
        }
    }

    public class MediaHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

        TextView image_file_name_txt, video_file_name, audi_file_name, doc_file_name;
        TextView audio_time, doc_time, doc_open;
        ImageView item_img_view, item_video_view;
        ImageButton video_play_btn, audio_play_btn;
        SeekBar seekBar;
        TextView aud_duration_;
        ConstraintLayout img_constriant, video_const, aud_const, doc_const;
        DefaultTimeBar defaultTimeBar;
        PlayerView playerView;
        ImageView exo_play_pause;
//        AudioSenseiPlayerView audioSenseiPlayerView;
//        PhonicPlayerView phonicPlayerView;


        public MediaHolder(@NonNull View itemView, int ViewTyp) {
            super(itemView);

            image_file_name_txt = itemView.findViewById(R.id.txt_img_file_name);
            video_file_name = itemView.findViewById(R.id.txt_videeo_file_name);
            audi_file_name = itemView.findViewById(R.id.txt_audio_file_name);
            doc_file_name = itemView.findViewById(R.id.doc_name);
            audio_time = itemView.findViewById(R.id.text_audio_time);
            doc_time = itemView.findViewById(R.id.doc_time_stamp);
            doc_open = itemView.findViewById(R.id.open_doc_text);
            item_img_view = itemView.findViewById(R.id.item_image);
            item_video_view = itemView.findViewById(R.id.item_video_img);
            video_play_btn = itemView.findViewById(R.id.item_video_play);
            audio_play_btn = itemView.findViewById(R.id.audio_play_btn);
            seekBar = itemView.findViewById(R.id.seek_bar);
            aud_duration_ = itemView.findViewById(R.id.exo_duration);
            img_constriant = itemView.findViewById(R.id.item_image_constraint);
            video_const = itemView.findViewById(R.id.item_video_constraint);
            aud_const = itemView.findViewById(R.id.item_audio_constraint);
            doc_const = itemView.findViewById(R.id.item_doc_constraint);
            // defaultTimeBar = itemView.findViewById(R.id.exo_progress);
            playerView = itemView.findViewById(R.id.playerview);
            exo_play_pause = itemView.findViewById(R.id.exo_play);
           // audioSenseiPlayerView = itemView.findViewById(R.id.audio_player);
           // phonicPlayerView = itemView.findViewById(R.id.a_main_audio_player1);
            seekBar.setOnSeekBarChangeListener(this);
            audio_play_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                player.seekTo(progress);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }


        private void PlaySound(File filesound) {

          /*  mediaPlayer = MediaPlayer.create(activity, Uri.parse(String.valueOf(filesound)));

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMediaPlayer();
                }
            });
            mediaPlayer.start();*/
        }

        private void releaseMediaPlayer() {
            if (null != playingHolder) {
                updateNonPlayingView(playingHolder);
            }


            player.release();
            player = null;
            currentPosPlaying = -1;
        }
    }


}
