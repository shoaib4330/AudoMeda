package com.emo.lkplayer.controller;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.emo.lkplayer.model.content_providers.AlbumsProvider;
import com.emo.lkplayer.model.content_providers.Specification.AudioAlbumsSpecification;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.model.content_providers.TracksProvider;
import com.emo.lkplayer.model.entities.Album;
import com.emo.lkplayer.model.entities.AudioTrack;

import java.util.List;

/**
 * Created by shoaibanwar on 7/13/17.
 */

public final class AlbumsController implements AlbumsProvider.MediaProviderEventsListener {

    public interface AlbumsControllerEventsListener {
        void onAlbumListProvision(List<Album> list);
    }

    private Context context;
    private LoaderManager loaderManager;
    private AlbumsProvider albumsProvider;

    private AlbumsControllerEventsListener albumsControllerEventsListener;

    public AlbumsController(Context context,LoaderManager loaderManager)
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
        albumsProvider = new AlbumsProvider(context,loaderManager,audioAlbumSpec,videoAlbumSpec);
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
