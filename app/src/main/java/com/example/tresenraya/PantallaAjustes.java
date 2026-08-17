package com.example.tresenraya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaAjustes extends AppCompatActivity {

    private ImageButton btnVolumen;
    private ImageButton btnVibracion;
    private ImageButton btnVolver;

    private boolean sonidoActivado;
    private boolean vibracionActivada;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_ajustes);

        btnVolumen = findViewById(R.id.btnVolumen);
        btnVibracion = findViewById(R.id.btnVibracion);
        btnVolver = findViewById(R.id.btnVolver);

        preferences = getSharedPreferences("AjustesJuego", Context.MODE_PRIVATE);

        //Cargar los estados guardados que por defecto son true
        sonidoActivado = preferences.getBoolean("sonido", true);
        vibracionActivada = preferences.getBoolean("vibracion", true);

        //Mostrar las imagenes correspondientes segun el estado actual
        actualizarImagenVolumen();
        actualizarImagenVibracion();

        //Control de volumen y sonido
        btnVolumen.setOnClickListener(v -> {
            sonidoActivado = !sonidoActivado;
            preferences.edit().putBoolean("sonido", sonidoActivado).apply();
            actualizarImagenVolumen();

            //Notificar al servicio de musica para encender o pausar inmediatamente
            Intent musicIntent = new Intent(PantallaAjustes.this, MusicaService.class);
            startService(musicIntent);
        });

        //Control de Vibracion
        btnVibracion.setOnClickListener(v -> {
            vibracionActivada = !vibracionActivada;
            preferences.edit().putBoolean("vibracion", vibracionActivada).apply();
            actualizarImagenVibracion();
        });

        //Boton de regresar
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }
    }

    private void actualizarImagenVolumen() {
        if (sonidoActivado) {
            btnVolumen.setImageResource(R.drawable.boton_volumen);
        } else {
            btnVolumen.setImageResource(R.drawable.boton_volumen_apagado);
        }
    }

    private void actualizarImagenVibracion() {
        if (vibracionActivada) {
            btnVibracion.setImageResource(R.drawable.boton_vibracion);
        } else {
            btnVibracion.setImageResource(R.drawable.boton_vibracion_apagado);
        }
    }
}