package com.example.tresenraya;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaEstadisticas extends AppCompatActivity {
    private TextView txtPartidasGanadas;
    private TextView txtPartidasJugadas;
    private TextView txtRachaVictorias;

    private SharedPreferences estadisticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_estadisticas);
        //Conecta los textos del XML
        txtPartidasGanadas = findViewById(R.id.txtParticulasGanadas);
        txtPartidasJugadas = findViewById(R.id.txtPartidasJugadas);
        txtRachaVictorias = findViewById(R.id.txtRachaVictorias);

        //Abre el archivo donde se guardaran las estadisticas
        estadisticas = getSharedPreferences("EstadisticasJugadorvsMaquina", MODE_PRIVATE);
        mostrarEstadisticas();

        //Boton para regresar a la pantalla anterior
        ImageButton btnVolver = findViewById(R.id.btnVolver);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); //Cierra esta pantalla y regresa a PantallaHome
                }
            });
        }
    }
        private void mostrarEstadisticas(){
            int partidasGanadas = estadisticas.getInt("partidas_ganadas", 0);
            int partidasJugadas = estadisticas.getInt("partidas_jugadas", 0);
            int rachaVictorias = estadisticas.getInt("racha_victorias", 0);

            txtPartidasGanadas.setText(String.valueOf(partidasGanadas));
            txtPartidasJugadas.setText(String.valueOf(partidasJugadas));
            txtRachaVictorias.setText(String.valueOf(rachaVictorias));

        }

        @Override
        protected void onResume(){
            super.onResume();
            if(estadisticas != null){
                mostrarEstadisticas();
            }
        }


    }
