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

        jugador.controladorMovimiento().mover(Direccion.ESTE, 5);
        System.out.println(jugador);

        jugador.controladorMovimiento().mover(Direccion.SUR, 3);
        System.out.println(jugador);

        jugador.controladorMovimiento().mover(Direccion.ESTE, 2);
        System.out.println(jugador);

        jugador.controladorMovimiento().mover(Direccion.NORTE, 1);
        System.out.println(jugador);

        // <- TESTING
    }
}
