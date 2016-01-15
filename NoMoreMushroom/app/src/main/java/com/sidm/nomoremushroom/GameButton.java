package com.sidm.nomoremushroom;

import android.graphics.Bitmap;

/**
 * Created by Jun Xiang on 14/1/2016.
 */
public class GameButton
{
    private Vec2 pos;
    private Vec2 size;
    private Bitmap texture;

    public GameButton()
    {
        pos = new Vec2();
        size = new Vec2();

        pos.setZero();
        size.setZero();
    }

    public void setPos(Vec2 pos)
    {
        this.pos = pos;
    }

    public void setSize(Vec2 size)
    {
        this.size = size;
    }

    public void setTexture(Bitmap texture)
    {
        this.texture = texture;
    }

    public Vec2 getPos()
    {
        return this.pos;
    }

    public Bitmap getTexture()
    {
        return this.texture;
    }
}
