package com.moutamid.whatzboost;

import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class VideoSplitter {

    Context context;

    public VideoSplitter(Context context) {
        this.context = context;
    }

    public void splitVideo(String inputVideoPath, String outputVideoPathPrefix) {
        MediaExtractor extractor = new MediaExtractor();
        MediaMuxer muxer = null;
        MediaCodec decoder = null;
        MediaCodec encoder = null;
        List<String> splitVideoFilePaths = new ArrayList<>();
        try {
            extractor.setDataSource(inputVideoPath);

            // Find and select a video track to extract
            int videoTrackIndex = selectVideoTrack(extractor);
            extractor.selectTrack(videoTrackIndex);
            MediaFormat inputFormat = extractor.getTrackFormat(videoTrackIndex);
            String path = outputVideoPathPrefix + "_%02d.mp4";
            // Create the output video files
            muxer = new MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            splitVideoFilePaths.add(path);
            // Create the decoder
            decoder = MediaCodec.createDecoderByType(inputFormat.getString(MediaFormat.KEY_MIME));
            decoder.configure(inputFormat, null, null, 0);
            decoder.start();

            // Create the encoder
            MediaFormat outputFormat = createOutputFormat(inputFormat);
            encoder = MediaCodec.createEncoderByType(outputFormat.getString(MediaFormat.KEY_MIME));
            encoder.configure(outputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            encoder.start();

            boolean isInputDone = false;
            boolean isOutputDone = false;
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

            while (!isOutputDone) {
                if (!isInputDone) {
                    int inputIndex = decoder.dequeueInputBuffer(0);
                    if (inputIndex >= 0) {
                        ByteBuffer inputBuffer = decoder.getInputBuffer(inputIndex);
                        int sampleSize = extractor.readSampleData(inputBuffer, 0);
                        if (sampleSize < 0) {
                            // End of input video
                            decoder.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            isInputDone = true;
                        } else {
                            long presentationTimeUs = extractor.getSampleTime();
                            decoder.queueInputBuffer(inputIndex, 0, sampleSize, presentationTimeUs, 0);
                            extractor.advance();
                        }
                    }
                }

                int outputIndex = encoder.dequeueOutputBuffer(bufferInfo, 0);
                if (outputIndex >= 0) {
                    ByteBuffer outputBuffer = encoder.getOutputBuffer(outputIndex);
                    muxer.writeSampleData(videoTrackIndex, outputBuffer, bufferInfo);
                    encoder.releaseOutputBuffer(outputIndex, false);

                    if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        isOutputDone = true;
                        shareVideos(splitVideoFilePaths);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Release and clean up resources
//            if (muxer != null) {
//                muxer.stop();
//                muxer.release();
//            }
//
//            if (decoder != null) {
//                decoder.stop();
//                decoder.release();
//            }
//
//            if (encoder != null) {
//                encoder.stop();
//                encoder.release();
//            }

            extractor.release();
        }
    }

    private void shareVideos(List<String> videoFilePaths) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("video/*");

        ArrayList<Uri> videoUris = new ArrayList<>();
        for (String videoFilePath : videoFilePaths) {
            File videoFile = new File(videoFilePath);
            Uri videoUri = FileProvider.getUriForFile(context, "com.moutamid.whatzboost.provider", videoFile);
            videoUris.add(videoUri);
        }

        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, videoUris);
        shareIntent.setPackage("com.whatsapp"); // Specify WhatsApp package name to share via WhatsApp

        // Grant permission to read the shared files
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(shareIntent, "Share videos"));
    }


    private int selectVideoTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/") || mime.equals("video/x-matroska")) {
                return i;
            }
        }
        return -1;
    }

    private MediaFormat createOutputFormat(MediaFormat inputFormat) {
        String inputMime = inputFormat.getString(MediaFormat.KEY_MIME);
        MediaFormat outputFormat;
        if (inputMime.equals("video/x-matroska")) {
            // Output format for MKV files
            outputFormat = MediaFormat.createVideoFormat("video/x-matroska", 1280, 720);
        } else {
            // Output format for MP4 files (default)
            outputFormat = MediaFormat.createVideoFormat("video/avc", 1280, 720);
        }

        outputFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2000000);
        outputFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        return outputFormat;
    }


}

