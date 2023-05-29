package com.example.buzzalrescate.buzz1;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.engine.Sprite;
import com.example.buzzalrescate.interfaz.ScoreGameObject;
import com.example.buzzalrescate.movement.Player;
import com.example.buzzalrescate.sound.GameEvent;

public class Bullet extends Sprite {
    //Variable de velocidad, padre, gameEngine
    private double speedFactor;
    private BuzzPlayer parent;
    private GameEngine gameEngine;

    //Contructor gancho, define gameEngine y velocidad
    public Bullet(GameEngine gameEngine){
        super(gameEngine, R.drawable.disparogancho,(1440d*0.4d)/MainActivity.heightDisplay);
        this.gameEngine = gameEngine;
        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    //Si el juego se inicia
    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Actualiza posición x
        positionX -= speedFactor * elapsedMillis;
        //Si se ha salido de la pantalla se elimina
        if (positionX > MainActivity.widthDisplay) {
            removeObject(gameEngine);
        }
    }

    //Inicializa el gancho, reproduce su efecto de sonido e inicializa su posición
    public void init(BuzzPlayer parentPlayer, double initPositionX, double initPositionY) {
        gameEngine.onGameEvent(GameEvent.GanchoFired);
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
    }

    //Elimina el objeto de la gameEngine y devuelve a la pool de bullets
    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseBullet(this);
    }

    //Método si colisiona un objeto con él
    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        //Si es un marciano
        if(otherObject instanceof Marciano){
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            Marciano m = (Marciano) otherObject;
            //Reproduce el sonido
            gameEngine.onGameEvent(GameEvent.CatchMarciano);
            //Llama al método de enganchar el marciano
            m.enganchar();
            // Add some score
            ScoreGameObject.sumarMarciano(parent.experiencia);
        //Si es un power up
        }else if(otherObject instanceof PowerUp){
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            PowerUp p = (PowerUp) otherObject;
            //Dependiendo del tipo del power up, llama a un método del padre
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
