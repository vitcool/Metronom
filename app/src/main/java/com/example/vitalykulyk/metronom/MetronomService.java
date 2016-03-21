package com.example.vitalykulyk.metronom;

/**
 * The Metronom program developed specially
 * according tecnical task
 * https://docs.google.com/document/d/1g_0IHQRu3aOm1sYKxWMtbhY8NjTABGInqpoFJAGX9Wg/edit
 *
 * @author  Vitalii Kulyk
 * @version 1.0
 * @since   2016-03-21
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * MetronomService is class extends Sevice and admit work with Metronom in new thread
 */

public class MetronomService extends Service {

    private static final String TAG = "MetronomService";

    private short noteValue = 4;
    private short beats = 4;

    private double beatSound = 2440;
    private double sound = 6440;

    /**
     * object of Metronom class
     */
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
            metronome.setIsSoundOn(intent.getExtras().getBoolean("isSoundOn", true));
            Log.i("isSoundOn valur", intent.getExtras().getBoolean("isSoundOn", true) + "");
            metronome.setIsVibrationOn(intent.getExtras().getBoolean("isVibrationOn", false));
        }

        /**
         * new thread of Service
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                _onStartSound(intent, startId);
            }
        }).start();
        return Service.START_STICKY;
    }

    /**
     * method sets metronom parameters like beat, note value, beat sound
     * * @param intent
     * @param startId
     */

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

    /**
     * method which calls when apps is closed
     */
    @Override
    public void onDestroy() {
        metronome.stop();
        metronome = null;
        Log.i(TAG, "Service onDestroy");
    }

    /**
     * onBind method
     * @param intent
     * @return IBinder
     */
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
