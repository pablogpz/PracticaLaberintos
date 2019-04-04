package uex.movimiento;

import java.util.function.BiFunction;

/**
 * Clase que modela una transformación de posiciones por movimiento. Un movimiento es un desplazamientos de unas unidades
 * dadas en una dirección dada
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
     * @return Función de transformación de posiciones por moviento.
     * Suma a las coordenadas de una posición las unidades indicadas por el movimiento en una dirección determinada
     */
    public static BiFunction<Posicion, Movimiento, Posicion> funcionTransformacion() {
        return (posicion, movimiento) -> {
            switch (movimiento.getDirMovimiento()) {
                case NORTE:
                    posicion.setY(posicion.getY() - movimiento.getUnidades());
                    break;
                case ESTE:
                    posicion.setX(posicion.getX() + movimiento.getUnidades());
                    break;
                case SUR:
                    posicion.setY(posicion.getY() + movimiento.getUnidades());
                    break;
                case OESTE:
                    posicion.setX(posicion.getX() - movimiento.getUnidades());
            }
            return posicion;
        };
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
