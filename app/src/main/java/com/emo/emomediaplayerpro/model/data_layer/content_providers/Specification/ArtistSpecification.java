package com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.emomediaplayerpro.model.domain.entities.NaatKhawan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/29/17.
 */

public final class ArtistSpecification extends BaseLoaderSpecification<NaatKhawan> {

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
    public List<NaatKhawan> returnMappedList(Cursor cursor) {
        List<NaatKhawan> naatKhawanList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            NaatKhawan newNaatKhawan;
            do {
                long artistID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                int numTracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS));
                int numAlbums = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS));

                newNaatKhawan = new NaatKhawan();

                newNaatKhawan.setArtistID(artistID);
                newNaatKhawan.setArtistName(artistName);
                newNaatKhawan.setNumOfTracks(numTracks);
                newNaatKhawan.setNumOfAlbums(numAlbums);

                naatKhawanList.add(newNaatKhawan);
            }
            while (cursor.moveToNext());
        }
        return naatKhawanList;
    }
}
