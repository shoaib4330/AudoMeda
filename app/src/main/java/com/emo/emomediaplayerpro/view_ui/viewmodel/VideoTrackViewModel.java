package com.emo.emomediaplayerpro.view_ui.viewmodel;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.AndroidViewModel;
import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.usecases.Interactor_GetVideoTracks;
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
