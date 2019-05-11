package com.ratintech.arrow;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class Arrow {

    private static ArrayList<ArrowDownload> arrowDownloads = new ArrayList<>();

    public static boolean download(ArrowDownload arrowDownload, boolean singular){
        if (!arrowDownloads.contains(arrowDownload)) {
            arrowDownloads.add(arrowDownload);
            if (singular)
                arrowDownload.execute();
            else
                arrowDownload.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return true;
        }
        return false;
    }

    public static boolean cancel(ArrowDownload arrowDownload, boolean mayInterruptIfRunning) {
        return cancel(arrowDownload.getTag(), mayInterruptIfRunning);
    }

    public static boolean cancel(String tag, boolean mayInterruptIfRunning){
        for (ArrowDownload download : arrowDownloads)
            if (download.getTag().equals(tag)) {
                download.cancel(mayInterruptIfRunning);
                arrowDownloads.remove(download);
                return true;
            }
        return false;
    }

    public static void cancelAll(){
        for (ArrowDownload download : arrowDownloads)
            download.cancel(true);
        arrowDownloads.clear();
    }

    public static boolean remove(ArrowDownload arrowDownload){
        return remove(arrowDownload.getTag());
    }

    public static boolean remove(String tag){
       return cancel(tag, true);
    }

    @Nullable
    public static ArrowDownload getDownload(String tag){
        for (ArrowDownload arrowDownload : arrowDownloads)
            if (arrowDownload.getTag().equals(tag)) {
                return arrowDownload;
            }
        return null;
    }
}
