package com.emo.emomediaplayerpro.view_ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.emo.emomediaplayerpro.model.domain.entities.iPlayable;
import com.emo.emomediaplayerpro.model.domain.usecases.ServiceAudioTracksManager;

import java.util.List;

/**
 * Created by shoaibanwar on 1/8/18.
 */

public class LocalAudiosViewModel extends AndroidViewModel {

    /* ViewModel Fundamentally requires:
        1) a use-case object reference to get data from
        2) a view's reference to pass data to and call data events on

        Good practice:
        1) ViewModel knows only of an interface that use-case object implements
        2) ViewModel knows only of an interface that "view" will implement to listen ViewModel's
        events.
     */
    private ServiceAudioTracksManager.iGetTracks iGetTracks;
    private LocalAudiosViewModel.Listener listener;

    public LocalAudiosViewModel(Application application)
    {
        super(application);
    }

    public void init(ServiceAudioTracksManager.iGetTracks tracksProvider)
    {
        this.iGetTracks = tracksProvider;
    }

    public void setListener(LocalAudiosViewModel.Listener tracksView)
    {
        this.listener = tracksView;
    }

    public void retrieveLocalAudios()
    {
        /* show progress bar or circular loader to indicate loading */
        listener.onShowLoadingInProgress();
        this.iGetTracks.execute("", new ServiceAudioTracksManager.iGetTracks.Callback() {
            @Override
            public void onTracksLoaded(List<iPlayable> tracksList)
            {
                listener.onTracksLoaded(tracksList);
            }

            @Override
            public void onNotFound()
            {
                listener.onLoadingEnded();
                listener.onAudioMessageNotFound();
            }

            @Override
            public void onConnectionError()
            {
                listener.onLoadingEnded();
            }
        });
    }

    /**
     * Interface created to work as ViewModel listener.
     * Every change in the view model will be
     * notified to Listener implementation.
     */
    public interface Listener {

        void onTracksLoaded(List<iPlayable> list);

        void onAudioMessageNotFound(); //when queried audio does not exist

        void onShowLoadingInProgress (); //when loading starts

        void onLoadingEnded(); //when loading ends

        /* can contain additional methods, to make ui elements visible/invisible
        depending upon, content is loaded, loading, not found, show progress etc.
         */
    }
}
