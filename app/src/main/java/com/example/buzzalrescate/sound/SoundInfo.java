package com.example.buzzalrescate.sound;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

public class SoundInfo {
    //Variables estáticas
    private static final long FLOOD_TIMEOUT = 500;
    private static final Random random = new Random();

    //Variables array de sonidos e id sonido
    private int[] soundId;
    private long msoundId;

    //Método que se encarga de cargar los sonidos y mostrar información si se producen errores
    public SoundInfo(Context context, SoundPool engine, String[] filename) {
        soundId = new int [filename.length];
        for (int i=0; i<filename.length; i++) {
            try {
                soundId[i] = engine.load(context.getAssets().openFd("music/"+filename[i]), 1);
            } catch (IOException e) {
                Log.e("SoundInfo", "Sound not found: "+filename[i]);
                e.printStackTrace();
            }
        }
    }

    //Método para reproducir sonidos
    public void play(SoundPool engineSound) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - msoundId > FLOOD_TIMEOUT) {
            msoundId = currentTime;
            int posToPlay = random.nextInt(soundId.length);
            engineSound.play(soundId[posToPlay], 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

}

