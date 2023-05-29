package com.example.buzzalrescate.interfaz;

import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.GameObject;

public class VidasZurgGameObject extends GameObject {

    //Referencia a textView e imageView
    private TextView textView;
    private ImageView imageView;

    //Vidas de zurg y si esta visible
    public static int vidasZurg = 50;
    private static boolean visible = false;

    //Constructor
    public VidasZurgGameObject(View view, int viewResId, int viewContZurg) {
        textView = (TextView) view.findViewById(viewResId);
        imageView = (ImageView) view.findViewById(viewContZurg);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //totalMillis += elapsedMillis;

    }

    //Pone visible ambas cosas y pinta el número de vidas
    @Override
    public void onDraw(Canvas canvas) {
        if(visible){
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
        textView.setText(String.valueOf(vidasZurg));
    }

    //Reseta las variables
    @Override
    public void startGame() {
       vidasZurg = 50;
       visible = false;
    }

    //Resta vidas al zurg
    public static void restarVida(int v){
        vidasZurg -= v;
        if(vidasZurg <= 0){
            vidasZurg = 0;
        }
    }

    //Pone visible el número de vidas de zurg
    public static void setVisible(){
        visible = true;
    }
}
