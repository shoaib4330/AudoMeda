package com.emo.emomediaplayerpro.model.domain.usecases;

import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.entities.Playlist;
import com.emo.emomediaplayerpro.model.data_layer.repository.PlaylistRepo;

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
