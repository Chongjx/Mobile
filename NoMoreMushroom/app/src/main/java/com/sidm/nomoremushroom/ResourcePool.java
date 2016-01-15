package com.sidm.nomoremushroom;

import android.graphics.BitmapFactory;

import java.util.HashMap;

/**
 * Created by Jun Xiang on 2/1/2016.
 */
public class ResourcePool
{
    private HashMap<String, SpriteAnimation> animationHashMap;

    public ResourcePool()
    {
        animationHashMap = new HashMap<>();
    }

    public void loadResources()
    {
        /*SpriteAnimation sprite;
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
        animationHashMap.put("SNAILRIGHT", sprite);*/
    }

    SpriteAnimation getSprite(String spriteName)
    {
        return animationHashMap.get(spriteName);
    }
}
