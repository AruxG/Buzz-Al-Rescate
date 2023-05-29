package com.example.buzzalrescate.buzz1;

import android.graphics.Canvas;

import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.GameObject;
import com.example.buzzalrescate.interfaz.ScrollingBackground;
import com.example.buzzalrescate.interfaz.VidasZurgGameObject;
import com.example.buzzalrescate.sound.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class GameController extends GameObject {

    //Tiempos entre enemigos, marcianos y power ups
    private static int TIME_BETWEEN_ENEMIES = 1500;
    private static int TIME_BETWEEN_MARCIANO = 5000;
    private static int TIME_BETWEEN_POWER_UP = 9000;
    //Contadores
    private long currentMillis, currentMillis2, currentMillis3;
    //Lista o pool de asteroides, marcianos y power ups, y referencia a zurg
    private List<Asteroid> asteroidPool = new ArrayList<Asteroid>();
    private List<Marciano> marcianoPool = new ArrayList<Marciano>();
    private List<PowerUp> powerupPool = new ArrayList<PowerUp>();
    private Zurg zurgBoss;

    //Contador de enemigos, marcianos y powerups spawneados
    private int enemiesSpawned;
    private int marcianoSpawned;
    private int powerupSpawned;

    //Si ya se ha cambiado a la parte de Zurg
    private boolean cambiadoBoss = false;

    //Constructor gameController
    public GameController(GameEngine gameEngine) {
        //Asigna zurg e inicializa las pools de objetos
        zurgBoss = new Zurg(this,gameEngine);
        // We initialize the pool of items now
        for (int i=0; i<20; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine));
        }
        for (int i=0; i<10; i++) {
            marcianoPool.add(new Marciano(this, gameEngine));
        }
        for (int i=0; i<4; i++) {
            powerupPool.add(new PowerUp(this, gameEngine));
        }
    }

    //Cuando se inicia el juego se resetean las variables
    @Override
    public void startGame() {
        currentMillis = 0;
        currentMillis2=0;
        enemiesSpawned = 0;
        marcianoSpawned = 0;
        powerupSpawned = 0;
        TIME_BETWEEN_ENEMIES = 1500;
        TIME_BETWEEN_MARCIANO = 5000;
        TIME_BETWEEN_POWER_UP = 10000;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

        //Si el número de enemigos spawneados es mayor a 50, se pasa al boss final
        if(enemiesSpawned<50){
            //Actualiza contadores
            currentMillis += elapsedMillis;
            currentMillis2 += elapsedMillis;
            currentMillis3 += elapsedMillis;
            //Si el contador es mayor al tiempo entre enemigos
            if (currentMillis > TIME_BETWEEN_ENEMIES) {
                // Spawn a new enemy
                if(!asteroidPool.isEmpty()){
                    Asteroid a = asteroidPool.remove(0);
                    a.init(gameEngine);
                    gameEngine.addGameObject(a);
                    enemiesSpawned++;
                    //Por cada enemigo spawneado se decrementa el tiempo entre enemigos y aumenta su velocidad
                    TIME_BETWEEN_ENEMIES -= 25;
                    a.speed += 0.05d;
                }
                //Se resetea el contador
                currentMillis =0;
                return;
            }

            //Si el contador es mayor al tiempo entre marcianos
            if (currentMillis2 > TIME_BETWEEN_MARCIANO) {
                // Spawn a new enemy
                if(!marcianoPool.isEmpty()){
                    Marciano a = marcianoPool.remove(0);
                    a.init(gameEngine);
                    gameEngine.addGameObject(a);
                    marcianoSpawned++;
                    //Por cada marciano spawneado decrementa el tiempo entre marcianos y aumenta su velocidad
                    TIME_BETWEEN_MARCIANO -= 20;
                    a.speed += 0.025d;
                }
                //Resetea el contador
                currentMillis2 =0;
                return;
            }

            //Si el contador es mayor al tiempo entre power ups
            if (currentMillis3 > TIME_BETWEEN_POWER_UP) {
                // Spawn a power up
                if(!powerupPool.isEmpty()){
                    PowerUp a = powerupPool.remove(0);
                    a.init(gameEngine);
                    gameEngine.addGameObject(a);
                    powerupSpawned++;
                }
                //Resetea el contador
                currentMillis3 =0;
                return;
            }
        }else{
            //Si se pasa al boss final
            //Si ya se ha hecho la transición al fondo del boss, añade el zurg
            if(ScrollingBackground.finalBossTransF){
                if(zurgBoss!= null){
                    zurgBoss.init(gameEngine);
                    gameEngine.addGameObject(zurgBoss);
                    zurgBoss = null;
                }
            }else{
                //Si no se ha cambiado a boss
                if(!cambiadoBoss){
                    //Variable a true
                    cambiadoBoss = true;
                    //Se inicializa transición a fondo morado, reproduce el sonido y visibiliza las vidas de zurg
                    ScrollingBackground.boss = true;
                    gameEngine.onGameEvent(GameEvent.TransicionZurg);
                    VidasZurgGameObject.setVisible();
                }

            }
        }

    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    //Métodos para devolver objetos a la pool
    public void returnToPool(Asteroid asteroid) {
        asteroidPool.add(asteroid);
    }
    public void returnToPool(Marciano marciano) {
        marcianoPool.add(marciano);
    }
    public void returnToPool(PowerUp powerUp) {
        powerupPool.add(powerUp);
    }
}
