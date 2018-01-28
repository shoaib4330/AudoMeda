package com.emo.emomediaplayerpro.view_ui.customviews;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.emo.emomediaplayerpro.R;
import com.emo.emomediaplayerpro.model.domain.entities.EQPreset;
import com.emo.emomediaplayerpro.utilities.Utility;

import java.util.List;


public final class PresetAlertDialog {

    private Context parentContext = null;
    private List<EQPreset> presetList = null;
    private ListView listView = null;
    private ArrayAdapter<String> arrayAdapter = null;

    private AlertDialog alertDialog = null;

    public PresetAlertDialog(Context parentContext ,List<EQPreset> presetList)
    {
        this.parentContext = parentContext;
        this.presetList = presetList;
    }

    public void initPlusBuildDialog(AdapterView.OnItemClickListener onItemClickListener)
    {
        arrayAdapter = new ArrayAdapter<String>(parentContext, R.layout.itemview_icon_text, R.id.tv_itemView_icon_text);
        arrayAdapter.addAll(Utility.EQListToStringArray(presetList));
        listView =  new ListView(parentContext);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onItemClickListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(parentContext);
        builder.setView(listView);

        alertDialog = builder.create();

        alertDialog.show();
        alertDialog.getWindow().setLayout(800, 1000);

    }


}
