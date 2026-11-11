package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Rol;
import ministore.modelo.Usuario;
import ministore.util.Seguridad;

/**
 * Prueba de Usuario (capa modelo), junto con Rol y Seguridad, de las que depende.
 * Se ejecuta con Shift + F6.
 * Comprueba los roles, el hash de las contraseñas, el inicio de sesión
 * y las validaciones.
 *
 * @author Mauricio
 */
public class PruebaUsuarioModelo {

    public static void main(String[] args) {

        System.out.println("--- Prueba 1: los dos roles ---");
        Rol administrador = new Rol(1, "Administrador");
        Rol vendedor = new Rol(2, "Vendedor");
        System.out.println(administrador.getId() + " - " + administrador.getNombre());   // 1 - Administrador
        System.out.println(vendedor.getId() + " - " + vendedor.getNombre());             // 2 - Vendedor

        System.out.println("--- Prueba 2: el hash de una contraseña ---");
        // static: se usa con el nombre de la clase, sin crear un objeto con new.
        String hash = Seguridad.hashSHA256("admin123");
        System.out.println(hash);   // 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
        System.out.println("Caracteres: " + hash.length());                              // 64
        System.out.println("¿Mismo texto, mismo hash? "
                + hash.equals(Seguridad.hashSHA256("admin123")));                        // true
        // Cambiar UNA letra produce un hash completamente distinto:
        System.out.println(Seguridad.hashSHA256("Admin123"));   // 3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2

        System.out.println("--- Prueba 3: crear usuarios ---");
        Usuario laura = new Usuario("admin", "admin123", "Laura Martínez", administrador);
        Usuario carlos = new Usuario("carlos", "carlos123", "Carlos Gómez", vendedor);
        System.out.println(laura.obtenerInformacion());     // Laura Martínez (Administrador)
        System.out.println(carlos.obtenerInformacion());    // Carlos Gómez (Vendedor)

        System.out.println("--- Prueba 4: verificar la contraseña (el login) ---");
        System.out.println("admin123: " + laura.verificarContrasena("admin123"));     // true
        System.out.println("Admin123: " + laura.verificarContrasena("Admin123"));     // false: distingue mayúsculas
        System.out.println("vacía: " + laura.verificarContrasena(""));                // false
        System.out.println("con espacio: " + laura.verificarContrasena("admin123 ")); // false: el espacio cuenta

        System.out.println("--- Prueba 5: ¿quién es administrador? ---");
        System.out.println("Laura: " + laura.esAdministrador());    // true
        System.out.println("Carlos: " + carlos.esAdministrador());  // false

        System.out.println("--- Prueba 6: validaciones ---");
        carlos.setNombreUsuario("");                                  // Vacío: se rechaza.
        System.out.println("Usuario: " + carlos.getNombreUsuario());  // carlos
        Usuario vacio = new Usuario("", "clave1", "", vendedor);      // El constructor también valida.
        System.out.println("Usuario: " + vacio.getNombreUsuario());   // sinusuario
        System.out.println("Nombre: " + vacio.getNombreCompleto());   // Sin nombre

        System.out.println("--- Prueba 7: desactivar un usuario ---");
        carlos.setActivo(false);
        System.out.println("¿Activo? " + carlos.isActivo());                              // false
        System.out.println("¿Clave correcta? " + carlos.verificarContrasena("carlos123")); // true (ver nota)
    }
}