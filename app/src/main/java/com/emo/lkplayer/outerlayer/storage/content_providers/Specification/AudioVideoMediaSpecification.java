package com.emo.lkplayer.outerlayer.storage.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;

import java.util.List;

/**
 * Created by shoaibanwar on 7/21/17.
 */

public final class AudioVideoMediaSpecification extends BaseLoaderSpecification {

    private static final Uri uriString = MediaStore.Files.getContentUri("external");

    @Override
    public Uri getUriForLoader()
    {
        return uriString;
    }

    @Override
    public String[] getProjection()
    {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE
        };
        return projection;
    }

    @Override
    public String getSelection()
    {
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        return selection;
    }

    @Override
    public String[] getSelectionArgs()
    {
        return null;
    }

    @Override
    public String getSortOrder()
    {
        return  MediaStore.Files.FileColumns.TITLE + " ASC";
    }

    @Override
    public List<AudioTrack> returnMappedList(Cursor cursor)
    {

//        List<AudioTrack> trackList = new ArrayList<>();
//        if (cursor.moveToFirst())
//        {
//            AudioTrack newAudioTrack;
//            do
//            {
//                long trackID = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
//                String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.ALBUM));
//                String trackTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE));
//                String trackArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.ARTIST));
//                long trackDuration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));
//
//                newAudioTrack = new AudioTrack();
//
//                newAudioTrack.setTrackID(trackID);
//                newAudioTrack.setAlbumName(albumName);
//                newAudioTrack.setTrackTitle(trackTitle);
//                newAudioTrack.setArtistName(trackArtist);
//                newAudioTrack.setTrackDuration(trackDuration);
//                newAudioTrack.setTrackType(AudioTrack.TRACK_TYPE_VIDEO);
//                //newAudioTrack.setContainingAlbumID();
//                trackList.add(newAudioTrack);
//            }
//            while (cursor.moveToNext());
//        }
//        return trackList;
        return null;
    }
}
