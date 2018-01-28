package com.emo.emomediaplayerpro.view_ui.customviews;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.emo.emomediaplayerpro.R;

public class SleepTimerDialog implements View.OnClickListener {

    public interface DialogCallbacks {
        void onTimerSet(int minutes, AlertDialog alertDialog);

        void onTimerdisable(AlertDialog alertDialog);
    }

    private Context context;

    private View dialogView;
    private EditText editText;
    private Button btn_setTimer;
    private Button btn_disableTimer;

    private AlertDialog alertDialog;
    private DialogCallbacks callbacks;

    public SleepTimerDialog(Context context, DialogCallbacks dialogCallbacks)
    {
        this.context = context;
        this.callbacks = dialogCallbacks;

        dialogView = View.inflate(context, R.layout.dialog_sleep_timer, null);
        editText = (EditText) dialogView.findViewById(R.id.editText_sleepDialog_minutes);
        btn_setTimer = (Button) dialogView.findViewById(R.id.btn_sleepDialog_set);
        btn_disableTimer = (Button) dialogView.findViewById(R.id.btn_sleepDialog_offtimer);

        btn_setTimer.setOnClickListener(this);
        btn_disableTimer.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        alertDialog = builder.create();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btn_sleepDialog_set)
        {
            if (editText.getText() != null && !editText.getText().toString().isEmpty())
            {
                if (this.callbacks != null)
                    this.callbacks.onTimerSet(Integer.valueOf(editText.getText().toString()), alertDialog);
            }

        } else if (v.getId() == R.id.btn_sleepDialog_offtimer)
        {
            if (this.callbacks != null)
                this.callbacks.onTimerdisable(alertDialog);
        }
    }

    public void show()
    {
        alertDialog.show();
    }
}
