package com.moutamid.whatzboost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoSplitter {

    Context context;
    String videoUri;
    ProgressDialog progressDialog;

    public VideoSplitter(Context context, String videoUri) {
        this.context = context;
        this.videoUri = videoUri;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Splitting Video");
        progressDialog.setCancelable(false);
    }

    public void splitVideo() {
        progressDialog.show();
        try {
            // Set up the MediaMetadataRetriever to get the video duration
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoUri);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long videoDuration = Long.parseLong(duration);

            // Calculate the number of segments based on the desired duration (29 seconds)
            int numSegments = (int) Math.ceil((double) videoDuration / 29000);

            // Set up the MediaExtractor to extract frames from the video
            MediaExtractor extractor = new MediaExtractor();
            extractor.setDataSource(videoUri);

            // Create a directory to save the split video segments
            // Create a directory to save the split video segments
            File rootDirectory = new File(context.getExternalFilesDir(null), "SplitVideos");
          //  File rootDirectory = new File(Environment.getExternalStorageDirectory(), "SplitVideos");
            if (!rootDirectory.exists()) {
                rootDirectory.mkdirs();
                Log.d("SPLITTER123", "CREATED");
            }

            // Iterate over each segment and extract frames to create the split videos
            for (int i = 0; i < numSegments; i++) {
                long segmentStart = i * 29000;
                long segmentEnd = Math.min(segmentStart + 29000, videoDuration);

                // Set up the MediaMuxer to create the split video
                File folder = new File(rootDirectory, "Split_" + i);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                String outputFilePath = folder.getAbsolutePath() + "/split_video_" + i + ".mp4";
                MediaMuxer muxer = new MediaMuxer(outputFilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

                // Add a new track to the muxer
                int videoTrackIndex = muxer.addTrack(extractor.getTrackFormat(0));

                // Start the muxer
                muxer.start();

                // Select the track and set the sample time range for extraction
                extractor.selectTrack(0);
                extractor.seekTo(segmentStart, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

                // Extract and write the samples to the muxer
                MediaFormat outputFormat = extractor.getTrackFormat(0);
                long frameTime = 0;

                while (frameTime < segmentEnd) {
                    int trackIndex = extractor.getSampleTrackIndex();
                    ByteBuffer inputBuffer = ByteBuffer.allocate(4096);

                    if (trackIndex == videoTrackIndex) {
                        // Extract a sample and write it to the muxer
                        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                        int sampleSize = extractor.readSampleData(inputBuffer, 0);

                        if (sampleSize >= 0) {
                            info.presentationTimeUs = extractor.getSampleTime();
                            info.offset = 0;
                            info.flags = extractor.getSampleFlags();

                            ByteBuffer buffer = ByteBuffer.allocate(sampleSize);
                            extractor.readSampleData(buffer, 0);
                            muxer.writeSampleData(trackIndex, buffer, info);
                            extractor.advance();
                        } else {
                            break;
                        }
                    } else {
                        extractor.advance();
                    }

                    frameTime = extractor.getSampleTime();
                }


                // Release the resources
                extractor.unselectTrack(0);
                muxer.stop();
                muxer.release();
                progressDialog.dismiss();
                // Share the split video on WhatsApp
                shareVideoOnWhatsApp(outputFilePath);
            }

            // Release the MediaMetadataRetriever
            retriever.release();

        } catch (IOException e) {
            progressDialog.dismiss();
            e.printStackTrace();
            Log.d("SPLITTER123", e.getMessage());
        }
    }

    private void shareVideoOnWhatsApp(String videoPath) {
        File videoFile = new File(videoPath);
        Uri videoUri = Uri.fromFile(videoFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");

        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
        shareIntent.setPackage("com.whatsapp");
        context.startActivity(shareIntent);
    }

}
