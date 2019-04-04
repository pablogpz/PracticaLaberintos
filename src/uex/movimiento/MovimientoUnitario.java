package uex.movimiento;

/**
 * Un tipo de movimiento que siempre tiene como desplazamiento la unidad (1)
 */
public class MovimientoUnitario extends Movimiento {

    /**
     * @param dirMovimiento Dirección del movimiento unitario
     */
    public MovimientoUnitario(Direccion dirMovimiento) {
        super(dirMovimiento, 1);
    }

    /**
     * No se puede cambiar las unidades de desplazamiento de un movimiento unitario, siempre serán (1)
     *
     * @throws UnsupportedOperationException Imposible
     */
    @Override
    protected void setUnidades(int unidades) {
        throw new UnsupportedOperationException("No es posible cambiar las unidades de un movimiento unitario");
    }
}
