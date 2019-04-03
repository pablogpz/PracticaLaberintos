import com.diffplug.common.base.TreeDef;
import com.diffplug.common.base.TreeNode;
import com.diffplug.common.base.TreeStream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneracionYPrueba extends ExpansorArbol {

    private static final int NUM_ITERACIONES = 25;          // Número máximo de intentos

    public GeneracionYPrueba() {
        super(null);
    }

    /**
     * Implementa el algoritmo de búsqueda sin información "Generación y Prueba". No emplea ninguna función heurística,
     * pero ordena los operadores disponibles por coste. El coste de un movimiento es el valor asociado a la casilla
     * de la posición destino.
     */
    @Override
    public void resolver() {
        TreeNode<EstadoLaberinto> copiaArbol;               // Copia del padre para cada iteración del algoritmo
        boolean exito;                                      // Resultado de la última iteración. Determina el éxito
        int numIt = 0;                                      // Número de iteración

        setContNodos(0);                                    // Reinicia el número de nodos generados
        // Realiza hasta un cierto número de iteraciones intentando encontrar una solución
        getReloj().start();
        do {
            copiaArbol = getArbolDecision().copy();
            exito = resolver(copiaArbol);
            numIt++;
        } while (!exito && numIt < NUM_ITERACIONES);
        getReloj().stop();

        // Comprueba si se encontró solución o si se agotaron las iteraciones disponibles
        if (exito) {
            System.out.println("ÉXITO en " + numIt + " intentos");
            mostrarSolucion(copiaArbol);
        } else {
            System.out.println("NO ENCONTRÓ SOLUCIÓN en " + NUM_ITERACIONES + " intentos\nPosiblemente no tenga solución");
        }

        getReloj().reset();                                 // Reinicia el reloj
    }

    /**
     * Función sumergida del método {@link GeneracionYPrueba#resolver()}
     *
     * @param nodo Árbol de decisión de entrada
     * @return Si la iteración encontró una solución
     */
    private boolean resolver(TreeNode<EstadoLaberinto> nodo) {
        // Variables del estado del laberinto actual
        EstadoLaberinto estadoLaberinto = nodo.getContent();
        List<Posicion> posVisitadas = estadoLaberinto.getPosVisitadas();
        ControladorMovimiento cMov = estadoLaberinto.getJugador().ctrlMovimiento();

        // Variables auxiliares
        Jugador clon;                                       // Copia del jugador para el siguiente nodo del árbol
        int nuevoUmbral;                                    // Umbral actualizado
        List<Posicion> visitadas;                           // Nueva lista de visitados

        // Comprueba si la casilla actual es el objetivo
        if (!Laberinto.instancia().casilla(cMov.getPosicion()).esObjetivo()) {
            // Selección de operando
            Posicion posDestino = seleccionarOperando(nodo);
            // Actualización del umbral. Si no hay posición disponible se actualiza a -1 para detener la iteración
            nuevoUmbral = posDestino != null ? estadoLaberinto.getUmbral() - actualizarUmbral(posDestino) : -1;
            setContNodos(getContNodos() + 1);               // Incrementa en 1 el número de nodos generados

            // Determina si debe seguir la iteración. El nuevo umbral no debe superar el disponible
            if (nuevoUmbral >= 0) {
                // El clon realiza el movimiento elegido
                clon = (Jugador) estadoLaberinto.getJugador().clone();
                clon.ctrlMovimiento().setPosicionAbsoluta(posDestino);

                // Añade la posición de destino a las visitadas
                visitadas = new ArrayList<>(posVisitadas);
                visitadas.add(posDestino);

                return resolver(new TreeNode<>(nodo, new EstadoLaberinto(clon, visitadas, nuevoUmbral)));
            } else return false;
        } else {
            return true;
        }
    }

    /**
     * Obtiene los movimientos posibles a partir de la posición actual y filtra aquellas que no resulten
     * en una posición ya visitada. Después ordena los movimientos por costes y elige uno aleatoriamente según una
     * función aleatoria ponderada que da más prioridad los elementos del inicio de la lista
     * <p>
     * idx = (Math.random() * (nPosiciones * (nPosiciones + 1)) / 2) / nPosiciones
     * <p>
     * Da prioridad a los elementos del inicio de la lista mapeando una cantidad de valores mayor en el intervalo
     * {@code (0, sum(indices+1)]} y equivalente a la fórmula {@code nValores = nPosiciones - idx}
     *
     * @param nodo Nodo Árbol de decisión a evaluar
     * @return Posición de destino
     */
    @Override
    protected Posicion seleccionarOperando(TreeNode<EstadoLaberinto> nodo) {
        // Variables del estado del laberinto actual
        EstadoLaberinto estadoLaberinto = nodo.getContent();
        List<Posicion> posVisitadas = estadoLaberinto.getPosVisitadas();
        ControladorMovimiento cMov = estadoLaberinto.getJugador().ctrlMovimiento();

        Posicion posDestino = null;

        /*
         Obtiene los movimientos posibles a partir de la posición actual y filtra aquellas que no resulten
         en una posición ya visitada. Después ordena los movimientos por costes
         */
        List<Posicion> posPosibles = cMov.movimientosPosibles()
                .stream()
                .map(cMov::aplicarMovimiento)
                .filter(posicion -> !posVisitadas.contains(posicion))
                .sorted(Comparator.comparingInt(p -> Laberinto.instancia().casilla(p).getValor()))
                .collect(Collectors.toList());

        // Comprueba si no se ha encerrado a sí mismo. Sino calcula la siguiente posición
        if (posPosibles.size() != 0)
            /*
             Función aleatoria ponderada : Selecciona un índice de la lista de posiciones posibles a través de la
             siguiente fórmula

                idx = (Math.random() * (nPosiciones * (nPosiciones + 1)) / 2) / nPosiciones

            Da prioridad a los elementos del inicio de la lista mapeando una cantidad de valores mayor en el intervalo
            (0, sum(indices+1)] y equivalente a la fórmula nValores = nPosiciones - idx
             */
            posDestino = posPosibles.get((int) (Math.random() *
                    ((posPosibles.size() * (posPosibles.size() + 1)) / 2) / posPosibles.size()));

        return posDestino;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void mostrarSolucion(TreeNode<EstadoLaberinto> arbolDecision) {
        // Lista de nodos del árbol
        List<TreeNode<EstadoLaberinto>> collect = TreeStream.depthFirst(TreeNode.treeDef(), arbolDecision)
                .collect(Collectors.toList());
        // Filtrado del nodo solución, el que no tiene hijos
        TreeNode<EstadoLaberinto> sol = TreeDef.filteredList(collect, nodo -> nodo.getChildren().size() == 0).get(0);

        // Imprime tiempo empleado
        System.out.println("Tiempo empleado : " + getReloj().elapsed(TimeUnit.MICROSECONDS) + " " + TimeUnit.MICROSECONDS);
        // Imprime el número de nodos generados en memoria
        System.out.println("Número de nodos generados : " + getContNodos());
        // Representación del camino solución
        System.out.println(new Laberinto.Solucionado(sol.getContent().getPosVisitadas()));
        // Secuencia de estados. Representación de la expansión
        System.out.println(arbolDecision.toStringDeep());
    }

    /**
     * @return Imposible
     * @throws UnsupportedOperationException El algoritmo de Generación Y Prueba no emplea ninguna heurística
     */
    @Override
    protected int aplicarHeuristica(TreeNode<EstadoLaberinto> nodo) {
        throw new UnsupportedOperationException("El algoritmo \"" + getClass().getName() +
                "\" no utiliza funciones heurísticas");
    }

    /**
     * @return Imposible
     * @throws UnsupportedOperationException El algoritmo de Generación Y Prueba no emplea ninguna heurística
     */
    @Override
    protected Function<EstadoLaberinto, Integer> getHeuristica() {
        throw new UnsupportedOperationException("El algoritmo \"" + getClass().getName() +
                "\" no utiliza funciones heurísticas");
    }
}
