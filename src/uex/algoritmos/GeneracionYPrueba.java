package uex.algoritmos;

import com.diffplug.common.base.TreeDef;
import com.diffplug.common.base.TreeNode;
import com.diffplug.common.base.TreeStream;
import uex.Jugador;
import uex.Laberinto;
import uex.heuristicas.Heuristica;
import uex.movimiento.ControladorMovimiento;
import uex.movimiento.Posicion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO Documentar
 */
public class GeneracionYPrueba extends ExpansorArbol {

    private static final int NUM_ITERACIONES = 25;          // Número máximo de intentos
    private final int NUM_MOV_PRUEBA;                       // Número de movimientos hasta la prueba heurística
    private final int UMBRAL_HEURISTICO;                    // Umbral para determinar si merece la pena seguir con la iteración

    private int numMov;                                     // Número de movimientos dados por el algoritmo en un momento dado

    /**
     * @param heuristica Heurística a emplear
     */
    public GeneracionYPrueba(Heuristica heuristica) {
        super(heuristica);

        // Ponderado en lo que consume de media el algoritmo en costes cuando ha realizado la mitad de los pasos medios
        NUM_MOV_PRUEBA = (int) (Laberinto.instancia().getUmbral() / 1.5 / 5);
        // Ponderado en los pasos que da de media en algoritmo hasta llegar al cuadrante de la posición objetivo
        UMBRAL_HEURISTICO = Laberinto.instancia().getDimension() / 2;
    }

    /**
     * Implementa el algoritmo de búsqueda con información "Generación y Prueba". Ordena los operadores disponibles
     * por coste. El coste de un movimiento es el valor asociado a la casilla de la posición destino.
     */
    @Override
    public void resolver() {
        TreeNode<EstadoLaberinto> copiaArbol;               // Copia del padre para cada iteración del algoritmo
        boolean exito;                                      // Resultado de la última iteración. Determina el éxito
        int numIt = 0;                                      // Número de iteración

        setContNodosGen(0);                                 // Reinicia el número de nodos generados
        setNumMov(0);                                       // Reinicia el número de movimientos realizados

        // Realiza hasta un cierto número de iteraciones intentando encontrar una solución
        getReloj().start();
        do {
            /* REINICIO DE VARIABLES ENTRE ITERACIONES */

            setNumMov(0);                                   // Reinicia el número de movimientos realizados en cada it.
            copiaArbol = getArbolDecision().copy();         // Reinicia la copia del nodo raíz inicial en cada it.

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
                    "Posiblemente no tenga solución o se rechazó la solución parcial\n");
        }
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
            /* Actualización del umbral. Si no hay posición disponible se actualiza a un valor mayor que el umbral
            para detener la iteración */
            nuevoUmbral = estadoLaberinto.getUmbral() + costeAsociado(posDestino);
            setContNodosGen(getContNodosGen() + 1);         // Incrementa en 1 el número de nodos generados

            // Determina si el nuevo umbral no supera el disponible
            if (nuevoUmbral < Laberinto.instancia().getUmbral()) {
                // Comrprobación heurística de si merece la pena seguir evaluando esta iteración
                if (!pruebaHeuristica(nodo)) return false;

                // El clon realiza el movimiento elegido
                clon = (Jugador) estadoLaberinto.getJugador().clone();
                clon.ctrlMovimiento().setPosicionAbsoluta(posDestino);

                // Añade la posición de destino a las visitadas
                visitadas = new ArrayList<>(posVisitadas);
                visitadas.add(posDestino);

                setNumMov(getNumMov() + 1);                 // Incrementa el número de movientos realizados

                return resolver(new TreeNode<>(nodo, new EstadoLaberinto(clon, visitadas, nuevoUmbral)));
            } else return false;
        } else {
            return true;                                    // Ha encontrado la solución
        }
    }

    /**
     * Comprueba si merece la pena seguir explorando la solución actual. Cuando el algoritmo ha realizado {@code numMov}
     * movimientos comprueba si está bien encaminado, que en este caso significa comprobar si se encuentra en el
     * cuadrante de la posición objetivo. El laberinto está dividido en 4 cuadrantes iguales que resultan de dividirlo a
     * la mitad vertical y horizontalmente
     *
     * @param nodo Nodo parte de la solución siendo evaluado
     * @return Si merece la pena seguir explorando esta solución
     */
    private boolean pruebaHeuristica(TreeNode<EstadoLaberinto> nodo) {
        return getNumMov() != NUM_MOV_PRUEBA || aplicarHeuristica(nodo).intValue() <= UMBRAL_HEURISTICO;
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
        System.out.println("Número de nodos generados : " + getContNodosGen());
        // Representación del camino solución
        System.out.println(new Laberinto.Solucionado(sol.getContent().getPosVisitadas(), sol.getContent().getUmbral()));
        // Secuencia de estados. Representación de la expansión
        System.out.println(arbolDecision.toStringDeep() + "\n");
    }

    /**
     * @return Número de movimientos dados por el algoritmo en un momento dado
     */
    private int getNumMov() {
        return numMov;
    }

    /**
     * @param numMov Nuevo número de movimientos
     */
    private void setNumMov(int numMov) {
        this.numMov = numMov;
    }
}
