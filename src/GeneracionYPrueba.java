import com.diffplug.common.base.TreeDef;
import com.diffplug.common.base.TreeNode;
import com.diffplug.common.base.TreeStream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneracionYPrueba extends ExpansorArbol {

    private static final int NUM_ITERACIONES = 25;          // Número máximo de intentos

    public GeneracionYPrueba() {
        super(null);
    }

    /**
     * TODO Describir el algoritmo
     */
    @Override
    public void ejecutar() {
        TreeNode<EstadoLaberinto> copiaArbol;               // Copia del padre para cada iteración del algoritmo
        boolean resultado;                                  // Resultado de la última iteración. Determina el éxito
        int numIt = 0;                                      // Número de iteración

        // Realiza hasta un cierto número de iteraciones intentando encontrar una solución
        do {
            copiaArbol = getArbolDecision().copy();
            resultado = ejecutar(copiaArbol);
            numIt++;
        } while (!resultado && numIt < NUM_ITERACIONES);

        // Comprueba si se encontró solución o si se agotaron las iteraciones disponibles
        if (resultado) {
            System.out.println("ÉXITO en " + numIt + " intentos");

            // Lista de nodos del árbol
            List<TreeNode<EstadoLaberinto>> collect = TreeStream.depthFirst(TreeNode.treeDef(), copiaArbol)
                    .collect(Collectors.toList());
            // Filtrado del nodo solución, el que no tiene hijos
            TreeNode<EstadoLaberinto> sol = TreeDef.filteredList(collect, nodo -> nodo.getChildren().size() == 0).get(0);

            System.out.println(new Laberinto.Representable(sol.getContent().getPosVisitadas()));
            System.out.println(copiaArbol.toStringDeep());
        } else {
            System.out.println("NO ENCONTRÓ SOLUCIÓN en " + NUM_ITERACIONES + " intentos\nPosiblemente no tenga solución");
        }

    }

    /**
     * Función sumergida del método {@link GeneracionYPrueba#ejecutar()}
     *
     * @param nodo Árbol de decisión de entrada
     * @return Si la iteración encontró una solución
     */
    private boolean ejecutar(TreeNode<EstadoLaberinto> nodo) {
        // Variables del estado del laberinto actual
        EstadoLaberinto estadoLaberinto = nodo.getContent();
        List<Posicion> posVisitadas = estadoLaberinto.getPosVisitadas();
        ControladorMovimiento cMov = estadoLaberinto.getJugador().controladorMovimiento();

        // Variables auxiliares
        Jugador clon;                                       // Copia del jugador para el siguiente nodo del árbol
        int nuevoUmbral;                                    // Umbral actualizado
        List<Posicion> visitadas;                           // Nueva lista de visitados

        // Comprueba si la casilla actual es el objetivo
        if (!Laberinto.recuperarInstancia().casilla(cMov.getPosicion()).esObjetivo()) {
            // Selección de operando
            Posicion posDestino = seleccionarOperando(nodo);
            // Actualización del umbral. Si no hay posición disponible se actualiza a -1 para detener la iteración
            nuevoUmbral = posDestino != null ? estadoLaberinto.getUmbral() - actualizarUmbral(posDestino) : -1;

            // Determina si debe seguir la iteración. El nuevo umbral no debe superar el disponible
            if (nuevoUmbral >= 0) {
                // El clon realiza el movimiento elegido
                clon = (Jugador) estadoLaberinto.getJugador().clone();
                clon.controladorMovimiento().setPosicionAbsoluta(posDestino);

                // Añade la posición de destino a las visitadas
                visitadas = new ArrayList<>(posVisitadas);
                visitadas.add(posDestino);

                return ejecutar(new TreeNode<>(nodo, new EstadoLaberinto(clon, visitadas, nuevoUmbral)));
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
        ControladorMovimiento cMov = estadoLaberinto.getJugador().controladorMovimiento();

        Posicion posDestino = null;

        /*
         Obtiene los movimientos posibles a partir de la posición actual y filtra aquellas que no resulten
         en una posición ya visitada. Después ordena los movimientos por costes
         */
        List<Posicion> posPosibles = cMov.movimientosPosibles()
                .stream()
                .map(cMov::aplicarMovimiento)
                .filter(posicion -> !posVisitadas.contains(posicion))
                .sorted(Comparator.comparingInt(p -> Laberinto.recuperarInstancia().casilla(p).getValor()))
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
     * @return Imposible
     * @throws UnsupportedOperationException El algoritmo de Generación Y Prueba no emplea ninguna heurística
     */
    @Override
    protected Function<EstadoLaberinto, Integer> getHeuristica() {
        throw new UnsupportedOperationException("El algoritmo \"" + getClass().getName() +
                "\" no utiliza funciones heurísticas");
    }
}
