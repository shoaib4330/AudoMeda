package com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 7/11/17.
 */

public class VideoTracksSpecification extends BaseLoaderSpecification<AudioTrack> {

    protected static Uri uriString = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

    @Override
    public Uri getUriForLoader()
    {
        return uriString;
    }

    @Override
    public String[] getProjection()
    {
        String[] projection = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.ARTIST,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.VideoColumns.ALBUM};
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
        return MediaStore.Video.VideoColumns.TITLE + " ASC";
    }

    @Override
    public List<AudioTrack> returnMappedList(Cursor cursor)
    {

        List<AudioTrack> trackList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            AudioTrack newAudioTrack;
            do {
                long trackID        = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                String albumName    = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.ALBUM));
                String trackTitle   = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE));
                String trackArtist  = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.ARTIST));
                long trackDuration  = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));

                newAudioTrack = new AudioTrack();

                newAudioTrack.setTrackID(trackID);
                newAudioTrack.setAlbumName(albumName);
                newAudioTrack.setTrackTitle(trackTitle);
                newAudioTrack.setArtistName(trackArtist);
                newAudioTrack.setTrackDuration(trackDuration);
                newAudioTrack.setTrackType(AudioTrack.TRACK_TYPE_VIDEO);
                //newAudioTrack.setContainingAlbumID();
                trackList.add(newAudioTrack);
            }
            while (cursor.moveToNext());
        }
        return trackList;
        //Log.d("VideoTrackSpecification","returnMappedList(Cursor cursor) not implemented");
    }

    /*----------------- Class For VideoTracks Recently Added-----------------------*/
    public static final class RecentlyAddedVideoTracksSpecification extends VideoTracksSpecification {

        private static final int RECENT_TRACK_HOURS_THRESHOLD = 88;

        public RecentlyAddedVideoTracksSpecification()
        {
        }

        /* Only this Method was required to be overridden for this Specification*/
        @Override
        public List<AudioTrack> returnMappedList(Cursor cursor)
        {
            List<AudioTrack> trackList = new ArrayList<>();
            if (cursor.moveToFirst())
            {
                AudioTrack newAudioTrack;
                do
                {
                    long timeinSeconds = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                    long currentTimeInSeconds = System.currentTimeMillis() / 1000;

                    long timeDiff = currentTimeInSeconds - timeinSeconds;
                    timeDiff = timeDiff / 3600;

                    if (timeDiff <= RECENT_TRACK_HOURS_THRESHOLD)
                    {
                        long trackID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                        String trackTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String trackArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        long trackDuration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        long trackAlbumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));

                        newAudioTrack = new AudioTrack();

                        newAudioTrack.setTrackID(trackID);
                        newAudioTrack.setTrackTitle(trackTitle);
                        newAudioTrack.setArtistName(trackArtist);
                        newAudioTrack.setTrackDuration(trackDuration);
                        newAudioTrack.setContainingAlbumID(trackAlbumID);
                        newAudioTrack.setTrackType(AudioTrack.TRACK_TYPE_VIDEO);

                        trackList.add(newAudioTrack);
                    }
                }
                while (cursor.moveToNext());
            }
            return trackList;
        }
    }

    /*----------------- Class For VideoTracks from a specific Folder-----------------------*/
    public static final class FolderVideoTracksSpecification extends VideoTracksSpecification {

        private String folderToQueryTracksFor;

        public FolderVideoTracksSpecification(String folderToQueryTracksFor)
        {
            this.folderToQueryTracksFor = folderToQueryTracksFor;
        }

        @Override
        public String getSelection()
        {
            String selection = MediaStore.Video.Media.DATA + " LIKE ? ";
            return selection;
        }

        @Override
        public String[] getSelectionArgs()
        {
            String[] selectionArgs = new String[]{"%" + folderToQueryTracksFor + "%"};
            return selectionArgs;
        }
    }

    /*----------------- Class For VideoTracks of a specific Collection-----------------------*/
    public static class AlbumVideoTracksSpecification extends VideoTracksSpecification {

        private String albumName = null;

        public AlbumVideoTracksSpecification(String albumName)
        {
            this.albumName = albumName;
        }

        @Override
        public String getSelection()
        {
            String selection = MediaStore.Video.Media.ALBUM + " = ?";
            return selection;
        }

        @Override
        public String[] getSelectionArgs()
        {
            String[] selArgs = new String[]{this.albumName};
            return selArgs;
        }

    }

    /*----------------- Class For AudioTracks of a specific NaatKhawan-----------------------*/
    public static final class VideoTracksByArtistSpecification extends VideoTracksSpecification {

        private String artistName;

        public VideoTracksByArtistSpecification(String artistName)
        {
            this.artistName = artistName;
        }

        @Override
        public String getSelection()
        {
            String selection = MediaStore.Video.Media.ARTIST + " = ?";
            return selection;

        }

        @Override
        public String[] getSelectionArgs()
        {
            return new String[]{this.artistName};
        }

    }


    /*---------------- SPECIFICATIONS FOR DELETION AND ETCETRA --------------------- */
    public static final class VideoTrackDeletionSpecification extends VideoTracksSpecification {

        private long trackID;

        public VideoTrackDeletionSpecification(long trackID)
        {
            this.trackID = trackID;
        }

        @Override
        public String getSelection()
        {
            String selection = MediaStore.Audio.Media._ID + " = ? ";
            return selection;
        }

        @Override
        public String[] getSelectionArgs()
        {
            String[] selectionArgs = new String[]{String.valueOf(this.trackID)};
            return selectionArgs;
        }
    }
}
