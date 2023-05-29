package com.example.buzzalrescate.interfaz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;

import com.example.buzzalrescate.BaseFragment;
import com.example.buzzalrescate.MainActivity;
import com.example.buzzalrescate.R;
import com.example.buzzalrescate.buzz1.BuzzPlayer;
import com.example.buzzalrescate.buzz1.GameController;
import com.example.buzzalrescate.engine.FramesPerSecondCounter;
import com.example.buzzalrescate.engine.GameEngine;
import com.example.buzzalrescate.engine.GameObject;
import com.example.buzzalrescate.engine.GameView;
import com.example.buzzalrescate.engine.ScreenGameObject;
import com.example.buzzalrescate.input.JoystickInputController;
import com.example.buzzalrescate.sound.GameEvent;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends BaseFragment implements View.OnClickListener{

    //Referencia gameEngine
    private GameEngine theGameEngine;

    //View y diálogo de pausa
    View view;
    AlertDialog pausa;

    //Constructor
    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_game, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Crea el juego
        view.findViewById(R.id.pauseGame).setOnClickListener(this);
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                //Carga los distintos bitmaps de fondo
                Bitmap bmp = BitmapFactory.decodeResource(getActivity().getResources(),
                        R.drawable.fondoobjetos);
                Bitmap bmpT = BitmapFactory.decodeResource(getActivity().getResources(),
                        R.drawable.fondoobjetostrans);
                Bitmap bmpB = BitmapFactory.decodeResource(getActivity().getResources(),
                        R.drawable.fondoobjetososc);

                //Para evitar que sea llamado múltiples veces,
                //se elimina el listener en cuanto es llamado
                observer.removeOnGlobalLayoutListener(this);

                //Asigna la gameView
                GameView gameView = (GameView) getView().findViewById(R.id.gameView);
                //Crea un nuevo game Engine y añade todos los gameObjects
                theGameEngine = new GameEngine(getActivity(), gameView,(MainActivity)getActivity());
                theGameEngine.addGameObject(new ScrollingBackground(bmp, bmpT,bmpB,MainActivity.widthDisplay,MainActivity.heightDisplay,theGameEngine));
                theGameEngine.setSoundManager(((MainActivity)getActivity()).getSoundManager());
                theGameEngine.setTheInputController(new JoystickInputController(getView()));
                theGameEngine.addGameObject(new BuzzPlayer(theGameEngine));
                //theGameEngine.addGameObject(new FramesPerSecondCounter(theGameEngine));
                theGameEngine.addGameObject(new GameController(theGameEngine));
                theGameEngine.addGameObject(new ScoreGameObject(view, R.id.score));
                theGameEngine.addGameObject(new VidasZurgGameObject(view, R.id.vidasZurg, R.id.contZurg));
                theGameEngine.addGameObject(new VidasGameObject(view, R.id.vidas));

                //Inicializa el juego
                theGameEngine.startGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        //Reproduce el sonido de los botones
        ((MainActivity)getActivity()).soundManager.loadSoundButton(GameEvent.botones);
        //Si se le da al botón de pausa abre el mensaje de dialogo
        if (v.getId() == R.id.pauseGame) {
            pauseGameAndShowPauseDialog();
        }
    }

    //Si el juego se pausa, abre el diálogo de pausa
    @Override
    public void onPause() {
        super.onPause();
        if (theGameEngine.isRunning()){
                pauseGameAndShowPauseDialog();
        }
    }

    //Destruye el gameEngine
    @Override
    public void onDestroy() {
        super.onDestroy();
        theGameEngine.stopGame();
    }

    //Gestión del botón de volver atrás
    @Override
    public boolean onBackPressed() {
        if (theGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
            return false;

    }

    //Muestra el mensaje de pausa y para el juego
    private void pauseGameAndShowPauseDialog() {
        theGameEngine.pauseGame();
        if(pausa != null){
            pausa.dismiss();
        }
        pausa = createCustomDialog();
        pausa.show();
    }


    //Crea un menú de pausa a partir de un view creado
    public AlertDialog createCustomDialog() {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.dialog_signin, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        Button btnMenu = (Button) v.findViewById(R.id.btn_menu);
        Button btnReanudar = (Button) v.findViewById(R.id.btn_reanudar);
        v.setLayoutParams(new RecyclerView.LayoutParams(300, 200));

        builder.setView(v);
        alertDialog = builder.create();
        // Add action buttons
        //Asigna acciones al botón de menu
        btnMenu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Aceptar
                        alertDialog.dismiss();
                        theGameEngine.stopGame();
                        ((MainActivity) getActivity()).menu(true);
                    }
                }
        );
        //Asigna accions al botón de resume o reanudar
        btnReanudar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        theGameEngine.resumeGame();
                    }
                }
        );
        alertDialog.getWindow().setLayout(100,100);
        //Gestión del botón hacia atrás
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    dialog.cancel();
                    theGameEngine.resumeGame();
                    return true;
                }
                return false;
            }
        });
        return alertDialog;
    }

    private void playOrPause() {
        Button button = (Button) getView().findViewById(R.id.pauseGame);
        if (theGameEngine.isPaused()) {
            theGameEngine.resumeGame();
            button.setText(R.string.pause);
        }
        else {
            theGameEngine.pauseGame();
            button.setText(R.string.resume);
        }
    }

}