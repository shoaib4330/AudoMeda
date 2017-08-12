package com.emo.lkplayer.middlelayer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.emo.lkplayer.innerlayer.interactors.Interactor_GetAlbumDetails;
import com.emo.lkplayer.innerlayer.model.entities.Album;

import java.util.List;

public class ArtFragViewModel extends AndroidViewModel {

    private Interactor_GetAlbumDetails interactor_getAlbumDetails;

    public ArtFragViewModel(Application application)
    {
        super(application);
        this.interactor_getAlbumDetails = new Interactor_GetAlbumDetails(application);
    }

    public LiveData<List<Album>> getTrackAlbum(long albumID)
    {
        return interactor_getAlbumDetails.albumByID(albumID);
    }
}
