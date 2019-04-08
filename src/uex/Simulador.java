package uex;

import uex.algoritmos.AEstrella;
import uex.algoritmos.EjecutorExpansor;
import uex.algoritmos.GeneracionYPrueba;
import uex.parsers.CasillaParser;

import java.io.File;
import java.io.FileNotFoundException;

public class Simulador {

    private static String RUTA_FCH_LABERINTOS = "res/laberintos";

    public static void main(String[] args) {
        // TESTING ->

        CargadorLaberinto cargador = null;

        try {
            cargador = new CargadorLaberinto(new File(RUTA_FCH_LABERINTOS), new CasillaParser());
            cargador.cargarSiguienteLaberinto();            // Carga el primer laberinto
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        EjecutorExpansor genYPrueba = new EjecutorExpansor(new GeneracionYPrueba(EjecutorExpansor.heuristicaPorDefecto()));
        EjecutorExpansor aEstrella = new EjecutorExpansor(new AEstrella(EjecutorExpansor.heuristicaPorDefecto()));

        //noinspection ConstantConditions
        do {
            // Ejecuta los algoritmos
            genYPrueba.ejecutar();
            aEstrella.ejecutar();
        } while (cargador.cargarSiguienteLaberinto());

        // <- TESTING
    }

}
