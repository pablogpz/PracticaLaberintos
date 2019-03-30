import java.io.File;
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
            for (int i = 0; i < Laberinto.recuperarInstancia().getDimension(); i++) {
                valores = scanner.nextLine().split(",");
                for (int j = 0; j < Laberinto.recuperarInstancia().getDimension(); j++)
                    laberinto[i][j] = new Casilla(Integer.parseInt(valores[j]));
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
     * @return Umbral asociado al laberinto parseado
     */
    public int getUmbral() {
        return umbral;
    }

    /**
     * @param umbral Nuevo valor del umbral asociado al laberinto parseado
     */
    private void setUmbral(int umbral) {
        this.umbral = umbral;
    }
}
