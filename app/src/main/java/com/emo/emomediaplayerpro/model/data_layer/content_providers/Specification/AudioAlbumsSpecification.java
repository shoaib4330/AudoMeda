package com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.emomediaplayerpro.model.domain.entities.Collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/27/17.
 */

public class AudioAlbumsSpecification extends BaseLoaderSpecification<Collection> {

    public AudioAlbumsSpecification()
    {

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
        String sortOrder = MediaStore.Audio.AlbumColumns.ALBUM + " ASC";
        return sortOrder;
    }

    @Override
    public List<Collection> returnMappedList(Cursor cursor)
    {
        List<Collection> collectionList = new ArrayList<>();
        if (cursor.moveToFirst())
        {
            Collection newCollection;
            do
            {
                long albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String albumTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM));
                String albumArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ARTIST));
                String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART));

                newCollection = new Collection();

                newCollection.setAlbumID(albumID);
                newCollection.setAlbumTitle(albumTitle);
                newCollection.setAlbumArtist(albumArtist);
                newCollection.setAlbumArtURI(albumArt);

                collectionList.add(newCollection);
            }
            while (cursor.moveToNext());
        }
        return collectionList;
    }

    public static final class AudioAlbumByIdSpecification extends AudioAlbumsSpecification {

        private long albumID = -1;

        public AudioAlbumByIdSpecification(long albumID)
        {
            this.albumID = albumID;
        }

        @Override
        public String getSelection()
        {
            String selection = MediaStore.Audio.Albums._ID + " = ?";
            return selection;
        }

        @Override
        public String[] getSelectionArgs()
        {
            return new String[]{String.valueOf(this.albumID)};
        }
    }

    public static final class VideoAlbumSpecification extends BaseLoaderSpecification<Collection> {
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
        public List<Collection> returnMappedList(Cursor cursor)
        {
            List<Collection> collectionList = new ArrayList<>();
            if (cursor.moveToFirst())
            {
                Collection newCollection;
                do
                {
                    String albumTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                    String albumArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.ARTIST));
                    String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC));

                    newCollection = new Collection();
                    newCollection.setAlbumID(albumID);
                    newCollection.setAlbumTitle(albumTitle);
                    newCollection.setAlbumArtist(albumArtist);
                    newCollection.setAlbumArtURI(albumArt);

                    collectionList.add(newCollection);
                }
                while (cursor.moveToNext());
            }
            return collectionList;
        }
    }
}
