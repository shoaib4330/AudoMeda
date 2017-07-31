package com.emo.lkplayer.outerlayer.storage.content_providers;

/**
 * Created by shoaibanwar on 6/23/17.
 */


//public class TracksLoader implements LoaderManager.LoaderCallbacks<Cursor> {
public class TracksLoader{

//    private Context context;
//    private LoaderManager loaderManager;
//
//    private MutableLiveData<List<AudioTrack>> LiveData_tracksList = new MutableLiveData<>();
//
//    private iLoaderSpecification<AudioTrack> audioTrackSpec = null;
//    private iLoaderSpecification<AudioTrack> videoTrackSpec = null;
//
//    private boolean audioTracksLoaderDone = false;
//    private boolean videoTracksLoaderDone = false;
//
//    private List<AudioTrack> audioTrackList;
//    private List<AudioTrack> videoTrackList;
//
//    private int ID_LOADER_ALL_AUDIO_TRACKS;
//    private int ID_LOADER_ALL_VIDEO_TRACKS;
//
//    /* shoaib: Old cursor, only kept to be given back to loader when we receive new cursor */
//    protected Cursor cursor;
//
//    public TracksLoader(Context context, LoaderManager loaderManager, int loaderAudioID, int loaderVideoID)
//    {
//        this.context = context;
//        this.loaderManager = loaderManager;
//        this.ID_LOADER_ALL_AUDIO_TRACKS = loaderAudioID;
//        this.ID_LOADER_ALL_VIDEO_TRACKS = loaderVideoID;
//    }
//
//    public void setSpecs(iLoaderSpecification[] specifications)
//    {
//
//        if (specifications == null)
//        {
//            loaderManager.destroyLoader(ID_LOADER_ALL_AUDIO_TRACKS);
//            loaderManager.destroyLoader(ID_LOADER_ALL_VIDEO_TRACKS);
//            return;
//        }
//        else if (specifications.length == 2)
//        {
//            if(!specifications[0].equals(this.audioTrackSpec) && !specifications[1].equals(this.videoTrackSpec))
//            {
//                this.audioTrackSpec = specifications[0];
//                this.videoTrackSpec = specifications[1];
//                loaderManager.destroyLoader(ID_LOADER_ALL_AUDIO_TRACKS);
//                loaderManager.destroyLoader(ID_LOADER_ALL_VIDEO_TRACKS);
//            }
//        }
//        else
//        {
//            if (!specifications[0].equals(this.audioTrackSpec))
//                loaderManager.destroyLoader(ID_LOADER_ALL_AUDIO_TRACKS);
//            this.audioTrackSpec = specifications[0];
//        }
//    }
//
//    public LiveData<List<AudioTrack>> requestTrackData()
//    {
//        init();
//        return this.LiveData_tracksList;
//    }
//
//    private void init()
//    {
//        if (audioTrackSpec != null)
//        {
//            if (loaderManager.getLoader(ID_LOADER_ALL_AUDIO_TRACKS) == null)
//                loaderManager.initLoader(ID_LOADER_ALL_AUDIO_TRACKS, null, this);
//            //else
//                //loaderManager.restartLoader(ID_LOADER_ALL_AUDIO_TRACKS, null, this);
//        }
//
//        if (videoTrackSpec != null)
//        {
//            if (loaderManager.getLoader(ID_LOADER_ALL_VIDEO_TRACKS) == null)
//                loaderManager.initLoader(ID_LOADER_ALL_VIDEO_TRACKS, null, this);
//            //else
//                //loaderManager.restartLoader(ID_LOADER_ALL_VIDEO_TRACKS, null, this);
//        }
//    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args)
//    {
//        if (id == ID_LOADER_ALL_AUDIO_TRACKS)
//        {
//            CursorLoader cursorLoader = new CursorLoader(context, audioTrackSpec.getUriForLoader(), audioTrackSpec.getProjection(), audioTrackSpec.getSelection(), audioTrackSpec.getSelectionArgs(), audioTrackSpec.getSortOrder());
//            return cursorLoader;
//        } else
//        {
//            CursorLoader cursorLoader = new CursorLoader(context, videoTrackSpec.getUriForLoader(), videoTrackSpec.getProjection(), videoTrackSpec.getSelection(), videoTrackSpec.getSelectionArgs(), videoTrackSpec.getSortOrder());
//            return cursorLoader;
//        }
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
//    {
//        if (loader.getId() == ID_LOADER_ALL_AUDIO_TRACKS)
//        {
//            this.audioTracksLoaderDone = true;
//            this.audioTrackList = this.audioTrackSpec.returnMappedList(data);
//        }
//        else if (loader.getId() == ID_LOADER_ALL_VIDEO_TRACKS)
//        {
//            this.videoTracksLoaderDone = true;
//            this.videoTrackList = this.videoTrackSpec.returnMappedList(data);
//        }
//
//        if ((audioTrackSpec != null && videoTrackSpec != null) && (this.audioTracksLoaderDone && this.videoTracksLoaderDone))
//        {
//            if (ID_LOADER_ALL_AUDIO_TRACKS == ConstantsHolder.LOADER_ID_NAVPLAYBACK_FRAGMENT)
//                Log.d("--Loader", "NavPlaybackLoader onLoadFinished audbool" + this.audioTracksLoaderDone + " vidbool" + this.videoTracksLoaderDone);
//            //Log.d("--Loader","Loader onLoadFinished audbool"+this.audioPlaylistHolderDone+" vidbool"+this.videoPlaylistHolderDone);
//            List<AudioTrack> allTracksList = new ArrayList<>();
//            allTracksList.addAll(videoTrackList);
//            allTracksList.addAll(audioTrackList);
//            this.LiveData_tracksList.setValue(allTracksList);
//            this.videoTracksLoaderDone = false;
//            this.audioTracksLoaderDone = false;
//        }
//        else if ((audioTrackSpec != null && videoTrackSpec == null) && (this.audioTracksLoaderDone))
//        {
//            List<AudioTrack> allTracksList = new ArrayList<>();
//            allTracksList.addAll(audioTrackList);
//            this.LiveData_tracksList.setValue(allTracksList);
//        }
//        else if ((audioTrackSpec == null && videoTrackSpec != null) && (this.videoTracksLoaderDone))
//        {
//            List<AudioTrack> allTracksList = new ArrayList<>();
//            allTracksList.addAll(videoTrackList);
//            this.LiveData_tracksList.setValue(allTracksList);
//        }
//            /* swap the cursor */
//        this.swapCursor(data, data = cursor);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader)
//    {
//        if (loader.getId() == ID_LOADER_ALL_AUDIO_TRACKS)
//        {
//            this.audioTracksLoaderDone = false;
//            this.audioTrackSpec = null;
//        }
//
//        if (loader.getId() == ID_LOADER_ALL_VIDEO_TRACKS)
//        {
//            this.videoTracksLoaderDone = false;
//            this.videoTrackSpec = null;
//        }
//        swapCursor(null, null);
//    }
//
//    private void swapCursor(Cursor cursorNew, Cursor dummy)
//    {
//            /* shoaib: we keep reference of the old cursor, it will be swapped with new one */
//        this.cursor = cursorNew;
//    }

}

