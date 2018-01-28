package com.emo.emomediaplayerpro.view_ui.viewmodel;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.emo.emomediaplayerpro.model.data_layer.content_providers.AlbumsLoader;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.AudioAlbumsSpecification;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.iLoaderSpecification;
import com.emo.emomediaplayerpro.model.domain.entities.Collection;

import java.util.List;

public final class AlbumsListingViewModel implements AlbumsLoader.MediaProviderEventsListener {

    public interface AlbumsControllerEventsListener {
        void onAlbumListProvision(List<Collection> list);
    }

    private Context context;
    private LoaderManager loaderManager;
    private AlbumsLoader albumsProvider;

    private AlbumsControllerEventsListener albumsControllerEventsListener;

    public AlbumsListingViewModel(Context context, LoaderManager loaderManager)
    {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    public void register(AlbumsControllerEventsListener eventsListener)
    {
        this.albumsControllerEventsListener = eventsListener;
    }

    public void unregister()
    {
        this.albumsControllerEventsListener = null;
    }

    public void retrieveAudioVideoAlbumsAll()
    {
        iLoaderSpecification audioAlbumSpec = new AudioAlbumsSpecification();
        iLoaderSpecification videoAlbumSpec = new AudioAlbumsSpecification.VideoAlbumSpecification();
        albumsProvider = new AlbumsLoader(context,loaderManager,audioAlbumSpec,videoAlbumSpec);
        albumsProvider.register(this);
        albumsProvider.requestTrackData();
    }

    @Override
    public void onListCreated(List<Collection> list)
    {
        if (albumsControllerEventsListener != null)
            albumsControllerEventsListener.onAlbumListProvision(list);
    }

}
