package com.emo.lkplayer.innerlayer.repository;

/**
 * Created by shoaibanwar on 7/15/17.
 */

public class TrackRepository{

//    //private WebService_Track webService_track;
//    private TracksLoader tracksLoader;
//
//    public TrackRepository(Context context, LoaderManager loaderManager,int loaderID)
//    {
//        tracksLoader = new TracksLoader(context, loaderManager,loaderID,loaderID+loaderID);
//    }
//
//    public LiveData<List<AudioTrack>> Query(iLoaderSpecification[] specifications)
//    {
//        tracksLoader.setSpecs(specifications);
//        return tracksLoader.requestTrackData();
//    }
//
//    public void deleteTrack(AudioTrack track, Context context)
//    {
//        iLoaderSpecification specification;
//        ContentResolver contentResolver = context.getContentResolver();
//        if (track.getTrackType().equals(AudioTrack.TRACK_TYPE_AUDIO))
//            specification = new AudioTracksSpecification.AudioTrackDeletionSpecification(track.getTrackID());
//        else
//            specification = new VideoTracksSpecification.VideoTrackDeletionSpecification(track.getTrackID());
//        contentResolver.delete(specification.getUriForLoader(), specification.getSelection(), specification.getSelectionArgs());
//    }
//
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
