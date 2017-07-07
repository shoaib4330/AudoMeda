package com.emo.lkplayer.model.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.model.entities.AudioTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 7/6/17.
 */

public final class TracksByGenreSpecification extends BaseLoaderSpecification<AudioTrack> {

    private long genreID;

    private Uri URI_GENRE_MEMBER_TRACKS;

    public TracksByGenreSpecification(long genreID){
        this.genreID = genreID;
        this.URI_GENRE_MEMBER_TRACKS = MediaStore.Audio.Genres.Members.getContentUri("external",this.genreID);
    }

    @Override
    public Uri getUriForLoader()
    {
        return this.URI_GENRE_MEMBER_TRACKS;
    }

    @Override
    public String[] getProjection()
    {
        String[] projection = new String[]{
                MediaStore.Audio.Genres.Members._ID,
                MediaStore.Audio.Genres.Members.ARTIST,
                MediaStore.Audio.Genres.Members.DURATION,
                MediaStore.Audio.Genres.Members.TITLE,
                MediaStore.Audio.Genres.Members.ALBUM_ID,
                MediaStore.Audio.Genres.Members.ALBUM
        };
        return projection;
    }

    @Override
    public String getSelection()
    {
        return null;
    }

    @Override
    public String[] getSelectionArgs()
    {
        return null;
    }

    @Override
    public String getSortOrder()
    {
        String sortOrder = MediaStore.Audio.Genres.Members.TITLE + " ASC";
        return sortOrder;
    }

    @Override
    public List<AudioTrack> returnMappedList(Cursor cursor)
    {
        List<AudioTrack> trackList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            AudioTrack newAudioTrack;
            do {
                long trackID        = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres.Members._ID));
                String trackTitle   = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.TITLE));
                String trackArtist  = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.ARTIST));
                String albumName    = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.ALBUM));
                long trackDuration  = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.DURATION));
                long trackAlbumID   = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.ALBUM_ID));

                newAudioTrack = new AudioTrack();

                newAudioTrack.setTrackID(trackID);
                newAudioTrack.setTrackTitle(trackTitle);
                newAudioTrack.setArtistName(trackArtist);
                newAudioTrack.setAlbumName(albumName);
                newAudioTrack.setTrackDuration(trackDuration);
                newAudioTrack.setContainingAlbumID(trackAlbumID);

                trackList.add(newAudioTrack);
            }
            while (cursor.moveToNext());
        }
        return trackList;
    }
}
