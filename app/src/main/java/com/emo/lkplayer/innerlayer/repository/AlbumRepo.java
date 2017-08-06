package com.emo.lkplayer.innerlayer.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.Album;
import com.emo.lkplayer.outerlayer.storage.content_providers.Provider_Album;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;

import java.util.ArrayList;
import java.util.List;

public class AlbumRepo {
    /* This repo class uses only one data source (as does DAO), because this class replaces a dao,
    and dao's bloated form. A Repo hiding multiple data sources can be used. (That would be a blend,
    blend of 1) Repo pattern avoiding duplicate query logic (plus replacing bloated Dao's)
    + 2) Repo pattern hiding multiple data sources
     */
    private Context context;
    private Provider_Album providerAlbum;
    private MutableLiveData<List<Album>> live_AlbumList = new MutableLiveData<>();

    public AlbumRepo(Context context)
    {
        this.context = context.getApplicationContext();
        this.providerAlbum = new Provider_Album(context, new Provider_Album.ProviderCallBacks() {
            @Override
            public void onQueryComplete(List<Album> albumList)
            {
                if (albumList==null)
                    albumList = new ArrayList<>();
                live_AlbumList.setValue(albumList);
            }
        });
    }

    public void addAlbum (Album album)
    {
        /* I dont add any album ever, so just confirming to repo design (standard methods in a repo,
        that replaces DAO)...
         */
    }

    public void removeAlbum (Album album)
    {
           /* I dont add any album ever, so just confirming to repo design (standard methods in a repo,
        that replaces DAO)...
         */
    }

    public void updateAlbum (Album album)
    {
           /* I dont add any album ever, so just confirming to repo design (standard methods in a repo,
        that replaces DAO)...
         */
    }

    public LiveData<List<Album>> Query(BaseLoaderSpecification specification)
    {
        /* Receives a specification object, (reusable query logic, avoids bloated Dao's)...*/
        providerAlbum.Query(specification);
        return live_AlbumList;
    }
}
