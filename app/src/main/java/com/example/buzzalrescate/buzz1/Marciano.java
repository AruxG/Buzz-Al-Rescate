package com.example.buzzalrescate.buzz1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.AnimatedSprite;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;

import java.util.Random;

public class Marciano extends AnimatedSprite {
    //Referencia al gameController
    private final GameController gameController;

    //Variables de velocidades y rotacion
    public double speed;
    private double speedX;
    private double speedY;
    private double rotationSpeed;

    //Variable si el marciano hay que engancharlo
    public boolean enganchar;

    //Constructo Marciano
    public Marciano(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.marcianos3, R.drawable.marcianoanim,(1440d*0.4d)/MainActivity.heightDisplay);
        //Velocidad inicial
        this.speed = 200d * pixelFactor/240d;
        this.gameController = gameController;
    }

    //Inicializa el marciano
    public void init(GameEngine gameEngine) {
        // They initialize in a [-22.5, 22.5] degrees angle
        double angle = gameEngine.random.nextDouble()*Math.PI/4d-Math.PI/8d;
        speedX = -speed;
        speedY = speed * Math.sin(angle);
        //Se inicializa a la derecha de la pantalla
        positionX = MainActivity.widthDisplay +100;
        //La posición Y se realiza de manera aleatoria
        positionY = gameEngine.random.nextInt(MainActivity.heightDisplay/2)+MainActivity.heightDisplay/4;
        //Si el marciano está en la parte superior, el ángulo debe ser negativo
        //Si el marciano está en la parte inferior, el ángulo debe ser positivo
        if((positionY> MainActivity.heightDisplay/2 && angle > 0) || positionY < MainActivity.heightDisplay/2 && angle < 0){
            angle = -angle;
        }
        //Velocidad de rotación y ángulo aleatorio
        rotationSpeed = angle*(180d / Math.PI)/100d; // They rotate 4 times their ange in a second.
        rotation = gameEngine.random.nextInt(100);

        //Referecnias a resources y random
        Resources r = gameEngine.getContext().getResources();
        Random random = new Random();
        //Mediante un random se elige uno de los 3 marcianos disponibles
        switch (random.nextInt(3)){
            case 0:
                bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.marcianos1)).getBitmap();
                break;
            case 1:
                bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.marcianos2)).getBitmap();
                break;
            case 2:
                bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.marcianos3)).getBitmap();
                break;
        }
    }

    @Override
    public void startGame() {
    }

    //Se elimina el objeto de la gameEngine y devuelve a la pool
    public void removeObject(GameEngine gameEngine) {
        // Return to the pool
        gameEngine.removeGameObject(this);
        gameController.returnToPool(this);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Actualiza la posición
        positionX += speedX * elapsedMillis;
        positionY += speedY * elapsedMillis;
        //Si hay que engancha el marciano
        if(enganchar){
            //Se le cambia la rotación para que vaya dirigido hacia el buzz
            double bc = positionX - positionX;
            double bc2 = 30;
            double ba = BuzzPlayer.posX - positionX;
            double ba2 = BuzzPlayer.posY - positionY;
            //Cálculo del ángulo hacia el buzz
            double cos = (bc*ba + bc2*ba2) / (Math.sqrt(bc*bc+bc2*bc2)*Math.sqrt(ba*ba+ba2*ba2));
            rotation = Math.asin(cos);
            //speed += 200d;
            speedX = -speed * Math.cos(rotation) *5;
            speedY = speed * Math.sin(rotation)*5;

            //Se reproduce la animación
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

        }else{
            //Si no hay que engancharlo, se actualiza la rotación
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
        }

    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }

    //Cambia la variable enganchar a true
    public void enganchar(){
        enganchar = true;
    }
}
