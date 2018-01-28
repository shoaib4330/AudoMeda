package com.emo.emomediaplayerpro.model.domain.entities;

import android.net.Uri;
import android.provider.MediaStore;

import java.io.Serializable;


public class AudioTrack implements iPlayable, Serializable {

//    public static class C_RemoteAudioTrack implements iPlayable,Serializable
//    {
//        private long id;
//        private String title;
//        private String trackLink;
//        private String imageLink;
//        private String dateTime_Created;
//        private String dateTime_Updated;
//
//        public long getId()
//        {
//            return id;
//        }
//
//        public void setId(long id)
//        {
//            this.id = id;
//        }
//
//        public String getTitle()
//        {
//            return title;
//        }
//
//        @Override
//        public String getAlbumTitle()
//        {
//            return null;
//        }
//
//        @Override
//        public String getAssociatedArtPath()
//        {
//            return null;
//        }
//
//        @Override
//        public long getDuration()
//        {
//            return 0;
//        }
//
//        public void setTitle(String title)
//        {
//            this.title = title;
//        }
//
//        public String getTrackLink()
//        {
//            return trackLink;
//        }
//
//        public void setTrackLink(String trackLink)
//        {
//            this.trackLink = trackLink;
//        }
//
//        public String getImageLink()
//        {
//            return imageLink;
//        }
//
//        public void setImageLink(String imageLink)
//        {
//            this.imageLink = imageLink;
//        }
//
//        public String getDateTime_Created()
//        {
//            return dateTime_Created;
//        }
//
//        public void setDateTime_Created(String dateTime_Created)
//        {
//            this.dateTime_Created = dateTime_Created;
//        }
//
//        public String getDateTime_Updated()
//        {
//            return dateTime_Updated;
//        }
//
//        public void setDateTime_Updated(String dateTime_Updated)
//        {
//            this.dateTime_Updated = dateTime_Updated;
//        }
//    }

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
