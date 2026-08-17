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

public class PantallaModos extends AppCompatActivity {

    private Vibrator vibrator;
    private SharedPreferences preferences;
    private boolean vibracionActivada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_modos);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        preferences = getSharedPreferences("AjustesJuego", Context.MODE_PRIVATE);

        //Referencias de los botones en la interfaz
        ImageButton btnJugvsMaquina = findViewById(R.id.btnJugvsMaquina);
        ImageButton btnJugvsJug = findViewById(R.id.btnJugvsJug);
        ImageButton btnMaqvsMaq = findViewById(R.id.btnMaqvsMaq);
        ImageButton btnVolver = findViewById(R.id.btnVolver);
        ImageButton btnTemas = findViewById(R.id.btnTemas);

        //Jugador vs Maquina a la PantallaIngresarNombreJugador
        btnJugvsMaquina.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaModos.this, PantallaIngresarNombreJugador.class);
            startActivity(intent);
        });

        //Jugador vs Jugador a la PantallaDosJug
        btnJugvsJug.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaModos.this, PantallaDosJug.class);
            startActivity(intent);
        });

        //Maquina vs Maquina a la PantallaIntermediaMaquina
        btnMaqvsMaq.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaModos.this, PantallaIntermediaMaquina.class);
            startActivity(intent);
        });

        //Boton Temas
        if (btnTemas != null) {
            btnTemas.setOnClickListener(v -> {
                ejecutarVibracion();
                Intent intent = new Intent(PantallaModos.this, PantallaTemas.class);
                startActivity(intent);
            });
        }

        //Boton Volver
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> {
                ejecutarVibracion();
                finish();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        vibracionActivada = preferences.getBoolean("vibracion", true);

        //Asegurar que la musica continue sonando
        Intent musicIntent = new Intent(this, MusicaService.class);
        startService(musicIntent);
    }

    private void ejecutarVibracion() {
        if (vibracionActivada && vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, 255));
            } else {
                vibrator.vibrate(50);
            }
        }
    }
}