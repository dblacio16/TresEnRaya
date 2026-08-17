package com.example.tresenraya;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JuegoTresEnRaya {
    private boolean turnoX = true;
    //Smbolos de cada jugador
    private char simboloHumano = 'X';
    private char simboloMaquina = 'O';
    //Generador para movimientos aleatorios
    private final Random random = new Random();
    //Indica quien comienza la partida
    private boolean humanoInicia = true;
    private boolean juegoTerminado = false;
    private int jugadas = 0;
    private char[] tablero = new char[9];
    private int[] lineaGanadora;
    private int[][] combinacionesGanadoras ={
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };

    public boolean jugar(int posicion){
        //Comprueba si la jugada es valida
        if(juegoTerminado || tablero[posicion] != '\0'){
            return false;
        }
        //Coloca el simbolo del jugador actual
        if(turnoX){
            tablero[posicion] = 'X';
        } else{
            tablero[posicion] = 'O';
        }
        //Aumenta el nmero de jugadas realizadas
        jugadas++;
        if(comprobarGanador()){
            juegoTerminado = true;
            return true;
        }
        if(jugadas==9){
            juegoTerminado = true;
            return true;
        }
        turnoX = !turnoX;
        return true;
    }
    public char getCasilla(int posicion){
        return tablero[posicion];
    }

    private boolean comprobarGanador(){
        for(int[] combinacion : combinacionesGanadoras){
            int a = combinacion[0];
            int b = combinacion[1];
            int c = combinacion[2];

            if (tablero[a] != '\0'
                    && tablero[a] == tablero[b]
                    && tablero[a] == tablero[c]) {
                lineaGanadora = combinacion;
                return true;
            }

        }
        return false;
    }

    public boolean hayGanador(){
        return lineaGanadora != null;
    }

    public boolean esEmpate(){
        return juegoTerminado && lineaGanadora == null && jugadas  == 9;
    }

    public int[] getLineaGanadora(){
        return lineaGanadora;
    }

    public char getGanador(){
        return turnoX ? 'X' : 'O';
    }

    public char[] getTableroCopia() {

        //Devuelve una copia para no modificar el tablero real
        return tablero.clone();
    }

    public char getSimboloTurno() {

        //Devuelve el simbolo que debe jugar actualmente
        return turnoX ? 'X' : 'O';
    }

    public boolean esTurnoHumano() {

        //Obtiene el simbolo correspondiente al turno actual
        char simboloTurno = turnoX ? 'X' : 'O';

        //Comprueba si ese simbolo pertenece al humano
        return simboloTurno == simboloHumano;
    }

    public int obtenerMejorMovimientoMaquina() {

        //Busca el mejor movimiento para la maquina
        return obtenerMejorMovimiento(
                simboloMaquina,
                simboloHumano
        );
    }

    public int jugarMaquina() {

        //No juega si la partida termin o es turno del humano
        if (juegoTerminado || esTurnoHumano()) {
            return -1;
        }

        //Obtiene la mejor posicion usando el rbol
        int posicion = obtenerMejorMovimientoMaquina();

        //No encontro una jugada vlida
        if (posicion == -1) {
            return -1;
        }

        //Realiza la jugada y devuelve la posicin utilizada
        if (jugar(posicion)) {
            return posicion;
        }

        return -1;
    }

    public void reiniciar() {
        //Vacia todas las posiciones del tablero
        tablero = new char[9];
        //X vuelve a comenzar
        //Determina si el primer turno corresponde a X o a O
        turnoX = humanoInicia
                ? simboloHumano == 'X'
                : simboloMaquina == 'X';
        //Reinicia el contador de jugadas
        jugadas = 0;
        //Indica que la nueva partida esta activa
        juegoTerminado = false;
        //Borra la combinacin ganadora anterior
        lineaGanadora = null;
    }

    public void configurarPartida(char simboloHumano, boolean humanoInicia) {

        //Guarda el smbolo elegido por el humano
        this.simboloHumano = simboloHumano;

        //La mquina recibe automticamente el otro simbolo
        this.simboloMaquina = (simboloHumano == 'X') ? 'O' : 'X';

        //Guarda quiin debe comenzar
        this.humanoInicia = humanoInicia;

        //Prepara una partida nueva con esta configuracion
        reiniciar();
    }

    public int obtenerMejorMovimiento(char simboloJugador, char simboloOponente) {

        //Crea la raz con el tablero actual
        NodoEstado raiz = new NodoEstado(tablero);

        //Genera las jugadas del jugador y las respuestas del oponente
        raiz.generarDosNiveles(
                simboloJugador,
                simboloOponente
        );

        //Busca el mejor movimiento mediante MIN y MAX
        NodoEstado mejorHijo =
                raiz.obtenerMejorHijo(
                        simboloJugador,
                        simboloOponente
                );

        //Si no hay movimientos posibles
        if (mejorHijo == null) {
            return -1;
        }

        //Devuelve la posición elegida por la IA
        return mejorHijo.getMovimiento();
    }

    public int jugarAutomatico() {

        //No realiza movimientos si la partida termino
        if (juegoTerminado) {
            return -1;
        }

        //Obtiene el simbolo que debe jugar
        char simboloActual = getSimboloTurno();

        //El oponente siempre usa el simbolo contrario
        char simboloOponente =
                (simboloActual == 'X') ? 'O' : 'X';

        //La IA calcula la mejor posicion para el simbolo actual
        int posicion = obtenerMejorMovimiento(
                simboloActual,
                simboloOponente
        );

        //Si no encontro una jugada valida
        if (posicion == -1) {
            return -1;
        }

        //Realiza la jugada
        if (jugar(posicion)) {
            return posicion;
        }

        return -1;
    }

    //Metodo excepcion para el modo maquina vs maquina(no minimax)
    public int jugarAleatorio() {

        // Guarda las casillas que todavía están libres
        List<Integer> posicionesLibres = new ArrayList<>();

        for (int i = 0; i < tablero.length; i++) {

            if (tablero[i] == '\0') {
                posicionesLibres.add(i);
            }
        }

        //Si no quedan casillas disponibles
        if (posicionesLibres.isEmpty()) {
            return -1;
        }

        //Escoge una posicion libre al azar
        int posicion = posicionesLibres.get(
                random.nextInt(posicionesLibres.size())
        );

        //Realiza la jugada
        if (jugar(posicion)) {
            return posicion;
        }

        return -1;
    }


    //Fuerza el estado interno del tablero y de los turnos basado en una partida guardada
    public void restaurarTablero(String tableroGuardado, boolean turnoHumanoGuardado) {
        //Reiniciamos contadores y estados antes de cargar
        this.jugadas = 0;
        this.juegoTerminado = false;
        this.lineaGanadora = null;

        //Recorremos el texto guardado para rellenar tu arreglo "tablero"
        for (int i = 0; i < 9; i++) {
            char ficha = tableroGuardado.charAt(i);

            if (ficha == 'X' || ficha == 'O') {
                tablero[i] = ficha;
                this.jugadas++; //Aumentamos el contador de jugadas
            } else {
                tablero[i] = '\0'; //El cdigo usa '\0' para casillas vacias porsi
            }
        }

        //Restablecemos de quin es el turno usando tu variable turnoX
        if (turnoHumanoGuardado) {
            //Si le toca al humano, turnoX es true solo si el humano usa 'X'
            this.turnoX = (this.simboloHumano == 'X');
        } else {
            //Si le toca a la mquina, turnoX es true solo si la maquina usa 'X'
            this.turnoX = (this.simboloMaquina == 'X');
        }

        //Finalmente, verificamos si la partida guardada ya habia terminado
        if (comprobarGanador() || jugadas == 9) {
            this.juegoTerminado = true;
        }
    }

}

