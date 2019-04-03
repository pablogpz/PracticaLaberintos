import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class Simulador {

    public static void main(String[] args) {
        // TESTING ->

        CargadorLaberinto cargador = null;
        ExpansorArbol generacionYPrueba;

        try {
            cargador = new CargadorLaberinto(new File("res/laberintos"), new CasillaParser());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Carga el primer laberinto
        Objects.requireNonNull(cargador).cargarSiguienteLaberinto();
        generacionYPrueba = new GeneracionYPrueba();

        //noinspection ConstantConditions
        for (int i = 0; i < cargador.getCarpetaLaberintos().listFiles().length - 1; i++) {
            Objects.requireNonNull(cargador).cargarSiguienteLaberinto();
            System.out.println(Laberinto.instancia());

            generacionYPrueba.ejecutar();
        }

        // <- TESTING
    }
}
