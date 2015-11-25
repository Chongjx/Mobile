package com.sidm.mgpnomoremushroom;

/**
 * Created by Jun Xiang on 25/11/2015.
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
    private Button btn_play;
    private Button btn_backMenu;
    private Button btn_options;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.worldmap);

        btn_play = (Button)findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);

        btn_backMenu = (Button)findViewById(R.id.btn_backMenu);
        btn_backMenu.setOnClickListener(this);

        btn_options = (Button)findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_play)
        {
            intent.setClass(this, Gamepage.class);
        }

        if (v == btn_backMenu)
        {
            intent.setClass(this, Mainmenu.class);
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