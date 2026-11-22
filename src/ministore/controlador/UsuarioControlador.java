/*
 * =========================================================================
 *  CLASE: UsuarioControlador              PAQUETE: ministore.controlador
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es el intermediario entre VentanaUsuarios y los repositorios.
 *    Recibe los TEXTOS que escribió el administrador, los VALIDA y, si
 *    todo está bien, usa el repositorio. Devuelve un MENSAJE para la
 *    ventana. Cubre el RF1 (gestión de usuarios, solo el administrador).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - UsuarioRepositorio → guardar, desactivar y listar usuarios.
 *      - RolRepositorio     → los roles para cmbRol.
 *      - Usuario y Rol (modelo).
 *    La usan:
 *      - VentanaUsuarios (y la crea Aplicacion, en abrirUsuarios).
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - UsuarioRepositorio con guardar y desactivar (probados).
 *    - RolRepositorio (probado).
 *
 *  LAS REGLAS:
 *    - El usuario, la contraseña y el nombre completo son obligatorios.
 *    - La contraseña tiene mínimo 6 caracteres. Esta regla está AQUÍ y no en
 *      Usuario, porque en Usuario la contraseña solo existe dentro del
 *      constructor: de inmediato se convierte en hash.
 *    - No se repite el nombre de usuario (tampoco el de uno desactivado).
 *    - El administrador no puede desactivarse a sí mismo: se quedaría
 *      sin poder entrar.
 *
 *  PENDIENTE (trabajo a futuro / ejercicio): actualizarUsuario.
 *    Ver el comentario al final de la clase.
 *
 *  REGLA DE MVC: no conoce botones ni campos de texto. Recibe y devuelve datos.
 * =========================================================================
 */
package ministore.controlador;

import java.util.ArrayList;
import ministore.modelo.Rol;
import ministore.modelo.Usuario;
import ministore.repositorio.RolRepositorio;
import ministore.repositorio.UsuarioRepositorio;

/**
 * Controlador de usuarios.
 *
 * @author Mauricio
 */
public class UsuarioControlador {

    // =====================================================================
    // ATRIBUTOS
    // Los repositorios llegan de afuera (los crea Aplicacion UNA sola vez).
    // =====================================================================

    private static final int MINIMO_CONTRASENA = 6;   // La regla en un solo lugar.

    private UsuarioRepositorio usuarios;
    private RolRepositorio roles;
    private Usuario usuarioActual;   // El administrador que inició sesión.
                                     // Sirve para que no se desactive a sí mismo.

    // =====================================================================
    // CONSTRUCTOR
    // Recibe el usuario que inició sesión, igual que VentaControlador
    // recibe al vendedor: Aplicacion se lo pasa en abrirUsuarios(usuario).
    // =====================================================================

    public UsuarioControlador(UsuarioRepositorio usuarios, RolRepositorio roles, Usuario usuarioActual) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.usuarioActual = usuarioActual;
    }

    // =====================================================================
    // CONSULTAS
    // =====================================================================

    // Los usuarios activos: para llenar tblUsuarios.
    public ArrayList<Usuario> listarUsuarios() {
        return usuarios.listar();
    }

    // Los roles (Administrador, Vendedor): para llenar cmbRol, en ese orden.
    public ArrayList<Rol> listarRoles() {
        return roles.listar();
    }

    // =====================================================================
    // C: GUARDAR un usuario nuevo  →  botón Guardar
    // Recibe los textos del formulario y la POSICIÓN elegida en cmbRol.
    // Cada if termina con return: al primer problema se devuelve el
    // mensaje y no se guarda nada.
    // =====================================================================

    public String guardarUsuario(String nombreUsuario, String contrasena, String nombreCompleto, int indiceRol) {
        // 1. Datos obligatorios. La contraseña NO lleva trim(): un espacio
        //    puede ser parte de una contraseña.
        if (nombreUsuario.trim().isEmpty() || contrasena.isEmpty() || nombreCompleto.trim().isEmpty()) {
            return "El usuario, la contraseña y el nombre completo son obligatorios";
        }

        // 2. La contraseña: mínimo 6 caracteres.
        if (contrasena.length() < MINIMO_CONTRASENA) {
            return "La contraseña debe tener mínimo " + MINIMO_CONTRASENA + " caracteres";
        }

        // 3. El rol: la posición debe existir en la lista (-1 = nada elegido).
        ArrayList<Rol> lista = roles.listar();
        if (indiceRol < 0 || indiceRol >= lista.size()) {
            return "Seleccione un rol";
        }

        // 4. El nombre de usuario no se repite. buscarPorNombreUsuario también
        //    encuentra a los desactivados: su nombre sigue ocupado en MySQL.
        if (usuarios.buscarPorNombreUsuario(nombreUsuario.trim()) != null) {
            return "Ya existe un usuario con ese nombre";
        }

        // 5. Todo bien. El PRIMER constructor de Usuario recibe la contraseña
        //    y la convierte en hash: la contraseña nunca llega a MySQL.
        Usuario nuevo = new Usuario(nombreUsuario.trim(), contrasena, nombreCompleto.trim(), lista.get(indiceRol));
        usuarios.guardar(nuevo);
        return "Usuario guardado";
    }

    // =====================================================================
    // D: DESACTIVAR  →  botón Desactivar
    // Recibe el id del usuario elegido en la tabla (-1 si no hay ninguno).
    // =====================================================================

    public String desactivarUsuario(int id) {
        Usuario u = usuarios.buscarPorId(id);
        if (u == null) {
            return "Seleccione un usuario de la tabla";                // La ventana envía -1
        }
        if (u.getId() == usuarioActual.getId()) {                      // Regla del negocio
            return "No puede desactivar su propio usuario";
        }
        usuarios.desactivar(id);
        return "Usuario desactivado";
    }

    // =====================================================================
    // U: ACTUALIZAR  →  PENDIENTE (trabajo a futuro / ejercicio)
    // El mockup de VentanaUsuarios no tiene botón Actualizar. Si se agrega:
    //   public String actualizarUsuario(int id, String nombreCompleto,
    //                                   String contrasena, int indiceRol)
    //   1. Buscar el usuario por id (si es null: "Seleccione un usuario de la tabla").
    //   2. Validar el nombre completo y el rol, como en guardarUsuario.
    //   3. Si la contraseña viene VACÍA, se conserva el hash anterior;
    //      si no, debe tener mínimo 6 caracteres.
    //   4. Armar un Usuario con el SEGUNDO constructor (el que recibe el hash)
    //      y llamar a usuarios.actualizar(...) (también pendiente).
    // =====================================================================
}