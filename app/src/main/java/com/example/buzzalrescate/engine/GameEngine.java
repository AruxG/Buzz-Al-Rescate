package com.example.buzzalrescate.engine;

import android.app.Activity;
import android.content.Context;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.input.InputController;
import com.example.buzzalrescate.interfaz.ScoreGameObject;
import com.example.buzzalrescate.sound.GameEvent;
import com.example.buzzalrescate.sound.SoundManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameEngine {


    private List<GameObject> gameObjects = new ArrayList<GameObject>();
    private List<GameObject> objectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> objectsToRemove = new ArrayList<GameObject>();

    private UpdateThread theUpdateThread;
    private DrawThread theDrawThread;
    public InputController theInputController;
    private final GameView theGameView;

    public Random random = new Random();

    public int width;
    public int height;
    public double pixelFactor;

    private Activity mainActivity;
    private MainActivity ma;
    public boolean gameOver = false;

    private SoundManager soundManager;

    public GameEngine(Activity activity, GameView gameView, MainActivity m) {
        mainActivity = activity;
        ma = m;
        theGameView = gameView;
        theGameView.setGameObjects(this.gameObjects);
        this.width = theGameView.getWidth()
                - theGameView.getPaddingRight() - theGameView.getPaddingLeft();
        this.height = theGameView.getHeight()
                - theGameView.getPaddingTop() - theGameView.getPaddingTop();

        this.pixelFactor = this.height / 400d;

    }

    public void setTheInputController(InputController inputController) {
        theInputController = inputController;
    }

    public void startGame() {
        // Stop a game if it is running
        stopGame();
        // Setup the game objects
        int numGameObjects = gameObjects.size();
        for (int i = 0; i < numGameObjects; i++) {
            gameObjects.get(i).startGame();
        }

        // Start the update thread
        theUpdateThread = new UpdateThread(this);
        theUpdateThread.start();

        // Start the drawing thread
        theDrawThread = new DrawThread(this);
        theDrawThread.start();
    }

    public void stopGame() {
        if (theUpdateThread != null) {
            theUpdateThread.stopGame();
        }
        if (theDrawThread != null) {
            theDrawThread.stopGame();
        }
    }

    public void pauseGame() {
        if (theUpdateThread != null) {
            theUpdateThread.pauseGame();
        }
        if (theDrawThread != null) {
            theDrawThread.pauseGame();
        }
    }

    public void resumeGame() {
        if (theUpdateThread != null) {
            theUpdateThread.resumeGame();
        }
        if (theDrawThread != null) {
            theDrawThread.resumeGame();
        }
    }

    public void stopGameOver(boolean over){
        gameOver = over;
        //Pausa el juego
        pauseGame();
        stopGame();
        //Si gana o pierde reproduce sonido de victoria o derrota
        if(!gameOver){
            onGameEvent(GameEvent.WinGame);
            ScoreGameObject.sumarZurg();
        }else{
            onGameEvent(GameEvent.GameOver);
        }
        //Cambia el fragmento a resultados
        ma.gameFinished();
    }


    public void addGameObject(GameObject gameObject) {
        if (isRunning()) {
            objectsToAdd.add(gameObject);
        } else {
            gameObjects.add(gameObject);
        }
        mainActivity.runOnUiThread(gameObject.onAddedRunnable);
    }

    public void removeGameObject(GameObject gameObject) {
        objectsToRemove.add(gameObject);
        mainActivity.runOnUiThread(gameObject.onRemovedRunnable);
    }

    public void onUpdate(long elapsedMillis) {
        int numGameObjects = gameObjects.size();
        for (int i = 0; i < numGameObjects; i++) {
            GameObject go =  gameObjects.get(i);
            go.onUpdate(elapsedMillis, this);
            if(go instanceof ScreenGameObject) {
                ((ScreenGameObject) go).onPostUpdate(this);
            }
        }
        checkCollisions();
        synchronized (gameObjects) {
            while (!objectsToRemove.isEmpty()) {
                gameObjects.remove(objectsToRemove.remove(0));
            }
            while (!objectsToAdd.isEmpty()) {
                gameObjects.add(objectsToAdd.remove(0));
            }
        }
    }

    public void onDraw() {
        theGameView.draw();
    }

    public boolean isRunning() {
        return theUpdateThread != null && theUpdateThread.isGameRunning();
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    public void onGameEvent (GameEvent gameEvent) {
        // We notify all the GameObjects
        // Also the sound manager
        soundManager.playSoundForGameEvent(gameEvent);
    }

    public boolean isPaused() {
        return theUpdateThread != null && theUpdateThread.isGamePaused();
    }

    public Context getContext() {
        return theGameView.getContext();
    }

    private void checkCollisions() {
        int numObjects = gameObjects.size();
        for (int i = 0; i < numObjects; i++) {
            if (gameObjects.get(i) instanceof ScreenGameObject) {
                ScreenGameObject objectA = (ScreenGameObject) gameObjects.get(i);
                for (int j = i + 1; j < numObjects; j++) {
                    if (gameObjects.get(j) instanceof ScreenGameObject) {
                        ScreenGameObject objectB = (ScreenGameObject) gameObjects.get(j);
                        if (objectA.checkCollision(objectB)) {
                            objectA.onCollision(this, objectB);
                            objectB.onCollision(this, objectA);
                        }
                    }
                }
            }
        }
    }
}
