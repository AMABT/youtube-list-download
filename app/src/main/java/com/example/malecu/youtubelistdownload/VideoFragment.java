package com.example.malecu.youtubelistdownload;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
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
    private static final String YT_URL = "yt-url";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private final String TAG = "VideoFragment";
    private String youtubeUrl = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoFragment() {
        Log.i(TAG, "VideoFragment init");
    }

    @SuppressWarnings("unused")
    public static VideoFragment newInstance(String ytUrl) {

        VideoFragment fragment = new VideoFragment();

        Bundle args = new Bundle();
        args.putString(YT_URL, ytUrl);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            youtubeUrl = getArguments().getString(YT_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        Log.i(TAG, "Current url " + youtubeUrl);
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
            recyclerView.setAdapter(new MyVideoRecyclerViewAdapter(getDummyContent(), this));
        }
        return view;
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
