package com.org.iii.will18;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.goodgood);
        int len = mediaPlayer.getDuration();
//        Log.v("will", "len:" + len);
        Intent it = new Intent("will");
        it.putExtra("len", len);
        sendBroadcast(it);

        timer = new Timer();
        timer.schedule(new MyTask(), 0, 500);

    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                Intent it = new Intent("will");
                it.putExtra("now", mediaPlayer.getCurrentPosition());
                sendBroadcast(it);
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isPause = intent.getBooleanExtra("will", false);
        int seekto = intent.getIntExtra("seekto", -1);
        if (mediaPlayer != null && seekto >= 0) {
            mediaPlayer.seekTo(seekto);

        } else if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            if (!isPause) {
                mediaPlayer.start();
            }
        } else if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            if (isPause) {
                mediaPlayer.pause();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;

        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

}
