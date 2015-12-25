package com.sidm.nomoremushroom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Filter;
import java.util.regex.Pattern;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;
    private Bitmap cross;
    private boolean running;

    // 1b) Define Screen width and Screen height as integer
    int screenWidth, screenHeight;

    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;

    private int moveSpeed;
    private float vibrateTimer;

    // 4a) bitmap array to stores 4 images of the spaceship
    //private Bitmap[] ship = new Bitmap[4];

    // 4b) Variable as an index to keep track of the spaceship images
    int frame = 0;
    float changeFrame = 0;

    // create paint to paint on canvas
    Paint paint = new Paint();
    Paint CDPaint = new Paint();
    Paint RectPaint = new Paint();
    Paint gameOverPaint = new Paint();
    // create rand and seed it
    Random rand = new Random(System.currentTimeMillis());

    // Variables for FPS
    public float FPS;
    float deltaTime;

    // store the touch location
    private short mX = 0, mY = 0;

    // Variable for Game State check

    private enum GAME_STATE
    {
        STATE_COUNTDOWN,
        STATE_PLAY,
        STATE_END
    }

    // Game State
    private GAME_STATE GameState;

    // check if the screen is touched
    private boolean miss;

    // store the mushrooms
    private Vector<Monster> monsterVector;

    // store the spawnPosition of the mushroom
    private Vector<Vec2> spawnPosition;
    private int xOffset;
    private float gameSpeed;
    private HashMap<String, SpriteAnimation> animationHashMap;

    private int level;
    private int gameScore;
    private int mushCount;
    private int maxMon;

    private float touchTimer;
    private float countDownTimer;
    private boolean toDisplay;
    private int alphaChange;
    // change rate of the alpha
    private float changeRate;

    // Use of vibration for feedback
    public Vibrator v;

    MediaPlayer bgm;

    private SoundPool sounds;
    private int soundCorrect, soundWrong;

    public boolean showAlert = false;
    AlertDialog.Builder alert = null;
    Activity activityTracker;
    public boolean showed = false;
    private Alert AlertObj;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView(Context context, Activity activity, int level)
    {
        // Context is the current state of the application/object
        super(context);

        // To track an activity
        activityTracker = activity;

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.worldmap);
        cross = BitmapFactory.decodeResource(getResources(), R.drawable.cross);
        cross = Bitmap.createScaledBitmap(cross, 100, 100, true);
        scaledbg = Bitmap.createScaledBitmap(bg, screenWidth, screenHeight, true);

        // Create the game loop thread
        bgm = MediaPlayer.create(context, R.raw.forest);
        myThread = new GameThread(getHolder(), this);

        Start();

        this.level = level;
        paint.setARGB(255, 0, 0, 0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);

        CDPaint.setARGB(255, 0, 0, 0);
        CDPaint.setStrokeWidth(100);
        CDPaint.setTextSize(120);
        CDPaint.setColor(Color.RED);

        RectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        RectPaint.setColor(Color.GRAY);

        gameOverPaint.setColor(Color.argb(125, 125, 125, 125));

        GameState = GAME_STATE.STATE_COUNTDOWN;

        moveSpeed = 0;

        gameSpeed = 30.f;
        gameScore = 0;
        mushCount = 0;
        maxMon = 3;
        xOffset = 150;
        deltaTime = 0.f;
        touchTimer = 0.f;
        countDownTimer = 3.f;
        toDisplay = false;
        alphaChange = 0;
        changeRate = 750;
        vibrateTimer = 0.5f;

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        //stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64,
        //        5, 5);
        //stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.badmush), 320, 64,
        //5, 6);
        //stone_anim.setX(0);
        //stone_anim.setY(600);

        animationHashMap = new HashMap<String, SpriteAnimation>();
        monsterVector = new Vector<Monster>();
        spawnPosition = new Vector<Vec2>();

        // Create all the sprite animations and store them into the hashmap
        {
            SpriteAnimation sprite;
            sprite = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.zombie_left), 320, 64,
                    7, 5);
            animationHashMap.put("ZOMBIELEFT", sprite);
            sprite = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.zombie_right), 320, 64,
                    7, 5);
            animationHashMap.put("ZOMBIERIGHT", sprite);
            sprite = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.raven_left), 320, 64,
                    5, 6);
            animationHashMap.put("RAVENLEFT", sprite);
            sprite = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.raven_right), 320, 64,
                    5, 6);
            animationHashMap.put("RAVENRIGHT", sprite);
            sprite = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.snail_left), 320, 64,
                    5, 8);
            animationHashMap.put("SNAILLEFT", sprite);
            sprite = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.snail_right), 320, 64,
                    5, 8);
            animationHashMap.put("SNAILRIGHT", sprite);
        }

        // add 10 mush into the vector first
        for (int i = 0; i < 10; ++i)
        {
            Monster mon = new Monster();
            mon.setUpdate(false);
            mon.setRender(false);
            monsterVector.add(mon);
        }

        /*Vec2 maxStart = new Vec2();
        maxStart.set(1, 2);

        Vec2 minStart = new Vec2();
        minStart.set(0, 0);

        //int yDivision = screenHeight / 2;
        // Create spawn location first
        /*spawnPosition.add(new Vec2(-xOffset,               (yDivision + 5) * 1 - yDivision + 30));
        spawnPosition.add(new Vec2(-xOffset,               (yDivision + 5) * 2 - yDivision + 30));
        spawnPosition.add(new Vec2(-xOffset,               (yDivision + 5) * 3 - yDivision + 30));
        spawnPosition.add(new Vec2(screenWidth + xOffset/2,(yDivision + 5) * 1 - yDivision + 30));
        spawnPosition.add(new Vec2(screenWidth + xOffset/2, (yDivision + 5) * 2 - yDivision + 30));
        spawnPosition.add(new Vec2(screenWidth + xOffset/2, (yDivision + 5) * 3 - yDivision + 30));*/

        /*for (int i = 1; i < 3; ++i)
        {
            spawnPosition.add(new Vec2(-xOffset, (yDivision) * (i - 1) + 75));
            spawnPosition.add(new Vec2(screenWidth + xOffset / 2, (yDivision) * (i - 1) + 75));
        }*/

        int yHeight = 125;
        // bottom left
        spawnPosition.add(new Vec2(-xOffset, screenHeight - yHeight));
        // bottom right
        spawnPosition.add(new Vec2(screenWidth + xOffset / 2, screenHeight - yHeight));
        // top left
        spawnPosition.add(new Vec2(-xOffset, 100));
        // top right
        spawnPosition.add(new Vec2(screenWidth + xOffset / 2, 100));

        // spawn 2 mush first
        for (int i = 0; i < monsterVector.size() && mushCount < 2; ++i)
        {
            if (monsterVector.elementAt(i).getRender() == false)
            {
                int spawnIndex = rand.nextInt(2);

                int spriteHeight = animationHashMap.get("ZOMBIERIGHT").getSpriteHeight();

                if (spawnPosition.elementAt(spawnIndex).x <= -xOffset)
                {
                    monsterVector.elementAt(i).setSprite(animationHashMap.get("ZOMBIERIGHT"));
                }

                else
                {
                    monsterVector.elementAt(i).setSprite(animationHashMap.get("ZOMBIELEFT"));
                }

                monsterVector.elementAt(i).init(rand.nextInt((screenWidth - 100) + 1), spawnPosition.elementAt
                        (spawnIndex).y - spriteHeight, 0, 0, Monster.MON_TYPE.MON_ZOMBIE, 5.f);
                ++mushCount;
            }
        }

        miss = false;

        sounds = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        soundCorrect = sounds.load(context, R.raw.correct, 1);
        soundWrong = sounds.load(context, R.raw.incorrect, 1);

        // create alert dialog
        AlertObj = new Alert(this);
        alert = new AlertDialog.Builder(getContext());

        // Allow players to input their name
        final EditText input = new EditText(getContext());

        // Define the input method where 'enter' key is disabled
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        // Define max of 20 characters to be entered for 'Name' field
        int maxLength = 20;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        input.setFilters(FilterArray);

        // setup the alert dialog
        alert.setCancelable(false);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1)
            {
                Intent intent = new Intent();
                intent.setClass(getContext(), WorldMap.class);
                activityTracker.startActivity(intent);
            }
        });
    }

    public void Start()
    {
        if (!running)
        {
            myThread.start();
            running = true;
        } else
        {
            myThread.unPause();
        }
    }

    public void spawnMush(Monster.MON_TYPE type)
    {
        for (int i = 0; i < monsterVector.size(); ++i)
        {
            if (monsterVector.elementAt(i).getRender() == false)
            {
                int spawnIndex = spawnIndex = rand.nextInt(2);

                // if spawning land monster
                if (type == Monster.MON_TYPE.MON_RAVEN)
                {
                    spawnIndex += 2;
                }

                int spriteHeight = 0;
                if (spawnPosition.elementAt(spawnIndex).x <= -xOffset)
                {
                    switch (type)
                    {
                        case MON_ZOMBIE:
                        {
                            monsterVector.elementAt(i).setSprite(animationHashMap.get("ZOMBIERIGHT"));
                            spriteHeight = animationHashMap.get("ZOMBIERIGHT").getSpriteHeight();
                            break;
                        }
                        case MON_RAVEN:
                        {
                            monsterVector.elementAt(i).setSprite(animationHashMap.get("RAVENRIGHT"));
                            break;
                        }
                        case MON_SNAIL:
                        {
                            monsterVector.elementAt(i).setSprite(animationHashMap.get("SNAILRIGHT"));
                            spriteHeight = animationHashMap.get("SNAILRIGHT").getSpriteHeight();
                            break;
                        }
                    }

                    monsterVector.elementAt(i).init(spawnPosition.elementAt(spawnIndex).x, spawnPosition.elementAt
                            (spawnIndex).y - spriteHeight, 1, 0, type, 5.f);
                }
                else
                {
                    switch (type)
                    {
                        case MON_ZOMBIE:
                        {
                            monsterVector.elementAt(i).setSprite(animationHashMap.get("ZOMBIELEFT"));
                            spriteHeight = animationHashMap.get("ZOMBIELEFT").getSpriteHeight();
                            break;
                        }
                        case MON_RAVEN:
                        {
                            monsterVector.elementAt(i).setSprite(animationHashMap.get("RAVENLEFT"));
                            break;
                        }
                        case MON_SNAIL:
                        {
                            monsterVector.elementAt(i).setSprite(animationHashMap.get("SNAILLEFT"));
                            spriteHeight = animationHashMap.get("SNAILLEFT").getSpriteHeight();
                            break;
                        }
                    }

                    monsterVector.elementAt(i).init(spawnPosition.elementAt(spawnIndex).x, spawnPosition.elementAt
                            (spawnIndex).y - spriteHeight, -1, 0, type, 5.f);
                }


                ++mushCount;
                break;
            }
        }
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder)
    {
        // Create the thread
        if (!myThread.isAlive())
        {
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
        bgm.setVolume(0.8f, 0.8f);
        bgm.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // Destroy the thread
        if (myThread.isAlive())
        {
            myThread.startRun(false);
        }
        boolean retry = true;
        while (retry)
        {
            try
            {
                myThread.join();
                retry = false;
            } catch (InterruptedException e)
            {
            }
        }

        bgm.stop();
        bgm.release();

        sounds.unload(soundCorrect);
        sounds.unload(soundWrong);
        sounds.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    public void RenderBlank(Canvas canvas)
    {
        if (canvas == null)
        {
            return;
        }
        // 0 alpha is transparent
        // 255 is opaque
        canvas.drawARGB(alphaChange, 0, 0, 0);
        //canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
    }

    public void RenderGameplay(Canvas canvas)
    {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null)
        {
            return;
        }
        //canvas.drawARGB(255, 255, 255, 255);
        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        //canvas.drawBitmap(scaledbg, bgX + screenWidth, bgY, null);

        //for (int i = 0; i < platform.size(); ++i)
        //{
        //    canvas.drawRect(platform.elementAt(i), RectPaint);
        //}

        // 4d) Draw the spaceships
        //canvas.drawBitmap(ship[frame], mX, mY, null);

        for (int i = 0; i < monsterVector.size(); ++i)
        {
            if (monsterVector.elementAt(i).getRender() == true)
            {
                monsterVector.elementAt(i).getSprite().draw(canvas);
            }
        }

        //stone_anim.draw(canvas);

        // Bonus) To print FPS on the screen
        canvas.drawText("FPS:" + FPS, 130, 75, paint);
    }

    public void RenderEnd(Canvas canvas)
    {
        if (canvas == null)
        {
            return;
        }

        canvas.drawBitmap(cross, mX - cross.getWidth() * 0.5f, mY - cross.getHeight()* 0.5f, null);
        String msg = String.valueOf(gameScore);
        canvas.drawText("Your Score: " + msg, screenWidth / 2 - 15 * 25, screenHeight / 2, CDPaint);
    }

    //Update method to update the game play
    public void update(float dt, float fps)
    {
        FPS = fps;
        this.deltaTime = dt;

        if (showAlert == true && !showed)
        {
            showed = true;
            alert.setMessage("GameOver");
            AlertObj.RunAlert();
            showAlert = false;
            showed = false;
        }

        switch (GameState)
        {
            case STATE_COUNTDOWN:
            {
                if (countDownTimer > 0)
                {
                    countDownTimer -= 0.015f;
                } else
                {
                    alphaChange = 255;
                    GameState = GAME_STATE.STATE_PLAY;
                }
                break;
            }
            case STATE_PLAY:
            {
                if (gameScore < 10)
                {
                    moveSpeed = 5;
                } else if (gameScore < 20)
                {
                    moveSpeed = 7;
                } else if (gameScore < 40)
                {
                    moveSpeed = 10;
                } else if (gameScore < 80)
                {
                    moveSpeed = 15;
                } else
                {
                    moveSpeed = 25;
                }

                if (toDisplay)
                {
                    touchTimer += dt;
                }

                if (touchTimer > 1.f)
                {
                    toDisplay = false;
                    touchTimer = 0.f;
                }

                if (alphaChange < 255)
                {
                    alphaChange += changeRate * dt;
                }

                if (alphaChange > 255)
                {
                    alphaChange = 255;
                }

                // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                changeFrame += dt;
                if (changeFrame > 0.5f)
                {
                    ++frame;
                    frame %= 4;
                }

//                if (CheckCollision(mX, mY, ship[frame].getWidth(), ship[frame].getHeight(), stone_anim.getX(),
//                        stone_anim.getY(), stone_anim.getSpriteWidth(), stone_anim.getSpriteHeight()))
//                {
//                    stone_anim.setX(rand.nextInt((screenWidth) + 1) + 0);
//                    stone_anim.setY(rand.nextInt((screenHeight) + 1) + 0);
//                }
                Vec2 vel = new Vec2();
                Vec2 pos = new Vec2();
                Vec2 dir = new Vec2();

                for (int i = 0; i < monsterVector.size(); ++i)
                {
                    if (monsterVector.elementAt(i).getUpdate() == true)
                    {
                        pos.set(monsterVector.elementAt(i).getxPos(), monsterVector.elementAt(i).getyPos());
                        vel.set(monsterVector.elementAt(i).getxDir() * gameSpeed, monsterVector.elementAt(i)
                                .getyDir() * gameSpeed);
                        dir.set(monsterVector.elementAt(i).getxDir(), monsterVector.elementAt(i)
                                .getyDir());

                        monsterVector.elementAt(i).setPos(pos.x + dir.x * moveSpeed, pos.y);

                        float xPos = monsterVector.elementAt(i).getxPos();
                        if (xPos > screenWidth + xOffset / 2 || xPos < -xOffset)
                        {
                            monsterVector.elementAt(i).deactivate();
                            --mushCount;
                        }
                    }
                }

                if (mushCount < maxMon)
                {
                    // rand to spawn diff type

                    if (level == 1)
                    {
                        spawnMush(Monster.MON_TYPE.MON_ZOMBIE);
                    }

                    else if (level == 2)
                    {
                        int chooseSpawn = rand.nextInt(6);

                        if (chooseSpawn <= 3)
                            spawnMush(Monster.MON_TYPE.MON_ZOMBIE);

                        else
                            spawnMush(Monster.MON_TYPE.MON_RAVEN);
                    }

                    else if (level == 3)
                    {
                        int chooseSpawn = rand.nextInt(10);

                        if (chooseSpawn <= 4)
                            spawnMush(Monster.MON_TYPE.MON_ZOMBIE);

                        else if (chooseSpawn <= 7)
                            spawnMush(Monster.MON_TYPE.MON_RAVEN);

                        else
                            spawnMush(Monster.MON_TYPE.MON_SNAIL);
                    }
                }

                if (miss)
                {
                    sounds.play(soundWrong, 1.0f, 1.0f, 0, 0, 1.5f);
                    GameState = GAME_STATE.STATE_END;
                }

                //stone_anim.update(System.currentTimeMillis());
                break;
            }

            case STATE_END:
            {
                if (vibrateTimer > 0)
                {
                    startVibrate();
                    vibrateTimer -= 0.015f;
                }

                else
                {
                    stopVibrate();
                }
                break;
            }
        }

        for (int i = 0; i < monsterVector.size(); ++i)
        {
            if (monsterVector.elementAt(i).getUpdate() == true)
            {
                monsterVector.elementAt(i).getSprite().update(System.currentTimeMillis());
            }
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas)
    {
        switch (GameState)
        {
            case STATE_COUNTDOWN:
            {
                RenderGameplay(canvas);

                int time = Math.round(countDownTimer);
                String msg = "";
                if (time == 0)
                {
                    msg += "GO!";
                } else
                {
                    msg += String.valueOf(Math.round(countDownTimer));
                }
                if (canvas != null)
                {
                    canvas.drawText(msg, screenWidth / 2, screenHeight / 2, CDPaint);
                }
                break;
            }

            case STATE_PLAY:
            {
                RenderGameplay(canvas);

                RenderBlank(canvas);
                break;
            }

            case STATE_END:
            {
                RenderGameplay(canvas);

                // Render overlay
                RenderEnd(canvas);
                break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        short X = (short) event.getX();
        short Y = (short) event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                if (GameState == GAME_STATE.STATE_PLAY)
                {
                    miss = true;
                    alphaChange = 0;

                    mX = X;
                    mY = Y;

                    for (int i = 0; i < monsterVector.size(); ++i)
                    {
                        if (monsterVector.elementAt(i).getRender() == true)
                        {
                            Vec2 mushPos = new Vec2(monsterVector.elementAt(i).getxPos(), monsterVector.elementAt(i)
                                    .getyPos());
                            Vec2 mushDimension = new Vec2(monsterVector.elementAt(i).getSprite().getSpriteWidth(),
                                    monsterVector
                                            .elementAt(i).getSprite().getSpriteHeight());

                            if (CheckCollision(Math.round(mushPos.getX()), Math.round(mushPos.getY()),
                                    Math.round(mushDimension.x), Math.round(mushDimension.y), X, Y, 0, 0))
                            {

                                switch(monsterVector.elementAt(i).getType())
                                {
                                    case MON_ZOMBIE:
                                        gameScore += 1;
                                    case MON_RAVEN:
                                        gameScore += 2;
                                        break;
                                    case MON_SNAIL:
                                        gameScore += 3;
                                        break;
                                }
                                monsterVector.elementAt(i).deactivate();
                                --mushCount;

                                miss = false;
                                sounds.play(soundCorrect, 1.0f, 1.0f, 0, 0, 2.f);
                                break;
                            }
                        }
                    }

                    if (miss)
                    {
                        showAlert = true;
                    }
                    //sounds.play(soundWrong, 1.0f, 1.0f, 0, 0, 2.f);
                }

                break;
            }

            case MotionEvent.ACTION_MOVE:
            {

            }

            case MotionEvent.ACTION_UP:
            {
                toDisplay = true;
            }
        }

        return true;
        //return super.onTouchEvent(event);
    }

    public boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
    {
        if (x2 >= x1 && x2 <= x1 + w1)  // start to detect collision of the top left corner
        {
            if (y2 >= y1 && y2 <= y1 + h1)
                return true;
        }

        if (x2 + w2 >= x1 && x2 + w2 <= x1 + w1)    // top right corner
        {
            if (y2 >= y1 && y2 <= y1 + h1)
                return true;
        }

        if (x2 >= x1 && x2 <= x1 + w1)    // bottom left corner
        {
            if (y2 + h2 >= y1 && y2 + h2 <= y1 + h1)
                return true;
        }

        if (x2 + w2 >= x1 && x2 + w2 <= x1 + w1)    // bottom right corner
        {
            if (y2 + h2 >= y1 && y2 + h2 <= y1 + h1)
                return true;
        }

        return false;
    }

    public void startVibrate()
    {
        long pattern[] = {0, 200, 500};
        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(pattern, 0);
    }

    public void stopVibrate()
    {
        v.cancel();
    }
}
