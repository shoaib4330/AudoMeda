package com.emo.emomediaplayerpro.view_ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.emo.emomediaplayerpro.model.domain.entities.iPlayable;
import com.emo.emomediaplayerpro.model.domain.usecases.ServiceAudioNaatManager;

import java.util.List;

/**
 * Created by shoaibanwar on 11/28/17.
 */

public class CloudNaatsViewModel extends AndroidViewModel {

    /* ViewModel Fundamentally requires:
        1) a use-case object reference to get data from
        2) a view's reference to pass data to and call data events on

        Good practice:
        1) ViewModel knows only of an interface that use-case object implements
        2) ViewModel knows only of an interface that "view" will implement to listen ViewModel's
        events.
     */
    private ServiceAudioNaatManager.iGetNaats miGetNaats;
    private Listener listener;

    public CloudNaatsViewModel(Application application)
    {
        super(application);
    }

    public void init(ServiceAudioNaatManager.iGetNaats naatsProvider)
    {
        this.miGetNaats = naatsProvider;
    }

    public void setListener(Listener cloudNaatsView)
    {
        this.listener = cloudNaatsView;
    }

    public void loadCloudNaats()
    {
        listener.onShowLoadingInProgress();
        this.miGetNaats.execute("", new ServiceAudioNaatManager.iGetNaats.Callback() {
            @Override
            public void onNaatsLoaded(List<iPlayable> naatList)
            {
                listener.onNaatsLoaded(naatList);
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
                listener.onConnectionError();
            }
        });
    }

    /**
     * Interface created to work as ViewModel listener.
     * Every change in the view model will be
     * notified to Listener implementation.
     */
    public interface Listener {

        void onNaatsLoaded(List<iPlayable> list);

        void onConnectionError(); //internet connection error

        void onAudioMessageNotFound(); //when queried audio does not exist

        void onShowLoadingInProgress (); //when loading starts

        void onLoadingEnded(); //when loading ends

        /* can contain additional methods, to make ui elements visible/invisible
        depending upon, content is loaded, loading, not found, show progress etc.
         */
    }
}
