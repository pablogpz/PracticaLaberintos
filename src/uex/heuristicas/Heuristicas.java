package uex.heuristicas;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Utilidades relacionadas con el tratamiento de las implementaciones de las funciones heurísticas
 */
public class Heuristicas {

    private static Heuristicas instancia;                   // Instancia Singleton

    private Collection<Heuristica> heuristicas;             // Colección de funciones heurísticas

    private Heuristicas() {
        heuristicas = new ArrayList<>();
        inicializarHeuristicas();
    }

    /**
     * @return Iterador de todas las heurísticas implementadas
     */
    public static Iterator<Heuristica> itHeuristicas() {
        return Heuristicas.instancia().heuristicas.iterator();
    }

    private static Heuristicas instancia() {
        if (instancia == null)
            instancia = new Heuristicas();

        return instancia;
    }

    /**
     * Inicializa la colección de implementaciones de heurísticas
     */
    private void inicializarHeuristicas() {
        // Inicializaciones manuales
        inicializarHeuristicas_manualmente();

        // Colección de implementaciones heurísticas conocidas
        Set<Class<? extends Heuristica>> implHeuristicas = new Reflections().getSubTypesOf(Heuristica.class);

        // Elimina las implementaciones heurísticas añadidas manualmente
        heuristicas.stream().map(Heuristica::getClass).forEach(implHeuristicas::remove);

        // Añade el resto de implementaciones heurísticas automáticamente
        for (Class<? extends Heuristica> implHeuristica : implHeuristicas) {
            try {
                heuristicas.add(implHeuristica.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inicializaciones manuales de implementaciones heurísticas
     */
    private void inicializarHeuristicas_manualmente() {
        heuristicas.add(DistanciaAlObjetivo.tipo(DistanciaAlObjetivo.Calculo.DISCRETA));
        heuristicas.add(DistanciaAlObjetivo.tipo(DistanciaAlObjetivo.Calculo.REAL));
    }
}
