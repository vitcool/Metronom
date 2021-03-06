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

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * AudioGenerator allow work with audio
 */

public class AudioGenerator {
	
    private int sampleRate;
    private AudioTrack audioTrack;
    
    public AudioGenerator(int sampleRate) {
    	this.sampleRate = sampleRate;
    }

    /**
     * getSineWave generate samples
     * @param samples
     * @param sampleRate
     * @param frequencyOfTone
     * @return double[] sample
     */
    
    public double[] getSineWave(int samples,int sampleRate,double frequencyOfTone) {
    	double[] sample = new double[samples];
        for (int i = 0; i < samples; i++) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / frequencyOfTone));
        }
		return sample;
    }

    /**
     * generate sounds
     * @param samples
     * @return generatedSound
     */
    public byte[] get16BitPcm(double[] samples) {
    	byte[] generatedSound = new byte[2 * samples.length];
    	int index = 0;
        for (double sample : samples) {
            // scale to maximum amplitude
            short maxSample = (short) ((sample * Short.MAX_VALUE));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSound[index++] = (byte) (maxSample & 0x00ff);
            generatedSound[index++] = (byte) ((maxSample & 0xff00) >>> 8);

        }
    	return generatedSound;
    }

    /**
     * create and customize new AudioTrack
     */
    public void createPlayer(){
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, sampleRate,
                AudioTrack.MODE_STREAM);
    	audioTrack.play();
    }

    /**
     * write sound in player
     * @param samples
     */
    public void writeSound(double[] samples) {
    	byte[] generatedSnd = get16BitPcm(samples);
    	audioTrack.write(generatedSnd, 0, generatedSnd.length);
    }

    /**
     * destroy audio player
     */
    public void destroyAudioTrack() {
    	audioTrack.stop();
    	audioTrack.release();
    }
    
}
