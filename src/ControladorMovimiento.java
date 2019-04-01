import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorMovimiento {

    private static final int POS_X_INI = 0;
    private static final int POS_Y_INI = 0;

    private Posicion posicion;

    /**
     * Instancia un controlador de movimiento en la posición {x:0, y:0}
     */
    public ControladorMovimiento() {
        posicion = new Posicion(0, 0);
    }

    /**
     * @param posicionInicial Posición inicial
     */
    public ControladorMovimiento(Posicion posicionInicial) {
        this.posicion = posicionInicial;
    }

    public void setPosicionRelativa(Direccion direccion) {
        setPosicionRelativa(direccion, 1);
    }

    /**
     * Mueve la posición del controlador en la direccion {@code direccion} {@code unidades} unidades
     *
     * @param direccion Dirección de desplazamiento
     * @param unidades  Unidades a desplazar. Solo se tiene en cuenta su valor absoluto
     * @throws IllegalArgumentException Si el movimiento resulta fuera de los límites del laberinto
     */
    public void setPosicionRelativa(Direccion direccion, int unidades) {
        if (!movRelativoLegal(direccion, unidades))
            throw new IllegalArgumentException("No se puede mover más allá de los límites del laberinto.\n\t" +
                    "Movimiento : " + unidades + " unidades en direccion " + direccion + "\n\tPosición actual " + getPosicion());

        // Mueve la posición en la dirección indicada las unidades indicadas
        switch (direccion) {
            case NORTE:
                getPosicion().setY(getPosicion().getY() - Math.abs(unidades));
                break;
            case ESTE:
                getPosicion().setX(getPosicion().getX() + Math.abs(unidades));
                break;
            case SUR:
                getPosicion().setY(getPosicion().getY() + Math.abs(unidades));
                break;
            case OESTE:
                getPosicion().setX(getPosicion().getX() - Math.abs(unidades));
        }
    }

    /**
     * Mueve la posición del controlador a una posición absoluta indicada por parámetro
     *
     * @param posicionAbsoluta Posición de destino
     * @throws IllegalArgumentException Si la posición suministada está fuera de los límites del laberinto
     */
    public void setPosicionAbsoluta(Posicion posicionAbsoluta) {
        if (!movAbsolutoLegal(posicionAbsoluta))
            throw new IllegalArgumentException("Movimiento absoluto fuera de los límites del laberinto.\n\t" +
                    "Movimiento : " + posicionAbsoluta);

        setPosicion(posicionAbsoluta);
    }

    public List<Direccion> movimientosPosibles() {
        return new ArrayList<>(EnumSet.allOf(Direccion.class))
                .stream()
                .filter(direccion -> movRelativoLegal(direccion, 1))
                .collect(Collectors.toList());
    }

    /**
     * Resetea el controlador a la posición inicial
     */
    public void resetPosicion() {
        setPosicionAbsoluta(new Posicion(POS_X_INI, POS_Y_INI));
    }

    /**
     * @param direccion Dirección de desplazamiento
     * @param unidades  Unidades a desplazar
     * @return Si el movimiento relativo es legal, es decir, si el resultado de moverte ciertas unidades en una
     * dirección está dentro de los límites del laberinto
     */
    private boolean movRelativoLegal(Direccion direccion, int unidades) {
        return direccion == Direccion.NORTE && getPosicion().getY() - Math.abs(unidades) >= 0 ||
                direccion == Direccion.ESTE && getPosicion().getX() + Math.abs(unidades) < Laberinto.recuperarInstancia().getDimension() ||
                direccion == Direccion.SUR && getPosicion().getY() + Math.abs(unidades) < Laberinto.recuperarInstancia().getDimension() ||
                direccion == Direccion.OESTE && getPosicion().getX() - Math.abs(unidades) >= 0;
    }

    /**
     * @param posicionAbsoluta Posición absoluta destino del movimiento
     * @return Si el movimiento absoluto es legal, es decir, es una posición dentro de los límites del laberinto
     */
    private boolean movAbsolutoLegal(Posicion posicionAbsoluta) {
        return posicionAbsoluta.getX() >= 0 &&
                posicionAbsoluta.getX() < Laberinto.recuperarInstancia().getDimension() &&
                posicionAbsoluta.getY() >= 0 &&
                posicionAbsoluta.getY() < Laberinto.recuperarInstancia().getDimension();
    }

    /**
     * @return Posición asociada al controlador
     */
    public Posicion getPosicion() {
        return posicion;
    }

    private void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
}
