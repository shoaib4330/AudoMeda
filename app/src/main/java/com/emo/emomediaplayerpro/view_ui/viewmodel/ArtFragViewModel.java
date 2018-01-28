package com.emo.emomediaplayerpro.view_ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.emo.emomediaplayerpro.model.domain.usecases.Interactor_GetAlbumDetails;
import com.emo.emomediaplayerpro.model.domain.entities.Collection;

import java.util.List;

public class ArtFragViewModel extends AndroidViewModel {

    private Interactor_GetAlbumDetails interactor_getAlbumDetails;

    public ArtFragViewModel(Application application)
    {
        super(application);
        this.interactor_getAlbumDetails = new Interactor_GetAlbumDetails(application);
    }

    public LiveData<List<Collection>> getTrackAlbum(long albumID)
    {
        return interactor_getAlbumDetails.albumByID(albumID);
    }
}
