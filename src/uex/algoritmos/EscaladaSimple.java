package uex.algoritmos;

import uex.Jugador;
import uex.Laberinto;
import uex.durian.TreeNode;
import uex.heuristicas.Heuristica;
import uex.movimiento.ControladorMovimiento;
import uex.movimiento.Posicion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class EscaladaSimple extends ExpansorArbol {

    private static final int NUM_ITERACIONES = 25;                  // Número máximo de intentos

    public EscaladaSimple(Heuristica heuristica) {
        super(heuristica);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void resetExpansor() {
        setContNodosGen(0);                                         // Reinicia el número de nodos generados
        getReloj().reset();                                         // Reinicia el cronómetro
    }

    @Override
    public void resolver() {
        TreeNode<EstadoLaberinto> copiaArbol;                       // Copia del padre para cada iteración del algoritmo
        boolean exito;                                              // Resultado de la última iteración. Determina el éxito
        int numIt = 0;                                              // Número de iteración

        // REINICIO DE VARIABLES ENTRE RESOLUCIONES DE LABERINTOS
        resetExpansor();
        getReloj().start();
        // Realiza hasta un cierto número de iteraciones intentando encontrar una solución
        do {
            // REINICIO DE VARIABLES ENTRE ITERACIONES
            copiaArbol = getArbolDecision().copy();                 // Reinicia la copia del nodo raíz inicial en cada it.

            exito = resolver(copiaArbol);
            numIt++;
        } while (!exito && numIt < NUM_ITERACIONES);
        getReloj().stop();

        // Comprueba si se encontró solución o si se agotaron las iteraciones disponibles
        if (exito) {
            System.out.println("ÉXITO en " + numIt + " intentos");
            mostrarSolucion(copiaArbol);
        } else {
            System.out.println("NO ENCONTRÓ SOLUCIÓN en " + NUM_ITERACIONES + " intentos\n" +
                    "Posiblemente no tenga solución\n");
        }
    }

    private boolean resolver(TreeNode<EstadoLaberinto> nodo) {
        // Variables del estado del laberinto actual
        EstadoLaberinto estadoLaberinto = nodo.getContent();
        List<Posicion> posVisitadas = estadoLaberinto.getPosVisitadas();
        ControladorMovimiento cMov = estadoLaberinto.getJugador().ctrlMovimiento();

        // Variables auxiliares
        Jugador clon;                                               // Copia del jugador para el siguiente nodo del árbol
        int nuevoUmbral;                                            // Umbral actualizado
        List<Posicion> visitadas;                                   // Nueva lista de visitados
        Posicion posDestino;                                        // Operando seleccionado
        boolean hijoSeleccionado;                                   // Si se encontró algún operando candidato viable
        EstadoLaberinto estadoExpandido = null;                     // Estado generado a partir del operando seleccionado

        // Comprueba si la casilla actual es el objetivo, sino expande otro nodo
        if (!Laberinto.instancia().casilla(cMov.posicion()).esObjetivo()) {
            // Recupera los operandos disponibles
            Collection<Posicion> operandos = operandosDisponibles(nodo);
            Iterator<Posicion> itOperandos = operandos.iterator();
            hijoSeleccionado = false;
            // Busca un hijo tal que su puntuación heurística sea mejor que la del padre
            while (itOperandos.hasNext() && !hijoSeleccionado) {
                // Selecciona el siguiente operando
                posDestino = itOperandos.next();

                /*
                 * Genera un nuevo estado expandido a partir del padre para evaluarlo según el operando seleccionado
                 */
                nuevoUmbral = estadoLaberinto.getUmbral() + costeAsociado(posDestino);
                // El clon realiza el movimiento elegido
                clon = (Jugador) estadoLaberinto.getJugador().clone();
                clon.ctrlMovimiento().setPosicionAbsoluta(posDestino);
                // Añade la posición de destino a las visitadas
                visitadas = new ArrayList<>(posVisitadas);
                visitadas.add(posDestino);
                // Crea el nuevo estado expandido para evaluarlo
                estadoExpandido = new EstadoLaberinto(clon, visitadas, nuevoUmbral);

                // Comprueba si es mejor que el padre
                if (getHeuristica().apply(estadoExpandido).floatValue() < aplicarHeuristica(nodo).floatValue())
                    hijoSeleccionado = true;
            }

            // Comprueba que e haya elegido algún operando candidato, sino todos eran peor que el padre
            if (!hijoSeleccionado) return false;

            setContNodosGen(getContNodosGen() + 1);                 // Incrementa en 1 el número de nodos generados

            // Determina si el nuevo umbral no supera el disponible
            if (estadoExpandido.getUmbral() <= Laberinto.instancia().getUmbral())
                return resolver(new TreeNode<>(nodo, estadoExpandido));
            else return false;
        } else {
            return true;                                            // Ha encontrado la solución
        }
    }

    private Collection<Posicion> operandosDisponibles(TreeNode<EstadoLaberinto> nodo) {
        // Variables del estado del laberinto actual
        EstadoLaberinto estadoLaberinto = nodo.getContent();
        List<Posicion> posVisitadas = estadoLaberinto.getPosVisitadas();
        ControladorMovimiento cMov = estadoLaberinto.getJugador().ctrlMovimiento();
        /*
         Obtiene los movimientos posibles a partir de la posición actual y filtra aquellas que no resulten
         en una posición ya expandida.
         */
        //noinspection ComparatorMethodParameterNotUsed
        return cMov.movimientosPosibles()
                .stream()
                .map(cMov::aplicarMovimiento)
                .filter(posicion -> !posVisitadas.contains(posicion))
                // Reordena aleatoriamente los operadores disponibles para introducir variabilidad en los caminos generados
                .sorted((o1, o2) -> (int) (Math.random() * 3) - 1)
                .collect(Collectors.toList());
    }

    @Override
    protected Posicion seleccionarOperando(TreeNode<EstadoLaberinto> nodo) {
        throw new UnsupportedOperationException("La selección de operando se lleva a cabo de otra manera");
    }

    @Override
    protected void mostrarSolucion(TreeNode<EstadoLaberinto> arbolDecision) {
        mostrarSolucionUnaHoja(arbolDecision);
    }

}