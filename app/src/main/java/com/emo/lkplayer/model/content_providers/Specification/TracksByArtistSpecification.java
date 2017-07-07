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

public final class TracksByArtistSpecification extends BaseLoaderSpecification<AudioTrack> {

    private String artistName;

    private Uri URI_ARTIST_TRACKS;

    public TracksByArtistSpecification(String artistName)
    {
        this.artistName = artistName;
        this.URI_ARTIST_TRACKS = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public Uri getUriForLoader()
    {
        return this.URI_ARTIST_TRACKS;
    }

    @Override
    public String[] getProjection()
    {
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM
        };
        return projection;
    }

    @Override
    public String getSelection()
    {
        String selection = MediaStore.Audio.Albums.ARTIST + " = ?";
        return selection;

    }

    @Override
    public String[] getSelectionArgs()
    {
        return new String[]{this.artistName};
    }

    @Override
    public String getSortOrder()
    {
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        return sortOrder;
    }

    @Override
    public List<AudioTrack> returnMappedList(Cursor cursor)
    {
        List<AudioTrack> trackList = new ArrayList<>();
        if (cursor.moveToFirst())
        {
            AudioTrack newAudioTrack;
            do
            {
                long trackID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres.Members._ID));
                String trackTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.TITLE));
                String trackArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.ARTIST));
                String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.ALBUM));
                long trackDuration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.DURATION));
                long trackAlbumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.ALBUM_ID));

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
