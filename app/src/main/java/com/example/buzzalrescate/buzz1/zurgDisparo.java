package com.example.buzzalrescate.buzz1;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.engine.Sprite;
import com.example.buzzalrescate.interfaz.ScoreGameObject;
import com.example.buzzalrescate.sound.GameEvent;

public class zurgDisparo extends Sprite {
    //Variable velocidad y tipo de bala(centro, arriba o abajo)
    private double speedFactor;
    public int tipo = 0;

    //Referencias a padre y gameEngine
    private Zurg parent;
    private GameEngine gameEngine;

    //Contructor disparo de zurg
    public zurgDisparo(GameEngine gameEngine){
        super(gameEngine, R.drawable.zurgdisparo,(1440d*0.09d)/ MainActivity.heightDisplay);
        this.gameEngine = gameEngine;
        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Dependiendo del tipo actualiza la posición en X y en Y
        switch (tipo){
            case 0:
                positionX += speedFactor * elapsedMillis;
                break;
            case 1:
                positionX += speedFactor * elapsedMillis;
                positionY -= speedFactor * elapsedMillis * 0.25;
                break;
            case 2:
                positionX += speedFactor * elapsedMillis;
                positionY += speedFactor * elapsedMillis * 0.25;
                break;
            default:
                break;
        }
        //Si se sale de la pantalla, se elimina
        if (positionX < 0) {
            removeObject(gameEngine);
        }

    }

    //Inicializa el disparo, con las posiciones
    public void init(Zurg parentPlayer, double initPositionX, double initPositionY) {
        gameEngine.onGameEvent(GameEvent.ZurgShot);
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
    }

    //Elimina el objeto de la gameEngine y devuelve el disparo a la pool
    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseDisparo(this);
    }

    //Si colisiona con el laser o con el gancho, se eliminan ambos
    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Laser) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            Laser l = (Laser) otherObject;
            l.removeObject(gameEngine);
        }else if(otherObject instanceof Bullet){
            removeObject(gameEngine);
            Bullet b = (Bullet) otherObject;
            b.removeObject(gameEngine);
        }
    }
}
