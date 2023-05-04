package com.moutamid.whatzboost.videosplitter;

import android.content.Context;

import java.io.File;

public class UtilCommand {
    public static String CreateFolder(Context context) {
        File[] directory;
        directory = context.getExternalMediaDirs();
        for (File file : directory) {
            if (file.getName().contains(context.getPackageName())) {
                return file.getAbsolutePath();
            }
        }
        return "";
    }
    public static String main(String[] args) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            if (i == (args.length - 1)) {
                sb.append("\"");
                sb.append(args[i]);
                sb.append("\"");
            } else {
                sb.append("\"");
                sb.append(args[i]);
                sb.append("\" ");
            }

        }
        String str = sb.toString();
        System.out.println(str);
        return str;

    }

}
