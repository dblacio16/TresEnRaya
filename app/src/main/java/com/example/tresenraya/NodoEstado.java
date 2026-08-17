package com.example.tresenraya;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class NodoEstado {
    //Posicin que se jugo para generar este nodo
    private int movimiento;
    //Utilidad calculada para este estado
    private int utilidad;
    //Permite escoger aleatoriamente entre movimientos
    //que tengan exactamente la misma utilidad
    private static final Random RANDOM = new Random();
    private static final int[][] LINEAS = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };
    //Estado del tablero que representa este nodo
    private char[] tablero;
    //Posibles estados que se pueden generar desde este tablero
    private List<NodoEstado> hijos;

    public NodoEstado(char[] tablero) {

        //-1 porque la raiz no fue generada por ninguna jugada
        this(tablero, -1);
    }

    public NodoEstado(char[] tablero, int movimiento) {

        //Guarda una copia del tablero
        this.tablero = tablero.clone();

        //Guarda la posicion que genero este estado
        this.movimiento = movimiento;

        //Inicializa los hijos del nodo
        this.hijos = new ArrayList<>();
    }

    public char[] getTablero(){
        return tablero;
    }

    public int getUtilidad() {

        //Devuelve la utilidad guardada del nodo
        return utilidad;
    }

    public int getMovimiento() {

        //Devuelve la posicion que genero este nodo
        return movimiento;
    }

    public List<NodoEstado> getHijos() {
        return hijos;
    }

    public void generarHijos(char simbolo) {
        //No genera mas estados si la partida ya termino
        if (haGanado('X') || haGanado('O')) {
            return;
        }
        //Recorre todas las posiciones del tablero
        for (int i = 0; i < tablero.length; i++) {

            //Solo se puede jugar en una casilla vacia
            if (tablero[i] == '\0') {

                //Crea una copia para no modificar el tablero actual
                char[] nuevoTablero = tablero.clone();

                //Realiza la jugada en la copia
                nuevoTablero[i] = simbolo;

                //Guarda tambin que casilla produjo este hijo
                NodoEstado hijo = new NodoEstado(nuevoTablero, i);

                //Agrega el nuevo estado como hijo
                hijos.add(hijo);
            }
        }
    }

    public void generarDosNiveles(char simboloJugador, char simboloOponente) {

        //Genera las posibles jugadas del jugador actual
        generarHijos(simboloJugador);

        //Por cada jugada se genera las posibles respuestas del oponente
        for (NodoEstado hijo : hijos) {
            hijo.generarHijos(simboloOponente);
        }
    }

    private int contarLineasDisponibles(char simbolo) {

        //Cuenta las lineas que todavia puede completar el jugador
        int contador = 0;

        for (int[] linea : LINEAS) {

            int a = linea[0];
            int b = linea[1];
            int c = linea[2];

            //La linea esta disponible si no contiene simbolos del rival
            if ((tablero[a] == '\0' || tablero[a] == simbolo)
                    && (tablero[b] == '\0' || tablero[b] == simbolo)
                    && (tablero[c] == '\0' || tablero[c] == simbolo)) {

                contador++;
            }
        }

        return contador;
    }

    public int calcularUtilidad(char simboloJugador, char simboloOponente) {

        //Una victoria del jugador tiene maxima prioridad
        if (haGanado(simboloJugador)) {
            utilidad = 100;
            return utilidad;
        }

        //Una victoria del oponente es el peor resultado
        if (haGanado(simboloOponente)) {
            utilidad = -100;
            return utilidad;
        }

        //Si nadie gano, usa la funcion de utilidad normal
        int pJugador = contarLineasDisponibles(simboloJugador);
        int pOponente = contarLineasDisponibles(simboloOponente);

        utilidad = pJugador - pOponente;

        return utilidad;
    }

    public int calcularUtilidadMinima(char simboloJugador, char simboloOponente) {
        //Si ya no existen respuestas se evalua directamente este tablero
        if (hijos.isEmpty()) {
            return calcularUtilidad(simboloJugador, simboloOponente);
        }
        //Empieza con el valor mas alto posible
        int minima = Integer.MAX_VALUE;

        //Recorre todas las posibles respuestas del oponente
        for (NodoEstado hijo : hijos) {

            //Calcula la utilidad de cada respuesta
            int utilidadHijo = hijo.calcularUtilidad(
                    simboloJugador,
                    simboloOponente
            );

            //Guarda la utilidad mas pequeña encontrada
            if (utilidadHijo < minima) {
                minima = utilidadHijo;
            }
        }

        //Guarda la utilidad minima en este nodo padre
        utilidad = minima;

        return minima;
    }

    public NodoEstado obtenerMejorHijo(
            char simboloJugador,
            char simboloOponente) {

        //Guarda la mayor utilidad minima encontrada
        int maxima = Integer.MIN_VALUE;

        //Almacena todos los movimientos que sean
        //igualmente optimos segun minimax
        List<NodoEstado> mejoresHijos = new ArrayList<>();


        //Recorre todas las posibles jugadas del jugador
        for (NodoEstado hijo : hijos) {

            //Obtiene la peor respuesta posible del oponente
            int minima = hijo.calcularUtilidadMinima(
                    simboloJugador,
                    simboloOponente
            );


            //Encontro una jugada mejor que las anteriores
            if (minima > maxima) {

                maxima = minima;

                //Las jugadas anteriores dejan de ser optimas
                mejoresHijos.clear();

                mejoresHijos.add(hijo);


                //Encontro otra jugada con la misma utilidad maxima
            } else if (minima == maxima) {

                mejoresHijos.add(hijo);
            }
        }


        //Guarda la mejor utilidad encontrada
        utilidad = maxima;


        //No existen movimientos disponibles
        if (mejoresHijos.isEmpty()) {
            return null;
        }


        //Escoge aleatoriamente entre todos los
        //movimientos igualmente optimos
        return mejoresHijos.get(
                RANDOM.nextInt(mejoresHijos.size())
        );
    }

    private boolean haGanado(char simbolo) {

        //Revisa todas las formas posibles de ganar
        for (int[] linea : LINEAS) {

            int a = linea[0];
            int b = linea[1];
            int c = linea[2];

            //Comprueba si las tres posiciones tienen el mismo simbolo
            if (tablero[a] == simbolo
                    && tablero[b] == simbolo
                    && tablero[c] == simbolo) {

                return true;
            }
        }

        return false;
    }

}
