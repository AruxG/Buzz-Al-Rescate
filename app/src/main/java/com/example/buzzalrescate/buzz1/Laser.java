package com.example.buzzalrescate.buzz1;

import android.os.Handler;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.engine.Sprite;
import com.example.buzzalrescate.interfaz.ScoreGameObject;
import com.example.buzzalrescate.interfaz.VidasGameObject;
import com.example.buzzalrescate.sound.GameEvent;

public class Laser extends Sprite {
    //Variable velocidad y tipo de laser(centro,arriba o abajo)
    private double speedFactor;
    public int tipo = 0;

    //Referencia a su padre y gameEngine
    private BuzzPlayer parent;
    private GameEngine gameEngine;

    //Constructor laser
    public Laser(GameEngine gameEngine){
        super(gameEngine, R.drawable.disparobase,(1440d*0.01d)/MainActivity.heightDisplay);
        this.gameEngine = gameEngine;
        //Velocidad del laser
        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Dependiendo del tipo se actualiza la posición Y con un factor diferente
        switch (tipo){
            case 0:
                positionX -= speedFactor * elapsedMillis;
                break;
            case 1:
                positionX -= speedFactor * elapsedMillis;
                positionY -= speedFactor * elapsedMillis * 0.25;
                break;
            case 2:
                positionX -= speedFactor * elapsedMillis;
                positionY += speedFactor * elapsedMillis * 0.25;
                break;
            default:
                break;
        }

        //Si el laser está fuera de la pantalla, se elimina
        if (positionX > MainActivity.widthDisplay) {
           removeObject(gameEngine);
        }
    }

    //Inicializa el laser con su posición y reproduce el sonido de laser
    public void init(BuzzPlayer parentPlayer, double initPositionX, double initPositionY) {
        gameEngine.onGameEvent(GameEvent.LaserFired);
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
    }

    //Elimina el objeto y devuelve el laser a la pool
    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseLaser(this);
    }

    //Método si colisiona con un objeto
    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        //Si es un asteroide
        if (otherObject instanceof Asteroid) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            Asteroid a = (Asteroid) otherObject;
            //Estalla el asteroide con una animación
            a.estallar();
            //Reproduce el sonido de estallar
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
            //a.removeObject(gameEngine);

            // Add some score
            ScoreGameObject.sumarAsteroide(parent.experiencia);
        //Si es un power up
        }else if(otherObject instanceof PowerUp){
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            PowerUp p = (PowerUp) otherObject;
            gameEngine.onGameEvent(GameEvent.CatchPowerUp);
            //a.removeObject(gameEngine);

            //Depende del power up, llama al método adecuado
            switch(p.tipo){
                case EXPERIENCIA:
                    parent.dobleExperiencia();
                    break;
                case DISPARO:
                    parent.disparoTriple();
                    break;
                case VIDA:
                    parent.sumarVida();
                    break;
                case ESCUDO:
                    parent.putEscudo();
                    break;
                default:
                    break;
            }
            //Elimina el power up
            p.removeObject(gameEngine);
        }
    }
}
