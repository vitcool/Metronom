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

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
/**
 * MetronomService is class extends Sevice and admit work with Metronom in new thread
 */

public class MetronomService extends Service {

    public static boolean IS_SERVICE_RUNNING = false;


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
        showNotification();
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
        super.onDestroy();
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


    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, MetronomService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, MetronomService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, MetronomService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);


        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Metronome")
                .setTicker("TutorialsFace Music Player")
                .setContentText("Metronome-Service")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous, "Previous",
                        ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play, "Play",
                        pplayIntent)
                .addAction(android.R.drawable.ic_media_next, "Next",
                        pnextIntent).build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);
    }
    
}
