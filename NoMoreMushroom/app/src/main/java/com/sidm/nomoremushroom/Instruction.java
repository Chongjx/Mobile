package com.sidm.nomoremushroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Jun Xiang on 15/1/2016.
 */
public class Instruction extends Activity implements OnClickListener
{
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.instruction);

        btn_back = (ImageButton)findViewById(R.id.back);
        btn_back.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_back)
        {
            intent.setClass(this, WorldMap.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
