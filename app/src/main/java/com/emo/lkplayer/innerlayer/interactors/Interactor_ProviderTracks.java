package com.emo.lkplayer.innerlayer.interactors;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.innerlayer.repository.PlaylistRepo;
import com.emo.lkplayer.innerlayer.repository.TrackRepository;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioTracksSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.TracksProvider;

import java.util.List;

public class Interactor_ProviderTracks {

    private Context context;
    private TrackRepository trackRepository;
    private PlaylistRepo playlistRepo;

    public Interactor_ProviderTracks(Context context)
    {
        this.context = context.getApplicationContext();
        this.trackRepository = new TrackRepository(this.context);
        this.playlistRepo = new PlaylistRepo(context);
    }

    public LiveData<List<AudioTrack>> getAudioTracksAll()
    {
        BaseLoaderSpecification specification = new AudioTracksSpecification();
        return trackRepository.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksRecentlyAdded()
    {
        BaseLoaderSpecification specification = new AudioTracksSpecification.RecentlyAddedAudioTracksSpecification();
        return trackRepository.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksListByAlbum(String albumName)
    {
        if (albumName==null)
            throw new IllegalArgumentException("Album name cannot be null");
        BaseLoaderSpecification specification = new AudioTracksSpecification.AlbumAudioTracksSpecification(albumName);
        return trackRepository.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksListByArtist(String artistName)
    {
        if (artistName==null)
            throw new IllegalArgumentException("Artsit name cannot be null");
        BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTracksByArtistSpecification(artistName);
        return trackRepository.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksListByGenre(long genreID)
    {
        if (genreID<0)
            throw new IllegalArgumentException("Genre id cannot be negative");
        BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTracksByGenreSpecification(genreID);
        return trackRepository.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracksListFolder(String folderName)
    {
        if (folderName==null)
            throw new IllegalArgumentException("Folder name cannot be null");
        BaseLoaderSpecification specification = new AudioTracksSpecification.FolderAudioTracksSpecification(folderName);
        return trackRepository.QueryTracks(specification);
    }

    public LiveData<List<AudioTrack>> getAudioTracks_playlist(String playlistName)
    {
        if (playlistName==null)
            throw new IllegalArgumentException("Playlist name cannot be null");
        Playlist.UserDefinedPlaylist playlist = playlistRepo.QueryPlaylistByname(playlistName);
        BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTracksByPlaylistSpecification(playlist);
        return trackRepository.QueryTracks(specification);
    }

}
