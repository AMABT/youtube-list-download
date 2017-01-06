package com.example.malecu.youtubelistdownload;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by malecu on 05/01/17.
 */
public interface ResourceReader<E, Reader> {
    E read(Reader reader) throws IOException, JSONException;
}
