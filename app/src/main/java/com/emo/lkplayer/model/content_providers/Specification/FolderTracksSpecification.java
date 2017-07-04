package com.emo.lkplayer.model.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.model.entities.AudioTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/25/17.
 */

public final class FolderTracksSpecification extends BaseLoaderSpecification<AudioTrack> {

    private String folderToQueryTracksFor;
    private boolean immuted = false;

    public void setFolderPathToQueryTracksFor(String folderPath) throws Exception {
        if (!immuted)
        {
            this.folderToQueryTracksFor = folderPath;
            immuted = true;
        }
        else{
            throw new Exception("Immutable paramter (FolderPath): Already Once Set");
        }
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
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID};
        return projection;
    }

    @Override
    public String getSelection() {
        String selection = MediaStore.Audio.Media.DATA + " LIKE ? ";
        return selection;
    }

    @Override
    public String[] getSelectionArgs() {
        String[] selectionArgs = new String[]{"%"+folderToQueryTracksFor+"%"};
        return selectionArgs;
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
                String albumName      = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String trackTitle   = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String trackArtist  = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                long trackDuration  = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long trackAlbumID   = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                newAudioTrack = new AudioTrack();

                newAudioTrack.setTrackID(trackID);
                newAudioTrack.setAlbumName(albumName);
                newAudioTrack.setTrackTitle(trackTitle);
                newAudioTrack.setArtistName(trackArtist);
                newAudioTrack.setTrackDuration(trackDuration);
                newAudioTrack.setContainingAlbumID(trackAlbumID);

                trackList.add(newAudioTrack);
            }
            while (cursor.moveToNext());
        }
        return trackList;
    }
}
