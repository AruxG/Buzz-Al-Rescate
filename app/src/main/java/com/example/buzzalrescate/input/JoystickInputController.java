package com.example.buzzalrescate.input;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.buzzalrescate.R;
import com.example.buzzalrescate.interfaz.ScrollingBackground;

public class JoystickInputController extends InputController{
    //Posición inicial
    private float startingPositionX;
    private float startingPositionY;

    //Máximo de distancia
    private final double maxDistance;

    //Referencia al botón de disparo
    public Button disparo;

    //Constructor disparo
    public JoystickInputController(View view) {
        view.findViewById(R.id.joystick_main).setOnTouchListener(new JoystickTouchListener());
        disparo = (Button)view.findViewById(R.id.botonDisparar);
        disparo.setOnTouchListener(new FireButtonTouchListener());

        double pixelFactor = view.getHeight() / 400d;
        maxDistance = 50*pixelFactor;
    }

    //Método que cambia el movimiento con el input del joystick tocando la pantalla
    private class JoystickTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                startingPositionX = event.getX(0);
                startingPositionY = event.getY(0);
            }
            else if (action == MotionEvent.ACTION_UP) {
                horizontalFactor = 0;
                verticalFactor = 0;
            }
            else if (action == MotionEvent.ACTION_MOVE) {
                // Get the proportion to the max
                horizontalFactor = (event.getX(0) - startingPositionX) / maxDistance;
                if (horizontalFactor > 1) {
                    horizontalFactor = 1;
                }
                else if (horizontalFactor < -1) {
                    horizontalFactor = -1;
                }
                verticalFactor = (event.getY(0) - startingPositionY) / maxDistance;
                if (verticalFactor > 1) {
                    verticalFactor = 1;
                }
                else if (verticalFactor < -1) {
                    verticalFactor = -1;
                }
            }
            return true;
        }
    }

    //Método para disparar ganchos cuando se pulsa el botón
    private class FireButtonTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
               disparo.setBackgroundResource(R.drawable.botondisparooff);
               //ScrollingBackground.boss = true;
                isFiring = true;
            }
            else if (action == MotionEvent.ACTION_UP) {
                disparo.setBackgroundResource(R.drawable.botondisparo);
                isFiring = false;
            }
            return true;
        }
    }
}
