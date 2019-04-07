package uex;

import uex.algoritmos.AEstrella;
import uex.algoritmos.EjecutorExpansor;
import uex.algoritmos.GeneracionYPrueba;
import uex.heuristicas.DistanciaAlObjetivo;
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

        cargador.cargarSiguienteLaberinto();
        new AEstrella(DistanciaAlObjetivo.tipo(DistanciaAlObjetivo.Calculo.REAL)).resolver();

//        //noinspection ConstantConditions
//        while (cargador.cargarSiguienteLaberinto())
//            ejecutorExpansor.ejecutar();

        // <- TESTING
    }
}
