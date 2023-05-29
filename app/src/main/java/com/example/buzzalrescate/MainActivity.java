package com.example.buzzalrescate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.buzzalrescate.bd.DbManager;
import com.example.buzzalrescate.interfaz.CreditosFragment;
import com.example.buzzalrescate.interfaz.GameFragment;
import com.example.buzzalrescate.interfaz.MainMenuFragment;
import com.example.buzzalrescate.interfaz.RankingFragment;
import com.example.buzzalrescate.interfaz.ResultadosFragment;
import com.example.buzzalrescate.interfaz.SeleccionFragment;
import com.example.buzzalrescate.interfaz.TutorialFragment;
import com.example.buzzalrescate.sound.SoundManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "content";
    public static final String PREFS_NAME = "MyPrefsFile";

    //Ancho y alto de la pantalla
    public static int widthDisplay;
    public static int heightDisplay;

    //Preferencias
    public static SharedPreferences settings;
    public static SharedPreferences.Editor editor;

    //Dbmanager base de datos y soundmanager de sonidos
    public DbManager dbManager;
    public SoundManager soundManager;

    //Referencias a fragmentos de juego y menu
    GameFragment gm;
    MainMenuFragment mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se inicializa con el fragmento de main menu
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainMenuFragment(), "MainMenuFragment")
                    .commit();
        }

        //Añade las preferencias
        settings = getSharedPreferences(PREFS_NAME, 0);
        settings.getInt("buzz", 0);
        editor = settings.edit();

        //DbManager de la base de datos
        dbManager = new DbManager(this);

        //Guarda las variables de ancho y alto del dispositivo
        DisplayMetrics display = getResources().getDisplayMetrics();
        widthDisplay = display.widthPixels;
        heightDisplay = display.heightPixels;
        if(widthDisplay<heightDisplay){
            widthDisplay = display.heightPixels;
            heightDisplay = display.widthPixels;
        }

        //Crea un sound Manager
        soundManager = new SoundManager(getApplicationContext());
    }

    //Método para obtener el sound manager
    public SoundManager getSoundManager() {
        return soundManager;
    }

    //Método que se llama cuando se quiere empezar el juego, cambia el fragmento
    public void startGame() {
        //Si el fragmento no ha sido creado, se crea
        if(gm == null){
            gm = new GameFragment();
        }
        // Navigate the the game fragment, which makes the start automatically
        //Cambia la música a la del juego
        soundManager.loadMusic(true);
        navigateToFragment(gm);
    }

    //Método que se llama para cambiar fragmento a créditos
    public void creditos() {
        //Navega al fragmento de creditos
        navigateToFragment( new CreditosFragment());
    }

    //Método que se llama para cambiar fragmento a seleccion de buzz
    public void seleccion(){
        //Navega al fragmento de seleccion de personaje o nave
        navigateToFragment( new SeleccionFragment());
    }

    //Método que se llama para cambiar fragmento a ranking
    public void ranking() {
        //Navega al fragmento de ranking
        navigateToFragment( new RankingFragment());
    }

    //Método que se llama para cambiar el fragmento a menú, como argumento si hay que cambiar la música
    public void menu(boolean musica){
        //Si el fragmento está a null, se crea
        if(mm == null){
            mm = new MainMenuFragment();
        }
        //Si hay que cambiar la musica, se llama al soundManager, false porque no es juego
        if(musica){
            soundManager.loadMusic(false);
        }
        //Navega al fragmento de menu
        navigateToFragment( mm);
    }

    //Método que cambia el fragmento a tutorial
    public void tutorial(){
        navigateToFragment(new TutorialFragment());
    }

    //Método que cambia a resultados, cuando se pierde o gana
    public void gameFinished(){
        //Navega al fragmento de game Over y cambia la música a la de menú
        soundManager.loadMusic(false);
        navigateToFragment( new ResultadosFragment());
    }

    //Método usado para navegar entre fragmentos
    private void navigateToFragment(Fragment dst) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, dst, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }


    //Gestión del botón de volver atrás
    @Override
    public void onBackPressed() {

    final BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }


    public void navigateBack() {
        // Do a push on the navigation history
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

}