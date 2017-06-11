package com.emo.audomeda.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.emo.audomeda.R;
import com.emo.audomeda.view.fragments.AlbumArtFragment;

public class PlaybackActivity extends AppCompatActivity {

    private ViewPager albumArtSlider;
    private SliderPagerAdapter fragmentStatePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        fragmentStatePagerAdapter = new SliderPagerAdapter(getSupportFragmentManager());
        albumArtSlider = (ViewPager) findViewById(R.id.vp_AlbumArtSlider);
        albumArtSlider.setAdapter(fragmentStatePagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_playback,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menuBtn_ToneVol)
        {
            Intent intent = new Intent(PlaybackActivity.this,EqualizerActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.menuBtn_Equalizer){
            Intent intent = new Intent(PlaybackActivity.this,EqualizerActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.menuBtn_Library){
            Intent intent = new Intent(PlaybackActivity.this,NagizarActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public static class SliderPagerAdapter extends FragmentStatePagerAdapter{


        public SliderPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);

        }

        @Override
        public Fragment getItem(int position) {
            return AlbumArtFragment.newInstance(null);
        }

        @Override
        public int getCount() {
            return 4;
        }

    }
}
