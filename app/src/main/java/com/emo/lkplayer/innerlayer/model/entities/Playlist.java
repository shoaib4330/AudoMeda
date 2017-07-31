package com.emo.lkplayer.innerlayer.model.entities;

/**
 * Created by shoaibanwar on 6/20/17.
 */

public final class Playlist {

    private long id;
    private String name;
    private int numTracks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumTracks() {
        return numTracks;
    }

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }
}
