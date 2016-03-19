package com.example.vitalykulyk.metronom;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MetronomService extends Service {


    private static final String TAG = "MetronomService";

    private short noteValue = 4;
    private short beats = 4;

    private double beatSound = 2440;
    private double sound = 6440;

    private boolean isSoundOn;
    private boolean isVibrationOn ;
    private boolean isFlashlightOn;


    // work with FlashLoght
    private boolean hasFlash;

    Metronome metronome;

    public MetronomService() {

    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        metronome = new Metronome(this);
    }



    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        if (intent !=null && intent.getExtras()!=null){
            metronome.setBpm(intent.getExtras().getShort("BPM"));
            metronome.setIsFlashOn(intent.getExtras().getBoolean("isFlashlightOn", false));
            metronome.setIsSoundOn(intent.getExtras().getBoolean("isSoundOn", false));
            metronome.setIsVibrationOn(intent.getExtras().getBoolean("isVibrationOn", false));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                _onStartSound(intent, startId);
                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.

                //Stop service once it finishes its task

            }
        }).start();
        return Service.START_STICKY;
    }

    private void _onStartSound(final Intent intent, final int startId){
        metronome.setBeat(beats);
        metronome.setNoteValue(noteValue);
        metronome.setBeatSound(beatSound);
        metronome.setSound(sound);

        try {
            metronome.play();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        metronome.stop();
        metronome = null;
        Log.i(TAG, "Service onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
