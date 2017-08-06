package com.emo.lkplayer.outerlayer.storage.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 7/12/17.
 */

public class AudioTracksSpecification extends BaseLoaderSpecification<AudioTrack> {

    public static final Uri AUDIO_CONTENT_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    public AudioTracksSpecification()
    {

    }

    @Override
    public Uri getUriForLoader()
    {
        return AUDIO_CONTENT_URI;
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
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        return sortOrder;
    }

    @Override
    public List<AudioTrack> returnMappedList(Cursor cursor)
    {
        List<AudioTrack> trackList = new ArrayList<>();
        if (cursor == null)
            return trackList;
        if (cursor.moveToFirst())
        {
            AudioTrack newAudioTrack;
            do
            {
                long trackID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String trackTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String trackArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long trackDuration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long trackAlbumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                newAudioTrack = new AudioTrack();

                newAudioTrack.setTrackID(trackID);
                newAudioTrack.setTrackTitle(trackTitle);
                newAudioTrack.setArtistName(trackArtist);
                newAudioTrack.setAlbumName(albumName);
                newAudioTrack.setTrackDuration(trackDuration);
                newAudioTrack.setContainingAlbumID(trackAlbumID);
                newAudioTrack.setTrackType(AudioTrack.TRACK_TYPE_AUDIO);

                trackList.add(newAudioTrack);
            }
            while (cursor.moveToNext());
        }
        return trackList;
    }

    /*----------------- Class For AudioTracks Recently Added-----------------------*/
    public static final class RecentlyAddedAudioTracksSpecification extends AudioTracksSpecification {

        private static final int RECENT_TRACK_HOURS_THRESHOLD = 88;

        public RecentlyAddedAudioTracksSpecification()
        {
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
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DATE_ADDED
            };
            return projection;
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
                        newAudioTrack.setTrackType(AudioTrack.TRACK_TYPE_AUDIO);

                        trackList.add(newAudioTrack);
                    }
                }
                while (cursor.moveToNext());
            }
            return trackList;
        }
    }

    /*----------------- Class For AudioTracks from a specific Folder-----------------------*/
    public static final class FolderAudioTracksSpecification extends AudioTracksSpecification {

        private String folderToQueryTracksFor;

        public FolderAudioTracksSpecification(String folderToQueryTracksFor)
        {
            this.folderToQueryTracksFor = folderToQueryTracksFor;
        }

        @Override
        public String getSelection()
        {
            String selection = MediaStore.Audio.Media.DATA + " LIKE ? ";
            return selection;
        }

        @Override
        public String[] getSelectionArgs()
        {
            String[] selectionArgs = new String[]{"%" + folderToQueryTracksFor + "%"};
            return selectionArgs;
        }
    }

    /*----------------- Class For AudioTracks of a specific Artist-----------------------*/
    public static final class AudioTracksByArtistSpecification extends AudioTracksSpecification {

        private String artistName;

        public AudioTracksByArtistSpecification(String artistName)
        {
            this.artistName = artistName;
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
    }

    /*----------------- Class For AudioTracks of a specific Album-----------------------*/
    public static class AlbumAudioTracksSpecification extends AudioTracksSpecification {

        private String albumName = null;

        public AlbumAudioTracksSpecification(String albumName)
        {
            this.albumName = albumName;
        }

        @Override
        public String getSelection()
        {
            String selection = MediaStore.Audio.Media.ALBUM + " = ?";
            return selection;
        }

        @Override
        public String[] getSelectionArgs()
        {
            String[] selArgs = new String[]{this.albumName};
            return selArgs;
        }

    }

    /*----------------- Class For AudioTracks of a specific Genre-----------------------*/
    public static final class AudioTracksByGenreSpecification extends BaseLoaderSpecification<AudioTrack> {

        private long genreID;

        private Uri URI_GENRE_MEMBER_TRACKS;

        public AudioTracksByGenreSpecification(long genreID)
        {
            this.genreID = genreID;
            this.URI_GENRE_MEMBER_TRACKS = MediaStore.Audio.Genres.Members.getContentUri("external", this.genreID);
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
                    newAudioTrack.setTrackType(AudioTrack.TRACK_TYPE_AUDIO);

                    trackList.add(newAudioTrack);
                }
                while (cursor.moveToNext());
            }
            return trackList;
        }
    }

    public static final class AudioTracksByPlaylistSpecification extends AudioTracksSpecification {

        private String[] selArgArr = new String[1];

        public AudioTracksByPlaylistSpecification(Playlist.UserDefinedPlaylist playlist)
        {
            ArrayList<Long> idList = playlist.getTracksIds();
            selArgArr[0] = Utility.LongArrListToINQueryString(idList);
        }

        @Override
        public String getSelection()
        {
            return MediaStore.Audio.Media._ID + " IN "+selArgArr[0];
        }

        @Override
        public String[] getSelectionArgs()
        {
            return null;
        }

        @Override
        public String getSortOrder()
        {
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            return sortOrder;
        }
    }

    /*---------------- SPECIFICATIONS FOR DELETION AND ETCETRA --------------------- */
    public static final class AudioTrackDeletionSpecification extends AudioTracksSpecification {

        private long trackID;

        public AudioTrackDeletionSpecification(long trackID)
        {
            this.trackID = trackID;
        }

        @Override
        public String[] getProjection()
        {
            String[] projection = new String[]{MediaStore.Audio.Media._ID};
            return projection;
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
