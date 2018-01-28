package com.emo.emomediaplayerpro.model.domain.usecases;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.entities.EQPreset;
import com.emo.emomediaplayerpro.model.data_layer.repository.CurrentSessionRepo;
import com.emo.emomediaplayerpro.model.data_layer.repository.MediaComponentsRepo;
import com.emo.emomediaplayerpro.model.data_layer.repository.PresetRepository;
import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.h6ah4i.android.media.audiofx.IBassBoost;
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

    public void updateCurrentProviderQueryPlusIndex(String folderName, String albumName, String artistName,String playlistName ,long genreID, int newIndex)
    {
        currentSessionRepo.updateCurrentProviderQueryPlusIndex(folderName, albumName, artistName, playlistName ,genreID, newIndex);
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

    public IBassBoost getBassBoost()
    {
        return new MediaComponentsRepo().getBassBoost();
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
