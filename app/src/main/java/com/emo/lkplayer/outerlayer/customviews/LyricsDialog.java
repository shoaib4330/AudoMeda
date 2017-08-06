package com.emo.lkplayer.outerlayer.customviews;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emo.lkplayer.R;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.utilities.Utility;

public class LyricsDialog {

    private Context context;

    private View dialogView;
    private TextView tv_lyrics;
    private Button btn_ok;

    private AlertDialog alertDialog;

    public LyricsDialog(Context context)
    {
        this.context = context;
        dialogView = View.inflate(context, R.layout.dialog_lyrics, null);
        tv_lyrics = (TextView) dialogView.findViewById(R.id.tv_lyricsDialog_lyrics);
        btn_ok = (Button) dialogView.findViewById(R.id.btn_dialog_lyrics_ok);

        tv_lyrics.setText("Loading lyrics...");

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

    public void show()
    {
        alertDialog.show();
        alertDialog.getWindow().setLayout(800, 1300);
    }

    public void updateLyrics(String lyrics)
    {
        this.tv_lyrics.setText(lyrics);
    }
}
