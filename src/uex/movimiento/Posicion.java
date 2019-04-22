package uex.movimiento;

import uex.Laberinto;

import java.util.Comparator;
import java.util.List;

/**
 * Clase encargada de modelar las distintas posiciones en laberinto siguiendo unos ejes cartesianos XY
 *
 * @author Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 * @author Sergio Barrantes de la Osa
 */
public class Posicion {

    private int x;                      // Columna de la posición especificada
    private int y;                      // Fila de la posición especificada

    /**
     * @param x Posición en el eje X
     * @param y Posición en el eje Y
     */
    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param posicion Posición a copiar
     */
    public Posicion(Posicion posicion) {
        x = posicion.getX();
        y = posicion.getY();
    }

    /**
     * Calcula la distancia real entre dos posiciones
     *
     * @param posicionDestino Posición de destino
     * @return Distancia a la posición destino en línea recta
     */
    public double distanciaA_real(Posicion posicionDestino) {
        return Math.sqrt(Math.pow(Math.abs(posicionDestino.getX() - getX()), 2) +
                Math.pow(Math.abs(posicionDestino.getY() - getY()), 2));
    }

    /**
     * Calcula la distancia discretizada en movimientos entre dos posiciones (Distancia diagonal)
     *
     * @param posicionDestino Posición de destino
     * @return Distancia a la posición destino cuantificada en movimientos de casilla
     */
    public int distanciaA_discreta(Posicion posicionDestino) {
        int dx = Math.abs(posicionDestino.getX() - getX());
        int dy = Math.abs(posicionDestino.getY() - getY());

        return dx + dy - Math.min(dx, dy);
    }

    /**
     * @return Casilla objetivo más cercana a la posición actual
     */
    public Posicion objetivoMasCercano() {
        List<Posicion> posObjetivos = Laberinto.instancia().getPosObjetivos();
        posObjetivos.sort(Comparator.comparingInt(this::distanciaA_discreta));

        return posObjetivos.get(0);
    }

    /**
     * @return Valor en el eje X
     */
    public int getX() {
        return x;
    }

    /**
     * @param x Nuevo valor en el eje X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return Valor en el eje Y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y Nuevo valor en el eje Y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @param x Nuevo valor en el eje X
     * @param y Nuevo valor en el eje Y
     */
    public void setPos(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * @param obj Posición a comparar
     * @return Dos posiciones son iguales si coinciden sus coordenadas X e Y
     */
    @Override
    public boolean equals(Object obj) {
        return getX() == ((Posicion) obj).getX() &&
                getY() == ((Posicion) obj).getY();
    }

    @Override
    public String toString() {
        return "{ x:" + getX() + ", y:" + getY() + " }";
    }
}
