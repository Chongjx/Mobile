package com.sidm.nomoremushroom;

/**
 * Created by Jun Xiang on 26/11/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class WorldMap extends Activity implements OnClickListener
{
    private Button btn_play1;
    private Button btn_play2;
    private Button btn_play3;
    private Button btn_options;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.worldmap);

        btn_play1 = (Button)findViewById(R.id.btn_play1);
        btn_play1.setOnClickListener(this);
        btn_play2 = (Button)findViewById(R.id.btn_play2);
        btn_play2.setOnClickListener(this);
        btn_play3 = (Button)findViewById(R.id.btn_play3);
        btn_play3.setOnClickListener(this);

        btn_options = (Button)findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_play1)
        {
            intent.setClass(this, GameLevel1.class);
        }

        else if (v == btn_play2)
        {
            intent.setClass(this, GameLevel2.class);
        }

        else if (v == btn_play3)
        {
            intent.setClass(this, GameLevel3.class);
        }

        else if (v == btn_options)
        {
            intent.setClass(this, Option.class);
        }

        startActivity(intent);
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