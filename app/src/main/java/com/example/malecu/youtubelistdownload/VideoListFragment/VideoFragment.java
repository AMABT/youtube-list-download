package com.example.malecu.youtubelistdownload.VideoListFragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.malecu.youtubelistdownload.Domain.Video;
import com.example.malecu.youtubelistdownload.Manager.DownloadList;
import com.example.malecu.youtubelistdownload.Net.OnListChange;
import com.example.malecu.youtubelistdownload.Net.OnListFragmentInteractionListener;
import com.example.malecu.youtubelistdownload.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class VideoFragment extends Fragment implements OnListFragmentInteractionListener {

    // TODO: Customize parameter argument names
    private static final String YT_LIST = "yt-list";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private final String TAG = "VideoFragment";
    private List<Video> videoList = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoFragment() {

        videoList = DownloadList.get().getVideoList();
        Log.i(TAG, "VideoFragment init");
    }

    @SuppressWarnings("unused")
    public static VideoFragment newInstance() {

        VideoFragment fragment = new VideoFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        Log.i(TAG, "Set adapter");
        // Set the adapter
        if (view instanceof RecyclerView) {

            final Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            MyVideoRecyclerViewAdapter adapter = new MyVideoRecyclerViewAdapter(videoList, this);
            recyclerView.setAdapter(adapter);

            // On download finished refresh video list
            DownloadList.get().listenForListChange(adapter);
            // Vibrate too on download finished
            DownloadList.get().listenForListChange(new OnListChange() {
                @Override
                public void notifyListChange() {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(100);
                }
            });
        }
        return view;
    }

    public void setVideoList(List<Video> ytVideoList) {
        videoList = ytVideoList;
    }

    private List<Video> getDummyContent() {

        List<Video> lst = new ArrayList<>();
        String index;

        for (int i = 0; i < 200; i++) {
            index = "video-" + String.valueOf(i);
            lst.add(new Video("http://youtube.com/" + index, index, "Vevo"));
        }

        return lst;
    }

    @Override
    public void onListFragmentInteraction(Video mItem) {

    }
}
