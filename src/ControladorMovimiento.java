public class ControladorMovimiento {

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

    /**
     * Mueve la posición del controlador en la direccion {@code direccion} {@code unidades} unidades
     *
     * @param direccion Dirección de desplazamiento
     * @param unidades  Unidades a desplazar. Solo se tiene en cuenta su valor absoluto
     * @throws IllegalArgumentException Si el movimiento resulta fuera de los límites del laberinto
     */
    public void mover(Direccion direccion, int unidades) {
        if (!movimientoLegal(direccion, unidades))
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
     * @param direccion Dirección de desplazamiento
     * @param unidades  Unidades a desplazar
     * @return Si el movimiento es legal, es decir, está dentro de los límites del laberinto
     */
    private boolean movimientoLegal(Direccion direccion, int unidades) {
        return direccion == Direccion.NORTE && getPosicion().getY() - Math.abs(unidades) >= 0 ||
                direccion == Direccion.ESTE && getPosicion().getX() + Math.abs(unidades) < Laberinto.recuperarInstancia().getDimension() ||
                direccion == Direccion.SUR && getPosicion().getY() + Math.abs(unidades) < Laberinto.recuperarInstancia().getDimension() ||
                direccion == Direccion.OESTE && getPosicion().getX() - Math.abs(unidades) >= 0;
    }

    /**
     * @return Posición asociada al controlador
     */
    public Posicion getPosicion() {
        return posicion;
    }
}
