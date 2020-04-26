package com.ngo.finder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_splash_screen);

        t=new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(SPLASH_TIME_OUT);
                    Intent i=new Intent(SplashScreen.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                catch (Exception e)
                {}
            }
        };
        t.start();
    }

}