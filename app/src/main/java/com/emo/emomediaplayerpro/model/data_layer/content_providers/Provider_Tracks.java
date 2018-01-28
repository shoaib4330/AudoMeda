package com.emo.emomediaplayerpro.model.data_layer.content_providers;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.BaseLoaderSpecification;

import java.util.List;

public class Provider_Tracks {

    public interface ProviderCallBacks{
        void onQueryComplete(List<AudioTrack> trackList);
    }

    private static final int TOKEN_DEFAULT = 222;
    private static final int TOKEN_DEFAULT_DELETE = 2222;

    private Context context;
    private BaseLoaderSpecification specification;
    private AsyncQueryHandler asyncQueryHandler;
    private ProviderCallBacks callBacksListener;

    public Provider_Tracks(Context context, final ProviderCallBacks callBacksListener)
    {
        this.context = context;
        this.callBacksListener = callBacksListener;
        this.asyncQueryHandler = new AsyncQueryHandler(Provider_Tracks.this.context.getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor)
            {
                List<AudioTrack> list = specification.returnMappedList(cursor);
                if (callBacksListener!=null)
                    Provider_Tracks.this.callBacksListener.onQueryComplete(list);
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
