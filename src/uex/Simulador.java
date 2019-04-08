package uex;

import uex.algoritmos.AEstrella;
import uex.algoritmos.EjecutorExpansor;
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
        EjecutorExpansor ejecutorExpansor = new EjecutorExpansor(new GeneracionYPrueba(null));

        try {
            cargador = new CargadorLaberinto(new File(RUTA_FCH_LABERINTOS), new CasillaParser());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        cargador.cargarSiguienteLaberinto();
        Heuristica heuristica = DistanciaAlObjetivo.tipo(DistanciaAlObjetivo.Calculo.DISCRETA);
        new AEstrella(heuristica).resolver();


//        //noinspection ConstantConditions
//        while (cargador.cargarSiguienteLaberinto())
//            ejecutorExpansor.ejecutar();

        // <- TESTING
    }

    static class Container implements Comparable {
        int conten;

        public Container(int conten) {
            this.conten = conten;
        }

        public int getConten() {
            return conten;
        }

        public void setConten(int conten) {
            this.conten = conten;
        }

        @Override
        public int compareTo(Object o) {
            return Integer.compare(getConten(), ((Container) o).getConten());
        }

        @Override
        public String toString() {
            return String.valueOf(getConten());
        }
    }

}
