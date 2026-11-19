/*
 * =========================================================================
 *  CLASE: UsuarioRepositorio              PAQUETE: ministore.repositorio
 *  VERSIÓN: MySQL
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es la bodega de los usuarios: los consulta en la tabla usuarios de
 *    MySQL. Por ahora tiene lo que necesita el LOGIN (RF2): buscar un
 *    usuario por su nombre de usuario. Guardar, actualizar y desactivar
 *    se agregan cuando se construya VentanaUsuarios (RF1).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ConexionBD    → para abrir la conexión con MySQL.
 *      - Usuario y Rol → convierte cada fila en un objeto Usuario con su Rol.
 *    La usan:
 *      - LoginControlador → y, a través de él, VentanaLogin.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - El SEGUNDO constructor de Usuario (el que recibe el hash).
 *    - Las tablas usuarios y roles, creadas y llenadas con los scripts 01 y 02.
 *
 *  LOS MÉTODOS Y SU SQL:
 *      listar                 →  SELECT ... JOIN roles ... WHERE activo = TRUE
 *      buscarPorId            →  SELECT ... WHERE u.id = ?
 *      buscarPorNombreUsuario →  SELECT ... WHERE u.nombre_usuario = ?
 * =========================================================================
 */
package ministore.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import ministore.modelo.Rol;
import ministore.modelo.Usuario;
import ministore.util.ConexionBD;

/**
 * La bodega de usuarios, guardada en MySQL.
 *
 * @author Mauricio
 */
public class UsuarioRepositorio {

    // El JOIN trae, en la misma fila, el nombre del rol del usuario.
    // r.nombre se renombra (AS rol_nombre) para no confundirlo con nombre_completo.
    private static final String SELECT_BASE =
            "SELECT u.id, u.nombre_usuario, u.contrasena_hash, u.nombre_completo, u.activo, "
          + "r.id AS rol_id, r.nombre AS rol_nombre "
          + "FROM usuarios u "
          + "JOIN roles r ON r.id = u.rol_id ";

    public UsuarioRepositorio() {
    }

    // =====================================================================
    // R: READ
    // =====================================================================

    // Solo los usuarios activos, ordenados por id.
    public ArrayList<Usuario> listar() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE u.activo = TRUE ORDER BY u.id";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {                              // Una vuelta por cada fila
                lista.add(crearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

    // Busca por id. Devuelve el usuario o null si no existe.
    public Usuario buscarPorId(int id) {
        String sql = SELECT_BASE + "WHERE u.id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el usuario: " + e.getMessage(), e);
        }
        return null;
    }

    // Busca por nombre de usuario ("carlos"). Devuelve el usuario o null.
    // Es el método del LOGIN. Devuelve también los desactivados:
    // el controlador decide qué hacer con ellos.
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        String sql = SELECT_BASE + "WHERE u.nombre_usuario = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el usuario: " + e.getMessage(), e);
        }
        return null;
    }

    // =====================================================================
    // MÉTODO AYUDANTE (private)
    // =====================================================================

    // Convierte UNA FILA en UN OBJETO Usuario (con su Rol).
    // Usa el SEGUNDO constructor: la contraseña ya llega como hash.
    private Usuario crearUsuario(ResultSet rs) throws SQLException {
        Rol rol = new Rol(rs.getInt("rol_id"), rs.getString("rol_nombre"));
        return new Usuario(rs.getInt("id"), rs.getString("nombre_usuario"),
                rs.getString("contrasena_hash"), rs.getString("nombre_completo"),
                rol, rs.getBoolean("activo"));
    }
}