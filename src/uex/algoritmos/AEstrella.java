package uex.algoritmos;

import com.diffplug.common.base.TreeNode;
import uex.heuristicas.Heuristica;
import uex.movimiento.Posicion;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO Documentar A*
 */
public class AEstrella extends ExpansorArbol {

    private PriorityQueue<TreeNode<EstadoLaberintoPonderado>> nodosAbiertos;    // Colección de nodos en exploración
    private Set<TreeNode<EstadoLaberintoPonderado>> nodosCerrados;              // Colección de nodos explorados

    /**
     * @param heuristica Función heurística a aplicar a los nodos del árbol
     */
    public AEstrella(Heuristica heuristica) {
        super(heuristica);

        nodosAbiertos = new PriorityQueue<>(Comparator.comparing(TreeNode::getContent));
        nodosAbiertos.add(new TreeNode<>(null, EstadoLaberintoPonderado.estadoInicial(heuristica)));
        nodosCerrados = new HashSet<>();
    }

    /**
     * TODO Documentar resolver A*
     */
    @Override
    public void resolver() {
    }

    /**
     * TODO Documentar seleccionarOperando A*
     */
    @Override
    protected Posicion seleccionarOperando(TreeNode<EstadoLaberinto> nodo) {
        return null;
    }

    /**
     * TODO Documentar mostrarSolucion A*
     */
    @Override
    protected void mostrarSolucion(TreeNode<EstadoLaberinto> arbolDecision) {
    }

    /**
     * @param estadoLaberintoPonderado Estado a comprobar si ya existe
     * @return Si el estado suministrado ya está expandido en la cola de prioridad de nodos abiertos
     */
    private boolean enNodosAbiertos(EstadoLaberintoPonderado estadoLaberintoPonderado) {
        return new ArrayList<>(nodosAbiertos
                .stream()
                .map(TreeNode::getContent)
                .collect(Collectors.toList()))
                .contains(estadoLaberintoPonderado);
    }

    /**
     * @param estadoLaberintoPonderado Estado a comprobar si ya existe
     * @return Si el estado suministrado ya está expandido en la colección de nodos cerrados
     */
    private boolean enNodosCerrados(EstadoLaberintoPonderado estadoLaberintoPonderado) {
        return new ArrayList<>(nodosCerrados
                .stream()
                .map(TreeNode::getContent)
                .collect(Collectors.toList()))
                .contains(estadoLaberintoPonderado);
    }

}
