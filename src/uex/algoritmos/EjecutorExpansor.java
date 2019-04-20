package uex.algoritmos;

import uex.Laberinto;
import uex.heuristicas.Heuristica;
import uex.heuristicas.Heuristicas;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * Maneja la ejecución de un esquema algorítmico sobre el laberinto y bajo distintos parámetros
 */
public class EjecutorExpansor {

    private final ExpansorArbol expansorArbol;              // Algoritmo a ejecutar
    private static Iterator<Heuristica> itHeuristicas;      // Iterador de heurísticas implementadas

    /**
     * @param expansorArbol Esquema algorítmo a ejecutar
     */
    public EjecutorExpansor(ExpansorArbol expansorArbol) {
        this.expansorArbol = expansorArbol;
    }

    /**
     * @return Primera heurística que se va a evaluar
     */
    public static Heuristica heuristicaPorDefecto() {
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
    private static void resetItHeuristicas() {
        itHeuristicas = Heuristicas.itHeuristicas();
    }

    /**
     * Muestra por consola la representación del laberinto con representación de caracteres UNICODE (solo en plataformas
     * con soporte para este conjunto de caracteres)
     */
    private void imprimirLaberinto() {
        Charset utf8 = Charset.forName("UTF-8");
        Charset defCharset = Charset.defaultCharset();
        PrintStream printStream;
        byte[] bytes;

        bytes = Laberinto.instancia().toString().getBytes(StandardCharsets.UTF_8);
        try {
            printStream = new PrintStream(System.out, true, utf8.name());
            printStream.println(new String(bytes, defCharset.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Esquema algorítmico establecido
     */
    public ExpansorArbol getExpansorArbol() {
        return expansorArbol;
    }
}
