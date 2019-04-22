package uex.movimiento;

/**
 * Clase que modela una transformación de posiciones por movimiento. Un movimiento es un desplazamiento de unas unidades
 * dadas en una dirección dada
 *
 * @author Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 * @author Sergio Barrantes de la Osa
 */
public class Movimiento {

    private Direccion dirMovimiento;                // Dirección del movimiento
    private int unidades;                           // Unidades de desplazamiento

    /**
     * @param dirMovimiento Dirección del movimiento
     * @param unidades      Unidades de desplazamiento. Solo se considera su valor absoluto
     */
    public Movimiento(Direccion dirMovimiento, int unidades) {
        this.dirMovimiento = dirMovimiento;
        this.unidades = Math.abs(unidades);
    }

    /**
     * @return Dirección del movimiento
     */
    protected Direccion getDirMovimiento() {
        return dirMovimiento;
    }

    /**
     * @param dirMovimiento Nueva dirección de movimiento
     */
    protected void setDirMovimiento(Direccion dirMovimiento) {
        this.dirMovimiento = dirMovimiento;
    }

    /**
     * @return Unidades de desplazamiento
     */
    protected int getUnidades() {
        return unidades;
    }

    /**
     * @param unidades Nuevas unidades de desplazamiento. Solo se considera su valor absoluto
     */
    protected void setUnidades(int unidades) {
        this.unidades = Math.abs(unidades);
    }

    @Override
    public String toString() {
        return "{ " + getUnidades() + " unidad(es) en dirección " + getDirMovimiento() + " }";
    }
}
