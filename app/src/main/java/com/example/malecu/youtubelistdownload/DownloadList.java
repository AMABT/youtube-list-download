package com.example.malecu.youtubelistdownload;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by malecu on 06/01/17.
 */
public class DownloadList {

    private static DownloadList instance;
    private Activity activity;
    private List<Video> videoList;
    protected YtVideoHelper ytVideoHelper = null;
    protected YtRestClient ytRestClient = null;
    protected Cancellable cancellable = null;
    protected String TAG = DownloadList.class.getCanonicalName();
    protected List<OnListChange> onListChangeList = new ArrayList<>();

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

    public void init(Activity applicationContext) {
        activity = applicationContext;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public YtVideoHelper getYtVideoHelper() {
        if (ytVideoHelper == null)
            ytVideoHelper = new YtVideoHelper(activity);
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

        video.setStatus(Video.VideoStatus.DOWNLOADING);

        cancellable = ytRestClient.downloadVideoAudio(video.getUrl(), new OnSuccessListener<InputStream>() {
            @Override
            public void onSuccess(InputStream value) {

                // Save mp3 to file
                getYtVideoHelper().writeInputStreamToFile(video.getName() + ".mp3", value, video.getPlaylist() != null ? video.getPlaylist() : null);

                video.setStatus(Video.VideoStatus.DONE);
                // notify adapter to update view
                videoListChanged();

                if (index == videoList.size() - 1) {
                    onSuccessListener.onSuccess(true);
                }

                // download next video
                if (videoList.size() - 1 > index) {
                    downloadVideoList(onSuccessListener, onErrorListener, index + 1);
                }
            }
        }, onErrorListener);
    }

    public void listenForListChange(OnListChange onListChange) {
        onListChangeList.add(onListChange);
    }

    public void videoListChanged() {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnListChange onListChange : onListChangeList) {
                    onListChange.notifyListChange();
                }
            }
        });
    }

    public void cancelDownload() {
        cancellable.cancel();
    }
}
