package uex.algoritmos;

import uex.Jugador;
import uex.Laberinto;
import uex.durian.TreeNode;
import uex.heuristicas.Heuristica;
import uex.movimiento.ControladorMovimiento;
import uex.movimiento.Posicion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EscaladaMaximaPendiente extends ExpansorArbol {

    public EscaladaMaximaPendiente(Heuristica heuristica) {
        super(heuristica);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void resetExpansor() {
        setArbolDecision(new TreeNode<>(null, EstadoLaberinto.estadoInicial()));

        setContNodosGen(0);                                         // Reinicia el número de nodos generados
        getReloj().reset();                                         // Reinicia el cronómetro
    }

    @Override
    public void resolver() {
        boolean exito;

        // REINICIO DE VARIABLES ENTRE RESOLUCIONES DE LABERINTOS
        resetExpansor();
        getReloj().start();

        exito = resolver(getArbolDecision());

        getReloj().stop();

        // Comprueba si se encontró solución o si se agotaron las iteraciones disponibles
        if (exito) {
            System.out.println("ÉXITO");
            mostrarSolucion(getArbolDecision());
        } else {
            System.out.println("NO ENCONTRÓ SOLUCIÓN\nPosiblemente no tenga solución\n");
        }
    }

    private boolean resolver(TreeNode<EstadoLaberinto> nodo) {
        // Variables del estado del laberinto actual
        EstadoLaberinto estadoLaberinto = nodo.getContent();
        ControladorMovimiento cMov = estadoLaberinto.getJugador().ctrlMovimiento();

        // Variables auxiliares
        EstadoLaberinto estadoExpandido;                            // Estado generado a partir del operando seleccionado

        // Comprueba si la casilla actual es el objetivo, sino expande otro nodo
        if (!Laberinto.instancia().casilla(cMov.posicion()).esObjetivo()) {
            // Obtiene el mejor candidato
            estadoExpandido = mejorEstadoExpandido(nodo);

            // Comprueba si el mejor candidato no es peor que el padre
            if (estadoExpandido != null &&
                    getHeuristica().apply(estadoExpandido).floatValue() >= aplicarHeuristica(nodo).floatValue())
                return false;

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
        return cMov.movimientosPosibles()
                .stream()
                .map(cMov::aplicarMovimiento)
                .filter(posicion -> !posVisitadas.contains(posicion))
                .collect(Collectors.toList());
    }

    @Override
    protected Posicion seleccionarOperando(TreeNode<EstadoLaberinto> nodo) {
        throw new UnsupportedOperationException("La selección de operando se lleva a cabo de otra manera");
    }

    private EstadoLaberinto mejorEstadoExpandido(TreeNode<EstadoLaberinto> nodo) {
        // Variables del estado del laberinto actual
        EstadoLaberinto estadoLaberinto = nodo.getContent();
        List<Posicion> posVisitadas = estadoLaberinto.getPosVisitadas();

        // Variables auxiliares
        List<EstadoLaberinto> estadosExpandidos = new ArrayList<>();// Lista de estados que se pueden expandir
        Jugador clon;                                               // Copia del jugador para el siguiente nodo del árbol
        int nuevoUmbral;                                            // Umbral actualizado
        List<Posicion> visitadas;                                   // Nueva lista de visitados

        for (Posicion operando : operandosDisponibles(nodo)) {
            nuevoUmbral = estadoLaberinto.getUmbral() + costeAsociado(operando);
            // El clon realiza el movimiento elegido
            clon = (Jugador) estadoLaberinto.getJugador().clone();
            clon.ctrlMovimiento().setPosicionAbsoluta(operando);
            // Añade la posición de destino a las visitadas
            visitadas = new ArrayList<>(posVisitadas);
            visitadas.add(operando);

            // Crea el nuevo estado expandido para evaluarlo
            estadosExpandidos.add(new EstadoLaberinto(clon, visitadas, nuevoUmbral));
            setContNodosGen(getContNodosGen() + 1);                 // Incrementa en 1 el número de nodos generados
        }

        return estadosExpandidos
                .stream()
                .min(Comparator.comparing(estadoExpandido -> getHeuristica().apply(estadoExpandido).floatValue()))
                .orElse(null);
    }

    @Override
    protected void mostrarSolucion(TreeNode<EstadoLaberinto> arbolDecision) {
        mostrarSolucionUnaHoja(arbolDecision);
    }

}