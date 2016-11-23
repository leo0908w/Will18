package com.org.iii.will18;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private MyReceiver receiver;
    private SoundPool sp;
    private static int sound1, sound2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter("will"));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    Intent it = new Intent(MainActivity.this, MyService.class);
                    it.putExtra("seekto", i);
                    startService(it);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        sound1 = sp.load(this, R.raw.nnn, 1);
        sound2 = sp.load(this, R.raw.ddd, 1);

    }

    @Override
    public void finish() {
        Intent it = new Intent(this, MyService.class);
        stopService(it);

        unregisterReceiver(receiver);

        super.finish();
    }

    public void start(View v) {
        Intent it = new Intent(this, MyService.class);
        startService(it);
    }

    public void pause(View v) {
        Intent it = new Intent(this, MyService.class);
        it.getBooleanExtra("isPause", true);
        startService(it);
    }

    public void stop(View v) {
        Intent it = new Intent(this, MyService.class);
        stopService(it);
    }

    public void sound1(View v) {
        sp.play(sound1,0.5f,0.5f,1,0,1);
    }
    public void sound2(View v) {
        sp.play(sound2,0.5f,0.5f,1,0,1);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int len = intent.getIntExtra("len", -1);
            int now = intent.getIntExtra("now", -1);
            if (len > 0) {
                seekBar.setMax(len);
            }
            if (now > 0) {
                seekBar.setProgress(now);
            }
        }
    }
}
