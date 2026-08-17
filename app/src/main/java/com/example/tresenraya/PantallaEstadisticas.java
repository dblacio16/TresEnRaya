package com.example.tresenraya;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaEstadisticas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_estadisticas);

        //Si agregas un boton para regresar en activity_pantalla_ajustes.xml
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
}