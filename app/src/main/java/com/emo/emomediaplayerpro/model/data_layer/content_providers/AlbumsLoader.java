package com.emo.emomediaplayerpro.model.data_layer.content_providers;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.iLoaderSpecification;
import com.emo.emomediaplayerpro.model.domain.entities.Collection;

import java.util.ArrayList;
import java.util.List;



public final class AlbumsLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface MediaProviderEventsListener {
        void onListCreated(List<Collection> pAlbumsList);
    }

    private AlbumsLoader.MediaProviderEventsListener mediaProviderEventsListener;

    private static final int ID_LOADER_ALBUMS_AUDIO = 4419;
    //private static final int ID_LOADER_ALBUMS_VIDEO = 9879;

    private boolean dataRequestMade = false;

    private Context context;
    private LoaderManager loaderManager;

    private List<Collection> collectionList;
    private List<Collection> audioAlbumsList;
    private List<Collection> videoAlbumsList;
    private boolean audioAlbumsLoaderDone = false;
    private boolean videoAlbumsLoaderDone = false;
    private iLoaderSpecification audioAlbumsSpecification;
    private iLoaderSpecification videoAlbumsSpecification;

    /* shoaib: Old cursor, only kept to be given back to loader when we receive new cursor */
    private Cursor cursor;

    public AlbumsLoader(Context context, LoaderManager loaderManager, iLoaderSpecification audioAlbumsSpecification, iLoaderSpecification videoAlbumsSpecification)
    {
        this.context = context;
        this.loaderManager = loaderManager;
        this.audioAlbumsSpecification = audioAlbumsSpecification;
        this.videoAlbumsSpecification = videoAlbumsSpecification;
    }

    public void requestTrackData()
    {
        dataRequestMade = true;
        init();
    }

    private void init()
    {
        loaderManager.initLoader(ID_LOADER_ALBUMS_AUDIO, null, this);
        //loaderManager.initLoader(ID_LOADER_ALBUMS_VIDEO, null, this);
    }

    public void register(AlbumsLoader.MediaProviderEventsListener mediaProviderEventsListener)
    {
        this.mediaProviderEventsListener = mediaProviderEventsListener;
        if (dataRequestMade)
            this.mediaProviderEventsListener.onListCreated(this.collectionList);
    }

    public void unRegister()
    {
        this.mediaProviderEventsListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        CursorLoader cursorLoader;
        if (id == ID_LOADER_ALBUMS_AUDIO)
            cursorLoader = new CursorLoader(context, audioAlbumsSpecification.getUriForLoader(), audioAlbumsSpecification.getProjection(), audioAlbumsSpecification.getSelection(), audioAlbumsSpecification.getSelectionArgs(), audioAlbumsSpecification.getSortOrder());
        else
            cursorLoader = new CursorLoader(context, videoAlbumsSpecification.getUriForLoader(), videoAlbumsSpecification.getProjection(), videoAlbumsSpecification.getSelection(), videoAlbumsSpecification.getSelectionArgs(), videoAlbumsSpecification.getSortOrder());
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if (loader.getId() == ID_LOADER_ALBUMS_AUDIO)
        {
            this.audioAlbumsLoaderDone = true;
            this.audioAlbumsList = audioAlbumsSpecification.returnMappedList(data);
        }
//        else if (loader.getId() == ID_LOADER_ALBUMS_VIDEO)
//        {
//            this.videoAlbumsLoaderDone = true;
//            this.videoAlbumsList = videoAlbumsSpecification.returnMappedList(data);
//        }

        //if (this.audioAlbumsLoaderDone && this.videoAlbumsLoaderDone)
        {
            collectionList = new ArrayList<>();
            collectionList.addAll(this.audioAlbumsList);
        //    collectionList.addAll(this.videoAlbumsList);
            if (this.mediaProviderEventsListener != null)
                this.mediaProviderEventsListener.onListCreated(collectionList);
        }
        /* swap the cursor */
        this.swapCursor(data, data = cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        if (loader.getId() == ID_LOADER_ALBUMS_AUDIO)
            this.audioAlbumsLoaderDone = false;
//        if (loader.getId() == ID_LOADER_ALBUMS_VIDEO)
//            this.videoAlbumsLoaderDone = false;

        swapCursor(null, null);
    }

    private void swapCursor(Cursor cursorNew, Cursor dummy)
    {
        /* shoaib: we keep reference of the old cursor, it will be swapped with new one */
        this.cursor = cursorNew;
    }
}
