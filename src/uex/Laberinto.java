package uex;

import uex.movimiento.Posicion;

import java.util.List;

/**
 * Clase que modela el comportamiento del Laberinto (encargado de gestionar las casillas
 * asociadas a este mismo). Dimensión establecida : 10
 */
public class Laberinto {

    private static final int DEF_DIEMNSION = 10;        // Dimensión por defecto del laberinto
    private static final int DEF_UMBRAL = 40;           // Umbral por defecto del laberinto

    private static Laberinto instancia;                 // Instancia Singleton del laberinto
    private final int dimension;                        // Dimensión del laberinto
    private Casilla[][] laberinto;                      // Matriz de casillas que representa un tablero
    private Posicion posObjetivc;                       // Posición de la casilla objetivo
    private int umbral;                                 // Umbral asociado al laberinto

    /**
     * @param dimension Dimensión del laberinto a crear
     */
    private Laberinto(int dimension) {
        laberinto = new Casilla[dimension][dimension];
        this.dimension = dimension;
    }

    /**
     * @return Instancia Singleton del laberinto
     */
    public static Laberinto instancia() {
        if (instancia == null)
            instancia = new Laberinto(DEF_DIEMNSION);

        return instancia;
    }

    /**
     * @param posicion Posición de la casilla a consultar
     * @return Casilla del laberinto localizada en la posición suministrada
     */
    public Casilla casilla(Posicion posicion) {
        return laberinto[posicion.getY()][posicion.getX()];
    }

    /**
     * Carga un laberinto a partir de una matriz de casillas que lo conforman.
     * Pensado para cargar rápidamente un laberinto en el proceso de carga
     *
     * @param laberinto Matriz de casillas que conforman el laberinto
     */
    public void cargarLaberinto(Casilla[][] laberinto) {
        this.laberinto = laberinto;
    }

    /**
     * Inserta una casilla en una posición determinada del Laberinto
     *
     * @param casilla  Casilla a insertar en el Laberinto
     * @param posicion Posición del Laberinto en la que insertar la nueva casilla
     */
    private void insertarCasilla(Casilla casilla, Posicion posicion) {
        laberinto[posicion.getY()][posicion.getX()] = casilla;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Umbral : ").append(getUmbral())
                .append("\nPosición del objetivo : ").append(getPosObjetivc()).append("\n\n")
                .append("   0   1   2   3   4   5   6   7   8   9").append("\n");
        for (int y = 0; y < getDimension(); y++) {
            stringBuilder.append(y).append(" ");
            for (int x = 0; x < getDimension(); x++) {
                if (y == 0 && x == 0) stringBuilder.append(" >  ");
                else stringBuilder.append(laberinto[y][x] != null ? laberinto[y][x] : "   ").append(" ");
            }
            stringBuilder.append(y).append("\n");
        }
        stringBuilder.append("   0   1   2   3   4   5   6   7   8   9").append("\n");


        return stringBuilder.toString();
    }

    /**
     * @return Posición de la casilla objetivo
     */
    public Posicion getPosObjetivc() {
        return posObjetivc;
    }

    /**
     * @param posObjetivc Posición de la celda objetivo
     */
    public void setPosObjetivc(Posicion posObjetivc) {
        this.posObjetivc = posObjetivc;
    }

    /**
     * @return Dimensión del laberinto
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * @return Umbral asociado al laberinto. Si el umbral es menor o igual a 0 devuelve uno por
     * defecto {@link Laberinto#DEF_UMBRAL}
     */
    public int getUmbral() {
        return umbral > 0 ? umbral : DEF_UMBRAL;
    }

    /**
     * @param umbral Nuevo valor del umbral
     */
    public void setUmbral(int umbral) {
        this.umbral = umbral;
    }

    /**
     * Clase para representar soluciones al laberinto como un camino en el laberinto
     */
    public static final class Solucionado extends Laberinto {

        /**
         * Inicializa un laberinto con las posiciones que conforman la solución al problema
         *
         * @param posiciones     Colección de posiciones que conforman la solución al problema
         * @param umbralRestante Umbral restante de la solución
         */
        public Solucionado(List<Posicion> posiciones, int umbralRestante) {
            super(Laberinto.instancia().getDimension());

            setUmbral(umbralRestante);
            setPosObjetivc(Laberinto.instancia().getPosObjetivc());
            posiciones.forEach(posicion -> super.insertarCasilla
                    (Laberinto.instancia().casilla(posicion), posicion));
        }

    }
}
