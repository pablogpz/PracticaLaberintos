package uex.algoritmos;

import com.google.common.base.Stopwatch;
import uex.Laberinto;
import uex.durian.TreeNode;
import uex.heuristicas.Heuristica;
import uex.movimiento.Posicion;

/**
 * Clase que modela un esquema algorítmo para manipular el laberinto. Se basa en un arbol de decisiones formado por
 * {@link EstadoLaberinto} y que aplica una heurística dada por una función que toma como entrada un estado del laberinto
 * y devuelve un entero como resultado de la estimación
 */
public abstract class ExpansorArbol {

    private TreeNode<EstadoLaberinto> arbolDecision;                // Arbol de decisión para modelar la expansión del algoritmo
    private Heuristica heuristica;                                  // Función heurística a aplicar a los nodos del árbol

    private int contNodosGen;                                       // Número de nodos generados en memoria
    private Stopwatch reloj;                                        // Medidor del tiempo de ejecución del algoritmo

    /**
     * @param heuristica Función heurística a aplicar a los nodos del árbol
     */
    public ExpansorArbol(Heuristica heuristica) {
        this.heuristica = heuristica;
        arbolDecision = new TreeNode<>(null, EstadoLaberinto.estadoInicial());

        contNodosGen = 0;
        reloj = Stopwatch.createUnstarted();
    }

    /**
     * Reinicia el estado interno del expansor, dejándolo listo para recibir otro laberinto
     */
    protected abstract void resetExpansor();

    /**
     * Ejecuta el esquema algorítmico
     */
    public abstract void resolver();

    /**
     * Implementa la lógica de selección de operando para el árbol de decisión dado en el contexto apropiado
     *
     * @param nodo Nodo Árbol de decisión a evaluar
     * @return Posición de destino calculada
     */
    protected abstract Posicion seleccionarOperando(TreeNode<EstadoLaberinto> nodo);

    /**
     * Aplica la función heurística al nodo suministrado
     *
     * @param nodo Nodo a evaluar
     * @return Resultado de la función heurística de evaluación
     */
    protected Number aplicarHeuristica(TreeNode<EstadoLaberinto> nodo) {
        return getHeuristica().apply(nodo.getContent());
    }

    /**
     * @param posicionDestino Posición de destino
     * @return Coste asociado a la posición de destino, o {@code  Laberinto.instancia().getUmbral()}
     * si la posición de destino es nula
     */
    protected int costeAsociado(Posicion posicionDestino) {
        return posicionDestino != null ? Laberinto.instancia().casilla(posicionDestino).valor() :
                Laberinto.instancia().getUmbral();
    }

    /**
     * Interpreta y representa la solución al algoritmo. El algoritmo debe haber hallado una solución.
     * Incluye la representación de la solución, el tiempo empleado y el número de nodos generados
     *
     * @param arbolDecision Árbol de decicisón que contiene la solución
     */
    protected abstract void mostrarSolucion(TreeNode<EstadoLaberinto> arbolDecision);

    /**
     * @return Función heurística a aplicar a los nodos del árbol
     */
    public Heuristica getHeuristica() {
        return heuristica;
    }

    /**
     * @param heuristica Nueva función heurística
     */
    public void setHeuristica(Heuristica heuristica) {
        this.heuristica = heuristica;
    }

    /**
     * @return Arbol de decisión para modelar la expansión del algoritmo
     */
    protected TreeNode<EstadoLaberinto> getArbolDecision() {
        return arbolDecision;
    }

    /**
     * @return Número de nodos generados en memoria
     */
    public int getContNodosGen() {
        return contNodosGen;
    }

    /**
     * @param contNodosGen Nuevo número de nodos generados en memoria
     */
    protected void setContNodosGen(int contNodosGen) {
        this.contNodosGen = contNodosGen;
    }

    /**
     * @return Medidor empleado para medir el tiempo de ejecución del algoritmo
     */
    protected Stopwatch getReloj() {
        return reloj;
    }

    protected void setArbolDecision(TreeNode<EstadoLaberinto> arbolDecision) {
        this.arbolDecision = arbolDecision;
    }
}
