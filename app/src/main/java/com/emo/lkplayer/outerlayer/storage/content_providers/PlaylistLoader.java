package com.emo.lkplayer.outerlayer.storage.content_providers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.GenreSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 7/1/17.
 */

public final class PlaylistLoader implements LoaderManager.LoaderCallbacks<Cursor>  {

    public interface MediaProviderEventsListener {
        void onListCreated(List<Playlist> pTrackList);
    }

    private static final int ID_LOADER_TRACKS = 22;

    private PlaylistLoader.MediaProviderEventsListener mediaProviderEventsListener;
    private boolean dataRequestMade = false;

    private Context context;
    private LoaderManager loaderManager;

    private List<Playlist> playlistList;

    private iLoaderSpecification specification;

    /* shoaib: Old cursor, only kept to be given back to loader when we receive new cursor */
    private Cursor cursor;

    public PlaylistLoader(Context context, LoaderManager loaderManager) {
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

    public void register(PlaylistLoader.MediaProviderEventsListener mediaProviderEventsListener) {
        this.mediaProviderEventsListener = mediaProviderEventsListener;
        if (dataRequestMade)
            this.mediaProviderEventsListener.onListCreated(this.playlistList);
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
            playlistList = specification.returnMappedList(data);
            if (this.mediaProviderEventsListener != null)
                this.mediaProviderEventsListener.onListCreated(playlistList);
        }
        iLoaderSpecification mSpec;
        for (int i=0; i < playlistList.size(); i++)
        {
            mSpec       = new GenreSpecification.GenreTrackCountSpecification(playlistList.get(i).getId());
            Cursor c    = context.getContentResolver().query(mSpec.getUriForLoader(),mSpec.getProjection(),
                    mSpec.getSelection(),mSpec.getSelectionArgs(),null);
            int count   = (int)mSpec.returnMappedList(c).get(0);
            playlistList.get(i).setNumTracks(count);
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

    public static class PlayListTrackCountSpecification extends BaseLoaderSpecification<Integer>{

        private long plID;

        public PlayListTrackCountSpecification(long plID){
            this.plID = plID;
        }

        private int getTrackCountForPlayList(Cursor cursor){
            if (cursor.moveToFirst()){
                int count = cursor.getInt(cursor.getColumnIndex("totalAudios"));
                return count;
            }
            return 0;
        }

        @Override
        public Uri getUriForLoader() {
            return MediaStore.Audio.Playlists.Members.getContentUri("external",plID);
        }

        @Override
        public String[] getProjection() {
            return new String[]{"COUNT(" + MediaStore.Audio.Playlists.Members.AUDIO_ID + ") AS totalAudios"};
        }

        @Override
        public String getSelection() {
            return null;
        }

        @Override
        public String[] getSelectionArgs() {
            return null;
        }

        @Override
        public String getSortOrder() {
            return MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER+" LIMIT 1";
        }

        @Override
        public List<Integer> returnMappedList(Cursor cursor) {
            int count = getTrackCountForPlayList(cursor);
            List<Integer> list = new ArrayList<>();
            list.add(count);
            return list;
        }
    }
}
