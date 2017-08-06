package com.emo.lkplayer.innerlayer.model.entities;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by shoaibanwar on 6/20/17.
 */

public final class AudioTrack implements iPlayable {

    public static final Uri URI_AUDIO_TRACKS = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final Uri URI_VIDEO_TRACKS = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

    public static final String TRACK_TYPE_AUDIO = "audio";
    public static final String TRACK_TYPE_VIDEO = "from_video";

    private long trackID;
    private String trackTitle;
    private String artistName;
    private String albumName;
    private String trackType;
    private long trackDuration;
    private long containingAlbumID;


    public String getTrackType()
    {
        return trackType;
    }

    public void setTrackType(String trackType)
    {
        this.trackType = trackType;
    }

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
        return null;
    }

    @Override
    public long getDuration()
    {
        return this.trackDuration;
    }
}
