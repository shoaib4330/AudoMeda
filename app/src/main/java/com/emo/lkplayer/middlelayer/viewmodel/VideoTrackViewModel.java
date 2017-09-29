package com.emo.lkplayer.middlelayer.viewmodel;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.AndroidViewModel;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.interactors.Interactor_GetVideoTracks;
import java.util.List;

public class VideoTrackViewModel extends AndroidViewModel{

    public VideoTrackViewModel(Application application)
    {
        super(application);
    }

    private LiveData<List<AudioTrack>> Live_TracksList = null;

    public LiveData<List<AudioTrack>> getVideoTracksList()
    {
        if (this.Live_TracksList == null)
        {
            this.Live_TracksList = new Interactor_GetVideoTracks(this.getApplication()).getVideoTracksList();
        }
        return this.Live_TracksList;
    }


}
