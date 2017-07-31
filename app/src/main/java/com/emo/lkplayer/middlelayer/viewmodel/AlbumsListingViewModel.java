package com.emo.lkplayer.middlelayer.viewmodel;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.emo.lkplayer.outerlayer.storage.content_providers.AlbumsLoader;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioAlbumsSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.innerlayer.model.entities.Album;

import java.util.List;

/**
 * Created by shoaibanwar on 7/13/17.
 */

public final class AlbumsListingViewModel implements AlbumsLoader.MediaProviderEventsListener {

    public interface AlbumsControllerEventsListener {
        void onAlbumListProvision(List<Album> list);
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
    public void onListCreated(List<Album> list)
    {
        if (albumsControllerEventsListener != null)
            albumsControllerEventsListener.onAlbumListProvision(list);
    }

}
