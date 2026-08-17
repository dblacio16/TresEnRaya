package com.example.tresenraya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaIngresarNombreJugador extends AppCompatActivity {

    private EditText etNombreJugador;
    private ImageButton btnVolver;
    private ImageButton btnJugarPartida;
    private ImageButton btnTemas;

    //Botones de seleccion de Ficha y Turno
    private ImageButton btnOpcionX;
    private ImageButton btnOpcionO;
    private ImageButton btnIniciaJug;
    private ImageButton btnIniciaMaq;

    //Variables de estado
    private char simboloHumano = 'X';
    private boolean humanoInicia = true;

    private Vibrator vibrator;
    private SharedPreferences preferences;
    private boolean vibracionActivada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_ingresar_nom_jug);

        //Inicializar servicios
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        preferences = getSharedPreferences("AjustesJuego", Context.MODE_PRIVATE);

        //Inicializar vistas principales
        etNombreJugador = findViewById(R.id.etNombreJugador);
        btnVolver = findViewById(R.id.btnVolver);
        btnJugarPartida = findViewById(R.id.btnJugarPartida);
        btnTemas = findViewById(R.id.btnTemas);

        //Inicializar vistas de las opciones
        btnOpcionX = findViewById(R.id.btnOpcionX);
        btnOpcionO = findViewById(R.id.btnOpcionO);
        btnIniciaJug = findViewById(R.id.btnIniciaJug);
        btnIniciaMaq = findViewById(R.id.btnIniciaMaq);

        //Estado visual inicial por defecto
        actualizarOpcionesSimbolo();
        actualizarOpcionesInicio();

        //Selecciona directamente la ficha X
        btnOpcionX.setOnClickListener(v -> {

            //Solo cambia si X todavía no esta seleccionada
            if (simboloHumano != 'X') {

                ejecutarVibracion();
                simboloHumano = 'X';
                actualizarOpcionesSimbolo();
            }
        });


        //Selecciona directamente la ficha O
        btnOpcionO.setOnClickListener(v -> {

            //Solo cambia si O todavia no esta seleccionada
            if (simboloHumano != 'O') {

                ejecutarVibracion();
                simboloHumano = 'O';
                actualizarOpcionesSimbolo();
            }
        });


        //Indica que comenzara el jugador
        btnIniciaJug.setOnClickListener(v -> {

            //Solo cambia si actualmente comenzaba la maquina
            if (!humanoInicia) {

                ejecutarVibracion();
                humanoInicia = true;
                actualizarOpcionesInicio();
            }
        });


        //Indica que comenzara la maquina
        btnIniciaMaq.setOnClickListener(v -> {

            //Solo cambia si actualmente comenzaba el jugador
            if (humanoInicia) {

                ejecutarVibracion();
                humanoInicia = false;
                actualizarOpcionesInicio();
            }
        });

        //Acción del boton Volver
        btnVolver.setOnClickListener(v -> {
            ejecutarVibracion();
            finish();
        });

        //Accion del boton Jugar Partida
        btnJugarPartida.setOnClickListener(v -> {
            ejecutarVibracion();
            String nombreIngresado = etNombreJugador.getText().toString().trim();

            if (nombreIngresado.isEmpty()) {
                nombreIngresado = "Jugador 1";
            }

            //Guardar el nombre en SharedPreferences para persistencia
            preferences.edit().putString("NOMBRE_JUGADOR", nombreIngresado).apply();

            Intent intent = new Intent(PantallaIngresarNombreJugador.this, MainActivity.class);
            intent.putExtra("NOMBRE_JUGADOR", nombreIngresado);
            intent.putExtra("simboloHumano", simboloHumano);
            intent.putExtra("humanoInicia", humanoInicia);
            startActivity(intent);
        });

        //Accion del boton Temas
        if (btnTemas != null) {
            btnTemas.setOnClickListener(v -> {
                ejecutarVibracion();

                //Guardar el nombre tambien al ir a Temas si el usuario ingreso algo
                String nombreIngresado = etNombreJugador.getText().toString().trim();
                if (!nombreIngresado.isEmpty()) {
                    preferences.edit().putString("NOMBRE_JUGADOR", nombreIngresado).apply();
                }

                Intent intent = new Intent(PantallaIngresarNombreJugador.this, PantallaTemas.class);
                startActivity(intent);
            });
        }
    }

    private void actualizarOpcionesSimbolo() {
        if (simboloHumano == 'X') {
            btnOpcionX.setImageResource(R.drawable.opcion_x_selecc);
            btnOpcionO.setImageResource(R.drawable.opcion_o);
        } else {
            btnOpcionX.setImageResource(R.drawable.opcion_x);
            btnOpcionO.setImageResource(R.drawable.opcion_o_selecc);
        }
    }

    private void actualizarOpcionesInicio() {
        if (humanoInicia) {
            btnIniciaJug.setImageResource(R.drawable.opcion_jug_selecc);
            btnIniciaMaq.setImageResource(R.drawable.opcion_rob);
        } else {
            btnIniciaJug.setImageResource(R.drawable.opcion_jug);
            btnIniciaMaq.setImageResource(R.drawable.opcion_rob_selecc);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        vibracionActivada = preferences.getBoolean("vibracion", true);

        //Cargar el nombre previamente guardado si existe
        String nombreGuardado = preferences.getString("NOMBRE_JUGADOR", "");
        if (!nombreGuardado.isEmpty()) {
            etNombreJugador.setText(nombreGuardado);
        }

        //Asegurar que el servicio de musica continue sonando
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
