import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * Clase encargada de cargar los laberintos en la instancia Singleton de la clase {@link Laberinto}.
 * Los ficheros de laberinto deben estar contenidos en una carpeta y deben tener la extensión {@code .lab}
 */
public class CargadorLaberinto {

    private File carpetaLaberintos;             // Ruta a la carpeta donde están los ficheros de laberintos
    private Iterator<File> itFichLaberintos;    // Iterador de archivos de laberintos leídos de la carpeta suministrada
    private CasillaParser parser;               // Parser de casillas para procesar cada fichero de laberinto

    /**
     * Construye y configura un cargador de laberintos si los parámetros son válidos
     *
     * @param carpetaLaberintos Ruta a la carpeta donde están los ficheros de laberintos
     * @param parser            Parser de casillas para procesar cada fichero de laberinto
     * @throws FileNotFoundException Si la ruta suministrada no es una carpeta
     */
    public CargadorLaberinto(File carpetaLaberintos, CasillaParser parser) throws FileNotFoundException {
        this.carpetaLaberintos = carpetaLaberintos;
        this.parser = parser;

        abrirCarpeta();
    }

    /**
     * Carga el siguiente fichero de laberinto
     */
    public void cargarSiguienteLaberinto() {
        // Parsea el siguiente fichero
        parser.parsearFichero(itFichLaberintos.next());
        // Carga el laberinto parseado
        Laberinto.recuperarInstancia().setUmbral(parser.getUmbral());
        Laberinto.recuperarInstancia().cargarLaberinto(parser.getLaberinto());
    }

    /**
     * Inicializa el iterador de ficheros de laberinto
     *
     * @throws FileNotFoundException Si la ruta suministrada no es una carpeta
     */
    private void abrirCarpeta() throws FileNotFoundException {
        if (carpetaLaberintos.isDirectory()) {
            itFichLaberintos = Arrays.asList(Objects
                    .requireNonNull(carpetaLaberintos
                            .listFiles(CargadorLaberinto::ficheroValido)))
                    .iterator();                // Filtra los ficheros '.lab'

            if (!itFichLaberintos.hasNext())
                throw new IllegalStateException("No se ha podido leer ningún fichero válido de laberinto");
        } else {
            throw new FileNotFoundException("La ruta: \"" + carpetaLaberintos + "\" no es un directorio");
        }
    }

    /**
     * Comprueba si un fichero es un fichero válido de laberinto
     *
     * @param dir  Objeto {@code File} que representa al fichero
     * @param name Nombre del fichero
     * @return Si es un fichero de laberinto válido
     */
    private static boolean ficheroValido(File dir, String name) {
        return name.matches("\\w+.lab$");
    }

    /**
     * @return Ruta de la carpeta de ficheros de laberinto
     */
    public File getCarpetaLaberintos() {
        return carpetaLaberintos;
    }

    /**
     * @param carpetaLaberintos Nueva ruta de la carpeta de ficheros de laberinto
     */
    public void setCarpetaLaberintos(File carpetaLaberintos) {
        this.carpetaLaberintos = carpetaLaberintos;
    }
}
