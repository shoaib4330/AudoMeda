package com.emo.lkplayer.innerlayer.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Provider_Tracks;

import java.util.ArrayList;
import java.util.List;

public class TrackRepo {

    private Context context;
    private Provider_Tracks providerTracks;
    private MutableLiveData <List<AudioTrack>> liveAudioTracksList = new MutableLiveData<>();

    public TrackRepo(Context context)
    {
        this.context = context.getApplicationContext();
        this.providerTracks = new Provider_Tracks(context, new Provider_Tracks.ProviderCallBacks() {
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
        providerTracks.Query(specification);
        return liveAudioTracksList;
    }

    public void deleteTrack (BaseLoaderSpecification specification)
    {
        providerTracks.remove(specification);
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
