package uex;

import uex.movimiento.Posicion;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<Posicion> posObjetivos;                // Posición de la casilla objetivo
    private int umbral;                                 // Umbral asociado al laberinto

    /**
     * @param dimension Dimensión del laberinto a crear
     */
    private Laberinto(int dimension) {
        laberinto = new Casilla[dimension][dimension];
        this.dimension = dimension;
        posObjetivos = new ArrayList<>();
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
     * @return Posición de la casilla objetivo
     */
    public List<Posicion> getPosObjetivos() {
        return posObjetivos;
    }

    /**
     * @param posObjetivos Posición de la celda objetivo
     */
    public void setPosObjetivos(List<Posicion> posObjetivos) {
        this.posObjetivos = posObjetivos;
    }

    /**
     * @return Cabecera horizontal para indicar las coordenadas del laberinto
     */
    private String cabecera() {
        StringBuilder stringBuilder = new StringBuilder();
        int currNumDig = 1;

        for (int i = 0; i < getDimension() && i < 1000; i++) {
            if (currNumDig >= digitosDe(i + 1) || getDimension() <= (i + 1)) {
                stringBuilder.append("   ".substring(digitosDe(i) - 1)).append(i);
            } else {
                stringBuilder.append("    ".substring(currNumDig)).append(i).append("\u2004");
                currNumDig++;
            }
        }

        return stringBuilder.toString();
    }

    /**
     * @param num Número
     * @return Cuantos dígitos tiene el número
     */
    private int digitosDe(int num) {
        if (num > 9) return 1 + digitosDe(num / 10);
        else return 1;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        final String buffer = "   ".substring(3 - digitosDe(getDimension() - 1));

        stringBuilder.append("Umbral : ").append(getUmbral())
                .append("\nPosición(es) del objetivo(s) : ").append(getPosObjetivos()).append("\n\n")
                .append(buffer.substring(1)).append(cabecera()).append("\n");
        for (int y = 0; y < getDimension(); y++) {
            stringBuilder.append(y).append(buffer.substring(digitosDe(y) - 1));
            for (int x = 0; x < getDimension(); x++) {
                if (y == 0 && x == 0) stringBuilder.append(" >  ");
                else stringBuilder.append(laberinto[y][x] != null ? laberinto[y][x] : "   ").append(" ");
            }
            stringBuilder.append(y).append("\n");
        }
        stringBuilder.append(buffer.substring(1)).append(cabecera()).append("\n");

        return stringBuilder.toString();
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
            setPosObjetivos(new ArrayList<>(Collections.singletonList(posiciones.get(posiciones.size() - 1))));
            posiciones.forEach(posicion -> super.insertarCasilla
                    (Laberinto.instancia().casilla(posicion), posicion));
        }
    }
}
