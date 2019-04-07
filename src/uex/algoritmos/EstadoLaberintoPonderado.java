package uex.algoritmos;

import uex.Jugador;
import uex.heuristicas.Heuristica;
import uex.movimiento.Posicion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estado del laberinto ponderado mediante una función de ponderación que devuelve el resultado de sumar la
 * puntuación heurística y el coste acumulado (umbral)
 */
public class EstadoLaberintoPonderado extends EstadoLaberinto implements Comparable {

    private Number puntuacionHeuristica;                // Puntuación heurística asociada al estado
    private int ponderacion;                            // Ponderación asociada al estado

    /**
     * @param jugador              Estado del jugador
     * @param posVisitadas         Lista de posiciones visitadas
     * @param umbral               Valor restante del umbral
     * @param puntuacionHeuristica Resultado de la aplicacion de una función heurística con respecto a este estado
     */
    public EstadoLaberintoPonderado(Jugador jugador, List<Posicion> posVisitadas, int umbral, Number puntuacionHeuristica) {
        super(jugador, posVisitadas, umbral);
        this.puntuacionHeuristica = puntuacionHeuristica;
        actualizarPonderacion();
    }

    /**
     * @param heuristica Función con la que evaluar el estado inicial
     * @return Estado inicial de un laberinto inexplorado
     */
    public static EstadoLaberintoPonderado estadoInicial(Heuristica heuristica) {
        // Nuevo jugador
        Jugador jugador = new Jugador();
        // Añade la posición inicial a las visitadas
        List<Posicion> posVisitadas = new ArrayList<>(Collections.singletonList(jugador.ctrlMovimiento().getPosicion()));
        // Estado inicial sin evaluar
        EstadoLaberintoPonderado estadoInicial = new EstadoLaberintoPonderado(jugador, posVisitadas, 0, 0);
        // Evalua según la heurística proporcionada el estado inicial
        estadoInicial.setPuntuacionHeuristica(heuristica.apply(estadoInicial));

        return estadoInicial;
    }

    /**
     * Actualiza el valor de la ponderación del estado
     */
    private void actualizarPonderacion() {
        setPonderacion(getPuntuacionHeuristica().intValue() + getUmbral());
    }

    /**
     * @return Puntuación heurística asociada al estado
     */
    public Number getPuntuacionHeuristica() {
        return puntuacionHeuristica;
    }

    /**
     * @return Ponderación asociada al estado
     */
    public int getPonderacion() {
        return ponderacion;
    }

    /**
     * Actualiza la puntuación heurística y su ponderación
     *
     * @param puntuacionHeuristica Nueva puntuación heurìstica
     */
    public void setPuntuacionHeuristica(Number puntuacionHeuristica) {
        this.puntuacionHeuristica = puntuacionHeuristica;
        actualizarPonderacion();
    }

    /**
     * Actualiza el umbral y su ponderación
     *
     * @param umbral Nuevo umbral
     */
    @Override
    protected void setUmbral(int umbral) {
        super.setUmbral(umbral);
        actualizarPonderacion();
    }

    private void setPonderacion(int ponderacion) {
        this.ponderacion = ponderacion;
    }

    /**
     * Los estados se ordenan por el orden natural del valor de su ponderación
     *
     * @param o Otro estado
     * @return Orden natural de las ponderaciones
     */
    @Override
    public int compareTo(Object o) {
        return Integer.compare(getPonderacion(), ((EstadoLaberintoPonderado) o).getPonderacion());
    }

    /**
     * @param obj Otro estado
     * @return Dos estados son equivalentes si el jugador se encuentra en la misma posición en el laberinto
     */
    @Override
    public boolean equals(Object obj) {
        return getJugador().ctrlMovimiento().getPosicion().equals(
                ((EstadoLaberintoPonderado) obj).getJugador().ctrlMovimiento().getPosicion());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n-------------------------------------------------------------\n");

        stringBuilder.append(getJugador()).append("\n");
        stringBuilder.append("Posiciones visitadas : \n");
        getPosVisitadas().forEach(pos -> stringBuilder.append("\t").append(pos).append("\n"));
        stringBuilder.append("Umbral : ").append(getUmbral()).append("\n");
        stringBuilder.append("Puntuación heurística : ").append(getPuntuacionHeuristica()).append("\n");
        stringBuilder.append("Ponderación : ").append(getPonderacion()).append("\n");

        stringBuilder.append("---------------------------------------------------------------\n");

        return stringBuilder.toString();
    }
}
