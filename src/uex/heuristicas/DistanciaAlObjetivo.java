package uex.heuristicas;

import uex.Laberinto;
import uex.algoritmos.EstadoLaberinto;
import uex.movimiento.Posicion;

/**
 * Heurística : Distancia a la casilla destino desde la casilla actual según distintos métodos de cálculo
 *
 * @see Calculo
 * @see DistanciaAlObjetivo_real
 * @see DistanciaAlObjetivo_discreta
 */
public abstract class DistanciaAlObjetivo {

    /**
     * Permite elegir un método de cálculo para determinar la distancia al objetivo
     *
     * @param calculo Selección del método de cálculo
     * @return Heurística asociada a ese método de cálculo
     */
    public static Heuristica tipo(Calculo calculo) {
        switch (calculo) {
            case REAL:
                return DistanciaAlObjetivo_real.singletonInstance();
            case DISCRETA:
                return DistanciaAlObjetivo_discreta.singletonInstance();
            default:
                return null;
        }
    }

    /**
     * Métodos de cálculo
     */
    public enum Calculo {
        REAL, DISCRETA
    }

    /**
     * Heurística : Distancia a la casilla destino desde la casilla actual discretizada en número de movimientos
     * Corresponde a la fórmula
     * <p>
     * posDestino.getX() - getX() + posDestino.getY() - getY()
     */
    private static class DistanciaAlObjetivo_discreta implements Heuristica {

        // Instancia Singleton de la heurística
        private static DistanciaAlObjetivo_discreta singletonInstance = null;

        private DistanciaAlObjetivo_discreta() {
        }

        private static DistanciaAlObjetivo_discreta singletonInstance() {
            if (singletonInstance == null)
                singletonInstance = new DistanciaAlObjetivo_discreta();

            return singletonInstance;
        }

        @Override
        public Number apply(EstadoLaberinto estadoLaberinto) {
            Posicion posActual = estadoLaberinto.getJugador().ctrlMovimiento().getPosicion();
            Posicion posDestino = Laberinto.instancia().getPosObjetivc();

            return posActual.distanciaA_discreta(posDestino);
        }
    }

    /**
     * Heurística : Distancia a la casilla destino desde la casilla actual en línea recta.
     * Corresponde a la fórmula
     * <p>
     * Math.sqrt(Math.pow((posicionDestino.getX() - getX()), 2) + Math.pow(posicionDestino.getY() - getY(), 2))
     */
    private static class DistanciaAlObjetivo_real implements Heuristica {

        // Instancia Singleton de la heurística
        private static DistanciaAlObjetivo_real singletonInstance = null;

        private DistanciaAlObjetivo_real() {
        }

        private static DistanciaAlObjetivo_real singletonInstance() {
            if (singletonInstance == null)
                singletonInstance = new DistanciaAlObjetivo_real();

            return singletonInstance;
        }

        @Override
        public Number apply(EstadoLaberinto estadoLaberinto) {
            Posicion posActual = estadoLaberinto.getJugador().ctrlMovimiento().getPosicion();
            Posicion posDestino = Laberinto.instancia().getPosObjetivc();

            return posActual.distanciaA_real(posDestino);
        }
    }
}


