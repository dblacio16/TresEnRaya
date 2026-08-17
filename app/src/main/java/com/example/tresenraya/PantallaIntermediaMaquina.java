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

public class PantallaIntermediaMaquina extends AppCompatActivity {

    private ImageButton btnVolver;
    private ImageButton btnTemas;
    private ImageButton btnJugarPartida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_intermaq);

        //Referencias de la UI
        btnVolver = findViewById(R.id.btnVolver);
        btnTemas = findViewById(R.id.btnTemas);
        btnJugarPartida = findViewById(R.id.btnJugarPartida);

        //Boton Volver que regresa a la pantalla anterior
        btnVolver.setOnClickListener(v -> finish());

        //Boton Jugar Partida que navega directamente a MainActivity (el tablero)
        btnJugarPartida.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaIntermediaMaquina.this, MainActivity.class);
            //Mandamos el valor "MAQUINAS" que el MainActivity ya reconoce
            intent.putExtra("modoJuego", "MAQUINAS");
            startActivity(intent);
        });

        //Boton Temas que navega a la pantalla de seleccion de temas
        btnTemas.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaIntermediaMaquina.this, PantallaTemas.class);
            intent.putExtra("modoJuego", "MAQUINAS");
            startActivity(intent);
        });

        //Ajuste con Insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}