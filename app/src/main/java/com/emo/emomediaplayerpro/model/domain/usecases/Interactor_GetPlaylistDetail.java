package com.emo.emomediaplayerpro.model.domain.usecases;

import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.Playlist;
import com.emo.emomediaplayerpro.model.data_layer.repository.PlaylistRepo;


public class Interactor_GetPlaylistDetail {

    private Context context;
    private PlaylistRepo playlistRepo;

    public Interactor_GetPlaylistDetail(Context context)
    {
        this.context = context.getApplicationContext();
        this.playlistRepo = new PlaylistRepo(context);
    }

    public Playlist.UserDefinedPlaylist detailsForPlaylist(String playlistName)
    {
        if (playlistName==null)
            return null;
        return playlistRepo.QueryPlaylistByname(playlistName);
    }
}
