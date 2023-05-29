package com.example.buzzalrescate.interfaz;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.buzzalrescate.BaseFragment;
import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.sound.GameEvent;

public class SeleccionFragment extends BaseFragment{
View view;
    public SeleccionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seleccion, container, false);

        //Se le asigna el listener del onclick a los botones
        view.findViewById(R.id.opcion1).setOnClickListener(this);
        view.findViewById(R.id.opcion2).setOnClickListener(this);
        view.findViewById(R.id.jugarSeleccion).setOnClickListener(this);
        view.findViewById(R.id.salirSeleccion).setOnClickListener(this);

        //Dependiendo del buzz elegido anteriormente, se selecciona el correspondiente, cambiando el drawable
        switch(MainActivity.settings.getInt("buzz",0)){
            case R.drawable.buzz1volando:
                ((ImageButton)view.findViewById(R.id.opcion1)).setImageResource(R.drawable.seleccionarbuzz1on);
                break;
            case R.drawable.buzz2volando:
                ((ImageButton)view.findViewById(R.id.opcion2)).setImageResource(R.drawable.seleccionarbuzz2on);
                break;
            default:
                ((ImageButton)view.findViewById(R.id.opcion1)).setImageResource(R.drawable.seleccionarbuzz1on);
                MainActivity.editor.putInt("buzz",R.drawable.buzz1volando);
                MainActivity.editor.commit();
                break;
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        //Reproduce sonido de botón
        ((MainActivity)getActivity()).soundManager.loadSoundButton(GameEvent.botones);
        switch(v.getId()){
            //Si se selecciona buzz1
            case R.id.opcion1:
                //Guarda la informacion y tinta
                ((ImageButton)view.findViewById(R.id.opcion1)).setImageResource(R.drawable.seleccionarbuzz1on);
                ((ImageButton)view.findViewById(R.id.opcion2)).setImageResource(R.drawable.seleccionarbuzz2);
                MainActivity.editor.putInt("buzz",R.drawable.buzz1volando);
                MainActivity.editor.commit();
                break;
            //Si se selecciona buzz2
            case R.id.opcion2:
                //Guarda información y tinta
                ((ImageButton)view.findViewById(R.id.opcion2)).setImageResource(R.drawable.seleccionarbuzz2on);
                ((ImageButton)view.findViewById(R.id.opcion1)).setImageResource(R.drawable.seleccionarbuzz1);
                MainActivity.editor.putInt("buzz",R.drawable.buzz2volando);
                MainActivity.editor.commit();
                break;
            //Salir al menu
            case R.id.salirSeleccion:
                ((MainActivity)getActivity()).menu(false);
                break;
            //Jugar, cambia al fragmento
            case R.id.jugarSeleccion:
                ((MainActivity)getActivity()).tutorial();
                break;
            default:
                break;
        }
    }

}