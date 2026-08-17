package com.example.tresenraya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaHome extends AppCompatActivity {

    private Vibrator vibrator;
    private SharedPreferences preferences;
    private boolean vibracionActivada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_home);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        preferences = getSharedPreferences("AjustesJuego", Context.MODE_PRIVATE);

        //Iniciar el servicio de musica continuo
        Intent musicIntent = new Intent(this, MusicaService.class);
        startService(musicIntent);

        //Obtener los botones del layout
        ImageButton btnJugar = findViewById(R.id.btnJugar);
        ImageButton btnEstadisticas = findViewById(R.id.btnEstadisticas);
        ImageButton btnAjustes = findViewById(R.id.btnAjustes);
        ImageButton btnSalir = findViewById(R.id.btnSalir);

        //Configuracion de clics directa sin animaciones
        btnJugar.setOnClickListener(v -> {
            ejecutarVibracion();
            startActivity(new Intent(PantallaHome.this, PantallaModos.class));
        });

        btnEstadisticas.setOnClickListener(v -> {
            ejecutarVibracion();
            startActivity(new Intent(PantallaHome.this, PantallaEstadisticas.class));
        });

        btnAjustes.setOnClickListener(v -> {
            ejecutarVibracion();
            startActivity(new Intent(PantallaHome.this, PantallaAjustes.class));
        });

        btnSalir.setOnClickListener(v -> {
            ejecutarVibracion();
            stopService(new Intent(PantallaHome.this, MusicaService.class));
            finishAffinity();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Cargar preferencias actualizadas al regresar a la pantalla principal
        vibracionActivada = preferences.getBoolean("vibracion", true);

        //Asegurar que el servicio actualice el estado de reproducción si cambió en Ajustes
        Intent musicIntent = new Intent(this, MusicaService.class);
        startService(musicIntent);
    }


    //Hace vibrar el telefono si la opcion est activada en las preferencias
    private void ejecutarVibracion() {
        if (vibracionActivada && vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //50ms con amplitud maxima (255)
                vibrator.vibrate(VibrationEffect.createOneShot(50, 255));
            } else {
                vibrator.vibrate(50);
            }
        }
    }
}