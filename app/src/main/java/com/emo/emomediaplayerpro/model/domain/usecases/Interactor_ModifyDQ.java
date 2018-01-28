package com.emo.emomediaplayerpro.model.domain.usecases;

import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.entities.Playlist;
import com.emo.emomediaplayerpro.model.data_layer.repository.PlaylistRepo;

public class Interactor_ModifyDQ {

    /* DQ is also implemented as a playlist */
    public static final String DQ_AS_PLAYLIST = "dq_playlist";

    private Context context;
    private PlaylistRepo playlistRepo;

    public Interactor_ModifyDQ(Context context)
    {
        this.context = context.getApplicationContext();
        this.playlistRepo = new PlaylistRepo(context);
    }

    public void addTrackToDQ(AudioTrack audioTrack)
    {
        Playlist.UserDefinedPlaylist dq = playlistRepo.QueryPlaylistByname(DQ_AS_PLAYLIST);
        if (dq==null)
            playlistRepo.addPlaylist(DQ_AS_PLAYLIST);
        dq = playlistRepo.QueryPlaylistByname(DQ_AS_PLAYLIST);
        dq.addTrack(audioTrack.getTrackID());
        playlistRepo.updatePlaylist(dq);
    }

    public void deleteTrackFromDQ (AudioTrack audioTrack)
    {
        Playlist.UserDefinedPlaylist dq = playlistRepo.QueryPlaylistByname(DQ_AS_PLAYLIST);
        dq.removeTrack(audioTrack.getTrackID());
        playlistRepo.updatePlaylist(dq);
    }

//    public LiveData<List<DynamicQueue>> getDynamicQueue()
//    {
//        if (AppDatabase.getDatabase(context).dynamicQueueModelDao().getDynamicQueue(DynamicQueue.QUEUE_NAME_CONSTANT).getValue()==null)
//        {
//            AppDatabase.getDatabase(context).dynamicQueueModelDao().addNewDynamicQueue(new DynamicQueue());
//        }
//        return AppDatabase.getDatabase(context).dynamicQueueModelDao().getDynamicQueue(DynamicQueue.QUEUE_NAME_CONSTANT);
//    }
//
//    public void addTrackToDQ(AudioTrack audioTrack)
//    {
//        DynamicQueue dynamicQueue = getDynamicQueue().getValue().get(0);
//        dynamicQueue.addNewTrackId(audioTrack.getTrackID());
//        AppDatabase.getDatabase(context).dynamicQueueModelDao().addNewDynamicQueue(dynamicQueue);
//    }
//
//    public void removeTrackFromDQ (AudioTrack audioTrack)
//    {
//        DynamicQueue dynamicQueue = getDynamicQueue().getValue().get(0);
//        dynamicQueue.removeTrack(audioTrack.getTrackID());
//        AppDatabase.getDatabase(context).dynamicQueueModelDao().addNewDynamicQueue(dynamicQueue);
//    }
}
