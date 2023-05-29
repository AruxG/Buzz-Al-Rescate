package com.example.buzzalrescate.interfaz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.engine.Sprite;

public class ScrollingBackground extends Sprite {
    //Referencia a bitmaps
    private Bitmap fondo,fondoTrans,fondoBoss;

    //Variables de size
    private int x, y, topeancho, topealto;

    //Velocidad a la que trasladar el bitmap
    private int VELOCIDAD = 2;
    private long contador = 0;

    //Booleanos que gestionan la transición
    public static boolean finalBossTrans = false;
    public static boolean finalBoss = false;
    public static boolean finalBossTransF = false;
    public static boolean boss = false;
    public boolean primer = false;

    //Tiempo haciendo scroll
    public static double TIME_SCROLLING = 1.5d;

    //Constructor
    public ScrollingBackground(Bitmap fondo, Bitmap fondoTrans, Bitmap fondoBoss, int anchopantalla, int altopantalla,GameEngine gameEngine)
    {
        super(gameEngine, R.drawable.fondojuego,1);
        //Asigna los bitmaps
        this.fondo = fondo;
        this.fondoTrans = fondoTrans;
        this.fondoBoss = fondoBoss;

        //Calcula el ancho y los escala
        int ancho = (fondo.getWidth()/fondo.getHeight())*altopantalla;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                fondo, ancho, altopantalla, false);
        this.fondo = resizedBitmap;

        ancho = (fondo.getWidth()/fondoTrans.getHeight())*altopantalla;
        resizedBitmap = Bitmap.createScaledBitmap(
                fondoTrans, ancho, altopantalla, false);
        this.fondoTrans = resizedBitmap;

        ancho = (fondo.getWidth()/fondoBoss.getHeight())*altopantalla;
        resizedBitmap = Bitmap.createScaledBitmap(
                fondoBoss, ancho, altopantalla, false);
        this.fondoBoss = resizedBitmap;

        //Calcula x, y, tope alto y tope ancho
        x = y = 0;
        topeancho = anchopantalla+200;
        topealto = altopantalla;
    }

    /**
     * Obtiene la posición X por donde va el fondo
     * @return Posición X por donde va el fondo
     */
    public int getX()
    {
        return x;
    }

    /**
     * Obtiene la posición Y por donde va el fondo
     * @return Posición Y por donde va el fondo
     */
    public int getY()
    {
        return y;
    }

    /**
     * Avanza el fondo
     */
    public void avanzarX()
    {
        x += VELOCIDAD;
        if(x > fondo.getWidth()){
            if(finalBossTrans){
                finalBossTrans = false;
            }else if(finalBossTransF){
                finalBossTransF = false;
                finalBoss = true;
            }

            x = 0;
        }

    }

    /**
     * Retrocede el fondo
     */
    public void retrocederX()
    {
        x -= VELOCIDAD;
        if(x < 0)
            x = 0;
    }

    //Resetea la variables cuand se inicializa el juego
    @Override
    public void startGame() {
    finalBoss = false;
    finalBossTrans = false;
    finalBossTransF = false;
    boss = false;
    primer = false;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //Actualiza el contador
        contador+= elapsedMillis;
        if(contador>TIME_SCROLLING) {
            //Si está en transición la velocidad es mayor
            if (boss || finalBossTrans) {
                VELOCIDAD = 5;
            } else {
                VELOCIDAD = 2;
            }
            //Resetea el contador
            contador = 0;
            //Avanza el bitmap en la X
            avanzarX();
        }

    }

    //Pinta el bitmap
    public void onDraw(Canvas lienzo)
    {
        //Calcula la posición x + el ancho de pantalla
        int x_tope = x + topeancho;

        //Si se termina el bitmap, para hacerlo circular
        if(x>fondo.getWidth()-topeancho){
            if(primer){
                primer = false;
                if(boss){
                    finalBossTrans = true;
                    boss = false;
                }
            }
            //Si esta en la transición al boss
            if(finalBossTrans){
                finalBossTransF = true;
                //Pinta lo que quede del bitmap
                Rect imagenfondoactual = new Rect(x, 0,  fondo.getWidth(), topealto);
                Rect imagenenpantalla = new Rect(0, 0, fondo.getWidth()-x, topealto);
                lienzo.drawBitmap(fondo, imagenfondoactual, imagenenpantalla, null);

                //Añade el principio del otro bitmap
                Rect imagenfondoactual2 = new Rect(0, 0, topeancho-(fondoTrans.getWidth()-x), topealto);
                Rect imagenenpantalla2 = new Rect(fondoTrans.getWidth()-x, 0, topeancho, topealto);
                lienzo.drawBitmap(fondoTrans, imagenfondoactual2, imagenenpantalla2, null);
            //Si esta en el boss
            }else if(finalBoss){
                //Pinta lo que quede del bitmap
                Rect imagenfondoactual = new Rect(x, 0,  fondo.getWidth(), topealto);
                Rect imagenenpantalla = new Rect(0, 0, fondoBoss.getWidth()-x, topealto);
                lienzo.drawBitmap(fondoBoss, imagenfondoactual, imagenenpantalla, null);

                //Añade el principio del otro bitmap
                Rect imagenfondoactual2 = new Rect(0, 0, topeancho-(fondoBoss.getWidth()-x), topealto);
                Rect imagenenpantalla2 = new Rect(fondoBoss.getWidth()-x, 0, topeancho, topealto);
                lienzo.drawBitmap(fondoBoss, imagenfondoactual2, imagenenpantalla2, null);
            //Si esta en la transición final al boss
            }else if(finalBossTransF){
                //Pinta lo que quede del bitmap
                Rect imagenfondoactual = new Rect(x, 0,  fondo.getWidth(), topealto);
                Rect imagenenpantalla = new Rect(0, 0, fondoTrans.getWidth()-x, topealto);
                lienzo.drawBitmap(fondoTrans, imagenfondoactual, imagenenpantalla, null);

                //Añade el principio del otro bitmap
                Rect imagenfondoactual2 = new Rect(0, 0, topeancho-(fondoBoss.getWidth()-x), topealto);
                Rect imagenenpantalla2 = new Rect(fondoBoss.getWidth()-x, 0, topeancho, topealto);
                lienzo.drawBitmap(fondoBoss, imagenfondoactual2, imagenenpantalla2, null);
            }else{
                //Pinta lo que quede del bitmap
                Rect imagenfondoactual = new Rect(x, 0, fondo.getWidth(), topealto);
                Rect imagenenpantalla = new Rect(0, 0, fondo.getWidth()-x, topealto);
                lienzo.drawBitmap(fondo, imagenfondoactual, imagenenpantalla, null);

                //Añade el principio del otro bitmap
                Rect imagenfondoactual2 = new Rect(0, 0, topeancho-(fondo.getWidth()-x), topealto);
                Rect imagenenpantalla2 = new Rect(fondo.getWidth()-x, 0, topeancho, topealto);
                lienzo.drawBitmap(fondo, imagenfondoactual2, imagenenpantalla2, null);

            }

        }else{
            primer =true;
            //Si está en la transición al boss
            if(finalBossTrans){
                Rect imagenfondoactual = new Rect(x, 0, x_tope, topealto);
                Rect imagenenpantalla = new Rect(0, 0, topeancho, topealto);
                lienzo.drawBitmap(fondoTrans, imagenfondoactual, imagenenpantalla, null);
            //Si está en el boss
            }else if(finalBoss){
                Rect imagenfondoactual = new Rect(x, 0, x_tope, topealto);
                Rect imagenenpantalla = new Rect(0, 0, topeancho, topealto);
                lienzo.drawBitmap(fondoBoss, imagenfondoactual, imagenenpantalla, null);
            //Si está en la transición final al boss
            }else if(finalBossTransF){
                Rect imagenfondoactual = new Rect(x, 0, x_tope, topealto);
                Rect imagenenpantalla = new Rect(0, 0, topeancho, topealto);
                lienzo.drawBitmap(fondoTrans, imagenfondoactual, imagenenpantalla, null);
            }else{
                Rect imagenfondoactual = new Rect(x, 0, x_tope, topealto);
                Rect imagenenpantalla = new Rect(0, 0, topeancho, topealto);
                lienzo.drawBitmap(fondo, imagenfondoactual, imagenenpantalla, null);
            }

        }

    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }
}
