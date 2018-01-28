package com.emo.emomediaplayerpro.model.data_layer.repository;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.iAudioRepository;

import java.util.List;

/**
 * Created by shoaibanwar on 1/8/18.
 */

public class AudioRepository implements iAudioRepository {
    @Override
    public List<AudioTrack> getAllAudios() throws Exception
    {
        return null;
    }

    @Override
    public List<AudioTrack> getAudiosByName(String param) throws Exception
    {
        return null;
    }

    @Override
    public List<AudioTrack> getRecentAudios(int hoursOld) throws Exception
    {
        return null;
    }
}
