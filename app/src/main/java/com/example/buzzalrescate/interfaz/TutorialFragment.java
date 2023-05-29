package com.example.buzzalrescate.interfaz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buzzalrescate.BaseFragment;
import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.sound.GameEvent;


public class TutorialFragment extends BaseFragment {
    View view;
    public TutorialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_tutorial, container, false);
        //Asigna los onclick a los botones
        view.findViewById(R.id.salirTutorial).setOnClickListener(this);
        view.findViewById(R.id.botonEntendido).setOnClickListener(this);
        return view;
    }

    public void onClick(View v){
        //Reproduce el sonido de botón
        ((MainActivity)getActivity()).soundManager.loadSoundButton(GameEvent.botones);
        switch (v.getId()){
            //Salir, vuelve a selección de buzz
            case R.id.salirTutorial:
                ((MainActivity)getActivity()).seleccion();
                break;
            //Inicia el juego
            case R.id.botonEntendido:
                ((MainActivity)getActivity()).startGame();
                break;
            default:
                break;
        }
    }

}