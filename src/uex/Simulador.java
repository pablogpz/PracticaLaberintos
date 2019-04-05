package uex;

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
        EjecutorExpansor ejecutorExpansor = new EjecutorExpansor(new GeneracionYPrueba(null));

        try {
            cargador = new CargadorLaberinto(new File(RUTA_FCH_LABERINTOS), new CasillaParser());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //noinspection ConstantConditions
        while (cargador.cargarSiguienteLaberinto())
            ejecutorExpansor.ejecutar();

        // <- TESTING
    }
}
