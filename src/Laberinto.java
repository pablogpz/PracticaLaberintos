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
     * Carga un laberinto a partir de una matriz de casillas que lo conforman.
     * Pensado para cargar rápidamente un laberinto en el proceso de carga
     *
     * @param laberinto Matriz de casillas que conforman el laberinto
     */
    public void cargarLaberinto(Casilla[][] laberinto) {
        this.laberinto = laberinto;
    }

    /**
     * @param posicion Posición de la casilla a consultar
     * @return Casilla del laberinto localizada en la posición suministrada
     */
    public Casilla casilla(Posicion posicion) {
        return laberinto[posicion.getX()][posicion.getY()];
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Umbral : ").append(getUmbral()).append("\nPosición del objetivo : ")
                .append(getPosObjetivc())
                .append("\n\n");
        for (int i = 0; i < getDimension(); i++) {
            for (int j = 0; j < getDimension(); j++)
                stringBuilder.append(laberinto[i][j]).append(" ");
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
