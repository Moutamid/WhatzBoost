package com.moutamid.whatzboost.services;


import static com.moutamid.whatzboost.constants.Constants.FILE_COPIED;
import static com.moutamid.whatzboost.constants.Constants.FILE_EXISTS;
import static com.moutamid.whatzboost.constants.Constants.copyImage;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class FileHelper {

    private static final String TAG = FileHelper.class.getSimpleName();

    public static int copy(File src, File dst) {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            return FILE_COPIED;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FILE_EXISTS;
    }

    public static int copyFile(File inputFile, File outputFolder) {
        if(inputFile.getName().contains("IMG")){
            copyImage(inputFile,outputFolder);
        }
        InputStream in;
        OutputStream out;
        try {

            //create output directory if it doesn't exist
            File dir = outputFolder;
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputFile);
            out = new FileOutputStream(new File(outputFolder, inputFile.getName()));

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file (You have now copied the file)
            out.flush();
            out.close();

            return FILE_COPIED;
        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        return FILE_EXISTS;
    }

    public static int makeCopy(File src, File dst) {
        try {
            try (InputStream in = new FileInputStream(src)) {
                try (OutputStream out = new FileOutputStream(dst)) {
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
            return FILE_COPIED;
        } catch (FileNotFoundException fnfe1) {
            Log.e("TAG", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
        return FILE_EXISTS;
    }

    // Checking method
    public static int moveFileNew(File input, File output) throws IOException {
        try {
            File newFile = new File(output, input.getName());
            FileChannel outputChannel = null;
            FileChannel inputChannel = null;
            try {
                outputChannel = new FileOutputStream(newFile).getChannel();
                inputChannel = new FileInputStream(input).getChannel();
                inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                inputChannel.close();
                return FILE_COPIED;
            } finally {
                if (inputChannel != null) inputChannel.close();
                if (outputChannel != null) outputChannel.close();
            }
        } catch (IOException e) {


        }

        return FILE_EXISTS;
    }

    public static int copyFilebrand(File sourceFile, File destFile) {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            try {
                destFile.createNewFile();
                FileChannel source = new FileInputStream(sourceFile).getChannel();
                FileChannel destination = new FileOutputStream(destFile).getChannel();
                destination.transferTo(0, source.size(), destination);
                source.close();
                destination.close();
                return FILE_COPIED;
            } catch (IOException e) {
//                Logger.logRed("CopyFile Error", "Unable to copy ==> " + e.getMessage());
                Log.e("error", " Unable to copy : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return FILE_EXISTS;
    }

    public void copyNew(File source, File target) {
        String sdCard = Environment.getExternalStorageDirectory().toString();
        // the file to be moved or copied
        File sourceLocation = new File(sdCard + "/sample.txt");
        // make sure your target location folder exists!
        File targetLocation = new File(sdCard + "/MyNewFolder/sample.txt");
        InputStream in = null;
        try {
            in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Copy the bits from instream to outstream

        Log.v(TAG, "Copy file successful.");
    }
}
