package com.emo.lkplayer.outerlayer.storage.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.innerlayer.model.entities.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/27/17.
 */

public final class AudioAlbumsSpecification extends BaseLoaderSpecification<Album> {

    private long albumID = -1;

    public AudioAlbumsSpecification()
    {

    }

    public AudioAlbumsSpecification(long id)
    {
        this.albumID = id;
    }

    @Override
    public Uri getUriForLoader()
    {
        return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection()
    {
        String[] projection = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.AlbumColumns.ALBUM,
                MediaStore.Audio.AlbumColumns.ARTIST,
                MediaStore.Audio.AlbumColumns.ALBUM_ART};
        return projection;
    }

    @Override
    public String getSelection()
    {
        if (this.albumID != -1)
        {
            String selection = MediaStore.Audio.Albums._ID + " = ?";
            return selection;
        }
        return null;
    }

    @Override
    public String[] getSelectionArgs()
    {
        if (this.albumID != -1)
        {
            return new String[]{String.valueOf(this.albumID)};
        }
        return null;
    }

    @Override
    public String getSortOrder()
    {
        String sortOrder = MediaStore.Audio.AlbumColumns.ALBUM + " ASC";
        return sortOrder;
    }

    @Override
    public List<Album> returnMappedList(Cursor cursor)
    {
        List<Album> albumList = new ArrayList<>();
        if (cursor.moveToFirst())
        {
            Album newAlbum;
            do
            {
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

    public static final class VideoAlbumSpecification extends BaseLoaderSpecification<Album> {
        private long albumID = -1;

        public VideoAlbumSpecification()
        {

        }

        public VideoAlbumSpecification(long id)
        {
            this.albumID = id;
        }

        @Override
        public Uri getUriForLoader()
        {
            return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        @Override
        public String[] getProjection()
        {
            String[] projection = new String[]{
                    MediaStore.Video.Media.ALBUM,
                    MediaStore.Video.Media.MINI_THUMB_MAGIC,
                    MediaStore.Video.VideoColumns.ARTIST};
            return projection;
        }

        @Override
        public String getSelection()
        {
            if (this.albumID != -1)
            {
                String selection = MediaStore.Audio.Albums._ID + " = ?" +
                        ") GROUP BY (" + MediaStore.Video.Media.ALBUM;
                return selection;
            }
            return null;
        }

        @Override
        public String[] getSelectionArgs()
        {
            if (this.albumID != -1)
            {
                return new String[]{String.valueOf(this.albumID)};
            }
            return null;
        }

        @Override
        public String getSortOrder()
        {
            String sortOrder = MediaStore.Audio.AlbumColumns.ALBUM + " ASC";
            return sortOrder;
        }

        @Override
        public List<Album> returnMappedList(Cursor cursor)
        {
            List<Album> albumList = new ArrayList<>();
            if (cursor.moveToFirst())
            {
                Album newAlbum;
                do
                {
                    String albumTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                    String albumArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.ARTIST));
                    String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC));

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
}
