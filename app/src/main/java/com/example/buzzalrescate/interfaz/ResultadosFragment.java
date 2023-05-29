package com.example.buzzalrescate.interfaz;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buzzalrescate.BaseFragment;
import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.bd.DbManager;
import com.example.buzzalrescate.sound.GameEvent;

public class ResultadosFragment extends BaseFragment {
    View view;
    DbManager dbManager;
    public ResultadosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_resultados, container, false);
        view.findViewById(R.id.salirResultados).setOnClickListener(this);
        view.findViewById(R.id.nuevaPartida).setOnClickListener(this);

        int puntos = ScoreGameObject.puntos + (VidasGameObject.vidas*300);
        ((TextView)view.findViewById(R.id.puntos)).setText("Puntos: "+puntos);
        ((TextView)view.findViewById(R.id.asteroides)).setText("Asteroides: "+ScoreGameObject.asteroides+ " x 20");
        ((TextView)view.findViewById(R.id.marcianos)).setText("Marcianos: "+ScoreGameObject.marcianos+" x 100");
        ((TextView)view.findViewById(R.id.vida)).setText("Vidas: "+VidasGameObject.vidas+ " x 300");
        ((TextView)view.findViewById(R.id.zurg)).setText("Zurg: "+ScoreGameObject.zurg);

        if(VidasGameObject.vidas > 0){
            ((ImageView)view.findViewById(R.id.tituloResultados)).setImageResource(R.drawable.victoriatitulo);
            ((Button)view.findViewById(R.id.nuevaPartida)).setBackgroundResource(R.drawable.botonvolverajugar);
            ((ImageView)view.findViewById(R.id.fondoResultados)).setImageResource(R.drawable.fondovictoria);
        }
        
        this.dbManager = new DbManager(getContext());
        dbManager.insertEntry("Player",puntos);
        Cursor ranking = this.dbManager.getEntries("ranking");
        //Si las puntuaciones que hay en la bd son mayores a 10, se eliminan los restantes
        while(ranking.getCount()>10){
            ranking.moveToPosition(10);
            this.dbManager.deleteTitle("ranking", ranking.getString(0),ranking.getInt(1));
            ranking = this.dbManager.getEntries("ranking");
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        //Reproduce el sonido del botón
        ((MainActivity)getActivity()).soundManager.loadSoundButton(GameEvent.botones);
        //Realiza una acción dependiendo del botón
        switch(v.getId()){
            case R.id.salirResultados:
                ((MainActivity)getActivity()).menu(false);
                break;
            case R.id.nuevaPartida:
                ((MainActivity)getActivity()).startGame();
                break;
            default:
                break;
        }
    }

}