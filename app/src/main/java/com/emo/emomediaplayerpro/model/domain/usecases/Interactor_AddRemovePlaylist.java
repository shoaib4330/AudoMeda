package com.emo.emomediaplayerpro.model.domain.usecases;

import android.content.Context;

import com.emo.emomediaplayerpro.model.data_layer.repository.PlaylistRepo;


public class Interactor_AddRemovePlaylist {

    private Context context;
    private PlaylistRepo playlistRepo;

    public Interactor_AddRemovePlaylist(Context context)
    {
        this.context = context.getApplicationContext();
        this.playlistRepo = new PlaylistRepo(context);
    }

    public void createNewPlaylist(String playlistName)
    {
        if (playlistName==null)
            return;
        playlistRepo.addPlaylist(playlistName);
    }

    public void deletePlaylist (String playlistName)
    {
        if (playlistName==null)
            return;
        playlistRepo.deletePlaylist(playlistName);
    }
}
