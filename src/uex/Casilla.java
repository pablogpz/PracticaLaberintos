package uex;

/**
 * Clase encargada de modelar la casilla de un laberinto.
 * Cada casilla almacena un valor que indica qué tipo de casllla es
 */
public class Casilla {

    private static final int VALOR_OBJETIVO = 0;    // Valor de la casilla objetivo

    private final int valor;                        // Valor numérico de la casilla

    /**
     * @param valor Valor inicial de la casilla
     */
    public Casilla(int valor) {
        this.valor = valor;
    }

    /**
     * @return Si es una celda objetivo
     */
    public boolean esObjetivo() {
        return valor() == VALOR_OBJETIVO;
    }

    /**
     * @return Valor de la casilla
     */
    public int valor() {
        return valor;
    }

    @Override
    public String toString() {
        return esObjetivo() ? " " + valor() + " " : "[" + valor() + "]";
    }
}
