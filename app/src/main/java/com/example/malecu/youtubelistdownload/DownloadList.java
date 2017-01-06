package com.example.malecu.youtubelistdownload;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by malecu on 06/01/17.
 */
public class DownloadList {

    private static DownloadList instance;
    private Context context;
    private List<Video> videoList;
    protected YtVideoHelper ytVideoHelper = null;
    protected YtRestClient ytRestClient = null;
    protected Cancellable cancellable = null;
    protected String TAG = DownloadList.class.getCanonicalName();

    public static DownloadList get() {
        if (instance == null) instance = getSync();
        return instance;
    }

    private static synchronized DownloadList getSync() {
        if (instance == null) instance = new DownloadList();
        return instance;
    }

    private DownloadList() {
    }

    public void init(Context applicationContext) {
        context = applicationContext;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public YtVideoHelper getYtVideoHelper() {
        if (ytVideoHelper == null)
            ytVideoHelper = new YtVideoHelper(context);
        return ytVideoHelper;
    }

    public void setYtRestClient(YtRestClient client) {
        ytRestClient = client;
    }

    public void downloadVideoList(final OnSuccessListener<Boolean> onSuccessListener, final OnErrorListener onErrorListener) {
        downloadVideoList(onSuccessListener, onErrorListener, 0);
    }

    public void downloadVideoList(final OnSuccessListener<Boolean> onSuccessListener, final OnErrorListener onErrorListener, final Integer index) {

        final Video video = videoList.get(index);
        final List<Video> videoList = getVideoList();

        Log.i(TAG, "Downloading " + video.getUrl());

        cancellable = ytRestClient.downloadVideoAudio(video.getUrl(), new OnSuccessListener<InputStream>() {
            @Override
            public void onSuccess(InputStream value) {
                // Save mp3 to file
                getYtVideoHelper().writeInputStreamToFile(video.getName() + ".mp3", value);
                onSuccessListener.onSuccess(true);

                if (videoList.size() - 1 < index) {
                    downloadVideoList(onSuccessListener, onErrorListener, index + 1);
                }
            }
        }, onErrorListener);
    }

    public void cancelDownload() {
        cancellable.cancel();
    }
}
