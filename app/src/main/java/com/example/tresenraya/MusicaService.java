package com.example.tresenraya;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicaService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inicializa el reproductor de audio con el archivo en res/raw/musica_fondo
        mediaPlayer = MediaPlayer.create(this, R.raw.musica_fondo);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true); // Se repite continuamente
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences preferences = getSharedPreferences("AjustesJuego", Context.MODE_PRIVATE);
        boolean sonidoActivado = preferences.getBoolean("sonido", true);

        if (sonidoActivado) {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } else {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}