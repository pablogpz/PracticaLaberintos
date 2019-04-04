package uex.heuristicas;

import uex.Laberinto;
import uex.algoritmos.EstadoLaberinto;
import uex.movimiento.Posicion;

/**
 * Heurística : Distancia a la casilla destino desde la casilla actual en línea recta.
 * Corresponde a la fórmula
 * <p>
 * Math.sqrt(Math.pow((posicionDestino.getX() - getX()), 2) + Math.pow(posicionDestino.getY() - getY(), 2))
 */
public class DistanciaAlObjetivo implements Heuristica {
    @Override
    public Number apply(EstadoLaberinto estadoLaberinto) {
        Posicion posActual = estadoLaberinto.getJugador().ctrlMovimiento().getPosicion();
        Posicion posDestino = Laberinto.instancia().getPosObjetivc();

        return posActual.distanciaA(posDestino);
    }
}
