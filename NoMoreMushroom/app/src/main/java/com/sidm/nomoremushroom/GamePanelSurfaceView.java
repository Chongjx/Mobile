package com.sidm.nomoremushroom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;

    // 1b) Define Screen width and Screen height as integer
    int screenWidth, screenHeight;

    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;

    // 4a) bitmap array to stores 4 images of the spaceship
    //private Bitmap[] ship = new Bitmap[4];

    // 4b) Variable as an index to keep track of the spaceship images
    int frame = 0;
    float changeFrame = 0;

    Paint paint = new Paint();
    Random rand = new Random(System.currentTimeMillis());

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;

    // store the touch location
    private short mX = 0, mY = 0;

    // Variable for Game State check
    private short GameState;

    // store the mushrooms
    private Vector<Mushroom> mushroomVector;
    // store the spawnPosition of the mushroom
    private Vector<Vec2> spawnPosition;
    private float gameSpeed;
    private HashMap<String, SpriteAnimation> animationHashMap;

    private int gameScore;
    private int mushCount;
    private int xOffset;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context){

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.level1);
        scaledbg = Bitmap.createScaledBitmap(bg, screenWidth, screenHeight, true);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        paint.setARGB(255, 0, 0, 0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);

        gameSpeed = 20.f;
        gameScore = 0;
        mushCount = 0;
        xOffset = 200;

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        //stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64,
        //        5, 5);
        //stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.badmush), 320, 64,
                //5, 6);
        //stone_anim.setX(0);
        //stone_anim.setY(600);

        animationHashMap = new HashMap<String, SpriteAnimation>();
        mushroomVector = new Vector<Mushroom>();
        spawnPosition = new Vector<Vec2>();

        // Create all the sprite animations and store them into the hashmap
        {
            SpriteAnimation sprite;
            sprite = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.goodmush), 320, 64,
                    5, 6);
            animationHashMap.put("GOOD", sprite);
            sprite = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.badmush), 320, 64,
                    5, 6);
            animationHashMap.put("BAD", sprite);
            sprite = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.evilmush), 320, 64,
                    5, 5);
            animationHashMap.put("EVIL", sprite);
        }

        // add 10 mush into the vector first
        for (int i = 0; i < 10; ++i)
        {
            Mushroom mush = new Mushroom();
            mush.setSprite(animationHashMap.get("BAD"));
            mushroomVector.add(mush);
        }

        Vec2 maxStart = new Vec2();
        maxStart.set(1, 2);

        Vec2 minStart = new Vec2();
        minStart.set(0, 0);

        int yDivision = screenHeight / 3;

        // Create spawn location first
        spawnPosition.add(new Vec2(-xOffset,               (yDivision + 5) * 1 - yDivision + 30));
        spawnPosition.add(new Vec2(-xOffset,               (yDivision + 5) * 2 - yDivision + 30));
        spawnPosition.add(new Vec2(-xOffset,               (yDivision + 5) * 3 - yDivision + 30));
        spawnPosition.add(new Vec2(screenWidth + xOffset/2,(yDivision + 5) * 1 - yDivision + 30));
        spawnPosition.add(new Vec2(screenWidth + xOffset/2, (yDivision + 5) * 2 - yDivision + 30));
        spawnPosition.add(new Vec2(screenWidth + xOffset/2, (yDivision + 5) * 3 - yDivision + 30));

        // spawn 2 mush first
        for (int i = 0; i < mushroomVector.size() && mushCount < 2; ++i)
        {
            if (mushroomVector.elementAt(i).getRender() == false)
            {
                int spawnIndex = rand.nextInt(6);
                mushroomVector.elementAt(i).init(rand.nextInt((screenWidth - 25) + 1), spawnPosition.elementAt
                        (spawnIndex).y, 0, 0, Mushroom.MUSH_TYPE.MUSH_BAD, 5.f);
                mushroomVector.elementAt(i).setSprite(animationHashMap.get("BAD"));
                ++mushCount;
            }
        }
    }

    public void spawnMush(Mushroom.MUSH_TYPE type)
    {
        for (int i = 0; i < mushroomVector.size(); ++i)
        {
            if (mushroomVector.elementAt(i).getRender() == false)
            {
                int spawnIndex = rand.nextInt(6);
                switch (type)
                {
                    case MUSH_GOOD:
                    {
                        mushroomVector.elementAt(i).init(spawnPosition.elementAt(spawnIndex).x, spawnPosition.elementAt
                                (spawnIndex).y, 0, 0, Mushroom.MUSH_TYPE.MUSH_GOOD, 5.f);
                        mushroomVector.elementAt(i).setSprite(animationHashMap.get("GOOD"));
                        break;
                    }
                    case MUSH_BAD:
                    {
                        mushroomVector.elementAt(i).init(spawnPosition.elementAt(spawnIndex).x, spawnPosition.elementAt
                                (spawnIndex).y, 0, 0, Mushroom.MUSH_TYPE.MUSH_BAD, 5.f);
                        mushroomVector.elementAt(i).setSprite(animationHashMap.get("BAD"));
                        break;
                    }
                    case MUSH_EVIL:
                    {
                        mushroomVector.elementAt(i).init(spawnPosition.elementAt(spawnIndex).x, spawnPosition.elementAt
                                (spawnIndex).y, 0, 0, Mushroom.MUSH_TYPE.MUSH_EVIL, 5.f);
                        mushroomVector.elementAt(i).setSprite(animationHashMap.get("EVIL"));
                        break;
                    }
                }

                if (mushroomVector.elementAt(i).getxPos() <= -xOffset)
                {
                    mushroomVector.elementAt(i).setxDir(1);
                }

                else
                {
                    mushroomVector.elementAt(i).setxDir(-1);
                }
                ++mushCount;
                break;
            }
        }
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder){
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);


        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null)
        {
            return;
        }
        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        //canvas.drawBitmap(scaledbg, bgX + screenWidth, bgY, null);

        // 4d) Draw the spaceships
        //canvas.drawBitmap(ship[frame], mX, mY, null);

        for (int i = 0; i < mushroomVector.size(); ++i)
        {
            if(mushroomVector.elementAt(i).getRender() == true)
            {
                mushroomVector.elementAt(i).getSprite().draw(canvas);
            }
        }

        //stone_anim.draw(canvas);

        // Bonus) To print FPS on the screen
        canvas.drawText("FPS:" + FPS, 130, 75, paint);
        canvas.drawText("SCORE:" + gameScore, screenWidth/ 2, 75, paint);
    }


    //Update method to update the game play
    public void update(float dt, float fps){
        FPS = fps;

        switch (GameState) {
            case 0:
            {
                // 3) Update the background to allow panning effect
                //bgX -= 500 * dt;    // change speed of the panning effect
                //if (bgX < -screenWidth)
                //{
                //    bgX = 0;
                //}

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

                for (int i = 0; i < mushroomVector.size(); ++i)
                {
                    if(mushroomVector.elementAt(i).getUpdate() == true)
                    {
                        pos.set(mushroomVector.elementAt(i).getxPos(), mushroomVector.elementAt(i).getyPos());
                        vel.set(mushroomVector.elementAt(i).getxDir() * gameSpeed, mushroomVector.elementAt(i)
                                .getyDir() * gameSpeed);
                        dir.set(mushroomVector.elementAt(i).getxDir(), mushroomVector.elementAt(i)
                                .getyDir());

                        mushroomVector.elementAt(i).setPos(pos.x + dir.x, pos.y);
                        mushroomVector.elementAt(i).getSprite().update(System.currentTimeMillis());

                        float xPos = mushroomVector.elementAt(i).getxPos();
                        if (xPos > screenWidth + xOffset/2 || xPos < -xOffset)
                        {
                            mushroomVector.elementAt(i).deactivate();
                            --mushCount;
                        }
                    }
                }

                if (mushCount < 3)
                {
                    spawnMush(Mushroom.MUSH_TYPE.MUSH_BAD);
                }

                //stone_anim.update(System.currentTimeMillis());
            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        short X = (short)event.getX();
        short Y = (short)event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            for (int i = 0; i < mushroomVector.size(); ++i)
            {
                if (mushroomVector.elementAt(i).getRender() == true)
                {
                    Vec2 mushPos = new Vec2(mushroomVector.elementAt(i).getxPos(), mushroomVector.elementAt(i)
                            .getyPos());
                    Vec2 mushDimension = new Vec2(mushroomVector.elementAt(i).getSprite().getSpriteWidth(),
                            mushroomVector
                            .elementAt(i).getSprite().getSpriteHeight());

                    if (CheckCollision(Math.round(mushPos.getX()), Math.round(mushPos.getY()),
                            Math.round(mushDimension.x), Math.round(mushDimension.y), X, Y, 0, 0))
                    {
                        mushroomVector.elementAt(i).deactivate();
                        --mushCount;
                        ++gameScore;
                        break;
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
    {
        if (x2 >= x1 && x2 <= x1 + w1)  // start to detect collision of the top left corner
        {
            if(y2 >= y1 && y2 <= y1 + h1)
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

        return  false;
    }
}
