package com.example.malecu.youtubelistdownload;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        Log.i(TAG, "VideoFragment init");
    }

    @SuppressWarnings("unused")
    public static VideoFragment newInstance(List<Video> ytVideoList) {

        VideoFragment fragment = new VideoFragment();

        fragment.setVideoList(ytVideoList);
//
//        Bundle args = new Bundle();
//        args.putParcelableArray(YT_LIST, (Parcelable[]) ytVideoList.toArray());
//        fragmentgment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //videoList = getArguments().getParcelableArrayList(YT_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        Log.i(TAG, "Set adapter");
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyVideoRecyclerViewAdapter(videoList, this));
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
