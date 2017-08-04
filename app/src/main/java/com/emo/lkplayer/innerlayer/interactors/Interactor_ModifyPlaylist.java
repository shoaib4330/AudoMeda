package com.emo.lkplayer.innerlayer.interactors;

import android.arch.persistence.room.ColumnInfo;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.innerlayer.repository.PlaylistRepo;
import com.emo.lkplayer.outerlayer.storage.AppDatabase;

/**
 * Created by shoaibanwar on 8/4/17.
 */

public class Interactor_ModifyPlaylist {

    private Context context;
    private PlaylistRepo playlistRepo;

    public Interactor_ModifyPlaylist(Context context)
    {
        this.context = context.getApplicationContext();
        this.playlistRepo = new PlaylistRepo(context);
    }

    public void addTrackToPlaylist(String playListName, AudioTrack audioTrack)
    {
        Playlist.UserDefinedPlaylist playlist = playlistRepo.QueryPlaylistByname(playListName);
        playlist.addTrack(audioTrack.getTrackID());
        playlistRepo.updatePlaylist(playlist);
    }

    public void deleteTrackFromPlaylist(String playListName, AudioTrack audioTrack)
    {
        if (playListName==null || playListName.isEmpty() || audioTrack==null)
            return;
        Playlist.UserDefinedPlaylist playlist = playlistRepo.QueryPlaylistByname(playListName);
        playlist.removeTrack(audioTrack.getTrackID());
        playlistRepo.updatePlaylist(playlist);
    }
}
