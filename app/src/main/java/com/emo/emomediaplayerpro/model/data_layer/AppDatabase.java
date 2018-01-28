package com.emo.emomediaplayerpro.model.data_layer;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.DynamicQueue;
import com.emo.emomediaplayerpro.model.domain.entities.EQPreset;
import com.emo.emomediaplayerpro.model.domain.entities.Playlist;
import com.emo.emomediaplayerpro.model.data_layer.daos.DBConstants;
import com.emo.emomediaplayerpro.model.data_layer.daos.DynamicQueueModelDao;
import com.emo.emomediaplayerpro.model.data_layer.daos.PlaylistModelDao;
import com.emo.emomediaplayerpro.model.data_layer.daos.PresetModelDao;
import com.emo.emomediaplayerpro.model.data_layer.daos.TypeConverter_IntegerArray;

@Database(entities = {EQPreset.UserDefPreset.class, Playlist.UserDefinedPlaylist.class, DynamicQueue.class}, version = 1,exportSchema = false)
@TypeConverters({TypeConverter_IntegerArray.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DBConstants.NAME_ROOM_DB)
                            .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public abstract PresetModelDao presetModelDao();

    public abstract PlaylistModelDao playlistModelDao();

    public abstract DynamicQueueModelDao dynamicQueueModelDao();
}
