package com.emo.lkplayer.model.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.model.entities.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/30/17.
 */

public final class GenreSpecification extends BaseLoaderSpecification<Genre> {

    private boolean immuted = false;

    @Override
    public Uri getUriForLoader() {
        return MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        String[] projection = new String[]{
                MediaStore.Audio.Genres._ID,
                MediaStore.Audio.Genres.NAME
                //MediaStore.Audio.Genres._COUNT
         };
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
        String sortOrder = MediaStore.Audio.Genres.NAME + " ASC";
        return sortOrder;
    }

    @Override
    public List<Genre> returnMappedList(Cursor cursor) {
        List<Genre> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Genre newGenre;
            do {
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres._ID));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME));

                //int trackCount = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Genres._COUNT));

                newGenre = new Genre();
                newGenre.setId(id);
                newGenre.setGenreName(name);
                //newGenre.setCountTracks(trackCount);

                list.add(newGenre);
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public static class GenreTrackCountSpecification extends BaseLoaderSpecification<Integer>{

        private long genreID;

        public GenreTrackCountSpecification(long genreID){
            this.genreID = genreID;
        }

        private int getTrackCountForGenre(Cursor cursor){
            if (cursor.moveToFirst()){
                int count = cursor.getInt(cursor.getColumnIndex("totalAudios"));
                return count;
            }
            return 0;
        }

        @Override
        public Uri getUriForLoader() {
            return MediaStore.Audio.Genres.Members.getContentUri("external",genreID);
        }

        @Override
        public String[] getProjection() {
            return new String[]{"COUNT(" + MediaStore.Audio.Genres.Members.AUDIO_ID + ") AS totalAudios"};
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
            return MediaStore.Audio.Genres.DEFAULT_SORT_ORDER+" LIMIT 1";
        }

        @Override
        public List<Integer> returnMappedList(Cursor cursor) {
            int count = getTrackCountForGenre(cursor);
            List<Integer> list = new ArrayList<>();
            list.add(count);
            return list;
        }
    }
}
