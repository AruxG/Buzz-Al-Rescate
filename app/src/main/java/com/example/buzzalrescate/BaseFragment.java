package com.example.buzzalrescate;

import android.view.View;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment implements View.OnClickListener{
    //Gestión del botón hacia atrás
    public boolean onBackPressed() {
        return false;
    }

    //Método on click
    @Override
    public void onClick(View v) {

    }
}
