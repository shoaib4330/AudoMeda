package com.emo.lkplayer.innerlayer.interactors;

import android.arch.lifecycle.LiveData;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.Lyrics;
import com.emo.lkplayer.innerlayer.repository.LyricsRepo;

import java.util.List;

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
