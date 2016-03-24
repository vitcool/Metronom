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
			//check phone to camera supports
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

	}

	/**
	 * set parameters params and camera
	 */
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

	/**
	 * calculate silence in player
	 */
	public void calcSilence() {
			silence = (int) (((60/bpm)*8000)-tick);
			soundTickArray = new double[this.tick];
			Log.i("calcSilence", silence + "");
			if (this.silence < Integer.MAX_VALUE){
				silenceSoundArray = new double[this.silence];
			}
			else {
				silence = 1;//(int) (((60/bpm)*8000)-tick);
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

	/**
	 * make one "tick" of metronom
	 * @throws InterruptedException
	 */
	public void play() throws InterruptedException {
		calcSilence();
		do {
			//make vibration if it needs
			if (isVibrationOn) {
				Log.i("ISVIBRATIONON", "");
				v.vibrate(50);
			}
			//make sound if it needs
			if (isSoundOn) {
				audioGenerator.writeSound(soundTickArray);
			}
				audioGenerator.writeSound(silenceSoundArray);
				currentBeat++;
				if (currentBeat > beat)
					currentBeat = 1;
			//make flashlight if it needs
			if (isFlashOn){
				Log.i("ISFLASHON", "");
				flashLightStart();
				flashLightFinish();
			}
		} while(play);
	}


	/**
	 * stops process of metronom playing
	 */
	public void stop() {
		play = false;
		audioGenerator.destroyAudioTrack();
		//flashLightFinish();
	}

	public void flashLightStart(){
		getCamera();
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

	/**
	 * getters and setters
	 * @return bpm
	 */
	public double getBpm() {
		return bpm;
	}

	/**
	 * sets value of bmp
	 * @param bpm
	 */

	public void setBpm(int bpm) {
		this.bpm = bpm;
	}

	/**
	 * getters and setters
	 * @return noteValue
	 */

	public int getNoteValue() {
		return noteValue;
	}

	/**
	 * sets value of noteValue
	 * @param bpmetre
	 */

	public void setNoteValue(int bpmetre) {
		this.noteValue = bpmetre;
	}

	/**
	 * getters and setters
	 * @return beat
	 */

	public int getBeat() {
		return beat;
	}

	/**
	 * sets vlue of beat
	 * @param beat
	 */
	public void setBeat(int beat) {
		this.beat = beat;
	}

	/**
	 * getters and setters
	 * @return beat sound
	 */
	public double getBeatSound() {
		return beatSound;
	}

	/**
	 * sets value of beatSound variable
	 * @param sound1
	 */
	public void setBeatSound(double sound1) {
		this.beatSound = sound1;
	}

	/**
	 * getters and setters
	 * @return sound
	 */

	public double getSound() {
		return sound;
	}

	/**
	 * getters and setters
	 * sets value of sound
	 */

	public void setSound(double sound2) {
		this.sound = sound2;
	}

	/**
	 * getters and setters
	 * sets value of isFlash variable
	 */

	public void setIsFlashOn(boolean isFlashOn) {
		this.isFlashOn = isFlashOn;
	}

	/**
	 * getters and setters
	 * sets value of IsSoundOn variable
	 */

	public void setIsSoundOn(boolean isSoundOn) {
		this.isSoundOn = isSoundOn;
	}

	/**
	 * getters and setters
	 * sets value of isVibrationOn variable
	 */

	public void setIsVibrationOn(boolean isVibrationOn) {
		this.isVibrationOn = isVibrationOn;
	}


}
