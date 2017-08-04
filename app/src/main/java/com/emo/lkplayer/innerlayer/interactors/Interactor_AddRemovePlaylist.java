package com.emo.lkplayer.innerlayer.interactors;

import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.innerlayer.repository.PlaylistRepo;
import com.emo.lkplayer.outerlayer.storage.AppDatabase;

/**
 * Created by shoaibanwar on 8/4/17.
 */

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
