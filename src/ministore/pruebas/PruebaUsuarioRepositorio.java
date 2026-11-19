package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Usuario;
import ministore.repositorio.UsuarioRepositorio;

/**
 * Prueba de UsuarioRepositorio (versión MySQL). Se ejecuta con Shift + F6.
 * Solo CONSULTA: no cambia los datos (no hace falta correr el script 02).
 *
 * @author Mauricio
 */
public class PruebaUsuarioRepositorio {

    public static void main(String[] args) {

        UsuarioRepositorio usuarios = new UsuarioRepositorio();

        // 1. Listar: los 9 usuarios activos del script 02 (camilo está desactivado).
        System.out.println("--- Prueba 1: listar ---");
        for (Usuario u : usuarios.listar()) {
            System.out.println(u.getId() + ". " + u.getNombreUsuario() + " - " + u.obtenerInformacion());
        }

        // 2. Buscar por nombre de usuario: el que existe, el desactivado y uno que no existe.
        System.out.println("--- Prueba 2: buscar por nombre de usuario ---");
        Usuario carlos = usuarios.buscarPorNombreUsuario("carlos");
        System.out.println("carlos: " + (carlos != null ? carlos.obtenerInformacion() : "no existe"));
        Usuario camilo = usuarios.buscarPorNombreUsuario("camilo");
        System.out.println("camilo activo: " + (camilo != null ? camilo.isActivo() : "no existe"));   // false
        System.out.println("pedro: " + usuarios.buscarPorNombreUsuario("pedro"));                       // null

        // 3. La contraseña: el hash de MySQL y el de Java deben coincidir.
        System.out.println("--- Prueba 3: verificar la contraseña ---");
        System.out.println("carlos123: " + carlos.verificarContrasena("carlos123"));   // true
        System.out.println("Carlos123: " + carlos.verificarContrasena("Carlos123"));   // false

        // 4. El rol llega con el JOIN.
        System.out.println("--- Prueba 4: el rol ---");
        Usuario admin = usuarios.buscarPorId(1);
        System.out.println(admin.getNombreUsuario() + " es administrador: " + admin.esAdministrador());   // true
        System.out.println("carlos es administrador: " + carlos.esAdministrador());                        // false
    }
}