/*
 * =========================================================================
 *  CLASE: LoginControlador                PAQUETE: ministore.controlador
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Decide si una persona puede entrar al MiniStore (RF2: acceso seguro).
 *    Recibe el usuario y la contraseña que se escribieron en VentanaLogin,
 *    los VALIDA y, si todo está bien, recuerda quién inició sesión.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:      UsuarioRepositorio y Usuario (verificarContrasena).
 *    La usan:  VentanaLogin (y VentanaPrincipal, para saber quién entró).
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    Usuario (con su segundo constructor) y UsuarioRepositorio.
 *
 *  REGLA DE MVC: no conoce campos de texto ni ventanas. Recibe textos
 *  y devuelve un mensaje.
 * =========================================================================
 */
package ministore.controlador;

import ministore.modelo.Usuario;
import ministore.repositorio.UsuarioRepositorio;

/**
 * Controlador del inicio de sesión.
 *
 * @author Mauricio
 */
public class LoginControlador {

    private UsuarioRepositorio usuarios;     // Llega de afuera: se crea UNA sola vez.
    private Usuario usuarioActual;           // Quien inició sesión (null = nadie).

    public LoginControlador(UsuarioRepositorio usuarios) {
        this.usuarios = usuarios;
    }

    // Intenta iniciar sesión. Devuelve un mensaje para la ventana.
    // Si todo está bien, guarda al usuario en usuarioActual.
    public String iniciarSesion(String nombreUsuario, String contrasena) {
        usuarioActual = null;                                                 // Empieza sin nadie.

        if (nombreUsuario.trim().isEmpty() || contrasena.isEmpty()) {
            return "Escriba el usuario y la contraseña";                      // 1. Datos obligatorios
        }
        Usuario u = usuarios.buscarPorNombreUsuario(nombreUsuario.trim());
        if (u == null || !u.verificarContrasena(contrasena)) {
            return "Usuario o contraseña incorrectos";                        // 2. No se dice cuál falló
        }
        if (!u.isActivo()) {
            return "El usuario está desactivado";                             // 3. Desactivado: no entra
        }
        usuarioActual = u;                                                    // 4. Todo bien
        return "Bienvenido, " + u.getNombreCompleto();
    }

    // Quien inició sesión. La ventana lo usa para saber si entró (null = no).
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // Cierra la sesión: ya no hay nadie.
    public void cerrarSesion() {
        usuarioActual = null;
    }
}