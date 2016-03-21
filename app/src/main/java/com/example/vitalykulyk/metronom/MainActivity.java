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


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * The MainActivity class is a class extends AppCompatActivity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Variables of UI
     */
    private ImageButton flashButton;
    private ImageButton vibrationButton;
    private ImageButton soundButton;

    private SeekBar seekBar;

    private RelativeLayout vibrationLayout;
    private RelativeLayout flashLayout;
    private RelativeLayout soundLayout;

    private Button startButton;

    private View view;

    private TextView vibrationText;
    private TextView soundText;
    private TextView flashText;
    private TextView speedEdit;

    /**
     * Variables to make my programm better
     */

    private boolean isFlashlightOn = true;
    private boolean isSoundOn = true;
    private boolean isVibrationOn = true;
    private boolean vibrationOn = true;
    private boolean flashOn = true;
    private boolean soundOn = true;
    private boolean isStarted;

    private int ms_per_beat;


    /**
     * @Override onCreate
     * method which calls first when the app execute
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();
        LoadPreferences();
        setsSeekbarValue();



        /**
         * add ability to write text into speedEdit TextView
         */
        speedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedEdit.setCursorVisible(true);
                speedEdit.setFocusableInTouchMode(true);
                speedEdit.setInputType(InputType.TYPE_CLASS_TEXT);
                speedEdit.requestFocus();
                try
                {
                    Integer.parseInt(speedEdit.getText().toString());
                }
                catch (NumberFormatException e)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Incorrect input (only numbers)", Toast.LENGTH_SHORT);
                    toast.show();
                    speedEdit.setText("100");
                }
                finally {
                    if ((Integer.parseInt(speedEdit.getText().toString()) > 200 ) || (Integer.parseInt(speedEdit.getText().toString()) == 0)){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Incorrect input (input number > 200) or equals 0", Toast.LENGTH_SHORT);
                        toast.show();
                        speedEdit.setText("100");
                    }
                }
                seekBar.setProgress(Integer.parseInt(speedEdit.getText().toString()));
            }
        });


        /**
         * set OnClickListener to vibrationButton as response to click on vibrationButton
         */
        vibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("vibration layout", vibrationOn + "");
                if (vibrationOn) {
                    vibrationOff();
                } else {
                    vibrationOn();
                }
                vibrationOn = !vibrationOn;
            }
        });


        /**
         * set OnClickListener to soundButton as response to click on soundButton
         */
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("sound layout", soundOn + "");
                if (soundOn) {
                    soundOff();
                } else {
                    soundOn();
                }
                soundOn = !soundOn;
            }
        });

        /**
         * set OnClickListener to flashButton as response to click on flashButton
         */
        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("flash layout", flashOn + "");
                if (flashOn) {
                    flashOff();
                } else {
                    flashOn();
                }
                flashOn = !flashOn;
            }
        });

        /**
         *  set OnClickListener to startButton as response to click on startButton
         *  program executing starts
         */

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStarted) {
                    startButtonStop();
                    startService();
                    startAnimation();

                } else {
                    startButtonStart();
                    stopService();
                    stopAnimation();
                }
                //ms_per_beat = Integer.parseInt(speedEdit.getText().toString());
                Log.i("ms_per_beat speed", speedEdit.getText().toString() + "");
                Log.i("ms_per_beat start button", ms_per_beat + "");
                isStarted = !isStarted;
            }
        });

        /**
         * customization of seekBar
         * start value is 100
         */

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                if (progresValue == 0){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Incorrect input - number equals 0", Toast.LENGTH_SHORT);
                    toast.show();
                    seekBar.setProgress(100);
                    progresValue = 100;
                }
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                speedEdit.setText(progress + "");
            }
        });
    }

    private void setsSeekbarValue() {


    }

    /**
     * method stops the animation of indicator
     */
    private void stopAnimation() {
        Animation anim = new AlphaAnimation(0.0f, 0.0f);
        view.startAnimation(anim);
    }

    /**
     * method stops service of metronom
     *
    */
    private void stopService() {
        stopService(
                new Intent(MainActivity.this, MetronomService.class));
    }

    /**
     * method sets text and background of startButton
     */
    private void startButtonStart() {
        startButton.setText(R.string.start);
        startButton.setBackgroundColor(Color.parseColor("#03a9f4"));

    }

    /**
     * method sets text and background of startButton
     */
    private void startButtonStop() {
        startButton.setText(R.string.stop);
        startButton.setBackgroundColor(Color.parseColor("#0BF2EA"));

    }

    /**
     * method starts animation of indicator
     */
    private void startAnimation() {

        ms_per_beat = 30000 / Integer.parseInt(speedEdit.getText().toString()) ;
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(ms_per_beat); //You can manage the time of the blink with this parameter
        Log.i("ms_per_beat Animation", ms_per_beat + "");
        anim.setStartOffset(0);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }

    /**
     * method starts service of metronom
     */
    private void startService() {
        Intent i = new Intent(MainActivity.this, MetronomService.class);
        i.putExtra("BPM", Short.parseShort(speedEdit.getText().toString()));
        i.putExtra("isSoundOn", isSoundOn);
        i.putExtra("isFlashlightOn", isFlashlightOn);
        i.putExtra("isVibrationOn", isVibrationOn);
        MainActivity.this.startService(i);
    }


    /**
     * onBackPressed mathod calls when back button pressed
     * execute method SavePreferences which save all preferences
     */
    @Override
    public void onBackPressed() {
        SavePreferences();
        super.onBackPressed();
    }

    /**
     * flashOn sets Image of flashButton and text color of flashText
     */
    private void flashOn() {
        flashButton.setImageResource(R.drawable.ic_flash_img);
        flashText.setTextColor(getResources().getColor(R.color.green));
        isFlashlightOn = true;
    }

    /**
     * flashOff sets Image of flashButton and text color of flashText
     */
    private void flashOff() {
        flashButton.setImageResource(R.drawable.ic_flash_off_img);
        flashText.setTextColor(getResources().getColor(R.color.red));
        isFlashlightOn = false;
    }

    /**
     * soundOn sets Image of soundButton and text color of soundText
     */
    private void soundOn() {
        soundButton.setImageResource(R.drawable.ic_sound_img);
        soundText.setTextColor(getResources().getColor(R.color.green));
        isSoundOn = true;
    }

    /**
     * soundOff sets Image of soundButton and text color of soundText
     */
    private void soundOff() {
        soundButton.setImageResource(R.drawable.ic_sound_off_img);
        soundText.setTextColor(getResources().getColor(R.color.red));
        isSoundOn = false;
    }

    /**
     * vibrationOn sets Image of vibrationButton and text color of vibrationText
     */
    private void vibrationOn() {
        vibrationButton.setImageResource(R.drawable.ic_vibration_img);
        vibrationText.setTextColor(getResources().getColor(R.color.green));
        isVibrationOn = true;

    }

    /**
     * vibrationOff sets Image of vibrationButton and text color of vibrationText
     */
    private void vibrationOff() {
        vibrationButton.setImageResource(R.drawable.ic_vibration_off_img);
        vibrationText.setTextColor(getResources().getColor(R.color.red));
        isVibrationOn = false;
    }



    /**
     * initializeVariables helps initializate all of UI variables
     * calls in OnCreate method
     */
    private void initializeVariables() {
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        speedEdit = (TextView) findViewById(R.id.speedEdit);
        vibrationButton = (ImageButton) findViewById(R.id.vibrationButton);
        flashButton = (ImageButton) findViewById(R.id.flashButton);
        soundButton = (ImageButton) findViewById(R.id.soundButton);
        vibrationLayout = (RelativeLayout) findViewById(R.id.vibrationLayout);
        soundLayout = (RelativeLayout) findViewById(R.id.soundLayout);
        flashLayout = (RelativeLayout) findViewById(R.id.flashLayout);
        startButton = (Button) findViewById(R.id.startButton);
        view = (View) findViewById(R.id.indicatorView);
        flashText = (TextView)findViewById(R.id.flashText);
        vibrationText = (TextView)findViewById(R.id.vibrationText);
        soundText = (TextView)findViewById(R.id.soundText);
    }


    /**
     * SavePreferences saved boolean variables into saved preferences
     */
    private void SavePreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFlashlightOn",isFlashlightOn);
        editor.putBoolean("isSoundOn    ", isSoundOn);
        editor.putBoolean("isVibrationOn", isVibrationOn);
        editor.putBoolean("isStarted    ", isStarted);
        editor.putInt("bpm",Integer.parseInt(speedEdit.getText().toString()));
        editor.commit();
    }

    /**
     * LoadPreferences load boolean variables from saved preferences
     */
    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        ms_per_beat = sharedPreferences.getInt("bpm", 100);
        seekBar.setProgress(ms_per_beat);
        /**
         * initialization of speedEdit by value of seekbar
         */
        speedEdit.setText(seekBar.getProgress() + "");

        Log.i("ms_per_beat Load", ms_per_beat + "");
        isFlashlightOn = sharedPreferences.getBoolean("isFlashlightOn", true);
        setFlashImage();
        isSoundOn     = sharedPreferences.getBoolean("isSoundOn    ", true);
        setSoundImage();
        isVibrationOn = sharedPreferences.getBoolean("isVibrationOn", true);
        setVibrationImage();
        isStarted     = sharedPreferences.getBoolean("isStarted    ", false);
        setStarted();

    }

    /**
     * method set color and text of button and start of animation
     */
    private void setStarted() {
        if (isStarted){
            startButtonStop();
            startAnimation();
        }
        else {
            startButtonStart();
        }
    }

    private void setVibrationImage() {
        if (isVibrationOn){
            vibrationOn();
        }
        else {
            vibrationOff();
        }
    }

    private void setSoundImage() {
        if (isSoundOn){
            soundOn();
        }
        else {
            soundOff();
        }
    }

    private void setFlashImage() {
        if (isFlashlightOn){
            flashOn();
        }
        else {
            flashOff();
        }
    }


}
