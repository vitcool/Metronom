package com.example.vitalykulyk.metronom;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Vitaly Kulyk on 03.04.2016.
 */
public class SplashScreen extends Activity {
    // Splash screen timer

    TextView text1;
    TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int SPLASH_TIME_OUT;
        boolean isStarted = sharedPreferences.getBoolean("isStarted", false);
        if (isStarted){
            Log.i("isStarted", "TRUE");
            SPLASH_TIME_OUT = 0;
        }
        else
        {
            Log.i("isStarted", "FALSE");
            SPLASH_TIME_OUT = 700;
        }
        text1 = (TextView)findViewById(R.id.madeBy);
        text2 = (TextView)findViewById(R.id.MetronomeText);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "airstrikechrome.ttf");
        text1.setTypeface(myTypeface);
        text2.setTypeface(myTypeface);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
