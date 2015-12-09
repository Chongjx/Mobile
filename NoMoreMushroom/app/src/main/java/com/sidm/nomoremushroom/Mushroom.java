package com.sidm.nomoremushroom;

/**
 * Created by Jun Xiang on 6/12/2015.
 */
public class Mushroom
{
    enum MUSH_TYPE
    {
        MUSH_GOOD,
        MUSH_BAD,
        MUSH_EVIL,
        MUSH_MAX
    };

    private float xPos;
    private float yPos;

    private float xDir;
    private float yDir;

    private float moveSpeed;

    private MUSH_TYPE type;

    private SpriteAnimation sprite;

    private boolean update;
    private boolean render;

    public Mushroom()
    {
        this.sprite = new SpriteAnimation();
        this.xPos = -2000.f;
        this.yPos = -2000.f;

        this.xDir = 0.f;
        this.yDir = 0.f;

        this.moveSpeed = 5.f;
        this.type = MUSH_TYPE.MUSH_GOOD;

        this.update = false;
        this.render = false;
    }

    public Mushroom(float xpos, float ypos, float xdir, float ydir, MUSH_TYPE type, float moveSpeed)
    {
        this.xPos = xpos;
        this.yPos = ypos;
        this.xDir = xdir;
        this.yDir = ydir;
        this.type = type;
        this.moveSpeed = moveSpeed;
        this.update = false;
        this.render = false;

        this.getSprite().setX(Math.round(xPos));
        this.getSprite().setY(Math.round(yPos));
    }

    public void init(float xpos, float ypos, float xdir, float ydir, MUSH_TYPE type, float moveSpeed)
    {
        this.xPos = xpos;
        this.yPos = ypos;
        this.xDir = xdir;
        this.yDir = ydir;
        this.type = type;
        this.moveSpeed = moveSpeed;
        this.update = true;
        this.render = true;

        this.getSprite().setX(Math.round(xPos));
        this.getSprite().setY(Math.round(yPos));
    }

    public float getxPos()
    {
        return xPos;
    }

    public void setxPos(float xPos)
    {
        this.xPos = xPos;
        this.getSprite().setX(Math.round(xPos));
    }

    public float getyPos()
    {
        return yPos;
    }

    public void setyPos(float yPos)
    {
        this.yPos = yPos;
        this.getSprite().setY(Math.round(yPos));
    }

    public void setPos(float xpos, float ypos)
    {
        this.xPos = xpos;
        this.yPos = ypos;

        this.getSprite().setX(Math.round(xPos));
        this.getSprite().setY(Math.round(yPos));
    }

    public float getxDir()
    {
        return xDir;
    }

    public void setxDir(float xDir)
    {
        this.xDir = xDir;
    }

    public float getyDir()
    {
        return yDir;
    }

    public void setyDir(float yDir)
    {
        this.yDir = yDir;
    }

    public void setDir(float xdir, float ydir)
    {
        this.xDir = xdir;
        this.yDir = ydir;
    }

    public float getMoveSpeed()
    {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed)
    {
        this.moveSpeed = moveSpeed;
    }

    public MUSH_TYPE getType()
    {
        return type;
    }

    public void setType(MUSH_TYPE type)
    {
        this.type = type;
    }

    public void setSprite(SpriteAnimation sprite)
    {
        this.sprite.Init(sprite);
    }

    public SpriteAnimation getSprite()
    {
        return sprite;
    }

    public void setUpdate(boolean update)
    {
        this.update = update;
    }

    public boolean getUpdate()
    {
        return this.update;
    }

    public void setRender(boolean render)
    {
        this.render = render;
    }

    public boolean getRender()
    {
        return this.render;
    }

    public void deactivate()
    {
        this.render = false;
        this.update = false;

        this.setPos(-2000.f, -2000.f);
    }
}
