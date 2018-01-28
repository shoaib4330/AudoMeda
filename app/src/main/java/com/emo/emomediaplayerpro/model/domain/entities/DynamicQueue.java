package com.emo.emomediaplayerpro.model.domain.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.emo.emomediaplayerpro.model.data_layer.daos.DBConstants;
import com.emo.emomediaplayerpro.model.data_layer.daos.TypeConverter_IntegerArray;

import java.util.ArrayList;

@Entity(tableName = DBConstants.TABLE_DYNAMICQUEUE_NAME)
public class DynamicQueue {
    @Ignore
    public static final String QUEUE_NAME_CONSTANT = "dynamic_queue";

    @PrimaryKey
    private String queueName;
    @TypeConverters(TypeConverter_IntegerArray.class)
    private ArrayList<Long> trackIds;

    @Ignore
    public DynamicQueue()
    {
        this.trackIds = new ArrayList<>();
        this.queueName = QUEUE_NAME_CONSTANT;
    }

    public DynamicQueue(String queueName,ArrayList<Long> trackIds)
    {
        this.trackIds = trackIds;
        this.queueName = QUEUE_NAME_CONSTANT;
    }

    public void setTrackIds(ArrayList<Long> trackIds)
    {
        this.trackIds = trackIds;
    }

    public void setQueueName(String queueName)
    {
        this.queueName = queueName;
    }

    public String getQueueName()
    {
        return queueName;
    }

    public ArrayList<Long> getTrackIds()
    {
        return this.trackIds;
    }

    @Ignore
    public void addNewTrackId(Long trackID)
    {
        this.trackIds.add(trackID);
    }

    @Ignore
    public void removeTrack(Long trackID)
    {
        if (this.trackIds.contains(trackID))
        {
            this.trackIds.remove(trackID);
        }
    }
}
