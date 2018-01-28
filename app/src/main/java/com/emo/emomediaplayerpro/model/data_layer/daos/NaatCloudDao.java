package com.emo.emomediaplayerpro.model.data_layer.daos;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.retro_entities.retro_CloudTrack;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by shoaibanwar on 11/26/17.
 */
public class NaatCloudDao {
   /*ToDo: This is a network/api/cloud based datastore, it must implement an interface (ideally, logically) that
    is consistent for different kinds of datastores (generic). Currently I'm skipping this abstraction.
      */

    /* -------------------------- Retrofit Interface as Dao ------------ */
    private interface INaatCloudAPI {

        @GET("/audio-api?access-token=101-token&serial_number=ACDBDA4013EAMH0507090454668")
        Call<List<retro_CloudTrack>> queryNaats();

        //@GET("matcher.colelction.get?apikey=59f3060c4ab34f175347668c1de73dde&")
        //Call<Object> queriedCollection (@Query("q_track") String q_track, @Query("q_artist")String q_artist);

        //@GET("matcher.colelction.get?apikey=59f3060c4ab34f175347668c1de73dde&")
        //Call<Object> queriedNasheed (@Query("q_track") String q_track, @Query("q_artist")String q_artist);
    }

    /* ------------------------- Class starts here -----------------------------*/
    /* Constant and static members here */
    private static final String BASE_URL = "http://naat.emo.com.pk";

    private Retrofit retrofit;
    private Call<List<retro_CloudTrack>> networkCall;
    private INaatCloudAPI naatCloudAPI;

    public NaatCloudDao()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        naatCloudAPI = retrofit.create(INaatCloudAPI.class);
    }

    private List<AudioTrack> getNaats() throws Exception
    {
        /* reference to a network call */
        networkCall = naatCloudAPI.queryNaats();
        /* get the Json response as List */
        List<retro_CloudTrack> list = networkCall.execute().body();
        if (list==null)
        {
            throw new Exception("No such Naats");
        }
        if (list.size()==0)
        {
            throw new Exception("No such Naats");
        }
        /* return the obtained list */
        return updateLiveDataList(list);
    }

    private List<AudioTrack> updateLiveDataList(List<retro_CloudTrack> audioTrackList)
    {
        List<AudioTrack> listTracks_AsDomainObject = new ArrayList<>();
        for (int i = 0; i < audioTrackList.size() ; i++)
        {
            /* create new domain relevant object */
            AudioTrack track = new AudioTrack();
            // ToDO: below stuff is commented, yahan naaten dlti hen actually.
            /* Do the mapping */
//            track.setId(audioTrackList.get(i).getId());
//            track.setTitle(audioTrackList.get(i).getTitle());
//            track.setDateTime_Created(audioTrackList.get(i).getCreated());
//            track.setDateTime_Updated(audioTrackList.get(i).getUpdated());
//            track.setImageLink(audioTrackList.get(i).getImage_link());
//            track.setTrackLink(audioTrackList.get(i).getLink());
//            listTracks_AsDomainObject.add(track);
        }
        return listTracks_AsDomainObject;
    }

//    @Override
//    public void onResponse(Call call, Response response)
//    {
//        /* get the Json response as List */
//        List<retro_CloudTrack> list = (List<retro_CloudTrack>) response.body();
//        /* update the listLiveData field, that is provided by repository to all its clients */
//        updateLiveDataList(list);
//        /* write to debug*/
//        Log.d("naat list",list.toString());
//    }
//
//    @Override
//    public void onFailure(Call call, Throwable t)
//    {
//        /* write to debug */
//        Log.d("naat list","....failed...");
//    }
}
