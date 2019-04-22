package uex;

import uex.movimiento.ControladorMovimiento;

/**
 * Clase encargada de modelar el comportamiento de un jugador sobre el tablero
 *
 * @author Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 * @author Sergio Barrantes de la Osa
 */
public class Jugador implements Cloneable {

    private ControladorMovimiento movimiento;       // Encapsula el movimiento sobre el laberinto

    public Jugador() {
        movimiento = new ControladorMovimiento();
    }

    public Jugador(Jugador jugador) {
        movimiento = new ControladorMovimiento(jugador.ctrlMovimiento().posicion());
    }

    /**
     * @return Controlador de movimiento asociado al jugador
     */
    public ControladorMovimiento ctrlMovimiento() {
        return movimiento;
    }

    @Override
    public Object clone() {
        return new Jugador(this);
    }

    @Override
    public String toString() {
        return "Jugador :\n\tPosición : " + ctrlMovimiento().posicion();
    }
}
