package uex.algoritmos;

import uex.Jugador;
import uex.Laberinto;
import uex.durian.TreeNode;
import uex.heuristicas.Heuristica;
import uex.movimiento.ControladorMovimiento;
import uex.movimiento.Posicion;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que implementa el algoritmo de la A Estrella (A*)
 *
 * @author Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 * @author Sergio Barrantes de la Osa
 */
public class AEstrella extends ExpansorArbol {

    private ArrayList<TreeNode<EstadoLaberinto>> nodosAbiertos;     // Colección de nodos en exploración
    private Set<TreeNode<EstadoLaberinto>> nodosCerrados;           // Colección de nodos explorados

    /**
     * @param heuristica Función heurística a aplicar a los nodos del árbol
     */
    public AEstrella(Heuristica heuristica) {
        super(heuristica);
        resetExpansor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void resetExpansor() {
        // Actualiza el estado inicial a un estado ponderado inicial
        setArbolDecision(new TreeNode<>(null, EstadoLaberintoPonderado.estadoInicial(getHeuristica())));
        nodosAbiertos = new ArrayList<>();
        nodosCerrados = new HashSet<>();
        nodosAbiertos.add(getArbolDecision());                      // Añade el primer nodo, el estado inicial

        setContNodosGen(0);                                         // Reinicia el número de nodos generados
        getReloj().reset();                                         // Reinicia el cronómetro
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resolver() {
        // REINICIO DE VARIABLES ENTRE RESOLUCIONES DE LABERINTOS
        resetExpansor();

        // Extracción de variables locales
        Laberinto laberinto = Laberinto.instancia();
        TreeNode<EstadoLaberinto> mejorNodo = nodosAbiertos.get(0); // Nodo más prometedor
        Posicion operando;
        Jugador clon;
        ArrayList<Posicion> visitadas;

        getReloj().start();

        // Comprueba si es la casilla objetivo o si hay suficiente umbral para continuar el algoritmo
        while (!laberinto.casilla(mejorNodo.getContent().getJugador().ctrlMovimiento().posicion()).esObjetivo() &&
                mejorNodo.getContent().getUmbral() <= laberinto.getUmbral()) {
            // Cerrar el nodo más prometedor
            agregarNodoCerrado(mejorNodo);
            // Expande todos sus estados
            do {
                clon = (Jugador) mejorNodo.getContent().getJugador().clone();
                visitadas = new ArrayList<>();
                operando = seleccionarOperando(mejorNodo);

                // Comprueba que el operador sea válido
                if (operando != null) {
                    // Aplica el operando
                    clon.ctrlMovimiento().setPosicionAbsoluta(operando);
                    visitadas.add(mejorNodo.getContent().getJugador().ctrlMovimiento().posicion());

                    // Genera el nuevo estado
                    EstadoLaberintoPonderado estadoExpandido =
                            new EstadoLaberintoPonderado(clon, visitadas, mejorNodo.getContent().getUmbral() +
                                    costeAsociado(operando), getHeuristica());
                    setContNodosGen(getContNodosGen() + 1);

                    // Comprueba si el nuevo estado ya ha sido expandido
                    if (enNodos(estadoExpandido)) {
                        TreeNode<EstadoLaberinto> nodoEquivalente = recNodoEquiv(estadoExpandido);
                        EstadoLaberinto estadoEquivalente = nodoEquivalente.getContent();
                        // Si ha sido expandido comprueba si supone una alternativa mejor
                        if (estadoExpandido.getUmbral() < estadoEquivalente.getUmbral()) {
                            // Cambia el padre al nodo más prometedor y actualiza su coste y el de sus hijos
                            nodoEquivalente.changeParent(mejorNodo);
                            estadoEquivalente.setUmbral(estadoExpandido.getUmbral());
                            actualizarHijos(nodoEquivalente);
                            ordenarNodosAbiertos();
                        } // Si no supone una alternativa mejor se descarta
                    } else {
                        // Si no ha sido expandido se añade al árbol de decisión y a la colección de nodos abiertos
                        TreeNode<EstadoLaberinto> nodoExpandido = new TreeNode<>(mejorNodo, estadoExpandido);
                        agregarNodoAbierto(nodoExpandido);
                    }
                }
            } while (operando != null);

            mejorNodo = nodosAbiertos.get(0);
        }

        getReloj().stop();

        // Se encontró una solución si el umbral está por debajo del establecido, sino no tiene solución
        if (mejorNodo.getContent().getUmbral() <= laberinto.getUmbral()) {
            System.out.println("SOLUCIÓN ENCONTRADA");
            mostrarSolucion(mejorNodo);
        } else {
            System.out.println("NO TIENE SOLUCIÓN");
        }
    }

    /**
     * Selecciona un operando aplicable a un nodo. La lista de posiciones visitadas en primer lugar contiene la posicion
     * del nodo que creó el nodo actual, y tras sucesivas llamadas a este método, contendrá cada posicion expandida
     * hasta que no queden posiciones posibles. Evita que se vuelva hacia atrás por el nodo padre
     *
     * @param nodo Nodo con el estado laberinto para el que seleccionar un operando
     * @return En cada llamada un operando disponible no visitado. Cuando se agoten devolverá nulo
     */
    @Override
    protected Posicion seleccionarOperando(TreeNode<EstadoLaberinto> nodo) {
        // Variables del estado del laberinto actual
        EstadoLaberinto estadoLaberinto = nodo.getContent();
        /*
        La lista de posiciones visitadas en primer lugar contiene la posicion del nodo que creó el nodo actual, y tras
        sucesivas llamadas a este método, contendrá cada posicion expandida hasta que no queden posiciones posibles.
        Evita que se vuelva hacia atrás por el nodo padre
         */
        List<Posicion> posVisitadas = estadoLaberinto.getPosVisitadas();
        ControladorMovimiento cMov = estadoLaberinto.getJugador().ctrlMovimiento();

        /*
         Obtiene los movimientos posibles a partir de la posición actual y filtra aquellas que no resulten
         en una posición ya expandida. Después ordena los movimientos por costes
         */
        List<Posicion> posPosibles = cMov.movimientosPosibles()
                .stream()
                .map(cMov::aplicarMovimiento)
                .filter(posicion -> !posVisitadas.contains(posicion))
                .sorted(Comparator.comparingInt(this::costeAsociado))
                .collect(Collectors.toList());

        // Si queda alguna posición posible la añade a las expandidas
        if (posPosibles.size() != 0) posVisitadas.add(posPosibles.get(0));
        else return null;

        // Siguente posición posible elegida
        return posPosibles.get(0);
    }

    /**
     * Muestra la solución al laberinto mostrando las casillas que conforman la solución y un resumen de cada estado
     * involucrado en la solución con el jugador, puntuación heurística, umbral acumulado y coste ponderado
     *
     * @param arbolDecision Árbol de decisión que representa la solución
     */
    @Override
    protected void mostrarSolucion(TreeNode<EstadoLaberinto> arbolDecision) {
        // Imprime tiempo empleado
        System.out.println("Tiempo empleado : " + getReloj());
        // Imprime el número de nodos generados en memoria
        System.out.println("Número de nodos generados : " + getContNodosGen());

        ArrayList<Posicion> camino = new ArrayList<>();
        recuperarCamino(arbolDecision, camino);
        camino.add(arbolDecision.getContent().getJugador().ctrlMovimiento().posicion());

        System.out.println(new Laberinto.Solucionado(camino, arbolDecision.getContent().getUmbral()));
        System.out.println(arbolDecision.getPath(EstadoLaberinto::toString, ""));
    }

    /**
     * @param nodo   Nodo que llega a la solución
     * @param camino Camino hasta la solución por el nodo suministrado
     */
    private void recuperarCamino(TreeNode<EstadoLaberinto> nodo, List<Posicion> camino) {
        camino.add(nodo.getContent().getJugador().ctrlMovimiento().posicion());
        if (nodo.getParent() != null)
            recuperarCamino(nodo.getParent(), camino);
    }

    /**
     * Añade un nodo a la lista ordenada de nodos abiertos
     *
     * @param nodo Nodo a añadir
     */
    private void agregarNodoAbierto(TreeNode<EstadoLaberinto> nodo) {
        nodosAbiertos.add(nodo);
        ordenarNodosAbiertos();
    }

    /**
     * Añade un nodo a la colección de nodos cerrados y lo elimina de los nodos abiertos
     *
     * @param nodo Nodo abierto a añadir
     */
    private void agregarNodoCerrado(TreeNode<EstadoLaberinto> nodo) {
        nodosAbiertos.remove(nodo);
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
    private Collection<TreeNode<EstadoLaberinto>> nodosExpandidos() {
        Set<TreeNode<EstadoLaberinto>> nodos = new HashSet<>(new HashSet<>(nodosAbiertos));
        nodos.addAll(nodosCerrados);

        return nodos;
    }

    /**
     * @param estadoLaberintoPonderado Estado a comprobar si ya existe
     * @return Si el estado suministrado ya ha sido expandido
     */
    private boolean enNodos(EstadoLaberinto estadoLaberintoPonderado) {
        return nodosExpandidos()
                .stream()
                .map(TreeNode::getContent)
                .filter(estadoLaberinto -> estadoLaberinto.equals(estadoLaberintoPonderado))
                .collect(Collectors.toSet()).size() != 0;
    }

    /**
     * @param estadoLaberintoPonderado Clave con la que recuperar el estado equivalente
     * @return Nodo con el estado equivalente al pasado por parámetro
     */
    private TreeNode<EstadoLaberinto> recNodoEquiv(EstadoLaberinto estadoLaberintoPonderado) {
        return nodosExpandidos()
                .stream()
                .filter(nodo -> nodo.getContent().equals(estadoLaberintoPonderado))
                .collect(Collectors.toList()).get(0);
    }

    /**
     * Actualiza el coste y la ponderación de los hijos del padre {@code nodo} a partir de su nuevo coste
     *
     * @param nodo Nodo padre con coste modificado
     */
    private void actualizarHijos(TreeNode<EstadoLaberinto> nodo) {
        int costeMovHijo;

        // Si tiene hijos los actualiza
        if (nodo.getChildren().size() != 0) {
            // Para cada hijo actualiza su coste y ponderación
            for (TreeNode<EstadoLaberinto> hijo : nodo.getChildren()) {
                EstadoLaberinto estadoHijo = hijo.getContent();
                // Coste del movimiento que representa el hijo
                costeMovHijo = costeAsociado(estadoHijo.getJugador().ctrlMovimiento().posicion());
                // Actualiza el coste del hijo con el nuevo coste del padre más el coste de su movimiento asociado
                estadoHijo.setUmbral(nodo.getContent().getUmbral() + costeMovHijo);
                // Si el hijo tiene más descendientes los actualiza también
                if (hijo.getChildren().size() != 0)
                    actualizarHijos(hijo);
            }
        }
    }

}
