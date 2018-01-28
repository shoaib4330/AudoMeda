package com.emo.emomediaplayerpro.view_ui.customviews;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emo.emomediaplayerpro.R;
import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.utilities.Utility;

public class TrackInfoDialog {

    private Context context;
    private AudioTrack audioTrack;

    private View dialogView;
    private TextView tv_trackTitle;
    private TextView tv_trackArtist;
    private TextView tv_trackDuration;
    private TextView tv_trackAlbum;
    private Button btn_ok;

    private AlertDialog alertDialog;

    public TrackInfoDialog(Context context, AudioTrack audioTrack)
    {
        this.context = context;
        this.audioTrack = audioTrack;
        dialogView = View.inflate(context, R.layout.dialog_track_info,null);
        tv_trackTitle = (TextView) dialogView.findViewById(R.id.tv_dialog_trackinfo_title);
        tv_trackArtist = (TextView) dialogView.findViewById(R.id.tv_dialog_trackinfo_artist);
        tv_trackDuration = (TextView) dialogView.findViewById(R.id.tv_dialog_trackinfo_duration);
        tv_trackAlbum = (TextView) dialogView.findViewById(R.id.tv_dialog_trackinfo_album);
        btn_ok = (Button) dialogView.findViewById(R.id.btn_dialog_trackinfo_ok);

        tv_trackTitle.setText(audioTrack.getTitle());
        tv_trackArtist.setText(audioTrack.getArtistName());
        tv_trackDuration.setText(Utility.millisToTrackTimeFormat(audioTrack.getDuration()));
        tv_trackAlbum.setText(audioTrack.getAlbumName());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        alertDialog = builder.create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });
    }

    public void show(){alertDialog.show();}
}
