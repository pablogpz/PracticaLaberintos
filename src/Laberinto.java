import java.util.List;

/**
 * Clase que modela el comportamiento del Laberinto (encargado de gestionar las casillas
 * asociadas a este mismo). Dimensión establecida : 10
 */
public class Laberinto {

    private static final int DEF_DIEMNSION = 10;        // Dimensión por defecto del laberinto

    private static Laberinto instancia;                 // Instancia Singleton del laberinto

    private Casilla[][] laberinto;                      // Matriz de casillas que representa un tablero
    private Posicion posObjetivc;                       // Posición de la casilla objetivo
    private final int dimension;                        // Dimensión del laberinto
    private int umbral;                                 // Umbral asociado al laberinto

    /**
     * @param dimension Dimensión del laberinto a crear
     */
    private Laberinto(int dimension) {
        laberinto = new Casilla[dimension][dimension];
        this.dimension = dimension;
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
            for (int x = 0; x < getDimension(); x++)
                stringBuilder.append(laberinto[y][x] != null ? laberinto[y][x] : "   ").append(" ");
            stringBuilder.append(y).append("\n");
        }
        stringBuilder.append("   0   1   2   3   4   5   6   7   8   9").append("\n");


        return stringBuilder.toString();
    }

    /**
     * @return Instancia Singleton del laberinto
     */
    public static Laberinto recuperarInstancia() {
        if (instancia == null)
            instancia = new Laberinto(DEF_DIEMNSION);

        return instancia;
    }

    /**
     * @return Posición de la casilla objetivo
     */
    public Posicion getPosObjetivc() {
        return posObjetivc;
    }

    /**
     * @return Dimensión del laberinto
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * @return Umbral asociado al laberinto
     */
    public int getUmbral() {
        return umbral;
    }

    /**
     * @param posObjetivc Posición de la celda objetivo
     */
    public void setPosObjetivc(Posicion posObjetivc) {
        this.posObjetivc = posObjetivc;
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
    public static final class Representable extends Laberinto {

        /**
         * Inicializa un laberinto con las posiciones que conforman la solución al problema
         *
         * @param posiciones Colección de posiciones que conforman la solución al problema
         */
        public Representable(List<Posicion> posiciones) {
            super(Laberinto.recuperarInstancia().getDimension());

            setPosObjetivc(Laberinto.recuperarInstancia().getPosObjetivc());
            posiciones.forEach(posicion -> super.insertarCasilla
                    (Laberinto.recuperarInstancia().casilla(posicion), posicion));
        }

    }
}
