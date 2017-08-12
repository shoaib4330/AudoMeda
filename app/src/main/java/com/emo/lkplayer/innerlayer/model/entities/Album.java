package com.emo.lkplayer.innerlayer.model.entities;

public final class Album {
    private long albumID;
    private String albumTitle;
    private String albumArtist;
    private String albumArtURI;

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getAlbumArtURI() {
        return albumArtURI;
    }

    public void setAlbumArtURI(String albumArtURI) {
        this.albumArtURI = albumArtURI;
    }

}
