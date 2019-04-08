package uex.algoritmos;

import com.diffplug.common.base.TreeNode;
import uex.Laberinto;
import uex.heuristicas.Heuristica;
import uex.movimiento.Posicion;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO Documentar A*
 */
public class AEstrella extends ExpansorArbol {

    private ArrayList<TreeNode<EstadoLaberintoPonderado>> nodosAbiertos;    // Colección de nodos en exploración
    private Set<TreeNode<EstadoLaberintoPonderado>> nodosCerrados;          // Colección de nodos explorados

    /**
     * @param heuristica Función heurística a aplicar a los nodos del árbol
     */
    public AEstrella(Heuristica heuristica) {
        super(heuristica);

        nodosAbiertos = new ArrayList<>();
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
     * Añade un nodo a la lista ordenada de nodos abiertos
     *
     * @param nodo Nodo a añadir
     */
    private void agregarNodoAbierto(TreeNode<EstadoLaberintoPonderado> nodo) {
        nodosAbiertos.add(nodo);
        ordenarNodosAbiertos();
    }

    /**
     * Añade un nodo a la colección de nodos cerrados
     *
     * @param nodo Nodo a añadir
     */
    private void agregarNodoCerrado(TreeNode<EstadoLaberintoPonderado> nodo) {
        nodosCerrados.add(nodo);
    }

    /**
     * Ordena los nodos abiertos por ponderación ascendentemente
     */
    private void ordenarNodosAbiertos() {
        nodosAbiertos.sort(Comparator.comparing(TreeNode::getContent));
    }

    /**
     * @return Todos los nodos expandidos hasta ahora
     */
    private Collection<TreeNode<EstadoLaberintoPonderado>> nodosExpandidos() {
        Set<TreeNode<EstadoLaberintoPonderado>> nodos = new HashSet<>(new HashSet<>(nodosAbiertos));
        nodos.addAll(nodosCerrados);

        return nodos;
    }

    /**
     * @param estadoLaberintoPonderado Estado a comprobar si ya existe
     * @return Si el estado suministrado ya ha sido expandido
     */
    private boolean enNodos(EstadoLaberintoPonderado estadoLaberintoPonderado) {
        return nodosExpandidos()
                .stream()
                .map(TreeNode::getContent)
                .collect(Collectors.toSet()).contains(estadoLaberintoPonderado);
    }

    /**
     * @param estadoLaberintoPonderado Clave con la que recuperar el estado equivalente
     * @return Estado equivalente al pasado por parámetro
     */
    private EstadoLaberintoPonderado recEstadoLaberintoExp(EstadoLaberintoPonderado estadoLaberintoPonderado) {
        return nodosExpandidos()
                .stream()
                .map(TreeNode::getContent)
                .filter(estado -> estado.equals(estadoLaberintoPonderado))
                .collect(Collectors.toList()).get(0);
    }

    /**
     * Actualiza el coste y la ponderación de los hijos del padre {@code nodo} a partir de su nuevo coste
     *
     * @param nodo Nodo padre con coste modificado
     */
    private void actualizarHijos(TreeNode<EstadoLaberintoPonderado> nodo) {
        Laberinto laberinto = Laberinto.instancia();

        // Si tiene hijos los actualiza
        if (nodo.getChildren().size() != 0) {
            // Para cada hijo actualiza su coste y ponderación
            for (TreeNode<EstadoLaberintoPonderado> hijo : nodo.getChildren()) {
                EstadoLaberinto estadoHijo = hijo.getContent();
                // Coste del movimiento que representa el hijo
                int costeMovHijo = laberinto.casilla(estadoHijo.getJugador().ctrlMovimiento().getPosicion()).getValor();

                // Actualiza el coste del hijo con el nuevo coste del padre más el coste de su movimiento asociado
                estadoHijo.setUmbral(nodo.getContent().getUmbral() + costeMovHijo);

                // Si el hijo tiene más descendientes los actualiza también
                if (hijo.getChildren().size() != 0)
                    actualizarHijos(hijo);
            }
        }
    }

}
