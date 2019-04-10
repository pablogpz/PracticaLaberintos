package uex.parsers;

import uex.Casilla;
import uex.Laberinto;
import uex.movimiento.Posicion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CasillaParserAmpl extends CasillaParser {

    public static final String CHAR_OBSTACULO = "▓";

    private List<Posicion> objetivos;

    public CasillaParserAmpl() {
        super();

        objetivos = new ArrayList<>();
    }

    /**
     * Parsea el fichero suministrado a un laberinto representable por el programa teniendo en cuenta las ampliaciones
     *
     * @param fichero Fichero de laberinto
     */
    @Override
    public void parsearFichero(File fichero) {
        String[] valores;                   // Valores de las casillas de cada fila

        objetivos = new ArrayList<>();
        try (Scanner scanner = new Scanner(fichero)) {
            // Lectura de la primera línea: umbral
            setUmbral(Integer.parseInt(scanner.nextLine()));
            // Lectura del resto de líneas: valores separados por comas
            for (int y = 0; y < Laberinto.instancia().getDimension(); y++) {
                valores = scanner.nextLine().split(",");
                for (int x = 0; x < Laberinto.instancia().getDimension(); x++) {
                    Casilla casilla;
                    String valorCasilla = valores[x];

                    casilla = !valorCasilla.equals(CHAR_OBSTACULO) ? new Casilla(Integer.parseInt(valorCasilla))
                            : new Casilla(-1, Casilla.TipoCasilla.OBSTACULO);

                    laberinto[y][x] = casilla;
                    // Comprueba si es la casilla objetivo
                    if (casilla.esObjetivo()) objetivos.add(new Posicion(x, y));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Posicion> getObjetivos() {
        return objetivos;
    }
}
