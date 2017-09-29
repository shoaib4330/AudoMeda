package com.emo.lkplayer.innerlayer.model.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.emo.lkplayer.outerlayer.storage.daos.DBConstants;
import com.emo.lkplayer.outerlayer.storage.daos.TypeConverter_IntegerArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public final class Playlist {
    private long id;
    private String name;
    private int numTracks;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getNumTracks()
    {
        return numTracks;
    }

    public void setNumTracks(int numTracks)
    {
        this.numTracks = numTracks;
    }

    @Entity(tableName = DBConstants.TABLE_PLAYLIST_NAME)
    public static final class UserDefinedPlaylist {
        @PrimaryKey
        private String playlistName;
        private int numOfTracks;
        @TypeConverters(TypeConverter_IntegerArray.class)
        private ArrayList<Long> tracksIds;

        @Ignore
        public UserDefinedPlaylist(String playlistName)
        {
            this.playlistName = playlistName;
            this.numOfTracks = 0;
            this.tracksIds = new ArrayList<>();
        }

        public UserDefinedPlaylist(String playlistName,int numOfTracks,ArrayList<Long> tracksIds)
        {
            this.playlistName = playlistName;
            this.numOfTracks = numOfTracks;
            this. tracksIds = tracksIds;
        }

        public String getPlaylistName()
        {
            return this.playlistName;
        }

        public int getNumOfTracks()
        {
            this.numOfTracks = getTracksIds().size();
            return this.numOfTracks;
        }

        public ArrayList<Long> getTracksIds()
        {
            return this.tracksIds;
        }

        @Ignore
        public void addTrack(Long trackID)
        {
            trackID = (long)Integer.valueOf(new DecimalFormat("####").format(trackID));
            if (!this.tracksIds.contains(trackID))
            {
                this.tracksIds.add(trackID);
            }
        }
        @Ignore
        public void removeTrack(long trackID)
        {
            if (this.tracksIds.contains(trackID))
            {
                this.tracksIds.remove(trackID);
            }
        }
    }
}
