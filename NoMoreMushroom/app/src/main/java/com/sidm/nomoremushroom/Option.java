package com.sidm.nomoremushroom;

/**
 * Created by Jun Xiang on 26/11/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class Option extends Activity implements OnClickListener
{
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private SharedPreferences score;
    private SharedPreferences.Editor scoreEditor;

    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.option);

        TextView textView = (TextView) findViewById(R.id.music);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Deanna.ttf");
        textView.setTypeface(font);

        textView = (TextView) findViewById(R.id.sfx);
        textView.setTypeface(font);

        textView = (TextView) findViewById(R.id.reset);
        textView.setTypeface(font);

        settings = getSharedPreferences("setting", Context.MODE_PRIVATE);

        score = getSharedPreferences("highscore", Context.MODE_PRIVATE);
        scoreEditor = score.edit();

        CheckBox checkbox = (CheckBox) findViewById(R.id.music);
        checkbox = (CheckBox)findViewById(R.id.music);
        checkbox.setChecked(settings.getBoolean("music", true));

        checkbox = (CheckBox)findViewById(R.id.sfx);
        checkbox.setChecked(settings.getBoolean("sfx", true));

        editor = settings.edit();

        checkbox = (CheckBox)findViewById(R.id.music);
        editor.putBoolean("music", checkbox.isChecked());
        checkbox = (CheckBox)findViewById(R.id.sfx);
        editor.putBoolean("sfx", checkbox.isChecked());
        editor.commit();

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

    public void toggleMusic(View v)
    {
        CheckBox checkbox = (CheckBox)v;

        editor.putBoolean("music", checkbox.isChecked());
        editor.commit();
    }

    public void toggleSFX(View v)
    {
        CheckBox checkbox = (CheckBox)v;

        editor.putBoolean("sfx", checkbox.isChecked());
        editor.commit();
    }

    public void resetGame(View v)
    {
        scoreEditor.putInt("level1highscore", 0);
        scoreEditor.putInt("level2highscore", 0);
        scoreEditor.putInt("level3highscore", 0);
        scoreEditor.commit();
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