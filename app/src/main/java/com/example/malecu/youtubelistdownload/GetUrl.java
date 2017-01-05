package com.example.malecu.youtubelistdownload;

import android.app.Activity;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class GetUrl extends Fragment {

    protected boolean copyFromClipboardEnabled;

    public GetUrl() {
        // Required empty public constructor
        copyFromClipboardEnabled = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_get_url, container, false);


        /**
         * On submit button, get text from clipboard
         * @param v
         */
        final Button submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (copyFromClipboardEnabled) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                    ((TextView) view.findViewById(R.id.yt_url)).setText(clipboard.getText());
                } else {
                    // call to a service that will trigger change
                }
            }
        });

        EditText yt_url = (EditText) view.findViewById(R.id.yt_url);
        yt_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    // reset button
                    copyFromClipboardEnabled = true;
                    submit.setText(getString(R.string.submit_button));
                } else if (copyFromClipboardEnabled) {
                    copyFromClipboardEnabled = false;
                    submit.setText(getString(R.string.submit_button_url));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//    }
}
