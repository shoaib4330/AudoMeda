package com.emo.lkplayer.customviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.emo.lkplayer.R;
import com.emo.lkplayer.Utility;
import com.emo.lkplayer.model.entities.iPlayable;

/**
 * Created by shoaibanwar on 6/7/17.
 */

public class AudoMedaController extends LinearLayoutCompat implements View.OnClickListener {

    private View rootView;
    private ImageButton btn_PlayPause;
    private ImageButton btn_FastForward;
    private ImageButton btn_FastRewind;
    private ImageButton btn_SkipNext;
    private ImageButton btn_SkipPrevious;
    private SeekBar seekBar;
    private TextView tv_TrackLength;
    private TextView tv_TrackCurrentPos;
    private TextView tv_TrackTitle;
    private TextView tv_AlbumTitle;

    private iPlayable playable;
    private AudoMedaControllerTouchEventsListener touchEventsUtilizer;

    public AudoMedaController(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {

        rootView = inflate(context, R.layout.audomedacontroller, this);

        btn_PlayPause = (android.support.v7.widget.AppCompatImageButton) rootView.findViewById(R.id.btn_Play);
        btn_FastForward = (ImageButton) rootView.findViewById(R.id.btn_FastForward);
        btn_FastRewind = (ImageButton) rootView.findViewById(R.id.btn_FastRewind);
        btn_SkipNext = (ImageButton) rootView.findViewById(R.id.btn_SkipNext);
        btn_SkipPrevious = (ImageButton) rootView.findViewById(R.id.btn_SkipPrev);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar_TrackSeeker);
        tv_TrackLength = (TextView) rootView.findViewById(R.id.tv_TrackLength);
        tv_TrackCurrentPos = (TextView) rootView.findViewById(R.id.tv_TrackCurrentPos);
        tv_TrackTitle = (TextView) rootView.findViewById(R.id.tv_TrackTitle);
        tv_AlbumTitle = (TextView) rootView.findViewById(R.id.tv_AlbumTitle);

        btn_PlayPause.setOnClickListener(this);
        btn_FastForward.setOnClickListener(this);
        btn_FastRewind.setOnClickListener(this);
        btn_SkipNext.setOnClickListener(this);
        btn_SkipPrevious.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                {
                    if (playable != null)
                    {
                        double factor = progress;
                        //double seekPos = factor*playable.getDuration();
                        if (AudoMedaController.this.touchEventsUtilizer != null)
                            AudoMedaController.this.touchEventsUtilizer.onSeekBarPositionChange(progress * 1000);
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

    }

    public void setPlayable(iPlayable playable)
    {
        this.playable = playable;
        this.seekBar.setMax((int) playable.getDuration() / 1000);
        this.seekBar.setProgress(0);
        this.tv_TrackTitle.setText(this.playable.getTitle());
        this.tv_AlbumTitle.setText(this.playable.getAlbumTitle());
        this.tv_TrackLength.setText(Utility.millisToTrackTimeFormat(this.playable.getDuration()));
        this.tv_TrackCurrentPos.setText("0:00");
    }

    public void setTouchEventsListener(AudoMedaControllerTouchEventsListener audoMedaControllerTouchEventsListener)
    {
        this.touchEventsUtilizer = audoMedaControllerTouchEventsListener;
    }

    public void setPlaybackProgress(int currentPosition)
    {
        this.tv_TrackCurrentPos.setText(Utility.millisToTrackTimeFormat(currentPosition));
        this.seekBar.setProgress(currentPosition / 1000);
    }

    public void setPlayPauseButtonState(boolean isPlaying)
    {
        if (isPlaying)
        {
            btn_PlayPause.setImageResource(R.drawable.mediacontroller_icon_pause);
        } else
        {
            btn_PlayPause.setImageResource(R.drawable.mediacontroller_icon_play);
        }

    }

    @Override
    public void onClick(View v)
    {

        if (AudoMedaController.this.touchEventsUtilizer == null)
        {
            return;
        } else if (v.getId() == R.id.btn_Play)
        {
            AudoMedaController.this.touchEventsUtilizer.onPlayButton_Click();
        } else if (v.getId() == R.id.btn_FastForward)
        {
            AudoMedaController.this.touchEventsUtilizer.onFastForward_Click();
        } else if (v.getId() == R.id.btn_FastRewind)
        {
            AudoMedaController.this.touchEventsUtilizer.onFastRewind_Click();

        } else if (v.getId() == R.id.btn_SkipNext)
        {
            AudoMedaController.this.touchEventsUtilizer.onSkipNext_Click();
        } else if (v.getId() == R.id.btn_SkipPrev)
        {
            AudoMedaController.this.touchEventsUtilizer.onSkipPrev_Click();
        }
    }

    public interface AudoMedaControllerTouchEventsListener {
        void onPlayButton_Click();

        void onFastForward_Click();

        void onFastRewind_Click();

        void onSkipNext_Click();

        void onSkipPrev_Click();

        void onSeekBarPositionChange(int newPosition);
    }
}
