package com.example.malecu.youtubelistdownload.Domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by malecu on 05/01/17.
 */

public class Video implements Parcelable {

    protected String url;
    protected String name;
    protected String chanel;
    protected String length;
    protected Quality quality;
    protected String location; // null means default value
    protected VideoStatus status;
    protected String playlist;
    protected String thumbnail;

    public Video() {
        url = null;
        name = null;
        chanel = null;
        length = null;
        quality = Quality.HD;
        location = null;
        status = VideoStatus.READY;
        playlist = null;
        thumbnail = null;
    }

    public Video(String url, String name, String chanel) {
        this.url = url;
        this.name = name;
        this.chanel = chanel;
        this.location = null;
        this.quality = Quality.HD;
        this.length = null;
        this.status = VideoStatus.READY;
        this.playlist = null;
        this.thumbnail = null;
    }

    public String getLength() {
        return length;
    }

    public String getLengthFormatted() {
        try {
            Integer seconds = Integer.parseInt(length);
            Integer minutes = seconds / 60;
            Integer hours = minutes / 60;
            minutes = minutes % 60;
            seconds = seconds % 60;

            if (hours > 0)
                return hours.toString() + ":" + minutes.toString() + ":" + seconds.toString();
            return minutes.toString() + ":" + seconds.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public void setQuality(String value) {
        for (Quality index : Quality.values()) {
            if (index.name().equals(status)) {
                setQuality(index);
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChanel() {
        return chanel;
    }

    public void setChanel(String chanel) {
        this.chanel = chanel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public VideoStatus getStatus() {
        return status;
    }

    public void setStatus(VideoStatus status) {
        this.status = status;
    }

    public void setStatus(String status) {
        for (VideoStatus index : VideoStatus.values()) {
            if (index.name().equals(status)) {
                setStatus(index);
            }
        }
    }

    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public enum VideoStatus {
        READY, DOWNLOADING, DONE, CANCELED;
    }

    public enum Quality {
        SD, HD
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
        dest.writeString(this.chanel);
        dest.writeString(this.length);
        dest.writeInt(this.quality == null ? -1 : this.quality.ordinal());
        dest.writeString(this.location);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeString(this.playlist);
        dest.writeString(this.thumbnail);
    }

    protected Video(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
        this.chanel = in.readString();
        this.length = in.readString();
        int tmpQuality = in.readInt();
        this.quality = tmpQuality == -1 ? null : Quality.values()[tmpQuality];
        this.location = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : VideoStatus.values()[tmpStatus];
        this.playlist = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
