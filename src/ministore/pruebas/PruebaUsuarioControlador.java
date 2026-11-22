package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.controlador.UsuarioControlador;
import ministore.modelo.Rol;
import ministore.modelo.Usuario;
import ministore.repositorio.RolRepositorio;
import ministore.repositorio.UsuarioRepositorio;

/**
 * Prueba de UsuarioControlador con MySQL, SIN ventana. Se ejecuta con Shift + F6.
 * Hace lo mismo que hará VentanaUsuarios, pero escribiendo los datos a mano.
 *
 * ¡OJO! Esta prueba CAMBIA los datos: crea el usuario pedro y lo desactiva.
 * Antes de ejecutarla, correr el script 02 en Workbench.
 *
 * @author Mauricio
 */
public class PruebaUsuarioControlador {

    public static void main(String[] args) {

        // Lo mismo que hará Aplicacion: los repositorios y el administrador que entró.
        UsuarioRepositorio usuarios = new UsuarioRepositorio();
        Usuario admin = usuarios.buscarPorNombreUsuario("admin");
        UsuarioControlador controlador = new UsuarioControlador(usuarios, new RolRepositorio(), admin);

        // 1. Los roles de cmbRol: posición 0 = Administrador, 1 = Vendedor.
        System.out.println("--- 1. Los roles ---");
        for (Rol r : controlador.listarRoles()) {
            System.out.println(r.getNombre());
        }

        // 2. Validaciones de guardar (lo que escribiría el administrador).
        System.out.println("--- 2. Validaciones de guardar ---");
        System.out.println(controlador.guardarUsuario("", "pedro123", "Pedro Salazar", 1));          // usuario vacío
        System.out.println(controlador.guardarUsuario("pedro", "abc", "Pedro Salazar", 1));          // contraseña corta
        System.out.println(controlador.guardarUsuario("pedro", "pedro123", "Pedro Salazar", -1));    // sin rol
        System.out.println(controlador.guardarUsuario("carlos", "carlos456", "Otro Carlos", 1));     // repetido
        System.out.println(controlador.guardarUsuario("camilo", "camilo456", "Otro Camilo", 1));     // repetido (desactivado)

        // 3. Guardar con datos correctos (con espacios: trim).
        System.out.println("--- 3. Guardar ---");
        System.out.println(controlador.guardarUsuario("  pedro ", "pedro123", "Pedro Salazar", 1));
        System.out.println("Usuarios activos: " + controlador.listarUsuarios().size());             // 10

        // 4. Desactivar: sin selección, a sí mismo y a pedro.
        System.out.println("--- 4. Desactivar ---");
        Usuario pedro = usuarios.buscarPorNombreUsuario("pedro");
        System.out.println(controlador.desactivarUsuario(-1));
        System.out.println(controlador.desactivarUsuario(admin.getId()));
        System.out.println(controlador.desactivarUsuario(pedro.getId()));
        System.out.println("Usuarios activos: " + controlador.listarUsuarios().size());             // 9
    }
}