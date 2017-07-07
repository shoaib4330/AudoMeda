package com.emo.lkplayer.model.entities;

/**
 * Created by shoaibanwar on 6/20/17.
 */

public final class AudioTrack implements iPlayable {

    private long trackID;
    private String trackTitle;
    private String artistName;
    private String albumName;
    private long containingAlbumID;
    private long trackDuration;

    public String getTrackArtUri()
    {
        return trackArtUri;
    }

    public void setTrackArtUri(String trackArtUri)
    {
        this.trackArtUri = trackArtUri;
    }

    private String trackArtUri;

    public long getTrackID()
    {
        return trackID;
    }

    public void setTrackID(long trackID)
    {
        this.trackID = trackID;
    }

    public String getTrackTitle()
    {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle)
    {
        this.trackTitle = trackTitle;
    }

    public String getArtistName()
    {
        return artistName;
    }

    public void setArtistName(String artistName)
    {
        this.artistName = artistName;
    }

    public long getContainingAlbumID()
    {
        return containingAlbumID;
    }

    public void setContainingAlbumID(long containingAlbumID)
    {
        this.containingAlbumID = containingAlbumID;
    }

    public String getAlbumName()
    {
        return albumName;
    }

    public void setAlbumName(String albumName)
    {
        this.albumName = albumName;
    }

    public long getTrackDuration()
    {
        return trackDuration;
    }

    public void setTrackDuration(long trackDuration)
    {
        this.trackDuration = trackDuration;
    }


    /* ------ playable interface methods ------- */
    @Override
    public String getTitle()
    {
        return AudioTrack.this.trackTitle;
    }

    @Override
    public String getAlbumTitle()
    {
        return this.albumName;
    }

    @Override
    public String getAssociatedArtPath()
    {
        return this.trackArtUri;
    }

    @Override
    public long getDuration()
    {
        return this.trackDuration;
    }
}
