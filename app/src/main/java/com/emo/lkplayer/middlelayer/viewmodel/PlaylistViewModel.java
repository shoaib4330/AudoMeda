package com.emo.lkplayer.middlelayer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.emo.lkplayer.innerlayer.interactors.Interactor_AddRemovePlaylist;
import com.emo.lkplayer.innerlayer.interactors.Interactor_GetPlaylistList;
import com.emo.lkplayer.innerlayer.interactors.Interactor_ModifyPlaylist;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;

import java.util.List;

public final class PlaylistViewModel extends AndroidViewModel {

    private Interactor_GetPlaylistList interactorGetPlaylistList;
    private LiveData<List<Playlist.UserDefinedPlaylist>> userDefinedPlaylists = null;

    public PlaylistViewModel(Application application)
    {
        super(application);
        interactorGetPlaylistList = new Interactor_GetPlaylistList(application.getApplicationContext());
    }

    public LiveData<List<Playlist.UserDefinedPlaylist>> getUserDefinedPlaylists()
    {
        if (userDefinedPlaylists==null)
        {
            this.userDefinedPlaylists = interactorGetPlaylistList.getPlaylistsList();
        }
        return userDefinedPlaylists;
    }

    public void addNewPlayList (String playlistName)
    {
       new Interactor_AddRemovePlaylist(this.getApplication()).createNewPlaylist(playlistName);
    }

    public void addAudioTrackToPlaylist(AudioTrack audioTrack,String playListName)
    {
        new Interactor_ModifyPlaylist(this.getApplication()).addTrackToPlaylist(playListName,audioTrack);
    }

    public void deleteTrackFromPlaylist(AudioTrack audioTrack,String playListName)
    {
        new Interactor_ModifyPlaylist(this.getApplication()).deleteTrackFromPlaylist(playListName,audioTrack);
    }
}
