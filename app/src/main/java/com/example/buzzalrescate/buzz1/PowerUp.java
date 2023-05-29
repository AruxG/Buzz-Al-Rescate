package com.example.buzzalrescate.buzz1;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.engine.Sprite;

import java.util.Random;

public class PowerUp extends Sprite {
    //Referencia a gameController
    private final GameController gameController;

    //Variables de velocidades y rotación
    private double speed;
    private double speedX;
    private double speedY;
    private double rotationSpeed;

    //Tipos de power ups
    enum Tipo{
        EXPERIENCIA, DISPARO, VIDA, ESCUDO;
    }

    //Tipo del power up
    public Tipo tipo;

    //Constructor power up
    public PowerUp(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.powerupexperiencia,(1440d*0.4d)/ MainActivity.heightDisplay);
        this.speed = 200d * pixelFactor/240d;
        this.gameController = gameController;
    }

    //Inicializa el power up
    public void init(GameEngine gameEngine) {
        // They initialize in a [-22.5, 22.5] degrees angle
        double angle = gameEngine.random.nextDouble()*Math.PI/4d-Math.PI/8d;
        speedX = -speed;
        speedY = speed * Math.sin(angle);
        //Se inicializa a la derecha de la pantalla
        positionX = MainActivity.widthDisplay +100;
        //Se inicializa la posición Y aleatoriamente
        positionY = gameEngine.random.nextInt(MainActivity.heightDisplay/2)+MainActivity.heightDisplay/4;
        //Si el powerup está en la parte superior, el ángulo debe ser negativo
        //Si el powerup está en la parte inferior, el ángulo debe ser positivo
        if((positionY> MainActivity.heightDisplay/2 && angle > 0) || positionY < MainActivity.heightDisplay/2 && angle < 0){
            angle = -angle;
        }

        //Actualiza la rotación y su velocidad
        rotationSpeed = angle*(180d / Math.PI)/100d; // They rotate 4 times their ange in a second.
        rotation = gameEngine.random.nextInt(100);
        Resources r = gameEngine.getContext().getResources();
        //Dependiendo del tipo de power up elegido aleatoriamente
        switch(gameEngine.random.nextInt(4)){
            case 0:
                //Cambia el tipo y le asigna su bitmap correspondiente
                tipo = Tipo.EXPERIENCIA;
                bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.powerupexperiencia)).getBitmap();
                break;
            case 1:
                //Cambia el tipo y le asigna su bitmap correspondiente
                tipo = Tipo.DISPARO;
                bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.powerupdisparo)).getBitmap();
                break;
            case 2:
                //Cambia el tipo y le asigna su bitmap correspondiente
                tipo = Tipo.VIDA;
                bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.powerupvida)).getBitmap();
                break;
            case 3:
                //Cambia el tipo y le asigna su bitmap correspondiente
                tipo = Tipo.ESCUDO;
                bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.powerupescudo)).getBitmap();
                break;
            default:
                break;
        }
    }

    @Override
    public void startGame() {
    }

    //Elimina el objeto y  lo devuelve a la pool
    public void removeObject(GameEngine gameEngine) {
        // Return to the pool
        gameEngine.removeGameObject(this);
        gameController.returnToPool(this);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Actualiza la posición y rotación
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

    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }
}
