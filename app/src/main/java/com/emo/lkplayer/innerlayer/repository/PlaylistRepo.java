package com.emo.lkplayer.innerlayer.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.outerlayer.storage.AppDatabase;
import com.h6ah4i.android.media.audiofx.IEqualizer;

import java.util.ArrayList;
import java.util.List;

public class PlaylistRepo {

    private Context context;

    public PlaylistRepo (Context context)
    {
        this.context = context.getApplicationContext();
    }

    public LiveData<List<Playlist.UserDefinedPlaylist>> QueryPlaylists()
    {
        return AppDatabase.getDatabase(context).playlistModelDao().getUserDefPlaylists();
    }

    public Playlist.UserDefinedPlaylist QueryPlaylistByname(String playlistName)
    {
        return AppDatabase.getDatabase(context).playlistModelDao().getUserDefPlaylistbyName(playlistName);
    }

    public void addPlaylist (String playListName)
    {
        Playlist.UserDefinedPlaylist newPlaylist = new Playlist.UserDefinedPlaylist(playListName);
        AppDatabase.getDatabase(context).playlistModelDao().addNewPlaylist(newPlaylist);
    }

    public void updatePlaylist (Playlist.UserDefinedPlaylist playlist)
    {
        AppDatabase.getDatabase(context).playlistModelDao().addNewPlaylist(playlist);
    }

    public void deletePlaylist(String playListName)
    {
        Playlist.UserDefinedPlaylist playlist = AppDatabase.getDatabase(context).playlistModelDao().getUserDefPlaylistbyName(playListName);
        AppDatabase.getDatabase(context).playlistModelDao().deletePlaylist(playlist);
    }
}
