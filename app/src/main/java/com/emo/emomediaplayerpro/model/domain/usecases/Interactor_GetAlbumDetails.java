package com.emo.emomediaplayerpro.model.domain.usecases;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.Collection;
import com.emo.emomediaplayerpro.model.data_layer.repository.CollectionRepo;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.AudioAlbumsSpecification;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.BaseLoaderSpecification;

import java.util.List;



public class Interactor_GetAlbumDetails {

    private Context context;
    private CollectionRepo collectionRepo;

    public Interactor_GetAlbumDetails(Context context)
    {
        this.context = context.getApplicationContext();
        this.collectionRepo = new CollectionRepo(context);
    }

    public LiveData<List<Collection>> albumByID (long albumID)
    {
        BaseLoaderSpecification albumByIDSpec = new AudioAlbumsSpecification.AudioAlbumByIdSpecification(albumID);
        return this.collectionRepo.Query(albumByIDSpec);
    }

    public Collection albumByName (String albumName)
    {
        return null;
    }
}
