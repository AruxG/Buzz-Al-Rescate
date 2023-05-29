package com.example.buzzalrescate.movement;

import android.view.View;
import android.widget.TextView;

import com.example.buzzalrescate.engine.GameObject;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.input.InputController;
import com.example.buzzalrescate.R;

public abstract class Player extends GameObject {
    //Variables de máximo x e y, posiciones, factor de velocidad, y factor píxel
    protected int maxX;
    protected int maxY;
    protected double positionX;
    protected double positionY;
    protected double speedFactor;
    protected double pixelFactor;

    //Variable textView y view
    protected final TextView textView;
    protected final View view;

    //Constructor player, define pixelfactor, speedfactor y máximos x e y
    public Player(final View parentView){
        view = parentView;
        pixelFactor = view.getHeight() / 400d;
        maxX = view.getWidth() - view.getPaddingRight() - view.getPaddingRight();
        maxY = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();
        speedFactor = pixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen

        textView = (TextView) view.findViewById(R.id.score);
    }

    //Cuando empieza el juego, se setea la posición relativa a los máximos
    @Override
    public void startGame() {
        positionX = maxX / 8;
        positionY = maxY / 2;
    }

}
