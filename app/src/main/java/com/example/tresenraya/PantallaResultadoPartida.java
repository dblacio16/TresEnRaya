package com.example.tresenraya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaResultadoPartida extends AppCompatActivity {

    //Imagen que mostrara el resultado de la partida
    private ImageView imgFondoResultado;

    //Botones disponibles al terminar
    private ImageButton btnModos;
    private ImageButton btnReintentar;

    //Informacion de la partida terminada
    private String modoJuego;
    private String resultado;
    private char ganador;

    //Configuracion de Jugador vs Maquina
    private String nombreJugador;
    private char simboloHumano;
    private boolean humanoInicia;

    //Nombres de Jugador vs Jugador
    private String nombreJugador1;
    private String nombreJugador2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resultado_partida);


        //Conecta las vistas del XML
        imgFondoResultado =
                findViewById(R.id.imgFondoResultado);

        btnModos =
                findViewById(R.id.btnModos);

        btnReintentar =
                findViewById(R.id.btnReintentar);


        //Recupera la informacion enviada desde MainActivity
        modoJuego = getIntent().getStringExtra("modoJuego");
        resultado = getIntent().getStringExtra("resultado");
        ganador = getIntent().getCharExtra("ganador", '\0');


        //Recupera la configuracion del modo Jugador vs Maquina
        nombreJugador =
                getIntent().getStringExtra("NOMBRE_JUGADOR");

        simboloHumano =
                getIntent().getCharExtra(
                        "simboloHumano",
                        'X'
                );

        humanoInicia =
                getIntent().getBooleanExtra(
                        "humanoInicia",
                        true
                );


        //Recupera los nombres del modo Jugador vs Jugador
        nombreJugador1 =
                getIntent().getStringExtra("NOMBRE_JUGADOR1");

        nombreJugador2 =
                getIntent().getStringExtra("NOMBRE_JUGADOR2");


        //Valores seguros si no se recibio alguna informacion
        if (modoJuego == null) {
            modoJuego = "MAQUINA";
        }

        if (resultado == null) {
            resultado = "EMPATE";
        }


        //Selecciona el fondo correspondiente
        mostrarResultado();


        //Regresa a la pantalla de seleccion de modos
        btnModos.setOnClickListener(view -> {

            Intent intent = new Intent(
                    PantallaResultadoPartida.this,
                    PantallaModos.class
            );

            //Evita acumular varias pantallas de modos
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            );

            startActivity(intent);
            finish();
        });


        //Inicia una partida nueva con la misma configuración
        btnReintentar.setOnClickListener(view -> {

            Intent intent = new Intent(
                    PantallaResultadoPartida.this,
                    MainActivity.class
            );


            //Conserva el modo de juego
            intent.putExtra(
                    "modoJuego",
                    modoJuego
            );


            //Conserva la configuracion de Jugador vs Maquina
            intent.putExtra(
                    "NOMBRE_JUGADOR",
                    nombreJugador
            );

            intent.putExtra(
                    "simboloHumano",
                    simboloHumano
            );

            intent.putExtra(
                    "humanoInicia",
                    humanoInicia
            );


            //Conserva los nombres de Jugador vs Jugador
            intent.putExtra(
                    "NOMBRE_JUGADOR1",
                    nombreJugador1
            );

            intent.putExtra(
                    "NOMBRE_JUGADOR2",
                    nombreJugador2
            );


            startActivity(intent);
            finish();
        });
    }


    //Selecciona la imagen de resultado correspondiente
    //segun el modo y el resultado de la partida
    private void mostrarResultado() {
        //JUGADOR VS MAQUINA
        if (modoJuego.equals("MAQUINA")) {

            if (resultado.equals("EMPATE")) {

                imgFondoResultado.setImageResource(
                        R.drawable.fondo_jugmaq_empate
                );

            } else if (ganador == simboloHumano) {

                //Gano el jugador
                imgFondoResultado.setImageResource(
                        R.drawable.fondo_jugmaq_ganaste
                );

            } else {

                //Gano la máquina
                imgFondoResultado.setImageResource(
                        R.drawable.fondo_jugmaq_perdiste
                );
            }

            return;
        }

        //JUGADOR VS JUGADOR
        if (modoJuego.equals("DOS_JUGADORES")) {

            if (resultado.equals("EMPATE")) {

                imgFondoResultado.setImageResource(
                        R.drawable.fondo_dosjug_empate
                );

            } else if (ganador == 'X') {

                //Gano el jugador que utiliza X
                imgFondoResultado.setImageResource(
                        R.drawable.fondo_dosjug_gano_x
                );

            } else {

                //Gano el jugador que utiliza O
                imgFondoResultado.setImageResource(
                        R.drawable.fondo_dosjug_gano_o
                );
            }

            return;
        }

        //MAQUINA VS MAQUINA
        if (modoJuego.equals("MAQUINAS")) {

            if (resultado.equals("EMPATE")) {

                imgFondoResultado.setImageResource(
                        R.drawable.fondo_dosmaq_empate
                );

            } else if (ganador == 'X') {

                //Gano la primera maquina
                imgFondoResultado.setImageResource(
                        R.drawable.fondo_dosmaq_gano_a
                );

            } else {

                //Gano la segunda maquina
                imgFondoResultado.setImageResource(
                        R.drawable.fondo_dosmaq_gano_b
                );
            }
        }
    }
}