package com.emo.lkplayer.controller;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.LoaderManager;

import com.emo.lkplayer.model.content_providers.Specification.AudioTracksSpecification;
import com.emo.lkplayer.model.content_providers.Specification.VideoTracksSpecification;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.model.content_providers.TracksProvider;
import com.emo.lkplayer.model.entities.AudioTrack;

import java.util.List;

/**
 * Created by shoaibanwar on 7/12/17.
 */
/* Controller to control/provide and return all content and requests regarding audio/video tracks */
public final class TrackController implements TracksProvider.MediaProviderEventsListener {

    public interface TrackControllerEventsListener {
        void onTrackListProvision(List<AudioTrack> list);
    }

    private Context context;
    private LoaderManager loaderManager;
    private TracksProvider tracksProvider;

    private TrackControllerEventsListener trackControllerEventsListener;

    public TrackController(Context context, LoaderManager loaderManager)
    {
        this.context = context;
        this.loaderManager = loaderManager;
        this.trackControllerEventsListener = null;
    }

    public TrackController(Context context, LoaderManager loaderManager, TrackControllerEventsListener trackControllerEventsListener)
    {
        this.context = context;
        this.loaderManager = loaderManager;
        this.trackControllerEventsListener = trackControllerEventsListener;
    }

    public void register(TrackControllerEventsListener trackControllerEventsListener)
    {
        this.trackControllerEventsListener = trackControllerEventsListener;
    }

    public void unregister()
    {
        this.trackControllerEventsListener = null;
    }

    public void retrieveAudioVideoTracksAll()
    {
        iLoaderSpecification audioSpec = new AudioTracksSpecification();
        iLoaderSpecification videoSpec = new VideoTracksSpecification();
        tracksProvider = new TracksProvider.AudioVideoTracksProvider(context, loaderManager, audioSpec, videoSpec);
        tracksProvider.register(this);
        tracksProvider.requestTrackData();
    }

    public void retrieveAudioVideoTracksRecentlyAdded()
    {
        iLoaderSpecification audioSpec = new AudioTracksSpecification.RecentlyAddedAudioTracksSpecification();
        iLoaderSpecification videoSpec = new VideoTracksSpecification.RecentlyAddedVideoTracksSpecification();
        tracksProvider = new TracksProvider.AudioVideoTracksProvider(context, loaderManager, audioSpec, videoSpec);
        tracksProvider.register(this);
        tracksProvider.requestTrackData();
    }

    public void retrieveAudioVideoTracksByFolder(String folderName)
    {
        iLoaderSpecification audioSpec = new AudioTracksSpecification.FolderAudioTracksSpecification(folderName);
        iLoaderSpecification videoSpec = new VideoTracksSpecification.FolderVideoTracksSpecification(folderName);
        tracksProvider = new TracksProvider.AudioVideoTracksProvider(context, loaderManager, audioSpec, videoSpec);
        tracksProvider.register(this);
        tracksProvider.requestTrackData();
    }

    public void retrieveAudioVideoTracksByAlbum(String albumName)
    {
        iLoaderSpecification audioSpec = new AudioTracksSpecification.AlbumAudioTracksSpecification(albumName);
        iLoaderSpecification videoSpec = new VideoTracksSpecification.AlbumVideoTracksSpecification(albumName);
        tracksProvider = new TracksProvider.AudioVideoTracksProvider(context, loaderManager, audioSpec, videoSpec);
        tracksProvider.register(this);
        tracksProvider.requestTrackData();
    }

    public void retrieveAudioVideoTracksByArtist(String artistName)
    {
        iLoaderSpecification audioSpec = new AudioTracksSpecification.AudioTracksByArtistSpecification(artistName);
        iLoaderSpecification videoSpec = new VideoTracksSpecification.VideoTracksByArtistSpecification(artistName);
        tracksProvider = new TracksProvider.AudioVideoTracksProvider(context, loaderManager, audioSpec, videoSpec);
        tracksProvider.register(this);
        tracksProvider.requestTrackData();
    }

    public void retrieveAudioTracksByGenre(long genreID)
    {
        iLoaderSpecification audioSpec = new AudioTracksSpecification.AudioTracksByGenreSpecification(genreID);
        tracksProvider = new TracksProvider(context, loaderManager, audioSpec);
        tracksProvider.register(this);
        tracksProvider.requestTrackData();
    }

    public List<AudioTrack> retrieveDynamicQueueTracks()
    {
        return null;
    }

    public void deleteTrack(AudioTrack track)
    {
        iLoaderSpecification specification;
        ContentResolver contentResolver = context.getContentResolver();
        if (track.getTrackType().equals(AudioTrack.TRACK_TYPE_AUDIO))
            specification = new AudioTracksSpecification.AudioTrackDeletionSpecification(track.getTrackID());
        else
            specification = new VideoTracksSpecification.VideoTrackDeletionSpecification(track.getTrackID());
        contentResolver.delete(specification.getUriForLoader(),specification.getSelection(),specification.getSelectionArgs());
    }

    @Override
    public void onListCreated(List<AudioTrack> list)
    {
        if (trackControllerEventsListener != null)
            trackControllerEventsListener.onTrackListProvision(list);
    }
}
