import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class Simulador {

    public static void main(String[] args) {
        // TESTING ->

        CargadorLaberinto cargador = null;
        Jugador jugador = new Jugador();

        try {
            cargador = new CargadorLaberinto(new File("res/laberintos"), new CasillaParser());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(cargador).cargarSiguienteLaberinto();
        System.out.println(Laberinto.recuperarInstancia());

        System.out.println(Laberinto.recuperarInstancia().casilla(new Posicion(0, 0)));

        jugador.controladorMovimiento().setPosicionAbsoluta(new Posicion(1, 1));
        jugador.controladorMovimiento().setPosicionRelativa(new MovimientoUnitario(Direccion.SUR));
        System.out.println(jugador);
        System.out.println(jugador.controladorMovimiento().movimientosPosibles());

        // <- TESTING
    }
}
