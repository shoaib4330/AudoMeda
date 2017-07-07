package com.emo.lkplayer.model.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.model.entities.AudioTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/26/17.
 */

public final class AllorAlbumTrackSpecification extends BaseLoaderSpecification<AudioTrack> {

    private boolean immuted = false;

    private String albumName = null;

    public AllorAlbumTrackSpecification(){

    }

    public void setAlbumSpec(String albumName){
        this.albumName = albumName;
    }

    @Override
    public Uri getUriForLoader() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
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
    public String getSelection() {
        if (this.albumName!=null)
        {
            String selection = MediaStore.Audio.Media.ALBUM + " = ?";
            return selection;
        }
        return null;
    }

    @Override
    public String[] getSelectionArgs() {
        if (this.albumName!=null)
        {
            String[] selArgs = new String[]{this.albumName};
            return selArgs;
        }
        return null;
    }

    @Override
    public String getSortOrder() {
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        return sortOrder;
    }

    @Override
    public List<AudioTrack> returnMappedList(Cursor cursor) {
        List<AudioTrack> trackList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            AudioTrack newAudioTrack;
            do {
                long trackID        = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String trackTitle   = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String trackArtist  = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String albumName    = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long trackDuration  = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long trackAlbumID   = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

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

    public static class RecentlyAddedTracksSpecification extends BaseLoaderSpecification<AudioTrack> {
        private static final int RECENT_TRACK_HOURS_THRESHOLD = 88;

        private boolean immuted = false;

        private String albumName = null;


        public RecentlyAddedTracksSpecification(){

        }

        public void setAlbumSpec(String albumName){
            this.albumName = albumName;
        }

        @Override
        public Uri getUriForLoader() {
            return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        @Override
        public String[] getProjection() {
            String[] projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Albums._ID,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DATE_ADDED
            };
            return projection;
        }

        @Override
        public String getSelection() {
            if (this.albumName!=null)
            {
                String selection = MediaStore.Audio.Media.ALBUM + " = ?";
                return selection;
            }
            return null;
        }

        @Override
        public String[] getSelectionArgs() {
            if (this.albumName!=null)
            {
                String[] selArgs = new String[]{this.albumName};
                return selArgs;
            }
            return null;
        }

        @Override
        public String getSortOrder() {
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            return sortOrder;
        }

        @Override
        public List<AudioTrack> returnMappedList(Cursor cursor) {
            List<AudioTrack> trackList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                AudioTrack newAudioTrack;
                do {
                    long timeinSeconds   = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                    long currentTimeInSeconds   = System.currentTimeMillis()/1000;

                    long timeDiff = currentTimeInSeconds - timeinSeconds;
                    timeDiff = timeDiff/3600;

                    if (timeDiff <= RECENT_TRACK_HOURS_THRESHOLD){
                        long trackID        = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                        String trackTitle   = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String trackArtist  = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        long trackDuration  = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        long trackAlbumID   = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));

                        newAudioTrack = new AudioTrack();

                        newAudioTrack.setTrackID(trackID);
                        newAudioTrack.setTrackTitle(trackTitle);
                        newAudioTrack.setArtistName(trackArtist);
                        newAudioTrack.setTrackDuration(trackDuration);
                        newAudioTrack.setContainingAlbumID(trackAlbumID);

                        trackList.add(newAudioTrack);
                    }
                }
                while (cursor.moveToNext());
            }
            return trackList;
        }
    }


}
