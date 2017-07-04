package com.emo.lkplayer.model.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.model.entities.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/27/17.
 */

public final class AlbumsSpecification extends BaseLoaderSpecification<Album> {

    private boolean immuted = false;

    @Override
    public Uri getUriForLoader() {
        return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        String[] projection = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.AlbumColumns.ALBUM,
                MediaStore.Audio.AlbumColumns.ARTIST,
                MediaStore.Audio.AlbumColumns.ALBUM_ART};
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
        String sortOrder = MediaStore.Audio.AlbumColumns.ARTIST + " ASC";
        return sortOrder;
    }

    @Override
    public List<Album> returnMappedList(Cursor cursor) {
        List<Album> albumList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Album newAlbum;
            do {
                long albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String albumTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM));
                String albumArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ARTIST));
                String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART));

                newAlbum = new Album();

                newAlbum.setAlbumID(albumID);
                newAlbum.setAlbumTitle(albumTitle);
                newAlbum.setAlbumArtist(albumArtist);
                newAlbum.setAlbumArtURI(albumArt);

                albumList.add(newAlbum);
            }
            while (cursor.moveToNext());
        }
        return albumList;
    }
}
