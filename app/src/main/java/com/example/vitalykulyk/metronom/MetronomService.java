package com.example.vitalykulyk.metronom;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MetronomService extends Service {


    private static final String TAG = "MetronomService";

    private boolean isRunning  = false;

    private final short minBpm = 40;
    private final short maxBpm = 208;

    private short bpm = 100;
    private short noteValue = 4;
    private short beats = 4;
    private short volume;
    private short initialVolume;
    private double beatSound = 2440;
    private double sound = 6440;
    private AudioManager audio;

    private Button plusButton;
    private Button minusButton;
    private TextView currentBeat;

    // work with FlashLoght
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters params;

    Metronome metronome = new Metronome();

    public MetronomService() {

    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        isRunning = true;



        // First check if device is supporting flashlight or not
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(MetronomService.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.show();
            return;
        }

        // get the camera
        getCamera();
    }

    // Get the camera
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera - Failed to Open", e.getMessage());
            }
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        if (intent !=null && intent.getExtras()!=null){
            metronome.setBpm(intent.getExtras().getShort("BPM"));
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

        metronome.play();

    }

    @Override
    public void onDestroy() {
        isRunning = false;
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
