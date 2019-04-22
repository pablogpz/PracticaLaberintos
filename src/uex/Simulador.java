package uex;

import uex.algoritmos.*;
import uex.parsers.CasillaParser;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Punto de entrada al programa. Carga cada laberinto especificado por la ruta de recursos y ejecuta cada
 * algoritmo implementado con cada heurística implementada sobre cada laberinto
 *
 * @author Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 * @author Sergio Barrantes de la Osa
 */
public class Simulador {

    private static final String DEF_RUTA_FCH_LABERINTOS = "res/laberintos";

    /**
     * Punto de entrada
     *
     * @param args Un primer argumento opcional indica una carpeta externa de la que cargar archivos de laberinto
     */
    public static void main(String[] args) {
        boolean hayRutaLabAlt = args.length != 0;                   // Si se ha indicado una carpeta externa
        CargadorLaberinto cargador = null;
        int idx = 1;

        try {
            cargador = new CargadorLaberinto(new File(hayRutaLabAlt ? args[0] : DEF_RUTA_FCH_LABERINTOS),
                    new CasillaParser());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Ejecutores de cada algorítmo de búsqueda
        EjecutorExpansor genYPrueba = new EjecutorExpansor(new GeneracionYPrueba(EjecutorExpansor.heuristicaPorDefecto()));
        EjecutorExpansor escSimple = new EjecutorExpansor(new EscaladaSimple(EjecutorExpansor.heuristicaPorDefecto()));
        EjecutorExpansor escMaxPen = new EjecutorExpansor(new EscaladaMaximaPendiente(EjecutorExpansor.heuristicaPorDefecto()));
        EjecutorExpansor primeroMejor = new EjecutorExpansor(new PrimeroMejor(EjecutorExpansor.heuristicaPorDefecto()));
        EjecutorExpansor aEstrella = new EjecutorExpansor(new AEstrella(EjecutorExpansor.heuristicaPorDefecto()));

        // Ejecuta cada algoritmo implementado con cada heurística implementada
        //noinspection ConstantConditions
        do {
            System.out.println("\n\n\t--- LABERINTO " + idx++ + " ---\n\n");

            genYPrueba.ejecutar();
            escSimple.ejecutar();
            escMaxPen.ejecutar();
            primeroMejor.ejecutar();
            aEstrella.ejecutar();
        } while (cargador.cargarSiguienteLaberinto());
    }

}
