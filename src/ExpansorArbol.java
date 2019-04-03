import com.diffplug.common.base.TreeNode;

import java.util.function.Function;

/**
 * Clase que modela un esquema algorítmo para manipular el laberinto. Se basa en un arbol de decisiones formado por
 * {@link EstadoLaberinto} y que aplica una heurística dada por una función que toma como entrada un estado del laberinto
 * y devuelve un entero como resultado de la estimación
 */
public abstract class ExpansorArbol {

    private TreeNode<EstadoLaberinto> arbolDecision;                // Arbol de decisión para modelar la expansión del algoritmo
    private Function<EstadoLaberinto, Integer> heuristica;          // Función heurística a aplicar a los nodos del árbol

    /**
     * @param heuristica Función heurística a aplicar a los nodos del árbol
     */
    public ExpansorArbol(Function<EstadoLaberinto, Integer> heuristica) {
        this.heuristica = heuristica;
        arbolDecision = new TreeNode<>(null, EstadoLaberinto.estadoInicial());
    }

    /**
     * Ejecuta el esquema algorítmico
     */
    public abstract void ejecutar();

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
    protected int aplicarHeuristica(TreeNode<EstadoLaberinto> nodo) {
        return getHeuristica().apply(nodo.getContent());
    }

    /**
     * @param posicionDestino Posición de destino
     * @return Coste sobre el umbral asociado a la posición de destino
     */
    protected int actualizarUmbral(Posicion posicionDestino) {
        return Laberinto.instancia().casilla(posicionDestino).getValor();
    }

    /**
     * @return Función heurística a aplicar a los nodos del árbol
     */
    protected Function<EstadoLaberinto, Integer> getHeuristica() {
        return heuristica;
    }

    /**
     * @param heuristica Nueva función heurística
     */
    protected void setHeuristica(Function<EstadoLaberinto, Integer> heuristica) {
        this.heuristica = heuristica;
    }

    /**
     * @return Arbol de decisión para modelar la expansión del algoritmo
     */
    protected TreeNode<EstadoLaberinto> getArbolDecision() {
        return arbolDecision;
    }
}
