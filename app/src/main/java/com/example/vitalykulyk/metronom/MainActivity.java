package com.example.vitalykulyk.metronom;


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


public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView speedEdit;
    private ImageButton flashButton;
    private ImageButton vibrationButton;
    private ImageButton soundButton;
    private boolean vibrationOn = true;
    private boolean flashOn = true;
    private boolean soundOn     = true;
    private RelativeLayout vibrationLayout;
    private RelativeLayout flashLayout;
    private RelativeLayout soundLayout;
    private Button startButton;
    private boolean isStarted;
    private TextView bmpText;
    private View view;

    private boolean isFlashlightOn   = true;
    private boolean isSoundOn        = true;
    private boolean isVibrationOn    = true;


    @Override
    public void onBackPressed() {
        SavePreferences();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();
        LoadPreferences();
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            Toast.makeText(this, "SavedInstance NOT null", Toast.LENGTH_SHORT).show();
        } else {
            // Probably initialize members with default values for a new instance
            Toast.makeText(this, "SavedInstance null", Toast.LENGTH_SHORT).show();
        }

        speedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedEdit.setCursorVisible(true);
                speedEdit.setFocusableInTouchMode(true);
                speedEdit.setInputType(InputType.TYPE_CLASS_TEXT);
                speedEdit.requestFocus();
                //seekBar.setProgress(Integer.parseInt((String) speedEdit.getText()));
            }
        });



        vibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("vibration layout", vibrationOn + "");
                if (vibrationOn) {
                    vibrationButton.setImageResource(R.drawable.ic_vibration_off_img);
                    isVibrationOn = false;
                } else {
                    vibrationButton.setImageResource(R.drawable.ic_vibration_img);
                    isVibrationOn = true;
                }
                vibrationOn = !vibrationOn;
            }
        });


        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("sound layout", soundOn + "");
                if (soundOn) {
                    soundButton.setImageResource(R.drawable.ic_sound_off_img);
                    isSoundOn = false;
                } else {
                    soundButton.setImageResource(R.drawable.ic_sound_img);
                    isSoundOn = true;
                }
                soundOn = !soundOn;
            }
        });


        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("flash layout", flashOn + "");
                if (flashOn) {
                    flashButton.setImageResource(R.drawable.ic_flash_off_img);
                    isFlashlightOn = false;
                } else {
                    flashButton.setImageResource(R.drawable.ic_flash_img);
                    isFlashlightOn = true;
                }
                flashOn = !flashOn;
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStarted) {
                    startButton.setText(R.string.stop);
                    startButton.setBackgroundColor(Color.parseColor("#0BF2EA"));
                    int ms_per_beat = 1000 * 60 / Short.parseShort(speedEdit.getText().toString());
                    Intent i = new Intent(MainActivity.this, MetronomService.class);
                    Log.i("OnClick", speedEdit.getText() + "");
                    i.putExtra("BPM", Short.parseShort(speedEdit.getText().toString()));
                    i.putExtra("isSoundOn", isSoundOn);
                    i.putExtra("isFlashlightOn", isFlashlightOn);
                    i.putExtra("isVibrationOn", isVibrationOn);
                    MainActivity.this.startService(i);
                    Animation anim = new AlphaAnimation(1.0f, 0.0f);
                    anim.setDuration(ms_per_beat * 2); //You can manage the time of the blink with this parameter
                    anim.setStartOffset(0);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    view.startAnimation(anim);


//                    startService(
//                            new Intent(MainActivity.this, MetronomService.class));
                } else {
                    startButton.setText(R.string.start);
                    startButton.setBackgroundColor(Color.parseColor("#03a9f4"));
                    stopService(
                            new Intent(MainActivity.this, MetronomService.class));
                    Animation anim = new AlphaAnimation(0.0f, 0.0f);
                    view.startAnimation(anim);
                }
                isStarted = !isStarted;
            }
        });


        seekBar.setProgress(100);
        // Initialize the textview with '0'.
        speedEdit.setText(seekBar.getProgress() + "");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
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

    // A private method to help us initialize our variables.
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
        bmpText = (TextView) findViewById(R.id.speedEdit);
        view = (View) findViewById(R.id.indicatorView);
    }

    private void flashlight() {

    }

    private void SavePreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFlashlightOn",isFlashlightOn);
        editor.putBoolean("isSoundOn    ", isSoundOn);
        editor.putBoolean("isVibrationOn", isVibrationOn);
        editor.putBoolean("isStarted    ", isStarted);
        editor.putInt("bpm",Integer.parseInt(speedEdit.getText().toString())*2);
        editor.commit();// I missed to save the data to preference here,.
    }

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        isFlashlightOn = sharedPreferences.getBoolean("isFlashlightOn", true);
        if (isFlashlightOn){
            flashButton.setImageResource(R.drawable.ic_flash_img);
        }
        else {
            flashButton.setImageResource(R.drawable.ic_flash_off_img);
        }
        isSoundOn     = sharedPreferences.getBoolean("isSoundOn    ", true);
        if (!isSoundOn){
            soundButton.setImageResource(R.drawable.ic_sound_img);
        }
        else {
            soundButton.setImageResource(R.drawable.ic_sound_off_img);
        }
        isVibrationOn = sharedPreferences.getBoolean("isVibrationOn", true);
        if (isVibrationOn){
            vibrationButton.setImageResource(R.drawable.ic_vibration_img);
        }
        else {
            vibrationButton.setImageResource(R.drawable.ic_vibration_off_img);
        }
        isStarted     = sharedPreferences.getBoolean("isStarted    ", false);
        if (isStarted){
            startButton.setText(R.string.stop);
            startButton.setBackgroundColor(Color.parseColor("#0BF2EA"));

            Animation anim = new AlphaAnimation(1.0f, 0.0f);
            int ms_per_beat = sharedPreferences.getInt("bpm", 0);
            anim.setDuration(ms_per_beat); //You can manage the time of the blink with this parameter
            anim.setStartOffset(0);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            view.startAnimation(anim);
        }
        else {
            startButton.setText(R.string.start);
            startButton.setBackgroundColor(Color.parseColor("#03a9f4"));
        }
    }





}
