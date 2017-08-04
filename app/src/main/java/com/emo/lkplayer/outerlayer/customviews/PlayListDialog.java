package com.emo.lkplayer.outerlayer.customviews;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.emo.lkplayer.R;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 8/2/17.
 */

public class PlayListDialog implements AdapterView.OnItemClickListener, View.OnClickListener {

    public interface DialogInteractionEventsListener{
        void onItemClickListener(AdapterView<?> parent,View view,int position, long id,AlertDialog playListDialog);
        void onNewPlayListCreated(String playListName);
    }

    private ListView listView = null;
    private Context parentContext = null;
    private ArrayAdapter<String> arrayAdapter = null;
    private List<Playlist.UserDefinedPlaylist> list_Playlist = null;

    private View dialogView = null;
    private Button btn_NewPlaylist = null;
    private AlertDialog alertDialog = null;

    private TextView textView_createNewPlaylist = null;
    private Button btn_playlistAddingDone = null;
    private EditText editText_newPlaylistName = null;

    private TextView textView_No_Playlist_Content = null;
    private DialogInteractionEventsListener dialogInteractionEventsListener;


    public PlayListDialog(Context parentContext ,List<Playlist.UserDefinedPlaylist> list_Playlist)
    {
        this.parentContext = parentContext;
        if (list_Playlist==null)
            this.list_Playlist = new ArrayList<>();
        else
            this.list_Playlist = list_Playlist;
    }

    public void initPlusBuildDialog(DialogInteractionEventsListener dialogInteractionEventsListener)
    {
        /* register the client for this dialog */
        this.dialogInteractionEventsListener = dialogInteractionEventsListener;
        /* Inflate the dialog view */
        dialogView = View.inflate(parentContext,R.layout.dialog_playlist_view,null);
        /* Get Reference to Add New Playlist Button */
        btn_NewPlaylist = (Button) dialogView.findViewById(R.id.btn_dialog_playlist_addNewPlaylist);
        btn_NewPlaylist.setOnClickListener(this);

        textView_No_Playlist_Content = (TextView) dialogView.findViewById(R.id.tv_no_playlist_content);
        /* setup array adapter for the listview */
        arrayAdapter = new ArrayAdapter<String>(parentContext,R.layout.itemview_playlist_icontext,R.id.tv_itemView_playlist);
        /* pass list of playlist names to adapter*/
        arrayAdapter.addAll(Utility.PlaylistListToStringArray(list_Playlist));
        /* Setup the list view will that is required*/
        listView = (ListView) dialogView.findViewById(R.id.listView_dialog_playlist);
        listView.setAdapter(arrayAdapter);
        /* set onItemClickListener for the list view */
        listView.setOnItemClickListener(this);

        if (list_Playlist==null || list_Playlist.size()<=0)
        {
            listView.setVisibility(View.GONE);
            textView_No_Playlist_Content.setVisibility(View.VISIBLE);
        }
        else
        {
            textView_No_Playlist_Content.setVisibility(View.GONE);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(parentContext);
        builder.setView(dialogView);

        alertDialog = builder.create();

        alertDialog.show();
        //alertDialog.getWindow().setLayout(800, 1200);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (dialogInteractionEventsListener!=null)
            dialogInteractionEventsListener.onItemClickListener(parent,view,position,id,alertDialog);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId()==R.id.btn_dialog_playlist_addNewPlaylist)
        {
            btn_NewPlaylist.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            textView_No_Playlist_Content.setVisibility(View.GONE);

            textView_createNewPlaylist = (TextView) dialogView.findViewById(R.id.tv_createNewPlaylist);
            textView_createNewPlaylist.setVisibility(View.VISIBLE);

            btn_playlistAddingDone = (Button) dialogView.findViewById(R.id.btn_playlistAddingDone);
            btn_playlistAddingDone.setOnClickListener(this);
            btn_playlistAddingDone.setVisibility(View.VISIBLE);

            editText_newPlaylistName = (EditText) dialogView.findViewById(R.id.editText_playlistName);
            editText_newPlaylistName.setVisibility(View.VISIBLE);
        }

        if (v.getId()==R.id.btn_playlistAddingDone)
        {
            if (dialogInteractionEventsListener!=null && editText_newPlaylistName.getText()!=null)
                dialogInteractionEventsListener.onNewPlayListCreated(editText_newPlaylistName.getText().toString());
            alertDialog.dismiss();
        }
    }
}
