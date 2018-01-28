package com.emo.emomediaplayerpro.view_ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.emo.emomediaplayerpro.model.domain.usecases.Interactor_AddRemovePlaylist;
import com.emo.emomediaplayerpro.model.domain.usecases.Interactor_GetPlaylistList;
import com.emo.emomediaplayerpro.model.domain.usecases.Interactor_ModifyPlaylist;
import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.entities.Playlist;

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
