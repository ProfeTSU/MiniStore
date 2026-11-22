package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import java.util.ArrayList;
import ministore.modelo.Rol;
import ministore.repositorio.RolRepositorio;

/**
 * Prueba de RolRepositorio (versión MySQL). Se ejecuta con Shift + F6.
 * Solo CONSULTA: no cambia los datos (no hace falta el script 02).
 *
 * @author Mauricio
 */
public class PruebaRolRepositorio {

    public static void main(String[] args) {

        RolRepositorio roles = new RolRepositorio();

        // 1. Los roles que insertó el script 02, en el orden en que irán en cmbRol.
        System.out.println("--- Prueba 1: listar ---");
        ArrayList<Rol> lista = roles.listar();
        System.out.println("Roles en MySQL: " + lista.size());                // 2
        for (int i = 0; i < lista.size(); i++) {                               // i = la posición en cmbRol
            Rol r = lista.get(i);
            System.out.println("Posición " + i + ": " + r.getId() + " - " + r.getNombre());
        }

        // 2. Lo que hará el controlador: de la POSICIÓN elegida en cmbRol al objeto Rol.
        System.out.println("--- Prueba 2: de la posición al rol ---");
        int posicionElegida = 1;                                               // El administrador eligió "Vendedor"
        Rol elegido = lista.get(posicionElegida);
        System.out.println("Posición 1 → " + elegido.getNombre() + " (id " + elegido.getId() + ")");   // Vendedor (id 2)
    }
}