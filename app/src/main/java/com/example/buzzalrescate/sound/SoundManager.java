package com.example.buzzalrescate.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;
import java.util.HashMap;

public final class SoundManager {
    //Constantes streams y volumen de la música por defecto
    private static final int MAX_STREAMS = 10;
    private static final float DEFAULT_MUSIC_VOLUME = 0.6f;

    //HashMap con los eventos de sonido
    private HashMap<GameEvent, Integer> soundsMap;

    //Variables contexto, pool de sonidos y mediaplayer
    private Context context;
    private SoundPool soundPool;
    private MediaPlayer bgPlayer;

    //Constructor de soundmanager, carga los sonidos e inicia la cancion de menu
    public SoundManager(Context context) {
        this.context = context;
        loadSounds();
        loadMusic(false);
    }

    //Carga el efecto de sonido por el evento
    private void loadEventSound(Context context, GameEvent event, String... filename) {
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd("sfx/" + filename[0]);
            int soundId = soundPool.load(descriptor, 1);
            soundsMap.put(event, soundId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Carga el sonido del botón
    public void loadSoundButton(GameEvent event){
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd("sfx/botones");
            int soundId = soundPool.load(descriptor, 1);
            soundsMap.put(event, soundId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Reproduce el sonido del gameevent
    public void playSoundForGameEvent(GameEvent event) {
        Integer soundId = soundsMap.get(event);
        if (soundId != null) {
            // Left Volume, Right Volume, priority (0 == lowest), loop (0 == no) and rate (1.0 normal playback rate)
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    //Crea el pool de sonidos y rellena el hashMap, con los distintos sonidos que pertenecen a cada evento
    private void loadSounds() {
        createSoundPool();
        soundsMap = new HashMap<GameEvent, Integer>();
        loadEventSound(context, GameEvent.AsteroidHit, "explosionasteroide.mp3");
        loadEventSound(context, GameEvent.BuzzHit, "perdervidabuzz.mp3");
        loadEventSound(context, GameEvent.LaserFired, "laserbuzz.mp3");
        loadEventSound(context, GameEvent.GanchoFired, "ganchobuzz.mp3");
        loadEventSound(context, GameEvent.CatchMarciano, "cogermarciano.mp3");
        loadEventSound(context, GameEvent.CatchPowerUp, "cogerpowerup.mp3");
        loadEventSound(context, GameEvent.GameOver, "derrota.mp3");
        loadEventSound(context, GameEvent.ZurgShot, "disparozurg.mp3");
        loadEventSound(context, GameEvent.ZurgLaser, "laserzurg.mp3");
        loadEventSound(context, GameEvent.WinGame, "victoria.mp3");
        loadEventSound(context, GameEvent.ZurgKilled, "zurgmuerte.mp3");
        loadEventSound(context, GameEvent.TransicionZurg, "transicion.mp3");
        //loadEventSound(context, GameEvent.ZurgHit, "laserzurg.mp3");
    }

    //Carga la musica, recibe un boolean si es la música del juego o del menú
    public void loadMusic(boolean juego) {
        try {
            // Important to not reuse it. It can be on a strange state
            if(bgPlayer != null){
                unloadMusic();
            }
            bgPlayer = new MediaPlayer();
            AssetFileDescriptor afd;
            //Si es música del juego
            if(juego){
                afd = context.getAssets().openFd("sfx/juego.mp3");
            }else{
                afd = context.getAssets().openFd("sfx/menu.mp3");
            }
            bgPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            bgPlayer.setLooping(true);
            bgPlayer.setVolume(DEFAULT_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
            bgPlayer.prepare();
            bgPlayer.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Crea la pool de sonidos
    private void createSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(MAX_STREAMS)
                    .build();
        }
    }

    //Limpia el pool y hashmap de sonidos
    private void unloadSounds() {
        soundPool.release();
        soundPool = null;
        soundsMap.clear();
    }

    //Para la música
    public void unloadMusic() {
        bgPlayer.stop();
        bgPlayer.release();
    }
}

