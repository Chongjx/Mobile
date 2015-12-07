package com.sidm.nomoremushroom;

/**
 * Created by Jun Xiang on 6/12/2015.
 */
public class Vec2
{
    public float x;
    public float y;

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public Vec2()
    {
        this.x = 0;
        this.y = 0;
    }

    public Vec2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
}
