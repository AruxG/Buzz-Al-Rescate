package com.example.buzzalrescate.interfaz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.engine.GameObject;
import com.example.buzzalrescate.engine.GameEngine;


public class ScoreGameObject extends GameObject {
    //Referencia al textView
    private final TextView textView;

    //Variables, puntos, asteroides, marcianos y zurg
    public static int puntos;
    public static int asteroides = 0;
    public static int marcianos = 0;
    public static int zurg = 0;

    //Constructor
    public ScoreGameObject(View view, int viewResId) {
        textView = (TextView) view.findViewById(viewResId);
        puntos = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

    }

    //Actualiza el textView
    @Override
    public void onDraw(Canvas canvas) {
        textView.setText("Puntos: "+puntos);
    }

    //Inicializa las variables
    @Override
    public void startGame() {
        puntos = 0;
        asteroides = 0;
        marcianos = 0;
        zurg = 0;
    }

    //Suma la puntuación de un marciano
    public static void sumarMarciano(boolean exp){
        marcianos++;
        //Si tiene power up de doble experiencia se suma el doble
        if(exp){
            puntos+=200;
        }else{
            puntos+=100;
        }
    }

    //Suma la puntuación de un asteroide
    public static void sumarAsteroide(boolean exp){
        asteroides++;
        //Si tiene power up de doble experiencia se suma el doble
        if(exp){
            puntos+=40;
        }else{
            puntos+=20;
        }

    }

    //Suma los puntos por matar a Zurg
    public static void sumarZurg(){
        puntos+=500;
        zurg += 500;
    }

    //Suma los disparos a zurg
    public static void zurgDisparo(int i){
        puntos += i;
        zurg += i;
    }

}
