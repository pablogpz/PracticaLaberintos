/**
 * Clase encargada de modelar el comportamiento de un jugador sobre el tablero
 */
public class Jugador implements Cloneable {

    private ControladorMovimiento movimiento;       // Encapsula el movimiento sobre el laberinto

    public Jugador() {
        movimiento = new ControladorMovimiento();
    }

    public Jugador(Jugador jugador) {
        movimiento = new ControladorMovimiento(jugador.controladorMovimiento().getPosicion());
    }

    /**
     * @return Controlador de movimiento asociado al jugador
     */
    public ControladorMovimiento controladorMovimiento() {
        return movimiento;
    }

    @Override
    protected Object clone() {
        return new Jugador(this);
    }

    @Override
    public String toString() {
        return "Jugador :\n\tPosición : " + controladorMovimiento().getPosicion();
    }
}
