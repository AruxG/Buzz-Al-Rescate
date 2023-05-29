package com.example.buzzalrescate.buzz1;

import android.graphics.drawable.BitmapDrawable;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.AnimatedSprite;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.interfaz.ScoreGameObject;
import com.example.buzzalrescate.interfaz.VidasGameObject;
import com.example.buzzalrescate.interfaz.VidasZurgGameObject;
import com.example.buzzalrescate.sound.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class Zurg extends AnimatedSprite {
    //Referencia a gamecontroller
    private final GameController gameController;

    //Tiempos entre balas, laser, transición entre ellos, disparando
    private static final long TIME_BETWEEN_BULLETS = 600;
    private static final long TIME_BETWEEN_LASER = 2590;
    private static long TIME_TRANSICION = 2000;
    private static final long TIME_BULLETS = 2000;

    //Número de balas inicial
    private static final int INITIAL_ZURG_POOL_AMOUNT = 70;

    //Pool de laser y balas
    List<zurgDisparo> zurgDisparos = new ArrayList<zurgDisparo>();
    List<LaserZurg> laserZurgs = new ArrayList<LaserZurg>();

    //Velocidades
    private double speed;
    private double speedX;

    //Tiempos entre balas y lasers
    private long timeSinceLastFire;
    private long timeSinceLastFire2;

    //Contador disparo
    private double contadorDisparo = 0;

    //Si el zurg está subiendo o bajando
    private boolean bajando = false;

    //Constructor del zurg
    public Zurg(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.zurgnave0, R.drawable.zurgnaveanim,(1440d*0.09d)/ MainActivity.heightDisplay);
        this.speed = 200d * pixelFactor/300d;
        this.gameController = gameController;
        //Inicializa la pool de laser y balas
        initZurgDisparosPool(gameEngine);
        initZurgLaser(gameEngine);
    }

    //Inicializa la pool de balas
    private void initZurgDisparosPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_ZURG_POOL_AMOUNT; i++) {
            zurgDisparos.add(new zurgDisparo(gameEngine));
        }
    }

    //Inicializa la pool de laser
    private void initZurgLaser(GameEngine gameEngine) {
        for (int i=0; i<1; i++) {
            laserZurgs.add(new LaserZurg(gameEngine));
        }
    }


    public void onPostUpdate(GameEngine gameEngine) {
        mBoundingRect.set(
                (int) positionX+50,
                (int) positionY+50,
                (int) positionX + width-50,
                (int) positionY + height-50);
    }

    //Inicializa el zurg con su velocidad y posiciones
    public void init(GameEngine gameEngine) {
        speedX = -speed;
        positionX = MainActivity.widthDisplay;
        positionY = MainActivity.heightDisplay/9d;
    }

    //Resetea variables al inicio de juego
    @Override
    public void startGame() {
        TIME_TRANSICION = 2000;
    }

    //Elimina el objeto
    public void removeObject(GameEngine gameEngine) {
        // Return to the pool
        gameEngine.removeGameObject(this);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Mueve el zurg si todavía no ha llegado a su posición definida
        if(positionX>MainActivity.widthDisplay-700d){
            positionX += speedX * elapsedMillis;
        }else{
            contadorDisparo += elapsedMillis;
            //Si está en el tiempo de disparar
            if(contadorDisparo >0 && contadorDisparo <= TIME_BULLETS) {
                checkFiring(elapsedMillis, gameEngine);
            //Si está en el tiempo de transición
            }else if(contadorDisparo>TIME_BULLETS && contadorDisparo<=(TIME_TRANSICION+TIME_BULLETS)){
                //Sube y baja el zurg
                if(bajando){
                    positionY -= 0.07d *elapsedMillis;
                    if(positionY <=0){
                        bajando = false;
                    }
                }else{
                    positionY += 0.07d *elapsedMillis;
                    if(positionY+height >= MainActivity.heightDisplay){
                        bajando = true;
                    }
                }
                //Setea el tiempo
                timeSinceLastFire2 = TIME_BETWEEN_LASER;
            //Si está en el tiempo que dispara laser
            }else if(contadorDisparo>(TIME_TRANSICION+TIME_BULLETS) && contadorDisparo<= (TIME_TRANSICION+TIME_BULLETS+ TIME_BETWEEN_LASER)) {
                checkFiringLaser(elapsedMillis, gameEngine);
            //Si está en el tiempo de transición
            }else if(contadorDisparo>(TIME_TRANSICION+TIME_BULLETS+ TIME_BETWEEN_LASER) && contadorDisparo<=(TIME_TRANSICION+TIME_BULLETS+ TIME_BETWEEN_LASER+TIME_TRANSICION)){
                //Baja o sube el zurg
                if(bajando){
                    positionY -= 0.05d *elapsedMillis;
                    if(positionY <=0){
                        bajando = false;
                    }
                }else{
                    positionY += 0.05d *elapsedMillis;
                    if(positionY+height >= MainActivity.heightDisplay){
                        bajando = true;
                    }
                }
            //Si ya se termino el tiempo de transición, resetea variables y decrementa tiempo de transición
            }else if(contadorDisparo > (TIME_TRANSICION+TIME_BULLETS+ TIME_BETWEEN_LASER+TIME_TRANSICION)){
                contadorDisparo = 0;
                timeSinceLastFire = 0;
                if(TIME_TRANSICION > 100){
                    TIME_TRANSICION -= 100d;
                }
            }
        }

        //Reproduce la animación de zurg con los fuegos de la nave
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

    }

    //Si colisiona con algún objeto
    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        //Si colisiona con el láser
        if(otherObject instanceof Laser){
            //Elimina el objeto
            Laser l = (Laser)otherObject;
            l.removeObject(gameEngine);
            //Resta 1 vida al zurg y suma 1 punto
            VidasZurgGameObject.restarVida(1);
            ScoreGameObject.zurgDisparo(1);
            //Comprueba si las vidas del zurg están a 0
            if(VidasZurgGameObject.vidasZurg == 0){
                //Reproduce sonido de muerte, elimina el objeto
                gameEngine.onGameEvent(GameEvent.ZurgKilled);
                removeObject(gameEngine);
                //Crea un objeto que reproduce la animación de muerte
                gameEngine.addGameObject(new ZurgMuerte(gameController,gameEngine,positionX,positionY));
            }
        //Si colisiona con un gancho
        }else if(otherObject instanceof Bullet){
            //Elimina el gancho
            Bullet b = (Bullet) otherObject;
            b.removeObject(gameEngine);
            //Resta 2 vidas al zurg y suma 2 puntos
            VidasZurgGameObject.restarVida(2);
            ScoreGameObject.zurgDisparo(2);
            //Comprueba si las vidas del zurg están a 0
            if(VidasZurgGameObject.vidasZurg == 0){
                //Reproduce sonido de muerte, elimina el objeto
                gameEngine.onGameEvent(GameEvent.ZurgKilled);
                removeObject(gameEngine);
                //Crea un objeto que reproduce la animación de muerte
                gameEngine.addGameObject(new ZurgMuerte(gameController,gameEngine,positionX,positionY));
            }
        //Si colisiona con buzz
        }else if(otherObject instanceof BuzzPlayer){
            //Cambia su posición
            BuzzPlayer b = (BuzzPlayer)otherObject;
            b.positionX -= 200;
            b.restarVidas(gameEngine);
        }
    }

    //Devuelve la bala a la pool
    void releaseDisparo(zurgDisparo z) {
        zurgDisparos.add(z);
    }

    //Devuelve el laser a la pool
    void releaseLaser(LaserZurg l) {
        laserZurgs.add(l);
    }

    //Obtiene una bala de la pool
    private zurgDisparo getZurgDisparo() {
        if (zurgDisparos.isEmpty()) {
            return null;
        }
        return zurgDisparos.remove(0);
    }

    //Obtiene un laser de la pool
    private LaserZurg getLaserZurg() {
        if (laserZurgs.isEmpty()) {
            return null;
        }
        return laserZurgs.remove(0);
    }

    //Método para disparar
    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        //Si el contador es mayor al tiempo entre balas
        if (timeSinceLastFire > TIME_BETWEEN_BULLETS) {
                zurgDisparo disparo = getZurgDisparo();
                if (disparo == null) {
                    return;
                }
            //Disparo central
            disparo.tipo = 0;
            disparo.init(this, positionX+5, positionY+(height/1.7d));
                gameEngine.addGameObject(disparo);
            disparo = getZurgDisparo();
                if (disparo == null) {
                    return;
                }

            //Disparo superior
            disparo.tipo = 1;
            disparo.init(this, positionX+5, positionY+(height/1.7d));
                gameEngine.addGameObject(disparo);
            disparo = getZurgDisparo();
                if (disparo == null) {
                    return;
                }
            //Disparo inferior
            disparo.tipo = 2;
            disparo.init(this, positionX+5, positionY+(height/1.7d));
                gameEngine.addGameObject(disparo);
            //Resetea el contador
            timeSinceLastFire = 0;
        }
        else {
            //Incrementa el contador
            timeSinceLastFire += elapsedMillis;
        }
    }

    //Dispara laser
    private void checkFiringLaser(long elapsedMillis, GameEngine gameEngine) {
        //Si el contador es mayor al tiempo entre lasers
        if (timeSinceLastFire2 > TIME_BETWEEN_LASER) {
            LaserZurg laser = getLaserZurg();
            if (laser == null) {
                return;
            }
            laser.init(this, positionX + 150, positionY+(height/2.9d));
            gameEngine.addGameObject(laser);
            //Resetea el contador
            timeSinceLastFire2 = 0;
        }else{
            //Incrementa el contador
            timeSinceLastFire2 += elapsedMillis;
        }
    }
}
