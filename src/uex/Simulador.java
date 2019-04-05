package uex;

import uex.algoritmos.ExpansorArbol;
import uex.algoritmos.GeneracionYPrueba;
import uex.heuristicas.DistanciaAlObjetivo;
import uex.heuristicas.Heuristica;
import uex.parsers.CasillaParser;

import java.io.File;
import java.io.FileNotFoundException;

public class Simulador {

    private static String RUTA_FCH_LABERINTOS = "res/laberintos";

    public static void main(String[] args) {
        // TESTING ->

        CargadorLaberinto cargador = null;
        Heuristica distancia_discreta = DistanciaAlObjetivo.tipo(DistanciaAlObjetivo.Calculo.DISCRETA);
        Heuristica distancia_real = DistanciaAlObjetivo.tipo(DistanciaAlObjetivo.Calculo.REAL);
        ExpansorArbol generacionYPrueba = new GeneracionYPrueba(distancia_discreta);

        try {
            cargador = new CargadorLaberinto(new File(RUTA_FCH_LABERINTOS), new CasillaParser());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //noinspection ConstantConditions
        while (cargador.cargarSiguienteLaberinto()) {
            System.out.println(Laberinto.instancia());
            for (int i = 0; i < 2; i++) {
                if (i % 2 != 0) generacionYPrueba.setHeuristica(distancia_discreta);
                else generacionYPrueba.setHeuristica(distancia_real);

                System.out.println("\t***\t" + generacionYPrueba.getHeuristica() + " ***");
                generacionYPrueba.resolver();
            }
        }

        // <- TESTING
    }
}
