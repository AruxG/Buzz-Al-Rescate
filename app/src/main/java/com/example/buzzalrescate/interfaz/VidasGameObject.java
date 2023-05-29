package com.example.buzzalrescate.interfaz;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.GameObject;

public class VidasGameObject extends GameObject {

    //Referencia a textView y vidas
    private final TextView textView;
    public static int vidas;

    //Constructor
    public VidasGameObject(View view, int viewResId) {
        textView = (TextView) view.findViewById(viewResId);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //totalMillis += elapsedMillis;

    }

    //Pinta el número de vidas por pantalla
    @Override
    public void onDraw(Canvas canvas) {
        textView.setText(String.valueOf(vidas));
    }

    @Override
    public void startGame() {
        //Dependiendo del buzz elegido se le asigna un número de vidas
        switch (MainActivity.settings.getInt("buzz",0)){
            case R.drawable.buzz1volando:
                vidas = 5;
                break;
            case R.drawable.buzz2volando:
                vidas = 3;
                break;
            default:
                vidas = 5;
                break;
        }
    }

    //Resta una vida
    public static void restarVidas(){
        vidas--;
    }

    //Suma una vida
    public static void sumarVidas(){
        vidas++;
    }
}
