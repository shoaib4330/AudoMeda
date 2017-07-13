package com.emo.lkplayer.model.content_providers;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.emo.lkplayer.model.content_providers.Specification.AudioAlbumsSpecification;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.model.entities.Album;
import com.emo.lkplayer.model.entities.AudioTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/23/17.
 */

public class TracksProvider implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface MediaProviderEventsListener {
        void onListCreated(List<AudioTrack> pTrackList);
    }

    protected static final int ID_LOADER_ALL_AUDIO_TRACKS = 1;

    protected TracksProvider.MediaProviderEventsListener mediaProviderEventsListener;
    protected boolean dataRequestMade = false;

    protected Context context;
    protected LoaderManager loaderManager;

    protected List<AudioTrack> tracksList;

    protected iLoaderSpecification specification;

    /* shoaib: Old cursor, only kept to be given back to loader when we receive new cursor */
    protected Cursor cursor;

//    public TracksProvider(Context context, LoaderManager loaderManager)
//    {
//        this.context = context;
//        this.loaderManager = loaderManager;
//    }

    public TracksProvider(Context context, LoaderManager loaderManager,iLoaderSpecification audioSpecification)
    {
        this.context = context;
        this.loaderManager = loaderManager;
        this.specification = audioSpecification;
    }

    public void requestTrackData()
    {
        dataRequestMade = true;
        init();
    }

    private void init()
    {
        loaderManager.initLoader(ID_LOADER_ALL_AUDIO_TRACKS, null, this);
    }

    public void register(TracksProvider.MediaProviderEventsListener mediaProviderEventsListener)
    {
        this.mediaProviderEventsListener = mediaProviderEventsListener;
        if (dataRequestMade)
            this.mediaProviderEventsListener.onListCreated(this.tracksList);
    }

    public void unRegister()
    {
        this.mediaProviderEventsListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        CursorLoader cursorLoader = new CursorLoader(context, specification.getUriForLoader(), specification.getProjection(), specification.getSelection(), specification.getSelectionArgs(), specification.getSortOrder());
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        tracksList = specification.returnMappedList(data);
        if (this.mediaProviderEventsListener != null)
            this.mediaProviderEventsListener.onListCreated(tracksList);
        /* swap the cursor */
        this.swapCursor(data, data = cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        swapCursor(null, null);
    }

    private void swapCursor(Cursor cursorNew, Cursor dummy)
    {
        /* shoaib: we keep reference of the old cursor, it will be swapped with new one */
        this.cursor = cursorNew;
    }

    public String getTrackArtUriByID(long albumID)
    {
        iLoaderSpecification mSpec;
        mSpec = new AudioAlbumsSpecification(albumID);
        Cursor c = context.getContentResolver().query(mSpec.getUriForLoader(), mSpec.getProjection(),
                mSpec.getSelection(), mSpec.getSelectionArgs(), null);
        if (((List<Album>) mSpec.returnMappedList(c)).size() > 0)
        {
            Album album = ((List<Album>) mSpec.returnMappedList(c)).get(0);
            return album.getAlbumArtURI();
        }
        return null;
    }

    public static class AudioVideoTracksProvider extends TracksProvider {

        protected static final int ID_LOADER_ALL_VIDEO_TRACKS = 113392;

        private iLoaderSpecification<AudioTrack> audioTrackSpec;
        private iLoaderSpecification<AudioTrack> videoTrackSpec;

        private boolean audioTracksLoaderDone=false;
        private boolean videoTracksLoaderDone=false;

        private List<AudioTrack> audioTrackList;
        private List<AudioTrack> videoTrackList;

        public AudioVideoTracksProvider(Context context, LoaderManager loaderManager,iLoaderSpecification audioTrackSpec,iLoaderSpecification videoTrackSpec)
        {
            super(context, loaderManager,audioTrackSpec);
            this.audioTrackSpec = audioTrackSpec;
            this.videoTrackSpec = videoTrackSpec;
        }

        public void requestTrackData()
        {
            super.requestTrackData();
            init();
        }

        private void init()
        {
            loaderManager.initLoader(ID_LOADER_ALL_VIDEO_TRACKS, null, this);
        }

        public void register(TracksProvider.MediaProviderEventsListener mediaProviderEventsListener)
        {
            super.register(mediaProviderEventsListener);
        }

        public void unRegister()
        {
            this.mediaProviderEventsListener = null;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args)
        {
            if (id==ID_LOADER_ALL_AUDIO_TRACKS)
            {
                CursorLoader cursorLoader = new CursorLoader(context, audioTrackSpec.getUriForLoader(), audioTrackSpec.getProjection(), audioTrackSpec.getSelection(), audioTrackSpec.getSelectionArgs(), audioTrackSpec.getSortOrder());
                return cursorLoader;
            }
            else
            {
                CursorLoader cursorLoader = new CursorLoader(context, videoTrackSpec.getUriForLoader(), videoTrackSpec.getProjection(), videoTrackSpec.getSelection(), videoTrackSpec.getSelectionArgs(), videoTrackSpec.getSortOrder());
                return cursorLoader;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data)
        {
            if (loader.getId()==ID_LOADER_ALL_AUDIO_TRACKS)
            {
                this.audioTracksLoaderDone = true;
                this.audioTrackList = this.audioTrackSpec.returnMappedList(data);
                //this.audioTrackCursor = data;
            }

            if (loader.getId()==ID_LOADER_ALL_VIDEO_TRACKS)
            {
                this.videoTracksLoaderDone = true;
                this.videoTrackList = this.videoTrackSpec.returnMappedList(data);
                //this.videoTrackCursor = data;
            }

            if (this.audioTracksLoaderDone && this.videoTracksLoaderDone)
            {
                tracksList = new ArrayList<AudioTrack>();
                tracksList.addAll(videoTrackList);
                tracksList.addAll(audioTrackList);
                if (this.mediaProviderEventsListener != null)
                    this.mediaProviderEventsListener.onListCreated(tracksList);
            }
            /* swap the cursor */
            this.swapCursor(data, data = cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader)
        {
            if (loader.getId()==ID_LOADER_ALL_AUDIO_TRACKS)
                this.audioTracksLoaderDone = false;
            if (loader.getId()==ID_LOADER_ALL_VIDEO_TRACKS)
                this.videoTracksLoaderDone = false;

            swapCursor(null, null);
        }

        private void swapCursor(Cursor cursorNew, Cursor dummy)
        {
            /* shoaib: we keep reference of the old cursor, it will be swapped with new one */
            this.cursor = cursorNew;
        }
    }
}
