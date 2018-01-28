package com.emo.emomediaplayerpro.model.domain.usecases;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.Playlist;
import com.emo.emomediaplayerpro.model.data_layer.repository.PlaylistRepo;

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
