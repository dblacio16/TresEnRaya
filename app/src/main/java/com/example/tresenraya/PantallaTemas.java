package com.example.tresenraya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class PantallaTemas extends AppCompatActivity {

    //Pestañas superiores
    private ImageButton btnSeccionIconos, btnSeccionTablero;
    private LinearLayout containerIconos, containerTableros;

    //Botones del catlogo de Iconos
    private ImageButton btnIconoA, btnIconoB, btnIconoC, btnIconoD, btnIconoE, btnIconoF, btnIconoG;

    //Botones del catalogo de Tableros
    private ImageButton btnTableroA, btnTableroB, btnTableroC, btnTableroD, btnTableroE, btnTableroF, btnTableroG;

    //Navegacion inferior
    private ImageButton btnVolver;

    private Vibrator vibrator;
    private SharedPreferences preferences;
    private boolean vibracionActivada;

    //Guardan el estado actual
    private boolean modoIconosActivo = true;
    private String iconoSeleccionado;   // "A", "B", "C", "D", "E", "F", "G"
    private String tableroSeleccionado; // "A", "B", "C", "D", "E", "F", "G"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_temas);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        preferences = getSharedPreferences("AjustesJuego", Context.MODE_PRIVATE);

        //Cargar selecciones previas o por defecto
        iconoSeleccionado = preferences.getString("ICONO_SELECCIONADO", "A");
        tableroSeleccionado = preferences.getString("TABLERO_SELECCIONADO", "A");

        //Referencias de Pestañas y Contenedores
        btnSeccionIconos = findViewById(R.id.btnSeccionIconos);
        btnSeccionTablero = findViewById(R.id.btnSeccionTablero);
        containerIconos = findViewById(R.id.containerIconos);
        containerTableros = findViewById(R.id.containerTableros);

        //Referencias de Iconos
        btnIconoA = findViewById(R.id.btnIconoA);
        btnIconoB = findViewById(R.id.btnIconoB);
        btnIconoC = findViewById(R.id.btnIconoC);
        btnIconoD = findViewById(R.id.btnIconoD);
        btnIconoE = findViewById(R.id.btnIconoE);
        btnIconoF = findViewById(R.id.btnIconoF);
        btnIconoG = findViewById(R.id.btnIconoG);

        //Referencias de Tableros
        btnTableroA = findViewById(R.id.btnTableroA);
        btnTableroB = findViewById(R.id.btnTableroB);
        btnTableroC = findViewById(R.id.btnTableroC);
        btnTableroD = findViewById(R.id.btnTableroD);
        btnTableroE = findViewById(R.id.btnTableroE);
        btnTableroF = findViewById(R.id.btnTableroF);
        btnTableroG = findViewById(R.id.btnTableroG);

        //Referencias de Navegacion
        btnVolver = findViewById(R.id.btnVolver);

        //Renderizar vistas segun las selecciones actuales
        actualizarVisibilidadPestanas();
        actualizarSeleccionIconos();
        actualizarSeleccionTableros();

        //Listeners para cambiar entre pestañas
        btnSeccionIconos.setOnClickListener(v -> {
            if (!modoIconosActivo) {
                ejecutarVibracion();
                modoIconosActivo = true;
                actualizarVisibilidadPestanas();
            }
        });

        btnSeccionTablero.setOnClickListener(v -> {
            if (modoIconosActivo) {
                ejecutarVibracion();
                modoIconosActivo = false;
                actualizarVisibilidadPestanas();
            }
        });

        //Listeners seleccion de ICONOS
        btnIconoA.setOnClickListener(v -> seleccionarIcono("A"));
        btnIconoB.setOnClickListener(v -> seleccionarIcono("B"));
        btnIconoC.setOnClickListener(v -> seleccionarIcono("C"));
        btnIconoD.setOnClickListener(v -> seleccionarIcono("D"));
        btnIconoE.setOnClickListener(v -> seleccionarIcono("E"));
        btnIconoF.setOnClickListener(v -> seleccionarIcono("F"));
        btnIconoG.setOnClickListener(v -> seleccionarIcono("G"));

        //Listeners seleccion de TABLEROS
        btnTableroA.setOnClickListener(v -> seleccionarTablero("A"));
        btnTableroB.setOnClickListener(v -> seleccionarTablero("B"));
        btnTableroC.setOnClickListener(v -> seleccionarTablero("C"));
        btnTableroD.setOnClickListener(v -> seleccionarTablero("D"));
        btnTableroE.setOnClickListener(v -> seleccionarTablero("E"));
        btnTableroF.setOnClickListener(v -> seleccionarTablero("F"));
        btnTableroG.setOnClickListener(v -> seleccionarTablero("G"));

        //Boton Volver
        btnVolver.setOnClickListener(v -> {
            ejecutarVibracion();
            finish();
        });
    }

    //Alterna la interfaz entre la vista de Iconos y Tableros
    private void actualizarVisibilidadPestanas() {
        if (modoIconosActivo) {
            btnSeccionIconos.setImageResource(R.drawable.boton_seccion_iconos);
            btnSeccionTablero.setImageResource(R.drawable.boton_seccion_tablero_apagado);
            containerIconos.setVisibility(View.VISIBLE);
            containerTableros.setVisibility(View.GONE);
        } else {
            btnSeccionIconos.setImageResource(R.drawable.boton_seccion_iconos_apagado);
            btnSeccionTablero.setImageResource(R.drawable.boton_seccion_tablero);
            containerIconos.setVisibility(View.GONE);
            containerTableros.setVisibility(View.VISIBLE);
        }
    }

    //Logica de Seleccion para ICONOS
    private void seleccionarIcono(String nuevoIcono) {
        if (!iconoSeleccionado.equals(nuevoIcono)) {
            ejecutarVibracion();
            iconoSeleccionado = nuevoIcono;
            preferences.edit().putString("ICONO_SELECCIONADO", iconoSeleccionado).apply();
            actualizarSeleccionIconos();
        }
    }

    private void actualizarSeleccionIconos() {
        btnIconoA.setImageResource(R.drawable.iconos_opcion_a);
        btnIconoB.setImageResource(R.drawable.iconos_opcion_b);
        btnIconoC.setImageResource(R.drawable.iconos_opcion_c);
        btnIconoD.setImageResource(R.drawable.iconos_opcion_d);
        btnIconoE.setImageResource(R.drawable.iconos_opcion_e);
        btnIconoF.setImageResource(R.drawable.iconos_opcion_f);
        btnIconoG.setImageResource(R.drawable.iconos_opcion_g);

        switch (iconoSeleccionado) {
            case "A": btnIconoA.setImageResource(R.drawable.iconos_opcion_a_selecc); break;
            case "B": btnIconoB.setImageResource(R.drawable.iconos_opcion_b_selecc); break;
            case "C": btnIconoC.setImageResource(R.drawable.iconos_opcion_c_selecc); break;
            case "D": btnIconoD.setImageResource(R.drawable.iconos_opcion_d_selecc); break;
            case "E": btnIconoE.setImageResource(R.drawable.iconos_opcion_e_selecc); break;
            case "F": btnIconoF.setImageResource(R.drawable.iconos_opcion_f_selecc); break;
            case "G": btnIconoG.setImageResource(R.drawable.iconos_opcion_g_selecc); break;
        }
    }

    //Logica de Seleccion para TABLEROS
    private void seleccionarTablero(String nuevoTablero) {
        if (!tableroSeleccionado.equals(nuevoTablero)) {
            ejecutarVibracion();
            tableroSeleccionado = nuevoTablero;
            preferences.edit().putString("TABLERO_SELECCIONADO", tableroSeleccionado).apply();
            actualizarSeleccionTableros();
        }
    }

    private void actualizarSeleccionTableros() {
        btnTableroA.setImageResource(R.drawable.tablero_opcion_a);
        btnTableroB.setImageResource(R.drawable.tablero_opcion_b);
        btnTableroC.setImageResource(R.drawable.tablero_opcion_c);
        btnTableroD.setImageResource(R.drawable.tablero_opcion_d);
        btnTableroE.setImageResource(R.drawable.tablero_opcion_e);
        btnTableroF.setImageResource(R.drawable.tablero_opcion_f);
        btnTableroG.setImageResource(R.drawable.tablero_opcion_g);

        switch (tableroSeleccionado) {
            case "A": btnTableroA.setImageResource(R.drawable.tablero_opcion_a_selecc); break;
            case "B": btnTableroB.setImageResource(R.drawable.tablero_opcion_b_selecc); break;
            case "C": btnTableroC.setImageResource(R.drawable.tablero_opcion_c_selecc); break;
            case "D": btnTableroD.setImageResource(R.drawable.tablero_opcion_d_selecc); break;
            case "E": btnTableroE.setImageResource(R.drawable.tablero_opcion_e_selecc); break;
            case "F": btnTableroF.setImageResource(R.drawable.tablero_opcion_f_selecc); break;
            case "G": btnTableroG.setImageResource(R.drawable.tablero_opcion_g_selecc); break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        vibracionActivada = preferences.getBoolean("vibracion", true);

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