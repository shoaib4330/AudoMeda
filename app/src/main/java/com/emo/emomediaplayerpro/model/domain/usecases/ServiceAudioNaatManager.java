package com.emo.emomediaplayerpro.model.domain.usecases;

import com.emo.emomediaplayerpro.executor.Executor;
import com.emo.emomediaplayerpro.executor.Interactor;
import com.emo.emomediaplayerpro.executor.MainThread;
import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.entities.iPlayable;
import com.emo.emomediaplayerpro.model.domain.iAudioRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 11/28/17.
 */

public class ServiceAudioNaatManager {
    // ToDo: This class was for NaatCloud, You can remove it from this one and add in NaatCloud
    // version of this project.

    /* Interface that abstracts whether the implementation beneath is.
    Remote business classes
    Local business classes
    Both should implement this
     */
    public interface iGetNaats {
        void execute(final String queryString, Callback callback);

        interface Callback{
            void onNaatsLoaded(List<iPlayable> naatList);
            void onNotFound();
            void onConnectionError();
        }
    }

    public static class RemoteTrackInteractor implements Interactor,iGetNaats {
        /* Interactor Fundamentally requires:
                1) An "Executor" reference to run tasks off the main thread
                2) A "Main thread" abstraction/reference to post results on it
                3) A "Data Source" reference (a data-source that it can directly call
                                                and get data from)
             */
        private final iAudioRepository audioRepository;
        private final Executor mExecutor;
        private final MainThread mMainThread;

        private String queryParam;
        private Callback callback;

        public RemoteTrackInteractor(Executor executor, MainThread mainThread, iAudioRepository repository)
        {
            if (repository == null)
            {
                throw new IllegalArgumentException("repository cannot be null, no data source" +
                        " available");
            }
            else if (executor == null)
            {
                throw new IllegalArgumentException("executor is null");
            }
            else if (mainThread == null)
            {
                throw new IllegalArgumentException("mainThread reference is null");
            }
            this.audioRepository = repository;
            this.mExecutor = executor;
            this.mMainThread = mainThread;
        }

        @Override
        public void execute(String queryString, Callback callback)
        {
            validateArgs(queryString,callback);
            this.queryParam = queryString;
            this.callback = callback;
            this.mExecutor.execute(this);
        }

        @Override
        public void run()
        {
            try
            {
                loadNaats(this.queryParam);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                /* notify mainThread about connection Error */
                notifyConnectionError();
            }
        }

        private void loadNaats(String queryString) throws Exception
        {
            /* filhal: ye mapping ghlt he, ese nahin hona chahiye */

            List<AudioTrack> audioTracksList;

            if (queryString==null || queryString.isEmpty())
            {
                audioTracksList = this.audioRepository.getAllAudios();
            }
            else
            {
                audioTracksList = this.audioRepository.getAudiosByName(queryString);
            }


            List<iPlayable> playableList = new ArrayList<>();
            playableList.addAll(audioTracksList);

            if (playableList.size() == 0)
                notifyNaatsNotFound();
            else
                notifyNaatsLoaded(playableList);
        }

        private void notifyNaatsLoaded(final List<iPlayable> naatList)
        {
            mMainThread.post(new Runnable() {
                @Override
                public void run()
                {
                    callback.onNaatsLoaded(naatList);
                }
            });
        }

        private void notifyNaatsNotFound()
        {
            mMainThread.post(new Runnable() {
                @Override
                public void run()
                {
                    callback.onNotFound();
                }
            });
        }

        private void notifyConnectionError ()
        {
            mMainThread.post(new Runnable() {
                @Override
                public void run()
                {
                    callback.onConnectionError();
                }
            });
        }

        private void validateArgs(String param, Callback callback)
        {
            if (callback == null)
                throw new IllegalArgumentException("Callback parameter cannot be null");

            // ToDo: Validation shall be done here if needed...
            /* filhal: kher he, khud param ki validation skip ki he */
            //if (param == null || param.isEmpty())

        }
    }
}
