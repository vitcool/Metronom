package com.example.vitalykulyk.metronom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;


public class Metronome {
	
	private double bpm;
	private int beat;
	private int noteValue;
	private int silence;

	private double beatSound;
	private double sound;
	private final int tick = 1000; // samples of tick
	
	private boolean play = true;

	private boolean isFlashOn;
	private boolean isSoundOn;
	private boolean isVibrationOn;

	private AudioGenerator audioGenerator = new AudioGenerator(8000);
//	private Handler mHandler;
	private double[] soundTickArray;
	private double[] silenceSoundArray;
	private Message msg;
	private int currentBeat = 1;

	private Camera camera;
	private boolean hasFlash;
	Camera.Parameters params;
	Context context;

	private Vibrator v;

	public Metronome(Context context) {
		audioGenerator.createPlayer();
		this.context = context;


			v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);




		/*
 * First check if device is supporting flashlight or not
 */

			hasFlash = context.getApplicationContext().getPackageManager()
					.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

			if (!hasFlash) {
				// device doesn't support flash
				// Show alert message and close the application
				AlertDialog alert = new AlertDialog.Builder(context)
						.create();
				alert.setTitle("Error");
				alert.setMessage("Sorry, your device doesn't support flash light!");
				alert.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// closing the application
					}
				});
				alert.show();
				return;
			}
			getCamera();
	}

	private void getCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();
				params = camera.getParameters();
			} catch (RuntimeException e) {
				Log.e("Camera Error-Error: ", e.getMessage());
			}
		}
	}
	
	public void calcSilence() {
			silence = (int) (((60/bpm)*8000)-tick);
			soundTickArray = new double[this.tick];
			Log.i("calcSilence", silence + "");
			if (this.silence < Integer.MAX_VALUE){
				silenceSoundArray = new double[this.silence];
			}
			else {
				silence = (int) (((60/bpm)*8000)-tick);
				silenceSoundArray = new double[this.silence];
			}
			double[] tick = audioGenerator.getSineWave(this.tick, 8000, beatSound);
			for(int i=0;i<this.tick;i++) {
				soundTickArray[i] = tick[i];
			}
			for(int i=0;i<silence;i++){
				silenceSoundArray[i] = 0;
			}
	}
	
	public void play() throws InterruptedException {
		calcSilence();
		do {
			//vibration
			if (isVibrationOn) {
				Log.i("ISVIBRATIONON", "");
				v.vibrate(200);
			}
			//make sound
			if (isSoundOn) {
				audioGenerator.writeSound(soundTickArray);
			}
				audioGenerator.writeSound(silenceSoundArray);
				currentBeat++;
				if (currentBeat > beat)
					currentBeat = 1;
			//flashLightStart();
			if (isFlashOn){
				Log.i("ISFLASHON", "");
				flashLightStart();
				flashLightFinish();
			}
		} while(play);
	}
	
	public void stop() {
		play = false;
		audioGenerator.destroyAudioTrack();
		//flashLightFinish();
	}

	public void flashLightStart(){
		params = camera.getParameters();
		params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		camera.setParameters(params);
		camera.startPreview();

	}

	public void flashLightFinish()  {
		params = camera.getParameters();
		params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		camera.setParameters(params);

		camera.stopPreview();


	}



	public double getBpm() {
		return bpm;
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
	}

	public int getNoteValue() {
		return noteValue;
	}

	public void setNoteValue(int bpmetre) {
		this.noteValue = bpmetre;
	}

	public int getBeat() {
		return beat;
	}

	public void setBeat(int beat) {
		this.beat = beat;
	}

	public double getBeatSound() {
		return beatSound;
	}

	public void setBeatSound(double sound1) {
		this.beatSound = sound1;
	}

	public double getSound() {
		return sound;
	}

	public void setSound(double sound2) {
		this.sound = sound2;
	}

	public void setIsFlashOn(boolean isFlashOn) {
		this.isFlashOn = isFlashOn;
	}

	public void setIsSoundOn(boolean isSoundOn) {
		this.isSoundOn = isSoundOn;
	}

	public void setIsVibrationOn(boolean isVibrationOn) {
		this.isVibrationOn = isVibrationOn;
	}


}
