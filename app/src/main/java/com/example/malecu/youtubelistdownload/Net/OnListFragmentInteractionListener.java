package com.example.malecu.youtubelistdownload.Net;

/**
 * Created by malecu on 05/01/17.
 */

import com.example.malecu.youtubelistdownload.Domain.Video;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the context and potentially other fragments contained in that
 * context.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnListFragmentInteractionListener {


    void onListFragmentInteraction(Video mItem);
}
