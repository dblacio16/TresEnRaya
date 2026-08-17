package com.example.tresenraya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PantallaDosJug extends AppCompatActivity {

    private ImageButton btnVolver;
    private ImageButton btnTemas;
    private ImageButton btnJugarPartida;

    private String jugador1;
    private String jugador2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_interdosjug);

        //Referencias a las vistas del layout
        btnVolver = findViewById(R.id.btnVolver);
        btnTemas = findViewById(R.id.btnTemas);
        btnJugarPartida = findViewById(R.id.btnJugarPartida);

        //Recuperar nombres desde el Intent o SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AjustesJuego", Context.MODE_PRIVATE);

        jugador1 = getIntent().getStringExtra("NOMBRE_JUGADOR1");
        jugador2 = getIntent().getStringExtra("NOMBRE_JUGADOR2");

        if (jugador1 == null || jugador1.trim().isEmpty()) {
            jugador1 = preferences.getString("NOMBRE_JUGADOR1", "Jugador 1");
        }
        if (jugador2 == null || jugador2.trim().isEmpty()) {
            jugador2 = preferences.getString("NOMBRE_JUGADOR2", "Jugador 2");
        }

        //Boton Volver que regresa a la pantalla anterior
        btnVolver.setOnClickListener(v -> finish());

        //Boton Jugar Partida que inicia la partida en el tablero (MainActivity)
        btnJugarPartida.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaDosJug.this, MainActivity.class);
            intent.putExtra("NOMBRE_JUGADOR1", jugador1);
            intent.putExtra("NOMBRE_JUGADOR2", jugador2);
            // Cambiamos a "modoJuego" para mantener el estándar de tu MainActivity
            intent.putExtra("modoJuego", "DOS_JUGADORES");
            startActivity(intent);
        });

        //Boton Temas que pasa los datos a la seleccion de temas
        btnTemas.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaDosJug.this, PantallaTemas.class);
            intent.putExtra("NOMBRE_JUGADOR1", jugador1);
            intent.putExtra("NOMBRE_JUGADOR2", jugador2);
            intent.putExtra("modoJuego", "DOS_JUGADORES");
            startActivity(intent);
        });

        //Manejo de margenes del sistema EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}