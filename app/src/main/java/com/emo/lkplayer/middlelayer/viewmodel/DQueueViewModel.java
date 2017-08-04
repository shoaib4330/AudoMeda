package com.emo.lkplayer.middlelayer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.DynamicQueue;
import com.emo.lkplayer.outerlayer.storage.AppDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 8/2/17.
 */

public final class DQueueViewModel extends AndroidViewModel {

    private LiveData<List<DynamicQueue>> live_DynamicQueue = null;

    public DQueueViewModel(Application application)
    {
        super(application);
    }

    public LiveData<List<DynamicQueue>> getDynamicQueue()
    {
        if (live_DynamicQueue==null)
        {
            live_DynamicQueue = AppDatabase.getDatabase(this.getApplication())
                    .dynamicQueueModelDao().getDynamicQueue(DynamicQueue.QUEUE_NAME_CONSTANT);
        }
        return live_DynamicQueue;
    }

    public void updateDynamicQueue(AudioTrack audioTrack)
    {
        if (live_DynamicQueue!=null)
        {
            if (live_DynamicQueue.getValue()==null)
            {
                List<DynamicQueue> listDQ = new ArrayList<DynamicQueue>();
                listDQ.add(new DynamicQueue());
                ((MutableLiveData)live_DynamicQueue).setValue(listDQ);
            }
            live_DynamicQueue.getValue().get(0).addNewTrackId(audioTrack.getTrackID());
            AppDatabase.getDatabase(this.getApplication()).dynamicQueueModelDao().addNewDynamicQueue(live_DynamicQueue.getValue().get(0));
        }
    }
}
