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
