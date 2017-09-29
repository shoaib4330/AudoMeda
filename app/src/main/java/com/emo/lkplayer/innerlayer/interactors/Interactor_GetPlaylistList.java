package com.emo.lkplayer.innerlayer.interactors;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.innerlayer.repository.PlaylistRepo;
import com.emo.lkplayer.outerlayer.storage.AppDatabase;

import java.util.List;

public class Interactor_GetPlaylistList {

    private Context context;
    private PlaylistRepo playlistRepo;

    public Interactor_GetPlaylistList(Context context)
    {
        this.context = context.getApplicationContext();
        this.playlistRepo = new PlaylistRepo(context);
    }

    public LiveData<List<Playlist.UserDefinedPlaylist>> getPlaylistsList()
    {
        return playlistRepo.QueryPlaylists();
    }
}
