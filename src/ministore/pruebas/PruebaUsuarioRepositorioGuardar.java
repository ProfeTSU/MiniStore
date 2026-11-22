package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Rol;
import ministore.modelo.Usuario;
import ministore.repositorio.RolRepositorio;
import ministore.repositorio.UsuarioRepositorio;

/**
 * Prueba de los métodos NUEVOS de UsuarioRepositorio: guardar y desactivar.
 * Se ejecuta con Shift + F6.
 *
 * ¡OJO! Esta prueba CAMBIA los datos: crea el usuario pedro y lo desactiva.
 * Antes de ejecutarla, correr el script 02 en Workbench (si pedro ya existe,
 * MySQL no deja guardarlo otra vez: el nombre de usuario es UNIQUE).
 *
 * @author Mauricio
 */
public class PruebaUsuarioRepositorioGuardar {

    public static void main(String[] args) {

        UsuarioRepositorio usuarios = new UsuarioRepositorio();
        Rol vendedor = new RolRepositorio().listar().get(1);                   // Posición 1 = Vendedor

        // 1. Guardar: el PRIMER constructor recibe la contraseña y calcula el hash.
        System.out.println("--- Prueba 1: guardar ---");
        System.out.println("Activos antes: " + usuarios.listar().size());      // 9
        Usuario pedro = new Usuario("pedro", "pedro123", "Pedro Salazar", vendedor);
        usuarios.guardar(pedro);
        System.out.println("Id asignado por MySQL: " + pedro.getId());         // 11
        System.out.println("Activos después: " + usuarios.listar().size());    // 10

        // 2. Leerlo de MySQL: ¿quedó bien guardado el hash?
        //    Se busca otra vez: así se lee de MySQL y no del objeto.
        System.out.println("--- Prueba 2: leerlo de MySQL ---");
        Usuario leido = usuarios.buscarPorNombreUsuario("pedro");
        System.out.println(leido.obtenerInformacion());                        // Pedro Salazar (Vendedor)
        System.out.println("pedro123: " + leido.verificarContrasena("pedro123"));   // true

        // 3. Desactivar: pedro y un id que no existe.
        System.out.println("--- Prueba 3: desactivar ---");
        System.out.println("¿Se desactivó pedro? " + usuarios.desactivar(pedro.getId()));   // true
        System.out.println("¿Se desactivó el 99? " + usuarios.desactivar(99));               // false
        System.out.println("Activos: " + usuarios.listar().size());                         // 9
        System.out.println("pedro activo: " + usuarios.buscarPorNombreUsuario("pedro").isActivo());   // false
    }
}