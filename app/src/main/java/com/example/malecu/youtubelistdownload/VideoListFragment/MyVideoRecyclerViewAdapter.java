package com.example.malecu.youtubelistdownload.VideoListFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.malecu.youtubelistdownload.Domain.Video;
import com.example.malecu.youtubelistdownload.Net.OnListChange;
import com.example.malecu.youtubelistdownload.Net.OnListFragmentInteractionListener;
import com.example.malecu.youtubelistdownload.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Video} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyVideoRecyclerViewAdapter extends RecyclerView.Adapter<MyVideoRecyclerViewAdapter.ViewHolder> implements OnListChange {

    private final List<Video> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyVideoRecyclerViewAdapter(List<Video> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mCanalView.setText(mValues.get(position).getChanel());
        holder.mLengthView.setText(mValues.get(position).getLengthFormatted());
        holder.mStatusView.setText(mValues.get(position).getStatus().name());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the context, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void notifyListChange() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mCanalView;
        public final TextView mLengthView;
        public final TextView mStatusView;
        public Video mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mCanalView = (TextView) view.findViewById(R.id.canal);
            mLengthView = (TextView) view.findViewById(R.id.length);
            mStatusView = (TextView) view.findViewById(R.id.status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
