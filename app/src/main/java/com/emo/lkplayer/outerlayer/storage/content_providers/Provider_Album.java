package com.emo.lkplayer.outerlayer.storage.content_providers;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.emo.lkplayer.innerlayer.model.entities.Album;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;

import java.util.List;

public class Provider_Album {

    public interface ProviderCallBacks{
        void onQueryComplete(List<Album> albumList);
    }

    private static final int TOKEN_DEFAULT = 111;
    private static final int TOKEN_DEFAULT_DELETE = 1111;

    private Context context;
    private BaseLoaderSpecification specification;
    private AsyncQueryHandler asyncQueryHandler;
    private Provider_Album.ProviderCallBacks callBacksListener;
    Handler handler = new Handler();

    public Provider_Album(Context context, final Provider_Album.ProviderCallBacks callBacksListener)
    {
        this.context = context;
        this.callBacksListener = callBacksListener;
        this.asyncQueryHandler = new AsyncQueryHandler(Provider_Album.this.context.getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor)
            {
                List<Album> list = specification.returnMappedList(cursor);
                if (callBacksListener!=null)
                    Provider_Album.this.callBacksListener.onQueryComplete(list);
            }

            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri)
            {
                super.onInsertComplete(token, cookie, uri);
            }

            @Override
            protected void onUpdateComplete(int token, Object cookie, int result)
            {
                super.onUpdateComplete(token, cookie, result);
            }

            @Override
            protected void onDeleteComplete(int token, Object cookie, int result)
            {
                super.onDeleteComplete(token, cookie, result);
            }
        };
    }

    public void Query(BaseLoaderSpecification specification)
    {
        this.specification = specification;
        if(this.asyncQueryHandler!=null)
            asyncQueryHandler.startQuery(TOKEN_DEFAULT,null,specification.getUriForLoader(),specification.getProjection(),
                    specification.getSelection(),specification.getSelectionArgs(),specification.getSortOrder());
    }

    public void remove (BaseLoaderSpecification specification)
    {
        this.specification = specification;
        if (this.asyncQueryHandler!=null)
        {
            asyncQueryHandler.startDelete(TOKEN_DEFAULT_DELETE,null,specification.getUriForLoader(),
                    specification.getSelection(),specification.getSelectionArgs());
        }
    }
}
