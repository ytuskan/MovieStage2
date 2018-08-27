package com.app.tuskan.moviestage2.models;

/**
 * Created by Yakup on 29.6.2018.
 */

public class Video {

    private int Id;
    private String videoTitle;
    private String videoKey;

    public Video() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

}
