package com.emo.lkplayer.model.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.emo.lkplayer.model.entities.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/30/17.
 */

public final class PlaylistSpecification extends BaseLoaderSpecification<Playlist>  {

    private boolean immuted = false;

    @Override
    public Uri getUriForLoader() {
        return MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        String[] projection = new String[]{
                MediaStore.Audio.Playlists._ID,
                MediaStore.Audio.Playlists.NAME};
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
        String sortOrder = MediaStore.Audio.Playlists.NAME + " ASC";
        return sortOrder;
    }

    @Override
    public List<Playlist> returnMappedList(Cursor cursor) {
        List<Playlist> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Playlist newPlaylist;
            do {
                long playlistID     = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID));
                String playlistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME));

                newPlaylist = new Playlist();
                newPlaylist.setId(playlistID);
                newPlaylist.setName(playlistName);
                list.add(newPlaylist);
            }
            while (cursor.moveToNext());
        }
        return list;
    }
}
