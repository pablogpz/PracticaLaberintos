/**
 * Clase encargada de modelar el comportamiento de un jugador sobre el tablero
 */
public class Jugador {

    private ControladorMovimiento movimiento;       // Encapsula el movimiento sobre el laberinto

    public Jugador() {
        movimiento = new ControladorMovimiento();
    }

    /**
     * @return Controlador de movimiento asociado al jugador
     */
    public ControladorMovimiento controladorMovimiento() {
        return movimiento;
    }

    @Override
    public String toString() {
        return "Jugador :\n\tPosición : " + controladorMovimiento().getPosicion();
    }
}
