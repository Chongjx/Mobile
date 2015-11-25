package com.sidm.mgpnomoremushroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Mainmenu extends Activity implements OnClickListener
{
    private Button btn_start;
    private Button btn_quit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.mainmenu);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_quit = (Button)findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_start)
        {
            intent.setClass(this, WorldMap.class);
        }

        else if (v == btn_quit)
        {
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