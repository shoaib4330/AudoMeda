package com.emo.lkplayer.innerlayer.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.TracksProvider;

import java.util.ArrayList;
import java.util.List;

public class TrackRepository{

    private Context context;
    private TracksProvider tracksProvider;
    private MutableLiveData <List<AudioTrack>> liveAudioTracksList = new MutableLiveData<>();

    public TrackRepository(Context context)
    {
        this.context = context.getApplicationContext();
        this.tracksProvider = new TracksProvider(context, new TracksProvider.ProviderCallBacks() {
            @Override
            public void onQueryComplete(List<AudioTrack> trackList)
            {
                if (trackList==null)
                    trackList = new ArrayList<>();
                liveAudioTracksList.setValue(trackList);
            }
        });
    }

    public LiveData<List<AudioTrack>> QueryTracks(BaseLoaderSpecification specification)
    {
        tracksProvider.Query(specification);
        return liveAudioTracksList;
    }

    public void deleteTrack (BaseLoaderSpecification specification)
    {
        tracksProvider.remove(specification);
    }
//    public String getTrackArtUriByID(long albumID,Context context)
//    {
//        iLoaderSpecification mSpec;
//        mSpec = new AudioAlbumsSpecification(albumID);
//        Cursor c = context.getContentResolver().query(mSpec.getUriForLoader(), mSpec.getProjection(),
//                mSpec.getSelection(), mSpec.getSelectionArgs(), null);
//        if (((List<Album>) mSpec.returnMappedList(c)).size() > 0)
//        {
//            Album album = ((List<Album>) mSpec.returnMappedList(c)).get(0);
//            return album.getAlbumArtURI();
//        }
//        return null;
//    }
}
