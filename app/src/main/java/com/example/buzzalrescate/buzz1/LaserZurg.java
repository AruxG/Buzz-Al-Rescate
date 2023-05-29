package com.example.buzzalrescate.buzz1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.AnimatedSprite;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.interfaz.ScoreGameObject;
import com.example.buzzalrescate.sound.GameEvent;

public class LaserZurg extends AnimatedSprite {
    //Variable velocidad, posiciones iniciales
    private double speedFactor;
    public double initPositionX, initPositionY;

    //Referencias al padre y gameEngine
    private Zurg parent;
    private GameEngine gameEngine;

    //Constructor laser de zurg
    public LaserZurg(GameEngine gameEngine){
        super(gameEngine, R.drawable.zurg0,R.drawable.laserzurganim,(1440d*0.09d)/ MainActivity.heightDisplay);
        this.gameEngine = gameEngine;
        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Actualiza el contador
        mCurrentTime += elapsedMillis;
        //Reproduce la animación del laser
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
                this.height = (int) (mAnimationDrawable.getFrame(i).getIntrinsicHeight() * this.pixelFactor);
                this.width = (int) (mAnimationDrawable.getFrame(i).getIntrinsicWidth() * this.pixelFactor);
                this.positionX = initPositionX - width;
                this.positionY = initPositionY - height/2;
                bitmap = ((BitmapDrawable)
                        mAnimationDrawable.getFrame(i)).getBitmap();
                break;
            }

        }
        //Si ya se ha reproducido la animación, se elimina
        if(mCurrentTime >2590){
            removeObject(gameEngine);
            mCurrentTime = 0;
        }
    }

    //Inicializa el laser de zurg
    public void init(Zurg parentPlayer, double initPositionX, double initPositionY) {
        gameEngine.onGameEvent(GameEvent.ZurgLaser);
        //Inicializa su posición
        this.initPositionX = initPositionX;
        this.initPositionY = initPositionY;
        positionX = initPositionX;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
    }

    //Elimina el objeto de la gameEngine y lo devuelve a la pool
    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseLaser(this);
    }

    //Si colisiona con el laser o gancho de buzz lo elimina
    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Laser) {
            Laser l = (Laser) otherObject;
            l.removeObject(gameEngine);
        }else if(otherObject instanceof Bullet){
            Bullet b = (Bullet) otherObject;
            b.removeObject(gameEngine);
        }
    }


}
