package com.emo.emomediaplayerpro.model.data_layer.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.Collection;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Provider_Album;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.BaseLoaderSpecification;

import java.util.ArrayList;
import java.util.List;

public class CollectionRepo {
    /* This repo class uses only one data source (as does DAO), because this class replaces a dao,
    and dao's bloated form. A Repo hiding multiple data sources can be used. (That would be a blend,
    blend of 1) Repo pattern avoiding duplicate query logic (plus replacing bloated Dao's)
    + 2) Repo pattern hiding multiple data sources
     */
    private Provider_Album providerAlbum;
    private MutableLiveData<List<Collection>> live_AlbumList = new MutableLiveData<>();

    public CollectionRepo(Context context)
    {
        this.providerAlbum = new Provider_Album(context, new Provider_Album.ProviderCallBacks() {
            @Override
            public void onQueryComplete(List<Collection> collectionList)
            {
                if (collectionList ==null)
                    collectionList = new ArrayList<>();
                live_AlbumList.setValue(collectionList);
            }
        });
    }

    public LiveData<List<Collection>> Query(BaseLoaderSpecification specification)
    {
        /* Receives a specification object, (reusable query logic, avoids bloated Dao's)...*/
        providerAlbum.Query(specification);
        return live_AlbumList;
    }

    public void addAlbum (Collection collection)
    {
        /* I dont add any collection ever, so just confirming to repo design (standard methods in a repo,
        that replaces DAO)...
         */
    }

    public void removeAlbum (Collection collection)
    {
           /* I dont add any collection ever, so just confirming to repo design (standard methods in a repo,
        that replaces DAO)...
         */
    }

    public void updateAlbum (Collection collection)
    {
           /* I dont add any collection ever, so just confirming to repo design (standard methods in a repo,
        that replaces DAO)...
         */
    }
}
