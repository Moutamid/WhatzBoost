package com.moutamid.whatzboost.services;

import static android.os.FileObserver.CREATE;
import static android.os.FileObserver.DELETE;
import static android.os.FileObserver.DELETE_SELF;
import static android.os.FileObserver.MOVED_TO;

import android.os.FileObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

import timber.log.Timber;

public class FileObserverHelper {
    private static final String TAG = FileObserverHelper.class.getSimpleName();

    private static int CHANGES_ONLY = CREATE | MOVED_TO | DELETE | DELETE_SELF;

    static FileObserver createObserver(File folderToObserve, ObserverCallBack callBack) {
        // ALL_EVENTS for all events
        // CHANGES_ONLY for specified events
        FileObserver observer = new FileObserver(folderToObserve.getPath(),CHANGES_ONLY) {
            @Override
            public void onEvent(int event, @Nullable String fileName) {
                if (fileName == null)
                    return;
                event &= FileObserver.ALL_EVENTS;
                String path = folderToObserve + File.separator + fileName;
                Timber.i("File path : %s", path);
//                if((FileObserver.ALL_EVENTS & event)!=0){
//                    Timber.i("FileObserver.CREATE : %s", fileName);
//                }
//                switch (event) {
//                    case MODIFY:
//                        Timber.i("Event : MODIFY ");
//                        break;
//                    case ATTRIB:
//                        Timber.i("Event : ATTRIB ");
//                        break;
//                    case CLOSE_WRITE:
//                        Timber.i("Event : CLOSE_WRITE");
//                        break;
//                    case CLOSE_NOWRITE:
//                        Timber.i("Event : CLOSE_NOWRITE ");
//                        break;
//                    case OPEN:
//                        Timber.i("Event : OPEN ");
//                        break;
//                    case MOVED_FROM:
//                        Timber.i("Event : MOVED_FROM ");
//                        break;
//                    case MOVED_TO:
//                        Timber.i("Event : MOVED_TO ");
//                        break;
//                    case DELETE:
//                        Timber.i("Event : DELETE ");
//                        break;
//                    case CREATE:
//                        Timber.i("Event : CREATE");
//                        break;
//                    case DELETE_SELF:
//                        Timber.i("Event : DELETE_SELF ");
//                        break;
//                    case MOVE_SELF:
//                        Timber.i("Event : MOVE_SELF");
//                        break;
//                }
                switch (event) {
                    case CREATE:
                    case MOVED_TO:
                        callBack.onNewFile(path);
                        break;
                    case MOVED_FROM:
                    case DELETE:
                    case DELETE_SELF:
                        callBack.onFileDeleted(path);
                        break;
                }
            }
        };
        observer.startWatching();
        Timber.i("Watching : %s", folderToObserve.getPath());
        return observer;
    }

    public interface ObserverCallBack {
        void onNewFile(@NonNull String newFilePath);
        void onFileDeleted(@NonNull String deletedFilePath);
    }
}
