package com.emo.lkplayer.innerlayer.interactors;

import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.innerlayer.repository.PlaylistRepo;
import com.emo.lkplayer.outerlayer.storage.AppDatabase;

/**
 * Created by shoaibanwar on 8/4/17.
 */

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
