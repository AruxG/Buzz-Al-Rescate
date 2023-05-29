package com.example.buzzalrescate.interfaz;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buzzalrescate.BaseFragment;
import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.sound.GameEvent;

public class CreditosFragment extends BaseFragment {

    public View view;
    public CreditosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_creditos, container, false);
        view.findViewById(R.id.salirCreditos).setOnClickListener(this);
        return view;
    }

    public void onClick(View v){
        //Reproduce el sonido del botón
        ((MainActivity)getActivity()).soundManager.loadSoundButton(GameEvent.botones);
       if(v.getId() == R.id.salirCreditos){
           //Cambia el fragmento a menu, sin parar la música
           ((MainActivity)getActivity()).menu(false);
        }
    }

}