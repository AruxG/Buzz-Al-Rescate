package com.example.buzzalrescate.buzz1;


import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.AnimatedSprite;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.engine.Sprite;

public class Asteroid extends AnimatedSprite {

    private final GameController gameController;

    //Variables velocidades
    public double speed;
    private double speedX;
    private double speedY;
    private double rotationSpeed;

    //Variable si el asteroide tiene que estallar
    public boolean estallar;
    Resources r;

    //Constructor asteroide, define velocidad y define variables de gamecontroller y resources
    public Asteroid(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.asteroide, R.drawable.asteroidanim,(1440d*0.09d)/MainActivity.heightDisplay);
        this.speed = 250d * pixelFactor/50d;
        this.gameController = gameController;
        r = gameEngine.getContext().getResources();
    }

    //Actualiza el collider
    public void onPostUpdate(GameEngine gameEngine) {
        mBoundingRect.set(
                (int) positionX+50,
                (int) positionY+50,
                (int) positionX + width-50,
                (int) positionY + height-50);
    }

    //Inicio del asteroide
    public void init(GameEngine gameEngine) {
        estallar = false;
        bitmap = ((BitmapDrawable)
                r.getDrawable(R.drawable.asteroide)).getBitmap();
        // They initialize in a [-10, 10] degrees angle
        double angle = gameEngine.random.nextDouble()*Math.PI/9d-Math.PI/18d;
        speedX = -speed;
        speedY = speed * Math.sin(angle);
        // Asteroids initialize in the central 50% of the screen horizontally
        positionX = MainActivity.widthDisplay +100;
        // They initialize outside of the screen vertically
        positionY = gameEngine.random.nextInt(MainActivity.heightDisplay/2)+MainActivity.heightDisplay/4;
        if((positionY> MainActivity.heightDisplay/2 && angle > 0) || positionY < MainActivity.heightDisplay/2 && angle < 0){
            angle = -angle;
        }
        rotationSpeed = angle*(180d / Math.PI)/250d; // They rotate 4 times their ange in a second.
        rotation = gameEngine.random.nextInt(360);
    }

    //Cuando se inicia el juego se pone la variable estallar a false
    @Override
    public void startGame() {
        estallar = false;
    }

    //Elimina el objeto de la gameEngine y lo devuelve a la pool de asteroides
    public void removeObject(GameEngine gameEngine) {
        // Return to the pool
        gameEngine.removeGameObject(this);
        gameController.returnToPool(this);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Actualiza las posiciones y rotaciones
        positionX += speedX * elapsedMillis;
        positionY += speedY * elapsedMillis;
        rotation += rotationSpeed * elapsedMillis;
        if (rotation > 360) {
            rotation = 0;
        }
        else if (rotation < 0) {
            rotation = 360;
        }
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool
            gameEngine.removeGameObject(this);
            gameController.returnToPool(this);
        }

        //Si el asteroide tiene que estallar, se reproduce una animación
        if(estallar){
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
            //Si ya se ha terminado la animación, se elimina el objeto
            if(mCurrentTime>200){
                this.removeObject(gameEngine);
                mCurrentTime = 0;
            }
        }

    }

    //Método de colisión
    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }

    //Pone la variable estallar a true
    public void estallar(){
        estallar = true;
    }
}
