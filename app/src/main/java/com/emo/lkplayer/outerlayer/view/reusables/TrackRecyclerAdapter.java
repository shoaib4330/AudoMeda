package com.emo.lkplayer.outerlayer.view.reusables;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emo.lkplayer.R;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.view.fragments.ListTrackFragment;
import com.emo.lkplayer.utilities.Utility;

import java.util.List;

public class TrackRecyclerAdapter extends RecyclerView.Adapter<TrackRecyclerAdapter.TrackViewHolder> {

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_trackTitle, tv_Duration, tv_ArtistName;

        private static View.OnClickListener mOnClickListener;
        private static View.OnLongClickListener mLongClickListener;

        public TrackViewHolder(View itemView)
        {
            super(itemView);
            tv_trackTitle = (TextView) itemView.findViewById(R.id.tv_trackTitle);
            tv_Duration = (TextView) itemView.findViewById(R.id.tv_trackDuration);
            tv_ArtistName = (TextView) itemView.findViewById(R.id.tv_trackArtist);
        }

        public void bind(AudioTrack track, int position)
        {
            this.itemView.setTag(position); /* shoaib: to get clicked item position */
            tv_trackTitle.setText(track.getTrackTitle());
            tv_ArtistName.setText(track.getArtistName());
            tv_Duration.setText(Utility.millisToTrackTimeFormat(track.getTrackDuration()));
                /* shoaib: this onCickListener will be initialized and assigned by setItemViewOnClickListener */
            this.itemView.setOnClickListener(this.mOnClickListener);
            this.itemView.setOnLongClickListener(mLongClickListener);
        }

        public static void setItemViewOnClickListener(View.OnClickListener listener, View.OnLongClickListener longClickListener)
        {
            mOnClickListener = listener;
            mLongClickListener = longClickListener;
        }
    }

    private List<AudioTrack> adapterTrackList;

    public TrackRecyclerAdapter(View.OnClickListener listener, View.OnLongClickListener longClickListener)
    {
        TrackViewHolder.setItemViewOnClickListener(listener, longClickListener);
    }

    public void updateFoldersList(List<AudioTrack> tracksList)
    {
        this.adapterTrackList = tracksList;
        notifyDataSetChanged();
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.m_track_view, parent, false);
        TrackViewHolder fvh = new TrackViewHolder(itemView);
        return fvh;
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position)
    {
        holder.bind(this.adapterTrackList.get(position), position);
    }

    @Override
    public int getItemCount()
    {
        if (this.adapterTrackList != null)
            return this.adapterTrackList.size();
        return 0;
    }
}

