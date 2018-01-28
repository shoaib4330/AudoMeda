package com.emo.emomediaplayerpro.model.domain;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;

import java.util.List;

/**
 * Created by shoaibanwar on 1/8/18.
 */

public interface iAudioRepository {
    List<AudioTrack> getAllAudios() throws Exception;
    List<AudioTrack> getAudiosByName(String param) throws Exception;
    List<AudioTrack> getRecentAudios(int hoursOld) throws Exception;
}
