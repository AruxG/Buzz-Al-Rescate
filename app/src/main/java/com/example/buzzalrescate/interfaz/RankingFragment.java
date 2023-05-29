package com.example.buzzalrescate.interfaz;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.buzzalrescate.BaseFragment;
import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.bd.DbManager;
import com.example.buzzalrescate.sound.GameEvent;

import java.util.ArrayList;

public class RankingFragment extends BaseFragment {

    public View view;
    private ArrayList<String> arrayResultados = new ArrayList<>();
    DbManager dbManager;

    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ranking, container, false);
        view.findViewById(R.id.salirRanking).setOnClickListener(this);
        //Obtenemos el cursor de la tabla ranking
        this.dbManager = new DbManager(getContext());
        Cursor ranking = this.dbManager.getEntries("ranking");

        //Añadimos al listview las puntuaciones
        ListView result = (ListView)view.findViewById(R.id.listRanking);
        for(int i=0;i<ranking.getCount();i++){
            ranking.moveToPosition(i);
            arrayResultados.add(ranking.getString(ranking.getColumnIndex("player"))+": "+ranking.getString(ranking.getColumnIndex("maximapuntuacion")));
        }
        //Le asignamos el array adapter
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(),R.layout.customlistview,arrayResultados);
        result.setAdapter(adaptador);
        return view;
    }

    public void onClick(View v){
        //Reproduce el sonido del botón
        ((MainActivity)getActivity()).soundManager.loadSoundButton(GameEvent.botones);
        if (v.getId() == R.id.salirRanking){
            //Vuelve al menu
            ((MainActivity)getActivity()).menu(false);
        }
    }

}