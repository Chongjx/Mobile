package com.sidm.mgpnomoremushroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Jun Xiang on 16/11/2015.
 */
public class Splashpage extends Activity
{
    protected boolean _active = true;
    protected int _splashTime = 5000; // time to display ms

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.splashpage);

        //thread for displaying the Splash Screen
        Thread splashTread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    int waited = 0;
                    while (_active && (waited < _splashTime))
                    {
                        sleep(200);
                        if (_active)
                        {
                            waited += 200;
                        }
                    }
                } catch (InterruptedException e)
                {
                    //do nothing
                } finally
                {
                    finish();

                    // Add codes
                    Intent intent = new Intent(Splashpage.this, Mainmenu.class);

                    startActivity(intent);
                }
            }
        };

        splashTread.start();
    }

    public boolean OnTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            _active = false;
        }

        return true;
    }

    protected void onPause()
    {
        super.onPause();
    }

    protected void onStop()
    {
        super.onStop();
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }
}
