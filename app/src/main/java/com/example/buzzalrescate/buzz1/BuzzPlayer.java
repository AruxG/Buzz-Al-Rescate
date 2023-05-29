package com.example.buzzalrescate.buzz1;



import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.engine.Sprite;
import com.example.buzzalrescate.input.InputController;
import com.example.buzzalrescate.interfaz.GameFragment;
import com.example.buzzalrescate.interfaz.ScoreGameObject;
import com.example.buzzalrescate.interfaz.VidasGameObject;
import com.example.buzzalrescate.sound.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class BuzzPlayer extends Sprite {

    //Constantes de tiempos entre disparos, power ups, y número de ganchos y laser iniciales
    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 900;
    private static final int INITIAL_LASER_POOL_AMOUNT = 20;
    private static final int TIME_ESCUDO = 3000;
    private static final int TIME_DISPARO_TRIPLE = 5000;
    private static final int TIME_VIDA_EXTRA = 500;
    private static final int TIME_EXPERIENCIA = 3000;

    //Lista de ganchos y lasers
    List<Bullet> bullets = new ArrayList<Bullet>();
    List<Laser> lasers = new ArrayList<Laser>();

    //Tiempo desde la última vez que se disparó
    private long timeSinceLastFire;

    //Variables máximo x e y, velocidad y contadores
    private int maxX;
    private int maxY;
    private double speedFactor;
    private int contador = 0;
    private int contadorEscudo = 0;
    private int contadorDisparo = 0;
    private int contadorVida = 0;
    private int contadorExperiencia = 0;

    //Posiciones estáticas, y booleanos para power ups
    public static double posX, posY;
    public boolean escudo = false;
    public boolean disparoTriple = false;
    public boolean vidaExtra = false;
    public boolean experiencia = false;

    public Resources r;

    //Constructo buzzPlayer
    public BuzzPlayer(GameEngine gameEngine){
        super(gameEngine, MainActivity.settings.getInt("buzz",0),(1440d*0.05d)/MainActivity.heightDisplay);
        //Dependiendo del buzz seleccionado, se define la velocidad
        switch (MainActivity.settings.getInt("buzz",0)){
            case R.drawable.buzz1volando:
                speedFactor = pixelFactor * 100d / 25d; // We want to move at 100px per second on a 400px tall screen
                break;
            case R.drawable.buzz2volando:
                speedFactor = pixelFactor * 100d / 20d; // We want to move at 100px per second on a 400px tall screen
                break;
            default:
                break;
        }

        //Se calcula el máximo x e y
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;

        //Inicializa la pool de lasers y ganchos
        initBulletPool(gameEngine);
        initLaserPool(gameEngine);

        //Obtiene resources
        r = gameEngine.getContext().getResources();
    }

    //Método que inicializa la pool de ganchos
    private void initBulletPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine));
        }
    }

    //Método que inicializa la pool de lasers
    private void initLaserPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_LASER_POOL_AMOUNT; i++) {
            lasers.add(new Laser(gameEngine));
        }
    }

    //Método para obtener un gancho de la pool
    private Bullet getBullet() {
        //Si no hay, devuelve null
        if (bullets.isEmpty()) {
            return null;
        }
        //Devuelve un elemento de la pool
        return bullets.remove(0);
    }

    //Método para obtener un laser de la pool
    private Laser getLaser() {
        //Si no hay, devuelve null
        if (lasers.isEmpty()) {
            return null;
        }
        //Devuelve un elemento de la pool
        return lasers.remove(0);
    }

    //Devuelve un gancho a la pool
    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    //Devuelve un laser a la pool
    void releaseLaser(Laser laser) {
        lasers.add(laser);
    }

    //Cuando se inicia el juego, se setea la posición
    @Override
    public void startGame() {
        positionX = maxX / 8;
        positionY = maxY / 2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Método para actualizar la posición
        updatePosition(elapsedMillis, gameEngine.theInputController);
        //Métodos que comprueba si debe lanzar láser y ganchos
        checkFiringLaser(elapsedMillis,gameEngine);
        checkFiring(elapsedMillis, gameEngine);

        //Si tiene el power up de escudo
        if(escudo){
            //Se actualiza el contador
            contadorEscudo += elapsedMillis;
            //Si se ha acabado el tiempo, se cambia el bitmap al normal y se resetean las variables
            if(contadorEscudo>TIME_ESCUDO){
                escudo = false;
                contadorEscudo = 0;
                bitmap = ((BitmapDrawable) r.getDrawable(MainActivity.settings.getInt("buzz",0))).getBitmap();
            }
        }

        //Si tiene el power up de disparo triple
        if(disparoTriple){
            //Se actualiza el contador
            contadorDisparo +=elapsedMillis;
            //Si se ha acabado el tiempo, se cambia el bitmap al normal y se resetean las variables
            if(contadorDisparo>TIME_DISPARO_TRIPLE){
                disparoTriple = false;
                contadorDisparo = 0;
                bitmap = ((BitmapDrawable) r.getDrawable(MainActivity.settings.getInt("buzz",0))).getBitmap();
            }
        }

        //Si tiene el power up de vida extra
        if(vidaExtra){
            //Se actualiza el contador
            contadorVida += elapsedMillis;
            //Si se ha acabado el tiempo, se cambia el bitmap al normal y se resetean las variables
            if(contadorVida>TIME_VIDA_EXTRA){
                vidaExtra = false;
                contadorVida = 0;
                bitmap = ((BitmapDrawable) r.getDrawable(MainActivity.settings.getInt("buzz",0))).getBitmap();
            }
        }

        //Si tiene el power up de doble experiencia
        if(experiencia){
            //Se actualiza el contador
            contadorExperiencia += elapsedMillis;
            //Si se ha acabado el tiempo, se cambia el bitmap al normal y se resetean las variables
            if(contadorExperiencia>TIME_EXPERIENCIA){
                experiencia = false;
                contadorExperiencia = 0;
                bitmap = ((BitmapDrawable) r.getDrawable(MainActivity.settings.getInt("buzz",0))).getBitmap();
            }
        }

    }

    //Método que actualiza la posición del buzz
    private void updatePosition(long elapsedMillis, InputController inputController) {
        //Cambia la posición respecto al input del joystick
        positionX += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
        }
        if (positionX > maxX) {
            positionX = maxX;
        }
        positionY += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }
        posX = positionX;
        posY = positionY;
    }

    //Método para disparar ganchos
    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        //Si se ha pulsado el botón de disparar gancho y el tiempo entre balas se ha superado
        if (gameEngine.theInputController.isFiring && timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            //Lanza el gancho
            Bullet bullet = getBullet();
            if (bullet == null) {
                return;
            }
            bullet.init(this, positionX+450, positionY+70);
            gameEngine.addGameObject(bullet);
            //Resetea el contador
            timeSinceLastFire = 0;
        }
        else {
            //Actualiza el contador
            timeSinceLastFire += elapsedMillis;
        }
    }

    private void checkFiringLaser(long elapsedMillis, GameEngine gameEngine) {
        //Si el jugador no esta lanzando ganchos y el tiempo entre balas se ha superado
        if (!gameEngine.theInputController.isFiring && timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            //Si hay power up de disparo triple, se inicializan 3 balas de 3 tipos
            if(disparoTriple){
                Laser laser = getLaser();
                if (laser == null) {
                    return;
                }
                //Setea el tipo 0: centro
                laser.tipo = 0;
                laser.init(this, positionX+500, positionY+100);
                gameEngine.addGameObject(laser);
                laser = getLaser();
                if (laser == null) {
                    return;
                }
                //Setea el tipo 1: arriba
                laser.tipo = 1;
                laser.init(this, positionX+500, positionY+120);
                gameEngine.addGameObject(laser);
                laser = getLaser();
                if (laser == null) {
                    return;
                }
                //Setea el tipo 2: abajo
                laser.tipo = 2;
                laser.init(this, positionX+500, positionY+80);
                gameEngine.addGameObject(laser);
            //Si el disparo es normal
            }else{
                //Inicializa el laser
                Laser laser = getLaser();
                if (laser == null) {
                    return;
                }
                laser.tipo = 0;
                laser.init(this, positionX+500, positionY+100);
                gameEngine.addGameObject(laser);
            }
            //Resetea el contador
            timeSinceLastFire = 0;
        }
        else {
            //Actualiza el contador
            timeSinceLastFire += elapsedMillis;
        }
    }

    //Pone el power up de escudo, setea variables y cambia el bitmap
    public void putEscudo(){
        contadorEscudo = 0;
        escudo = true;
        bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.b2escudo)).getBitmap();
    }

    //Pone el power up de disparo triple, setea variables y cambia el bitmap
    public void disparoTriple(){
        contadorDisparo = 0;
        disparoTriple = true;
        bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.b2disparotriple)).getBitmap();
    }

    //Pone el power up de sumarVidas, setea variables y cambia el bitmap
    public void sumarVida(){
        //Suma una vida
        VidasGameObject.sumarVidas();
        contadorVida = 0;
        vidaExtra = true;
        bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.b2vidaextra)).getBitmap();
    }

    //Pone el power up de doble experiencia, setea variables y cambia el bitmap
    public void dobleExperiencia(){
        contadorExperiencia = 0;
        experiencia = true;
        bitmap = ((BitmapDrawable) r.getDrawable(R.drawable.b2dobleexperiencia)).getBitmap();
    }

    //Método si algún objeto colisiona con él
    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        //Si es un asteroide
        if (otherObject instanceof Asteroid) {
            //Si no tiene escudo, se le resta una vida
            if(!escudo){
               restarVidas(gameEngine);
                //gameEngine.stopGame();
            }
            //Elimina el asteroide
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
        //Si es un marciano
        }else if(otherObject instanceof Marciano){
            //Si ya está enganchado, elimina el marciano
            Marciano m = (Marciano) otherObject;
            if(m.enganchar){
                m.removeObject(gameEngine);
            }
        //Si es un disparo de Zurg
        }else if(otherObject instanceof zurgDisparo){
            //Resta una vida y elimina el disparo de zurg
            restarVidas(gameEngine);
            zurgDisparo z = (zurgDisparo) otherObject;
            z.removeObject(gameEngine);
        //Si es el laser de zurg
        }else if(otherObject instanceof LaserZurg){
            //Resta una vida y elimina el láser
            LaserZurg l = (LaserZurg) otherObject;
            l.removeObject(gameEngine);
            restarVidas(gameEngine);
        }
    }

    //Método que resta vida, reproduce el sonido y comprueba si las vidas están a 0
    public void restarVidas(GameEngine gameEngine){
        VidasGameObject.restarVidas();
        gameEngine.onGameEvent(GameEvent.BuzzHit);
        //Si no hay vidas, se para el juego y se muestra la pantalla de game over
        if(VidasGameObject.vidas ==0){
            gameEngine.removeGameObject(this);
            gameEngine.stopGameOver(true);
        }
    }

}
