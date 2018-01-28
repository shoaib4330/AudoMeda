package com.emo.emomediaplayerpro.model.domain.usecases;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.entities.Playlist;
import com.emo.emomediaplayerpro.model.data_layer.repository.PlaylistRepo;
import com.emo.emomediaplayerpro.model.data_layer.daos.LocalAudioTracksDao;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.AudioTracksSpecification;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.BaseLoaderSpecification;

import java.util.List;

public class Interactor_ProviderTracks {

    private Context context;
    private LocalAudioTracksDao localAudioDao;
    private PlaylistRepo playlistRepo;

    public Interactor_ProviderTracks(Context context)
    {
        this.context = context.getApplicationContext();
        this.localAudioDao = new LocalAudioTracksDao(this.context);
        this.playlistRepo = new PlaylistRepo(context);
    }

    public LiveData<List<AudioTrack>> getAudioTracksAll()
    {
        BaseLoaderSpecification specification = new AudioTracksSpecification();
        return localAudioDao.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksRecentlyAdded()
    {
        BaseLoaderSpecification specification = new AudioTracksSpecification.RecentlyAddedAudioTracksSpecification();
        return localAudioDao.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksListByAlbum(String albumName)
    {
        if (albumName==null)
            throw new IllegalArgumentException("Collection name cannot be null");
        BaseLoaderSpecification specification = new AudioTracksSpecification.AlbumAudioTracksSpecification(albumName);
        return localAudioDao.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksListByArtist(String artistName)
    {
        if (artistName==null)
            throw new IllegalArgumentException("Artsit name cannot be null");
        BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTracksByArtistSpecification(artistName);
        return localAudioDao.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksListByGenre(long genreID)
    {
        if (genreID<0)
            throw new IllegalArgumentException("Nasheed id cannot be negative");
        BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTracksByGenreSpecification(genreID);
        return localAudioDao.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksListFolder(String folderName)
    {
        if (folderName==null)
            throw new IllegalArgumentException("Folder name cannot be null");
        BaseLoaderSpecification specification = new AudioTracksSpecification.FolderAudioTracksSpecification(folderName);
        return localAudioDao.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracks_playlist(String playlistName)
    {
        if (playlistName==null)
            throw new IllegalArgumentException("Playlist name cannot be null");
        Playlist.UserDefinedPlaylist playlist = playlistRepo.QueryPlaylistByname(playlistName);
        if (playlist!=null)
        {
            BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTracksByPlaylistSpecification(playlist);
            return localAudioDao.QueryTracks(specification);
        }
        return null;
    }
}
