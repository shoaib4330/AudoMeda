package com.emo.emomediaplayerpro.model.data_layer.content_providers;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.iLoaderSpecification;
import com.emo.emomediaplayerpro.model.domain.entities.NaatKhawan;

import java.util.List;

/**
 * Created by shoaibanwar on 6/30/17.
 */

public final class ArtistsLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface MediaProviderEventsListener {
        void onListCreated(List<NaatKhawan> pTrackList);
    }

    private static final int ID_LOADER_TRACKS = 1;

    private ArtistsLoader.MediaProviderEventsListener mediaProviderEventsListener;
    private boolean dataRequestMade = false;

    private Context context;
    private LoaderManager loaderManager;

    private List<NaatKhawan> naatKhawanList;

    private iLoaderSpecification specification;

    /* shoaib: Old cursor, only kept to be given back to loader when we receive new cursor */
    private Cursor cursor;

    public ArtistsLoader(Context context, LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    public void setSpecification(iLoaderSpecification specification){
        this.specification = specification;
    }

    public void requestTrackData() {
        dataRequestMade = true;
        init();
    }

    private void init() {
        loaderManager.initLoader(ID_LOADER_TRACKS, null, this);
    }

    public void register(ArtistsLoader.MediaProviderEventsListener mediaProviderEventsListener) {
        this.mediaProviderEventsListener = mediaProviderEventsListener;
        if (dataRequestMade)
            this.mediaProviderEventsListener.onListCreated(this.naatKhawanList);
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
        naatKhawanList = specification.returnMappedList(data);
        if (this.mediaProviderEventsListener != null)
            this.mediaProviderEventsListener.onListCreated(naatKhawanList);
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
