package com.emo.lkplayer.model.content_providers;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.emo.lkplayer.model.content_providers.Specification.GenreSpecification;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.model.entities.Genre;

import java.util.List;

/**
 * Created by shoaibanwar on 6/30/17.
 */

public final class GenreProvider implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface MediaProviderEventsListener {
        void onListCreated(List<Genre> pTrackList);
    }

    private static final int ID_LOADER_TRACKS = 11;

    private GenreProvider.MediaProviderEventsListener mediaProviderEventsListener;
    private boolean dataRequestMade = false;

    private Context context;
    private LoaderManager loaderManager;

    private List<Genre> genreList;

    private iLoaderSpecification specification;

    /* shoaib: Old cursor, only kept to be given back to loader when we receive new cursor */
    private Cursor cursor;

    public GenreProvider(Context context, LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    public void setSpecification(iLoaderSpecification specification) {
        this.specification = specification;
    }

    public void requestTrackData() {
        dataRequestMade = true;
        init();
    }

    private void init() {
        loaderManager.initLoader(ID_LOADER_TRACKS, null, this);

    }

    public void register(GenreProvider.MediaProviderEventsListener mediaProviderEventsListener) {
        this.mediaProviderEventsListener = mediaProviderEventsListener;
        if (dataRequestMade)
            this.mediaProviderEventsListener.onListCreated(this.genreList);
    }

    public void unRegister() {
        this.mediaProviderEventsListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(context, specification.getUriForLoader(), specification.getProjection(), specification.getSelection(), specification.getSelectionArgs(), specification.getSortOrder());
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == ID_LOADER_TRACKS) {
            genreList = specification.returnMappedList(data);
            if (this.mediaProviderEventsListener != null)
                this.mediaProviderEventsListener.onListCreated(genreList);
        }
        iLoaderSpecification mSpec;
        for (int i=0; i < genreList.size(); i++)
        {
            mSpec       = new GenreSpecification.GenreTrackCountSpecification(genreList.get(i).getId());
            Cursor c    = context.getContentResolver().query(mSpec.getUriForLoader(),mSpec.getProjection(),
                    mSpec.getSelection(),mSpec.getSelectionArgs(),null);
            int count   = (int)mSpec.returnMappedList(c).get(0);
            genreList.get(i).setCountTracks(count);
        }
        /* swap the cursor */
        this.swapCursor(data, data = cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(null, null);
    }

    private void swapCursor(Cursor cursorNew, Cursor dummy) {
        /* shoaib: we keep reference of the old cursor, it will be swapped with new one */
        this.cursor = cursorNew;
    }
}
