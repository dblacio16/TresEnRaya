package com.example.tresenraya;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {

    private JuegoTresEnRaya juego = new JuegoTresEnRaya();


    //Guarda el modo de juego seleccionado
    private String modoJuego = "MAQUINA";
    //Indica quien comenza la partida contra la maquina
    private boolean humanoInicia;

    //Nombres utilizados en Jugador vs Jugador
    private String nombreJugador1;
    private String nombreJugador2;

    //Evita abrir dos veces la pantalla de resultado
    private boolean resultadoMostrado = false;
    private ImageButton btnVolver;
    private ImageButton btnPista;
    private TrazaView trazaView;
    //Permite hacer una pequeña pausa entre jugadas automaticas
    private Handler handler = new Handler(Looper.getMainLooper());
    //Casillas visuales del tablero
    private ImageButton[] casillas = new ImageButton[9];
    //Aleatoriedad utilizada en Bot vs Bot
    private final Random random = new Random();

    //Indica si todavia no se realizo la primera jugada automatica
    private boolean primeraJugadaAutomatica = true;
    //Fondo del tablero
    private ImageView imgFondoTablero;

    //Imagenes de X y O segun el tema seleccionado
    private int resImagenX;
    private int resImagenO;

    private ImageView imgFichaX;
    private ImageView imgFichaO;

    //Nombre del jugador
    private String nombreJugador;

    //Simbolo seleccionado por el jugador
    private char simboloHumano;

    //Indicadores visuales del turno
    private TextView tvNombreJugador;
    private TextView tvNombreJugadorEsquina;
    private ImageView imgFichaTurno;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        //setContentView(R.layout.activity_main);

        //Recibe el modo seleccionado ANTES de inflar el layout
        String modoRecibido = getIntent().getStringExtra("modoJuego");

        if (modoRecibido != null) {
            modoJuego = modoRecibido;
        }

        //Decide que Layout o XML mostrar basado en el modo
        if (modoJuego.equals("DOS_JUGADORES")) {
            setContentView(R.layout.activity_juego_jugvsjug);
        } else if (modoJuego.equals("MAQUINAS")) {
            setContentView(R.layout.activity_juego_maqvsmaq);
        } else {
            // Por defecto seria MAQUINA (Jugador vs Maquina)
            setContentView(R.layout.activity_main);
        }

        btnVolver = findViewById(R.id.btnVolver);
        btnPista = findViewById(R.id.btnPista);

        //Conecta las vistas del nombre y turno
        tvNombreJugador = findViewById(R.id.tvNombreJugador);
        tvNombreJugadorEsquina = findViewById(R.id.tvNombreJugadorEsquina);
        imgFichaTurno = findViewById(R.id.imgFichaTurno);

        //Configura el boton Volver solamente si existe
        //en el XML correspondiente al modo actual
        if (btnVolver != null) {

            btnVolver.setOnClickListener(view -> finish());
        }


        //Primero intenta obtener el nombre enviado mediante Intent
        nombreJugador = getIntent().getStringExtra("NOMBRE_JUGADOR");

        //Si no llego mediante Intent, lo busca en SharedPreferences
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {

            SharedPreferences preferences =
                    getSharedPreferences(
                            "AjustesJuego",
                            Context.MODE_PRIVATE
                    );

            nombreJugador =
                    preferences.getString(
                            "NOMBRE_JUGADOR",
                            "Jugador 1"
                    );
        }


        //Mostrar el nombre en la esquina si existe esa vista
        if (tvNombreJugadorEsquina != null) {
            tvNombreJugadorEsquina.setText(nombreJugador);
        }



        //Recibe la configuración enviada desde la pantalla anterior
        //Si todavía no se envian datos:
        //Humano = X y comienza el humano
        //char simboloHumano = getIntent().getCharExtra("simboloHumano", 'X');
        simboloHumano = getIntent().getCharExtra("simboloHumano", 'X');

        humanoInicia = getIntent().getBooleanExtra("humanoInicia", true);
        //Recupera los nombres del modo Jugador vs Jugador
        nombreJugador1 =
                getIntent().getStringExtra("NOMBRE_JUGADOR1");

        nombreJugador2 =
                getIntent().getStringExtra("NOMBRE_JUGADOR2");

        //En Jugador vs Maquina usa la configuracion elegida
        if (modoJuego.equals("MAQUINA")) {

            juego.configurarPartida(
                    simboloHumano,
                    humanoInicia
            );

        } else {

            //En Jugador vs Jugador comienza X
            juego.configurarPartida(
                    'X',
                    true
            );
        }


        //Conecta la vista de la traza
        trazaView = findViewById(R.id.trazaView);

        //Conecta el fondo del tablero
        imgFondoTablero = findViewById(R.id.imgFondoTablero);


        //Carga las imgenes del tema seleccionado
        cargarTemaSeleccionado();

        //Enlazar las vistas del layout
        imgFichaX = findViewById(R.id.imgFichaX);
        imgFichaO = findViewById(R.id.imgFichaO);

        //Validar que existan en el layout actual y ponerles la imagen elegida
        if (imgFichaX != null) {
            imgFichaX.setImageResource(resImagenX);
        }

        if (imgFichaO != null) {
            imgFichaO.setImageResource(resImagenO);
        }



        //Conecta las 9 casillas del XML
        casillas[0] = findViewById(R.id.casilla0);
        casillas[1] = findViewById(R.id.casilla1);
        casillas[2] = findViewById(R.id.casilla2);
        casillas[3] = findViewById(R.id.casilla3);
        casillas[4] = findViewById(R.id.casilla4);
        casillas[5] = findViewById(R.id.casilla5);
        casillas[6] = findViewById(R.id.casilla6);
        casillas[7] = findViewById(R.id.casilla7);
        casillas[8] = findViewById(R.id.casilla8);

        //Accion del boton de pistas
        if (btnPista != null) {
            btnPista.setOnClickListener(view -> {
                if (!juego.esTurnoHumano()) {
                    Toast.makeText(this, "Es turno de Rob-Bot", Toast.LENGTH_SHORT).show();
                    return;
                }
                int mejorMovimiento = juego.obtenerMejorMovimientoMaquina();
                if (mejorMovimiento != -1) {
                    Toast.makeText(this, "Sugerencia: Casilla " + (mejorMovimiento + 1), Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Muestra inicialmente de quion es el turno
        actualizarIndicadorTurno();


        //Asigna el mismo funcionamiento a las 9 casillas
        for (int i = 0; i < casillas.length; i++) {

            int posicion = i;

            casillas[i].setOnClickListener(view -> {
                //En Maquina vs Maquina el usuario no puede colocar fichas
                if (modoJuego.equals("MAQUINAS")) {
                    return;
                }
                //Esta restriccion solo se aplica contra la maquina
                if (modoJuego.equals("MAQUINA")
                        && !juego.esTurnoHumano()) {

                    return;
                }


                //Intenta realizar la jugada del humano
                if (juego.jugar(posicion)) {

                    //Obtiene el simbolo colocado
                    char ficha = juego.getCasilla(posicion);
                    //Muestra la imagen correspondiente
                    casillas[posicion].setImageResource(ficha == 'X' ? resImagenX : resImagenO);


                    //Comprueba si gano el humano
                    if (juego.hayGanador()) {
                        Toast.makeText(this, "Ganó " + juego.getGanador(), Toast.LENGTH_SHORT).show();
                        trazaView.setLineaGanadora(juego.getLineaGanadora());
                        mostrarPantallaResultado("GANADOR");
                        return;
                    }

                    //Comprueba si hubo empate
                    if (juego.esEmpate()) {
                        Toast.makeText(this, "Empate", Toast.LENGTH_SHORT).show();
                        mostrarPantallaResultado("EMPATE");
                        return;
                    }

                    //Actualiza el indicador para mostrar que es turno de la maquina
                    actualizarIndicadorTurno();

                    if (modoJuego.equals("MAQUINA")) {

                        //Espera antes de realizar el turno de la mquina
                        programarTurnoMaquina();
                    }
                }
            });
        }

        //Recupera el tablero antes de iniciar cualquier turno automtico
        recuperarEstadoPartida();
        //Inicia automaticamente segn el modo seleccionado
        if (modoJuego.equals("MAQUINAS")) {

            //Decide aleatoriamente si comienza X u O
            boolean empiezaX = random.nextBoolean();

            if (empiezaX) {

                //Comienza X
                juego.configurarPartida('X', true);

            } else {

                //Comienza O
                juego.configurarPartida('O', true);
            }

            //Comienza la partida automatica
            realizarTurnoAutomatico();

        } else if (modoJuego.equals("MAQUINA")
                && !juego.esTurnoHumano()) {

            //La mquina espera antes de realizar su primera jugada
            programarTurnoMaquina();
        }

        actualizarIndicadorTurno();

        //Ajuste automtico con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                }
        );



    }



    //Actualiza el nombre mostrado y la imagen del icono X u O
    //segun a quién le corresponda el turno actual

    private void actualizarIndicadorTurno() {
        //Verificamos si tvNombreJugador existe en el XML actual antes de usarlo
        if (tvNombreJugador != null) {
            if (juego.esTurnoHumano()) {
                tvNombreJugador.setText(nombreJugador);
            } else {
                tvNombreJugador.setText("Rob-Bot");
            }
        }

        //Verificamos si imgFichaTurno existe en el XML actual antes de usarlo
        if (imgFichaTurno != null) {
            if (juego.esTurnoHumano()) {
                imgFichaTurno.setImageResource(simboloHumano == 'X' ? resImagenX : resImagenO);
            } else {
                char simboloMaquina = (simboloHumano == 'X') ? 'O' : 'X';
                imgFichaTurno.setImageResource(simboloMaquina == 'X' ? resImagenX : resImagenO);
            }
        }
    }



    //Turno automatico bot vs bot
    private void realizarTurnoAutomatico() {

        //No continua si la partida ya termino
        if (juego.hayGanador() || juego.esEmpate()) {
            return;
        }

        //La IA del turno actual realiza su mejor movimiento
        int posicion;

        //La primera jugada de Bot vs Bot es completamente aleatoria
        if (primeraJugadaAutomatica) {

            posicion = juego.jugarAleatorio();

            if (posicion != -1) {
                primeraJugadaAutomatica = false;
            }

        } else {

            //Desde la segunda jugada se utiliza minimax normalmente
            posicion = juego.jugarAutomatico();
        }

        //Si no pudo jugar, termina
        if (posicion == -1) {
            return;
        }

        //Obtiene qué simbolo coloco
        char ficha = juego.getCasilla(posicion);

        //Muestra visualmente la ficha
        if (ficha == 'X') {
            casillas[posicion].setImageResource(resImagenX);
        } else if (ficha == 'O') {
            casillas[posicion].setImageResource(resImagenO);
        }

        //Comprueba si alguna mquina gano
        if (juego.hayGanador()) {

            Toast.makeText(
                    this,
                    "Ganó " + juego.getGanador(),
                    Toast.LENGTH_SHORT
            ).show();

            trazaView.setLineaGanadora(
                    juego.getLineaGanadora()
            );
            mostrarPantallaResultado("GANADOR");

            return;
        }

        //Comprueba empate
        if (juego.esEmpate()) {

            Toast.makeText(
                    this,
                    "Empate",
                    Toast.LENGTH_SHORT
            ).show();
            mostrarPantallaResultado("EMPATE");
            return;
        }

        //Espera un poco antes de hacer la siguiente jugada
        handler.postDelayed(
                this::realizarTurnoAutomatico,
                700
        );
    }


    private void realizarTurnoMaquina() {

        //La IA calcula y realiza su mejor movimiento
        int posicionMaquina = juego.jugarMaquina();


        //Si no encontro una jugada valida, termina
        if (posicionMaquina == -1) {
            return;
        }


        //Obtiene el simbolo que coloco la maquina
        char ficha = juego.getCasilla(posicionMaquina);


        //Muestra visualmente la jugada de la mquina
        if (ficha == 'X') {
            casillas[posicionMaquina].setImageResource(resImagenX);
        } else if (ficha == 'O') {
            casillas[posicionMaquina].setImageResource(resImagenO);
        }


        //Comprueba si gan la máquina
        if (juego.hayGanador()) {
            Toast.makeText(this, "Ganó " + juego.getGanador(), Toast.LENGTH_SHORT).show();
            trazaView.setLineaGanadora(juego.getLineaGanadora());
            mostrarPantallaResultado("GANADOR");
            return;
        }

        if (juego.esEmpate()) {
            Toast.makeText(this, "Empate", Toast.LENGTH_SHORT).show();
            mostrarPantallaResultado("EMPATE");
            return;
        }

        actualizarIndicadorTurno();
    }

    //Para la pausa de la maquina
    private void programarTurnoMaquina() {

        //Espera un poco antes de que la mquina juegue
        handler.postDelayed(() -> {

            //Solo contina en el modo Jugador vs Maquina
            if (!modoJuego.equals("MAQUINA")) {
                return;
            }

            //No juega si la partida ya termino
            if (juego.hayGanador() || juego.esEmpate()) {
                return;
            }

            //Comprueba que realmente sea turno de la mquina
            if (juego.esTurnoHumano()) {
                return;
            }

            //La maquina realiza su movimiento
            realizarTurnoMaquina();

        }, 700);
    }



    //Carga el tema guardado y selecciona las imágenes
    //correspondientes para el tablero, X y O

    private void cargarTemaSeleccionado() {
        SharedPreferences preferences = getSharedPreferences("AjustesJuego", Context.MODE_PRIVATE);

        String tableroSeleccionado = preferences.getString("TABLERO_SELECCIONADO", "A");
        String iconoSeleccionado = preferences.getString("ICONO_SELECCIONADO", "A");

        //Configura el fondo del tablero
        switch (tableroSeleccionado) {
            case "B":
                imgFondoTablero.setImageResource(R.drawable.marco_solar_orange);
                break;
            case "C":
                imgFondoTablero.setImageResource(R.drawable.marco_hielo_artico);
                break;
            case "D":
                imgFondoTablero.setImageResource(R.drawable.marco_verde_neon);
                break;
            case "E":
                imgFondoTablero.setImageResource(R.drawable.marco_golden);
                break;
            case "F":
                imgFondoTablero.setImageResource(R.drawable.marco_pinky);
                break;
            case "G":
                imgFondoTablero.setImageResource(R.drawable.marco_turquesa);
                break;
            case "A":
            default:
                imgFondoTablero.setImageResource(R.drawable.marco_noche_naval);
                break;
        }

        //Configura las imágenes de X y O segun el tema seleccionado
        switch (iconoSeleccionado) {
            case "B":
                resImagenX = R.drawable.x_robot;
                resImagenO = R.drawable.o_robot;
                break;
            case "C":
                resImagenX = R.drawable.x_cosmic;
                resImagenO = R.drawable.o_cosmic;
                break;
            case "D":
                resImagenX = R.drawable.x_hashtag;
                resImagenO = R.drawable.o_hashtag;
                break;
            case "E":
                resImagenX = R.drawable.x_amor;
                resImagenO = R.drawable.o_amor;
                break;
            case "F":
                resImagenX = R.drawable.x_pirata;
                resImagenO = R.drawable.o_pirata;
                break;
            case "G":
                resImagenX = R.drawable.x_clima;
                resImagenO = R.drawable.o_clima;
                break;
            case "A":
            default:
                resImagenX = R.drawable.x_neon;
                resImagenO = R.drawable.o_neon;
                break;
        }

    }


     //Abre la pantalla correspondiente al resultado de la partida
     //el resultado Debe ser "GANADOR" o "EMPATE"

    private void mostrarPantallaResultado(String resultado) {

        //Evita que la pantalla se abra más de una vez
        if (resultadoMostrado) {
            return;
        }

        resultadoMostrado = true;


        Intent intent = new Intent(
                MainActivity.this,
                PantallaResultadoPartida.class
        );


        //Envia el modo y el tipo de resultado
        intent.putExtra(
                "modoJuego",
                modoJuego
        );

        intent.putExtra(
                "resultado",
                resultado
        );


        //Si hubo victoria, envia el simbolo ganador
        if (resultado.equals("GANADOR")) {

            intent.putExtra(
                    "ganador",
                    juego.getGanador()
            );
        }


        //Conserva los datos de Jugador vs Maquina
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


        //Conserva los datos de Jugador vs Jugador
        intent.putExtra(
                "NOMBRE_JUGADOR1",
                nombreJugador1
        );

        intent.putExtra(
                "NOMBRE_JUGADOR2",
                nombreJugador2
        );


        //Espera para que se pueda observar la ultima ficha
        //y la traza de la combinacion ganadora
        handler.postDelayed(() -> {

            startActivity(intent);

            //Cierra el tablero terminado para evitar regresar a el
            finish();

        }, 900);
    }

    private void nuevaPartida() {

        //Reinicia la logica de la partida
        juego.reiniciar();


        //Limpia visualmente las 9 casillas
        for (ImageButton casilla : casillas) {
            casilla.setImageDrawable(null);
        }


        //Elimina la traza de la partida anterior
        trazaView.limpiarTraza();


        //Si estamos contra la mquina y le toca iniciar,
        //realiza su primer movimiento
        if (modoJuego.equals("MAQUINA")
                && !juego.esTurnoHumano()) {

            realizarTurnoMaquina();
        }
    }

    //Se ejecuta automticamente cuando minimizas o sales de la app
    @Override
    protected void onPause() {
        super.onPause();
        guardarEstadoPartida();
    }

    private void guardarEstadoPartida() {
        SharedPreferences prefs = getSharedPreferences("PartidaGuardada", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //Bot vs Bot termina automticamente en pocos segundos,
        //por lo que no necesita recuperar partidas incompletas
        if (modoJuego.equals("MAQUINAS")) {

            editor.remove("MAQUINAS_TABLERO_ACTUAL");
            editor.remove("MAQUINAS_ES_TURNO_HUMANO");
            editor.apply();

            return;
        }

        //Creamos un prefijo unico basado en el modo actual como "MAQUINA" o "DOS_JUGADORES"
        String prefijoModo = modoJuego + "_";

        //Si la partida ya termino, borramos SOLO la partida de este modo
        if (juego.hayGanador() || juego.esEmpate()) {
            editor.remove(prefijoModo + "TABLERO_ACTUAL");
            editor.remove(prefijoModo + "ES_TURNO_HUMANO");
            editor.apply();
            return;
        }

        //Leemos las 9 casillas y las convertimos en un texto como X-O--X---
        StringBuilder tableroStr = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            char ficha = juego.getCasilla(i);
            //Usamos '-' para representar casillas vacias
            if (ficha == 'X' || ficha == 'O') {
                tableroStr.append(ficha);
            } else {
                tableroStr.append('-');
            }
        }

        //Guardamos los datos añadiendo el prefijo unico de este modo
        editor.putString(prefijoModo + "TABLERO_ACTUAL", tableroStr.toString());
        editor.putBoolean(prefijoModo + "ES_TURNO_HUMANO", juego.esTurnoHumano());
        editor.apply();
    }

    private void recuperarEstadoPartida() {
        SharedPreferences prefs = getSharedPreferences("PartidaGuardada", Context.MODE_PRIVATE);

        //Buscamos especficamente el guardado del modo en el que estamos entrando
        String prefijoModo = modoJuego + "_";
        String tableroGuardado = prefs.getString(prefijoModo + "TABLERO_ACTUAL", null);

        //Si hay una partida guardada para ESTE modo
        if (tableroGuardado != null) {

            boolean turnoHumanoGuardado = prefs.getBoolean(prefijoModo + "ES_TURNO_HUMANO", true);

            //Restaurar la lgica interna del juego
            juego.restaurarTablero(tableroGuardado, turnoHumanoGuardado);

            //Restaurar la interfaz grfica (poner las imagenes)
            for (int i = 0; i < 9; i++) {
                char ficha = tableroGuardado.charAt(i);
                if (ficha == 'X') {
                    casillas[i].setImageResource(resImagenX);
                } else if (ficha == 'O') {
                    casillas[i].setImageResource(resImagenO);
                } else {
                    casillas[i].setImageDrawable(null);
                }
            }

            //Actualizar quien juega ahora
            actualizarIndicadorTurno();


        }
    }

}