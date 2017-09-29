package com.emo.lkplayer.innerlayer.model.entities;


public class Artist {
    private long artistID;
    private String artistName;
    private int numOfTracks;
    private int numOfAlbums;

    public long getArtistID() {
        return artistID;
    }

    public void setArtistID(long artistID) {
        this.artistID = artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getNumOfTracks() {
        return numOfTracks;
    }

    public void setNumOfTracks(int numOfTracks) {
        this.numOfTracks = numOfTracks;
    }

    public int getNumOfAlbums() {
        return numOfAlbums;
    }

    public void setNumOfAlbums(int numOfAlbums) {
        this.numOfAlbums = numOfAlbums;
    }

}
