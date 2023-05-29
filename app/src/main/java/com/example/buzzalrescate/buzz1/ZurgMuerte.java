package com.example.buzzalrescate.buzz1;

import android.graphics.drawable.BitmapDrawable;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.buzz1.GameController;
import com.example.buzzalrescate.engine.AnimatedSprite;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;

public class ZurgMuerte extends AnimatedSprite {
    //Referencia a game Controller
    private final GameController gameController;

    //Constructor zurg muerte con sus posiciones
    public ZurgMuerte(GameController gameController, GameEngine gameEngine, double posX, double posY) {
        super(gameEngine, R.drawable.zurgmuerte0, R.drawable.zurgmuerteanim,(1440d*0.09d)/ MainActivity.heightDisplay);
        this.gameController = gameController;
        this.positionX = posX;
        this.positionY = posY;
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Reproduce la animación de muerte del zurg
        mCurrentTime += elapsedMillis;
        if (mCurrentTime > mTotalTime) {
            if (mAnimationDrawable.isOneShot()) {
                return;
            }
            else {
                mCurrentTime = mCurrentTime % mTotalTime;
            }
        }
        long animationElapsedTime = 0;
        for (int i=0; i<mAnimationDrawable.getNumberOfFrames(); i++) {
            animationElapsedTime += mAnimationDrawable.getDuration(i);
            if (animationElapsedTime > mCurrentTime) {
                bitmap = ((BitmapDrawable)
                        mAnimationDrawable.getFrame(i)).getBitmap();
                break;
            }
        }
        //Cuando se realiza la animación, se para el juego y elimina el objeto
        if(mCurrentTime>900){
            gameEngine.stopGameOver(false);
            gameEngine.removeGameObject(this);
        }
    }
}
