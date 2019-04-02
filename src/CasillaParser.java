import java.io.File;
import java.util.Objects;
import java.util.Scanner;

/**
 * Clase encargada de parsear archivos de laberinto {@code .lab} en colecciones de {@link Casilla} para ser cargados
 * en el Singleton de la clase {@link Laberinto}.
 * <p>
 * La primera línea del fichero debe contener un entero indicando el umbral.
 * Admite valores enteros como representación de casillas separados por comas y cada fila en una nueva línea
 */
public class CasillaParser {

    private Casilla[][] laberinto;          // Laberinto parseado
    private Posicion posObjetivo;           // Posición de la casilla objetivo
    private int umbral;                     // Umbral asociado al laberinto

    public CasillaParser() {
        laberinto = new Casilla[Laberinto.recuperarInstancia().getDimension()][Laberinto.recuperarInstancia().getDimension()];
    }

    /**
     * Parsea el fichero suministrado a un laberinto representable por el programa
     *
     * @param fichero Fichero de laberinto
     */
    public void parsearFichero(File fichero) {
        String[] valores;                   // Valores de las casillas de cada fila

        try (Scanner scanner = new Scanner(fichero)) {
            // Lectura de la primera línea: umbral
            setUmbral(Integer.parseInt(scanner.nextLine()));
            // Lectura del resto de líneas: valores separados por comas
            for (int y = 0; y < Laberinto.recuperarInstancia().getDimension(); y++) {
                valores = scanner.nextLine().split(",");
                for (int x = 0; x < Laberinto.recuperarInstancia().getDimension(); x++) {
                    Casilla casilla = new Casilla(Integer.parseInt(valores[x]));

                    laberinto[y][x] = casilla;
                    // Comprueba si es la casilla objetivo
                    if (casilla.esObjetivo()) setPosObjetivo(new Posicion(x, y));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Laberinto pareseado
     */
    public Casilla[][] getLaberinto() {
        return laberinto;
    }

    /**
     * @return Posición de la celda objetivo
     * @throws NullPointerException Si no hay ninguna casilla objetivo
     */
    public Posicion getPosObjetivo() {
        return Objects.requireNonNull(posObjetivo, "ERROR : No hay casilla objetivo en el laberinto");
    }

    /**
     * @return Umbral asociado al laberinto parseado
     */
    public int getUmbral() {
        return umbral;
    }

    /**
     * @param posObjetivo Nueva posición de la casilla objetivo
     */
    private void setPosObjetivo(Posicion posObjetivo) {
        this.posObjetivo = posObjetivo;
    }

    /**
     * @param umbral Nuevo valor del umbral asociado al laberinto parseado
     */
    private void setUmbral(int umbral) {
        this.umbral = umbral;
    }
}
