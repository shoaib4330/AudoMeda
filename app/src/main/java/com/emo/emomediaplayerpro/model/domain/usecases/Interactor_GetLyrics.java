package com.emo.emomediaplayerpro.model.domain.usecases;

import android.arch.lifecycle.LiveData;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.data_layer.repository.LyricsRepo;

public class Interactor_GetLyrics {

    private LyricsRepo lyricsRepo;

    public Interactor_GetLyrics()
    {
        lyricsRepo = new LyricsRepo();
    }

    public LiveData<String> getLyrics (AudioTrack track)
    {
        if ( (track.getTitle()==null && track.getArtistName()==null) || (track.getTitle().isEmpty() && track.getArtistName().isEmpty()))
            return null;
        return lyricsRepo.getLyrics(track.getTitle(),track.getArtistName());
    }
}
