package com.emo.lkplayer.innerlayer.model.entities;

/**
 * Created by shoaibanwar on 6/30/17.
 */

public final class Genre {

    private long id;
    private String genreName;
    private int countTracks;

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public int getCountTracks() {
        return countTracks;
    }

    public void setCountTracks(int countTracks) {
        this.countTracks = countTracks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
