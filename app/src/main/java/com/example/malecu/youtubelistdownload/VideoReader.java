package com.example.malecu.youtubelistdownload;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by malecu on 05/01/17.
 */

public class VideoReader implements ResourceReader<List<Video>, JsonReader> {

    private static final String TAG = VideoReader.class.getSimpleName();

    @Override
    public List<Video> read(JsonReader jsonReader) throws IOException, JSONException {
        List<Video> videoList = new ArrayList<>();
        Video video = new Video();
        jsonReader.beginObject();

        while (jsonReader.hasNext()) {

            String name = jsonReader.nextName();
            JsonToken check = jsonReader.peek();

            if (check == JsonToken.NULL) {
                name = "none";
            }

            switch (name) {
                case "webpage_url":
                    video.setUrl(jsonReader.nextString());
                    break;
                case "title":
                    video.setName(jsonReader.nextString());
                    break;
                case "uploader":
                    video.setChanel(jsonReader.nextString());
                    break;
                case "duration":
                    video.setLength(jsonReader.nextString());
                    break;
                case "playlist":
                    video.setPlaylist(jsonReader.nextString());
                    break;
                case "thumbnail":
                    video.setThumbnail(jsonReader.nextString());
                    break;
                case "entries":
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        videoList.add(readVideo(jsonReader));
                    }
                    jsonReader.endArray();
                    break;
                default:
                    jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        if (videoList.size() == 0) {
            videoList.add(video);
        }

        Log.d(TAG, "Return videos");
        return videoList;
    }

    public Video readVideo(JsonReader jsonReader) throws IOException, JSONException {
        Video video = new Video();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "webpage_url":
                    video.setUrl(jsonReader.nextString());
                    break;
                case "title":
                    video.setName(jsonReader.nextString());
                    break;
                case "uploader":
                    video.setChanel(jsonReader.nextString());
                    break;
                case "duration":
                    video.setLength(jsonReader.nextString());
                    break;
                case "playlist":
                    video.setPlaylist(jsonReader.nextString());
                    break;
                case "thumbnail":
                    video.setThumbnail(jsonReader.nextString());
                    break;
                default:
                    jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        Log.d(TAG, "return video " + video.getName());
        return video;
    }
}

