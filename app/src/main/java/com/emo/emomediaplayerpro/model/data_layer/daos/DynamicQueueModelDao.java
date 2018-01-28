package com.emo.emomediaplayerpro.model.data_layer.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.emo.emomediaplayerpro.model.domain.entities.DynamicQueue;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by shoaibanwar on 8/2/17.
 */
@Dao
public interface DynamicQueueModelDao {

    @Query("select * from "+ DBConstants.TABLE_DYNAMICQUEUE_NAME+" where queueName = :queueName")
    LiveData<List<DynamicQueue>> getDynamicQueue(String queueName);

    @Insert(onConflict = REPLACE)
    void addNewDynamicQueue(DynamicQueue dynamicQueue);
}
