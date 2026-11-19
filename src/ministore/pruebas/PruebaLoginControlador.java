package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.controlador.LoginControlador;
import ministore.repositorio.UsuarioRepositorio;

/**
 * Prueba de LoginControlador con MySQL, SIN ventana. Se ejecuta con Shift + F6.
 * Solo CONSULTA: no cambia los datos.
 *
 * @author Mauricio
 */
public class PruebaLoginControlador {

    public static void main(String[] args) {

        UsuarioRepositorio usuarios = new UsuarioRepositorio();
        LoginControlador login = new LoginControlador(usuarios);

        // 1. Datos vacíos.
        System.out.println(login.iniciarSesion("", "carlos123"));
        System.out.println(login.iniciarSesion("carlos", ""));

        // 2. Usuario que no existe y contraseña equivocada: el MISMO mensaje.
        System.out.println(login.iniciarSesion("pedro", "pedro123"));
        System.out.println(login.iniciarSesion("carlos", "Carlos123"));

        // 3. Usuario desactivado (camilo), aunque su contraseña es correcta.
        System.out.println(login.iniciarSesion("camilo", "camilo123"));
        System.out.println("¿Alguien entró? " + (login.getUsuarioActual() != null));     // false

        // 4. Datos correctos: un vendedor y un administrador.
        System.out.println(login.iniciarSesion("carlos", "carlos123"));
        System.out.println("¿Es administrador? " + login.getUsuarioActual().esAdministrador());   // false
        System.out.println(login.iniciarSesion("admin", "admin123"));
        System.out.println("¿Es administrador? " + login.getUsuarioActual().esAdministrador());   // true

        // 5. Cerrar sesión.
        login.cerrarSesion();
        System.out.println("¿Alguien entró? " + (login.getUsuarioActual() != null));     // false
    }
}