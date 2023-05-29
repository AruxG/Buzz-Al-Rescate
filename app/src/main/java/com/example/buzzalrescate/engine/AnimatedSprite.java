package com.example.buzzalrescate.engine;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

public abstract class AnimatedSprite extends Sprite {
    public AnimationDrawable mAnimationDrawable;
    public int mTotalTime;
    public long mCurrentTime;
    public AnimatedSprite(GameEngine gameEngine,
                          int drawableRes, int animatedSprite, double scale) {
        super(gameEngine, drawableRes,animatedSprite,scale);
        // Now, the drawable must be an animation drawable
        mAnimationDrawable = (AnimationDrawable) mSpriteDrawable;
        // Calculate the total time of the animation
        mTotalTime = 10;
        for (int i=0; i<mAnimationDrawable.getNumberOfFrames(); i++) {
            mTotalTime += mAnimationDrawable.getDuration(i);
        }
    }

    @Override
    public void onUpdate(long elapsedMillis,
                         GameEngine gameEngine) {

    }
}
