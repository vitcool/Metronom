package com.example.vitalykulyk.metronom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Message;
import android.os.Vibrator;


public class Metronome {
	
	private double bpm;
	private int beat;
	private int noteValue;
	private int silence;

	private double beatSound;
	private double sound;
	private final int tick = 1000; // samples of tick
	
	private boolean play = true;
	
	private AudioGenerator audioGenerator = new AudioGenerator(8000);
//	private Handler mHandler;
	private double[] soundTickArray;
	private double[] silenceSoundArray;
	private Message msg;
	private int currentBeat = 1;

	private Camera camera;
	private boolean isFlashOn;
	private boolean hasFlash;
	Camera.Parameters params;
	Context context;

	private Vibrator v;

	public Metronome(Context context) {
		audioGenerator.createPlayer();
		this.context = context;
		v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	public void calcSilence() {
		silence = (int) (((60/bpm)*8000)-tick);		
		soundTickArray = new double[this.tick];	
		silenceSoundArray = new double[this.silence];
		double[] tick = audioGenerator.getSineWave(this.tick, 8000, beatSound);
		for(int i=0;i<this.tick;i++) {
			soundTickArray[i] = tick[i];
		}
		for(int i=0;i<silence;i++)
			silenceSoundArray[i] = 0;
	}
	
	public void play() {
		calcSilence();

//		params = camera.getParameters();
//		params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//		camera.setParameters(params);
//		camera.startPreview();
//		isFlashOn = true;

		do {
			//vibration
			v.vibrate(200);
			//make sound
			audioGenerator.writeSound(soundTickArray);
			audioGenerator.writeSound(silenceSoundArray);
			currentBeat++;
			if(currentBeat > beat)
				currentBeat = 1;
		} while(play);
	}
	
	public void stop() {
		play = false;
		audioGenerator.destroyAudioTrack();
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

}
