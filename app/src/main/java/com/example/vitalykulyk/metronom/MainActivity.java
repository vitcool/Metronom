package com.example.vitalykulyk.metronom;


import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private boolean soundOn = true;
    private RelativeLayout vibrationLayout;
    private RelativeLayout flashLayout;
    private RelativeLayout soundLayout;
    private Button startButton;
    private boolean isStarted;
    private TextView bmpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();

        camera = Camera.open();
        params = camera.getParameters();


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
                if (vibrationOn){
                    vibrationButton.setImageResource(R.drawable.ic_vibration_off_img);
                    vibrationOn = false;
                }
                else{
                    vibrationButton.setImageResource(R.drawable.ic_vibration_img);
                    vibrationOn = true;
                }

            }
        });


        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("sound layout", soundOn + "");
                if (soundOn){
                    soundButton.setImageResource(R.drawable.ic_sound_off_img);
                }
                else{
                    soundButton.setImageResource(R.drawable.ic_sound_img);
                }
                soundOn = !soundOn;
            }
        });


        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("flash layout", flashOn + "");
                if (flashOn){
                    flashButton.setImageResource(R.drawable.ic_flash_off_img);
                }
                else{
                    flashButton.setImageResource(R.drawable.ic_flash_img);
                }
                flashOn = !flashOn;
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStarted){
                    startButton.setText(R.string.stop);
                    startButton.setBackgroundColor(Color.parseColor("#010101"));
                    Intent i = new Intent(MainActivity.this, MetronomService.class);
                    Log.i("OnClick", speedEdit.getText() + "");
                    i.putExtra("BPM", Short.parseShort(speedEdit.getText().toString()));
                    MainActivity.this.startService(i);

//                    startService(
//                            new Intent(MainActivity.this, MetronomService.class));
                }
                else{
                    startButton.setText(R.string.start);
                    startButton.setBackgroundColor(Color.parseColor("#03a9f4"));
                    stopService(
                            new Intent(MainActivity.this, MetronomService.class));
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
        startButton = (Button)findViewById(R.id.startButton);
        bmpText = (TextView) findViewById(R.id.speedEdit);
    }

    private void flashlight(){

    }
}
