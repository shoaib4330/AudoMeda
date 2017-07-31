package com.emo.lkplayer.outerlayer.storage.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.innerlayer.model.entities.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/29/17.
 */

public final class ArtistSpecification extends BaseLoaderSpecification<Artist> {

    @Override
    public Uri getUriForLoader() {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        String[] projection = new String[]{
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS,
                MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS};
        return projection;
    }

    @Override
    public String getSelection() {
        return null;
    }

    @Override
    public String[] getSelectionArgs() {
        return null;
    }

    @Override
    public String getSortOrder() {
        String sortOrder = MediaStore.Audio.Artists.ARTIST + " ASC";
        return sortOrder;
    }

    @Override
    public List<Artist> returnMappedList(Cursor cursor) {
        List<Artist> artistList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Artist newArtist;
            do {
                long artistID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                int numTracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS));
                int numAlbums = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS));

                newArtist = new Artist();

                newArtist.setArtistID(artistID);
                newArtist.setArtistName(artistName);
                newArtist.setNumOfTracks(numTracks);
                newArtist.setNumOfAlbums(numAlbums);

                artistList.add(newArtist);
            }
            while (cursor.moveToNext());
        }
        return artistList;
    }
}
