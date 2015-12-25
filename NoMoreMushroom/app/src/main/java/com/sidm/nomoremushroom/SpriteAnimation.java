package com.sidm.nomoremushroom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Jun Xiang on 3/12/2015.
 */
public class SpriteAnimation
{
    boolean play;
    private Bitmap bitmap;      // the animation sequence
    private Rect sourceRect;    // the rectangle to be drawn from the animation bitmap
    private int frame;          // number of frames in animation
    private int currentFrame;   // the current frame
    private long frameTicker;   // the time of the last frame update
    private int framePeriod;    // ms between each frame (1000/fps)

    private int spriteWidth;    // the width of the sprite to calculate the cut out rectangle
    private int spriteHeight;   // the height of the sprite

    private int x;              // the X coordinate of the object (top left of the image)
    private int y;              // the Y coordinate of the object (top left of the image)

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public Rect getSourceRect()
    {
        return sourceRect;
    }

    public void setSourceRect(Rect sourceRect)
    {
        this.sourceRect = sourceRect;
    }

    public int getFrame()
    {
        return frame;
    }

    public void setFrame(int frame)
    {
        this.frame = frame;
    }

    public int getCurrentFrame()
    {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame)
    {
        this.currentFrame = currentFrame;
    }

    public long getFrameTicker()
    {
        return frameTicker;
    }

    public void setFrameTicker(long frameTicker)
    {
        this.frameTicker = frameTicker;
    }

    public int getFramePeriod()
    {
        return framePeriod;
    }

    public void setFramePeriod(int framePeriod)
    {
        this.framePeriod = framePeriod;
    }

    public int getSpriteWidth()
    {
        return spriteWidth;
    }

    public void setSpriteWidth(int spriteWidth)
    {
        this.spriteWidth = spriteWidth;
    }

    public int getSpriteHeight()
    {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight)
    {
        this.spriteHeight = spriteHeight;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public SpriteAnimation()
    {
    }

    public SpriteAnimation(Bitmap bitmap, int x, int y, int fps, int frameCount)
    {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;

        currentFrame = 0;

        frame = frameCount;

        spriteWidth = bitmap.getWidth() / frameCount;   // assumed that each frame has the same width
        spriteHeight = bitmap.getHeight();

        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

        framePeriod = 1000 / fps;
        frameTicker = 01;
    }

    public void Init(Bitmap bitmap, int x, int y, int fps, int frameCount)
    {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;

        currentFrame = 0;

        frame = frameCount;

        spriteWidth = bitmap.getWidth() / frameCount;   // assumed that each frame has the same width
        spriteHeight = bitmap.getHeight();

        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

        framePeriod = 1000 / fps;
        frameTicker = 01;
    }

    public void Init(SpriteAnimation copySprite)
    {
        this.bitmap = copySprite.bitmap;
        this.x = copySprite.x;
        this.y = copySprite.y;

        currentFrame = copySprite.currentFrame;

        frame = copySprite.frame;

        spriteWidth = copySprite.spriteWidth;   // assumed that each frame has the same width
        spriteHeight = copySprite.spriteHeight;

        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

        framePeriod = copySprite.framePeriod;
        frameTicker = 01;
    }

    public void update(long gameTime)   // gameTime = system time from the thread which is running
    {
        if (gameTime > frameTicker + framePeriod)
        {
            frameTicker = gameTime;
            currentFrame++;             // increment the frame

            if (currentFrame >= frame)  // frame = total no. of frames and currentFrame starts from 0
            {
                currentFrame = 0;       // reached end of frame, reset to 0
            }
        }

        // define the rectangle to cut out sprite
        this.sourceRect.left = currentFrame * spriteWidth;
        this.sourceRect.right = this.sourceRect.left + spriteWidth;
    }

    public void draw(Canvas canvas)
    {
        // image of each frame is defined by sourceRect
        // destRect is the area for the image of each frame to be drawn
        Rect destRect = new Rect(getX(), getY(), getX() + spriteWidth, getY() + spriteHeight);
        canvas.drawBitmap(bitmap,sourceRect, destRect, null);
    }
}
