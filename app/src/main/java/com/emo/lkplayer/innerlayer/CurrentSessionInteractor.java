package com.emo.lkplayer.innerlayer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.util.Log;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.emo.lkplayer.innerlayer.repository.CurrentSessionRepo;
import com.emo.lkplayer.innerlayer.repository.MediaComponentsRepo;
import com.emo.lkplayer.innerlayer.repository.PresetRepository;
import com.emo.lkplayer.outerlayer.storage.SessionStorage;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioTracksSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.TracksProvider;
import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.h6ah4i.android.media.audiofx.IPreAmp;

import java.util.List;

public class CurrentSessionInteractor {

    private Context context;
    private CurrentSessionRepo currentSessionRepo;

    public CurrentSessionInteractor(Context context)
    {
        this.context = context;
        currentSessionRepo = new CurrentSessionRepo(context);
    }

    public void updateCurrentProviderQueryPlusIndex(String folderName, String albumName, String artistName, long genreID, int newIndex)
    {
        currentSessionRepo.updateCurrentProviderQueryPlusIndex(folderName, albumName, artistName, genreID, newIndex);
    }

    public void updateCurrentProviderQueryPlusIndexAllTracks(int newIndex)
    {
        currentSessionRepo.updateCurrentProviderQueryPlusIndexAllTracks(newIndex);
    }

    public void updateCurrentProviderQueryPlusIndexRecentTracks(int newIndex)
    {
        currentSessionRepo.updateCurrentProviderQueryPlusIndexRecentTracks(newIndex);
    }

    public LiveData<List<AudioTrack>> getCurrentAudioTracksList()
    {
        return currentSessionRepo.getAudioTracksList();
    }

    public LiveData<Integer> getCurrentTrackIndex()
    {
        return currentSessionRepo.getCurrentTrackIndex();
    }


    /*--------------------------- Equalizer Interaction Methods exist here -----------------------*/
    public IBasicMediaPlayer getMediaPlayer()
    {
        return new MediaComponentsRepo().getMediaPlayer();
    }

    public IPreAmp getPreAmp()
    {
        return new MediaComponentsRepo().getPreAmp();
    }

    public IEqualizer getEqualizer()
    {
        return new MediaComponentsRepo().getEqualizer();
    }


    /* ---------------------------Preset Interaction methods here------------------------------  */
    public void setEqLastUseState(boolean toUse)
    {
        currentSessionRepo.saveEqualizerUseState(toUse);
    }

    public boolean getEqLastUseState()
    {
        return currentSessionRepo.getEqualizerUseState();
    }

    public void saveNewSelectedEqPreset(String presetName)
    {
        currentSessionRepo.saveUsedPreset(presetName);
    }

    public EQPreset getSavedSelectedEqPreset()
    {
        String latUsedPresetName = currentSessionRepo.getSavedUsedPresetName();
        List<EQPreset> presetList = getAllEqPresets();
        for (int i = 0; i < presetList.size(); i++)
        {
            if (presetList.get(i).getPresetName().equals(latUsedPresetName))
                return presetList.get(i);
        }
        return presetList.get(0);
    }

    /* Returns default and user-defined presets combined*/
    public List<EQPreset> getAllEqPresets()
    {
        PresetRepository presetRepository = new PresetRepository(context);
        List<EQPreset.UserDefPreset> userDefPresetsList = presetRepository.Query();
        List<EQPreset> defaultPresetList = presetRepository.Query(getEqualizer());
        defaultPresetList.addAll(userDefPresetsList);
        return defaultPresetList;
    }

    public void addNewPreset(String presetName,int[] bandLevelArr)
    {
        if (presetName!=null && bandLevelArr!=null && bandLevelArr.length==EQPreset.PRESET_ARR_SIZE)
        {
            EQPreset.UserDefPreset newPreset = new EQPreset.UserDefPreset(presetName,bandLevelArr);

            PresetRepository presetRepository = new PresetRepository(context);
            presetRepository.add(newPreset);
        }
    }
}
