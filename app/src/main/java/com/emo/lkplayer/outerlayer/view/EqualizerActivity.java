package com.emo.lkplayer.outerlayer.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.emo.lkplayer.R;
import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.emo.lkplayer.middlelayer.viewmodel.EqualizerViewModel;
import com.emo.lkplayer.outerlayer.customviews.KnobController;
import com.emo.lkplayer.outerlayer.view.fragments.NavEqualizerFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavToneAndVolFragment;
import com.emo.lkplayer.outerlayer.view.navigation.NavigationManagerContentFlow;
import com.emo.lkplayer.outerlayer.view.navigation.NavigationManagerSettingsFlow;
import com.emo.lkplayer.utilities.Utility;
import com.h6ah4i.android.media.audiofx.IEqualizer;

import java.util.List;

public class EqualizerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationManagerContentFlow.NavigationListener, View.OnClickListener, LifecycleRegistryOwner {

    public static final String SHARED_EQVIEWMODEL_KEY = "shared_eq_viewmodel";

    @Override
    public LifecycleRegistry getLifecycle()
    {
        return registry;
    }

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    private NavigationManagerSettingsFlow mNavigationManager;

    /* View fields references */
    private KnobController knob_bass;
    private KnobController knob_treble;

    private Button btn_equ;
    private Button btn_tone;
    private Button btn_limit;
    private Button btn_preset;
    private Button btn_save;
    private Button btn_reset;

    /* ViewModel reference here */
    private EqualizerViewModel equalizerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        /* Get reference to viewModel at the start, viewSetup might use it */
        equalizerViewModel = ViewModelProviders.of(this).get(SHARED_EQVIEWMODEL_KEY,EqualizerViewModel.class);

        /* Gets references to all the view fields and does the view related initial work */
        setupActivityView();

        mNavigationManager = new NavigationManagerSettingsFlow();
        mNavigationManager.init(getSupportFragmentManager(), R.id.fragmentHolder_SettingsActivity);
        mNavigationManager.startUpInitialFragment_Equalizer();
    }

    private void setupActivityView()
    {
        getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navView_EqualizerAcitiy);
        navigation.getMenu().findItem(R.id.navMenu_toToneAndVolFrag_EqualizerActivity).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(this);

        knob_bass = (KnobController) findViewById(R.id.knob_bass);
        knob_treble = (KnobController) findViewById(R.id.knob_treble);

        btn_equ = (Button) findViewById(R.id.btn_equ);
        btn_tone = (Button) findViewById(R.id.btn_tone);
        btn_limit = (Button) findViewById(R.id.btn_limit);
        btn_preset = (Button) findViewById(R.id.btn_preset);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_reset = (Button) findViewById(R.id.btn_reset);

        btn_equ.setOnClickListener(this);
        btn_limit.setOnClickListener(this);
        btn_tone.setOnClickListener(this);
        btn_preset.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_reset.setOnClickListener(this);

        knob_bass.setBottomLeftText("Bass");
        knob_treble.setBottomLeftText("Treble");

        btn_equ.setActivated(equalizerViewModel.getEQUseState());
    }

    @Override
    public void onBackPressed()
    {
        mNavigationManager.navigateBack(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.navMenu_toEqualizerFrag_EqualizerActivity:
                if (!mNavigationManager.bringExistingFragment(NavEqualizerFragment.class.toString(), false))
                {
                    mNavigationManager.open(NavEqualizerFragment.newInstance(), true, false);
                }
                return true;
            case R.id.navMenu_toToneAndVolFrag_EqualizerActivity:
                if (!mNavigationManager.bringExistingFragment(NavToneAndVolFragment.class.toString(), false))
                {
                    mNavigationManager.open(NavToneAndVolFragment.newInstance(null, null), true, false);
                }
                return true;
            case R.id.navMenu_toPlaybackActivity_EqualizerActivity:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onBackstackChanged()
    {

    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btn_preset)
        {
            final List<EQPreset> presetArr = equalizerViewModel.getAllEqPresets();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EqualizerActivity.this, R.layout.itemview_icon_text, R.id.tv_itemView_icon_text);
            arrayAdapter.addAll(Utility.EQListToStringArray(presetArr));

            ListView listView = new ListView(EqualizerActivity.this);
            listView.setAdapter(arrayAdapter);

            AlertDialog.Builder builder = new AlertDialog.Builder(EqualizerActivity.this);
            builder.setView(listView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setLayout(800, 1000);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    try
                    {
                        presetArr.get(position).applyToEQ(equalizerViewModel.getEqualizerInstance());
                        equalizerViewModel.setEqPreset(presetArr.get(position));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    alertDialog.dismiss();
                }
            });
            return;
        } else if (v.getId() == R.id.btn_equ)
        {
            if (v.isActivated())
            {
                v.setActivated(false);
                equalizerViewModel.setEQUseState(false);
            } else
            {
                v.setActivated(true);
                equalizerViewModel.setEQUseState(true);
            }
        } else if (v.getId() == R.id.btn_reset)
        {
            /* We know that Flat Preset basically sets all bands to zero/centre */
            if (equalizerViewModel.getEQResetPreset() != null)
            {
                try
                {
                    equalizerViewModel.setEqPreset(equalizerViewModel.getEQResetPreset());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        } else if (v.getId() == R.id.btn_save)
        {
            final int[] bandLevelArr = new int[EQPreset.PRESET_ARR_SIZE];
            IEqualizer equalizer = equalizerViewModel.getEqualizerInstance();
            for (int i = 0; i < bandLevelArr.length && i < equalizer.getNumberOfBands(); i++)
            {
                bandLevelArr[i] = equalizer.getBandLevel((short) i);
            }

            final View addNewPresetView = View.inflate(this, R.layout.dialog_view_addnewpreset, null);
            Button addPresetButton = (Button) addNewPresetView.findViewById(R.id.btn_addPreset);

            AlertDialog.Builder builder = new AlertDialog.Builder(EqualizerActivity.this);
            builder.setView(addNewPresetView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            addPresetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    EditText editText = (EditText) addNewPresetView.findViewById(R.id.editText_presetName);
                    if (editText.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Preset name empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    equalizerViewModel.addNewPreset(editText.getText().toString(), bandLevelArr);
                    alertDialog.dismiss();
                }
            });
        } else
        {
            if (v.isActivated())
                v.setActivated(false);
            else
                v.setActivated(true);
        }

    }
}
