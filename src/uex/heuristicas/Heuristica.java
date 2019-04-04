package uex.heuristicas;

import uex.algoritmos.EstadoLaberinto;

import java.util.function.Function;

/**
 * Clase para modelar una función heurística que toma como argumento un estado de laberinto {@link EstadoLaberinto} y
 * devuelve un número {@link Number} (evaluable como todas las primitivas de números)
 */
public interface Heuristica extends Function<EstadoLaberinto, Number> {
}
