package com.sidm.nomoremushroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jun Xiang on 21/1/2016.
 */
public class Helppage  extends FragmentActivity
{
    private LoginButton loginBtn;
    private TextView userName;

    SharedPreferences SharePrefscore;

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.newpage);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        List<String> PERMISSIONS = Arrays.asList("publish_actions");

        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this, PERMISSIONS);
        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult)
            {
                sharePhotoToFacebook();
            }


            @Override
            public void onCancel() {
                userName.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                userName.setText("Login attempt failed.");

            }
        });
    }

    private void sharePhotoToFacebook()
    {
        int highscore = 0;

        SharePrefscore = getSharedPreferences("Scoredata", Context.MODE_PRIVATE);

        highscore = SharePrefscore.getInt("Passhighscore", 0);

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("You have played SIDM. Your current Score is " + highscore + ".")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
