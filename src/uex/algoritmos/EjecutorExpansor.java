package uex.algoritmos;

import uex.Laberinto;
import uex.heuristicas.Heuristica;
import uex.heuristicas.Heuristicas;

import java.util.Iterator;

/**
 * Maneja la ejecución de un esquema algorítmico sobre el laberinto y bajo distintos parámetros
 */
public class EjecutorExpansor {

    private ExpansorArbol expansorArbol;                    // Algoritmo a ejecutar
    private Iterator<Heuristica> itHeuristicas;             // Iterador de heurísticas implementadas

    public EjecutorExpansor() {
    }

    /**
     * @param expansorArbol Esquema algorítmo a ejecutar
     */
    public EjecutorExpansor(ExpansorArbol expansorArbol) {
        this.expansorArbol = expansorArbol;
    }

    /**
     * @return Primera heurística que se va a evaluar
     */
    public Heuristica heuristicaPorDefecto() {
        resetItHeuristicas();
        return itHeuristicas.next();
    }

    /**
     * Ejecuta el esquema algorítmico establecido sobre el laberinto cargado. Emplea todas las heurísticas
     * implementadas
     */
    public void ejecutar() {
        resetItHeuristicas();                               // Reinicia las heurísticas disponibles

        imprimirCabecera();
        imprimirLaberinto();                                // Muestra el laberinto sin resolver
        // Ejecuta el algoritmo establecido en el laberinto cargado empleando todas las heurísticas
        while (itHeuristicas.hasNext()) {
            expansorArbol.setHeuristica(itHeuristicas.next());
            System.out.println("*\t" + expansorArbol.getHeuristica() + "\t*");
            expansorArbol.resolver();
        }
        imprimirPie();
    }

    private void imprimirCabecera() {
        System.out.println("ALGORITMO : " + getExpansorArbol().getClass().getSimpleName());
        System.out.println("*****************************************************************************************");
    }

    private void imprimirPie() {
        System.out.println("*****************************************************************************************");
    }

    /**
     * Reinicia el iterador de heurísticas
     */
    private void resetItHeuristicas() {
        itHeuristicas = Heuristicas.itHeuristicas();
    }

    /**
     * Muestra por consola la representación del laberinto
     */
    private void imprimirLaberinto() {
        System.out.println(Laberinto.instancia());
    }

    /**
     * @return Esquema algorítmico establecido
     */
    public ExpansorArbol getExpansorArbol() {
        return expansorArbol;
    }

    /**
     * @param expansorArbol Nuevo esquema algorítmico
     */
    public void setExpansorArbol(ExpansorArbol expansorArbol) {
        this.expansorArbol = expansorArbol;
    }
}
