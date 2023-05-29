package com.example.buzzalrescate.interfaz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.buzzalrescate.BaseFragment;
import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.sound.GameEvent;

public class MainMenuFragment extends BaseFragment {

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        //Asigna el onclicklistener a los botones
        view.findViewById(R.id.jugar).setOnClickListener(this);
        view.findViewById(R.id.ranking).setOnClickListener(this);
        view.findViewById(R.id.creditos).setOnClickListener(this);
        view.findViewById(R.id.salir).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        //Reproduce el sonido del botón
        ((MainActivity)getActivity()).soundManager.loadSoundButton(GameEvent.botones);
        //Cada botón realiza una acción
        switch(v.getId()){
            case R.id.jugar:
                ((MainActivity)getActivity()).seleccion();
                break;
            case R.id.ranking:
                ((MainActivity)getActivity()).ranking();
                break;
            case R.id.creditos:
                ((MainActivity)getActivity()).creditos();
                break;
            case R.id.salir:
                ((MainActivity)getActivity()).finish();
                ((MainActivity)getActivity()).soundManager.unloadMusic();
                break;
            default:
                break;
        }
    }

    //Gestión del botón de vuelta atrás
    @Override
    public boolean onBackPressed() {
        ((MainActivity)getActivity()).finish();
        ((MainActivity)getActivity()).soundManager.unloadMusic();
        return true;
    }
}