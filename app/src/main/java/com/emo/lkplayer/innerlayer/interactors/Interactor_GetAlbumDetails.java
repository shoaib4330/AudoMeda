package com.emo.lkplayer.innerlayer.interactors;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.Album;
import com.emo.lkplayer.innerlayer.repository.AlbumRepo;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioAlbumsSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;

import java.util.List;

/**
 * Created by shoaibanwar on 8/4/17.
 */

public class Interactor_GetAlbumDetails {

    private Context context;
    private AlbumRepo albumRepo;

    public Interactor_GetAlbumDetails(Context context)
    {
        this.context = context.getApplicationContext();
        this.albumRepo = new AlbumRepo(context);
    }

    public LiveData<List<Album>> albumByID (long albumID)
    {
        BaseLoaderSpecification albumByIDSpec = new AudioAlbumsSpecification.AudioAlbumByIdSpecification(albumID);
        return this.albumRepo.Query(albumByIDSpec);
    }

    public Album albumByName (String albumName)
    {
        return null;
    }
}
