package uex.algoritmos;

import uex.Jugador;
import uex.movimiento.Posicion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que modela un estado del laberinto tras realizar una acción sobre él. Mantiene la definición del laberinto
 * estable durante la ejecución de un algoritmo guardando el estado del jugador, una lista de posiciones visitadas y el
 * umbral restante
 */
public class EstadoLaberinto {

    private Jugador jugador;                        // Estado del jugador
    private List<Posicion> posVisitadas;            // Lista de posiciones visitadas
    private int umbral;                             // Valor restante del umbral

    /**
     * @param jugador      Estado del jugador
     * @param posVisitadas Lista de posiciones visitadas
     * @param umbral       Valor restante del umbral
     */
    public EstadoLaberinto(Jugador jugador, List<Posicion> posVisitadas, int umbral) {
        this.jugador = jugador;
        this.posVisitadas = posVisitadas;
        this.umbral = umbral;
    }

    /**
     * @return Estado inicial de un laberinto inexplorado
     */
    public static EstadoLaberinto estadoInicial() {
        // Nuevo jugador
        Jugador jugador = new Jugador();
        // Añade la posición inicial a las visitadas
        List<Posicion> posVisitadas = new ArrayList<>(Collections.singletonList(jugador.ctrlMovimiento().getPosicion()));

        return new EstadoLaberinto(jugador, posVisitadas, 0);
    }

    /**
     * @return Estado del jugador
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * @return Lista de posiciones visitadas
     */
    public List<Posicion> getPosVisitadas() {
        return posVisitadas;
    }

    /**
     * @return Valor restante del umbral
     */
    public int getUmbral() {
        return umbral;
    }

    protected void setUmbral(int umbral) {
        this.umbral = umbral;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n-------------------------------------------------------------\n");

        stringBuilder.append(getJugador()).append("\n");
        stringBuilder.append("Posiciones visitadas : \n");
        getPosVisitadas().forEach(pos -> stringBuilder.append("\t").append(pos).append("\n"));
        stringBuilder.append("Umbral : ").append(getUmbral()).append("\n");

        stringBuilder.append("---------------------------------------------------------------\n");

        return stringBuilder.toString();
    }
}
