package com.emo.lkplayer.outerlayer.storage.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.emo.lkplayer.innerlayer.model.entities.EQPreset;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by shoaibanwar on 7/30/17.
 */
@Dao
public interface PresetModelDao {

    @Query("select * from "+ DBConstants.TABLE_USERDEFINEDEQPRESET_NAME)
    List<EQPreset.UserDefPreset> getUserDefPresets();

    @Query("select * from " + DBConstants.TABLE_USERDEFINEDEQPRESET_NAME+ " where presetName = :presetName")
    EQPreset.UserDefPreset getPresetbyName(String presetName);

    @Insert(onConflict = REPLACE)
    void addNewPreset(EQPreset.UserDefPreset borrowModel);

    @Delete
    void deletePreset(EQPreset.UserDefPreset borrowModel);
}
