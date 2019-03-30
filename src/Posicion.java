/**
 * Clase encargada de modelar las distintas posiciones en laberinto siguiendo unos ejes cartesianos XY
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
     * @return Valor en el eje X
     */
    public int getX() {
        return x;
    }

    /**
     * @return Valor en el eje Y
     */
    public int getY() {
        return y;
    }

    /**
     * @param x Nuevo valor en el eje X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y Nuevo valor en el eje Y
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{ x:" + getX() + ", y:" + getY() + " }";
    }
}
