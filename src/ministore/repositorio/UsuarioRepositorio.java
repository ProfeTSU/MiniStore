/*
 * =========================================================================
 *  CLASE: UsuarioRepositorio              PAQUETE: ministore.repositorio
 *  VERSIÓN: MySQL
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es la bodega de los usuarios, guardados en la tabla usuarios de MySQL.
 *    Tiene lo que necesita el LOGIN (RF2): buscar un usuario por su nombre
 *    de usuario. Y lo que necesita VentanaUsuarios (RF1): guardar un
 *    usuario nuevo y desactivarlo.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ConexionBD    → para abrir la conexión con MySQL.
 *      - Usuario y Rol → convierte cada fila en un objeto Usuario con su Rol.
 *    La usan:
 *      - LoginControlador   → y, a través de él, VentanaLogin.
 *      - UsuarioControlador → y, a través de él, VentanaUsuarios.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - El SEGUNDO constructor de Usuario (el que recibe el hash).
 *    - Las tablas usuarios y roles, creadas y llenadas con los scripts 01 y 02.
 *
 *  LOS MÉTODOS Y SU SQL:
 *      listar                 →  SELECT ... JOIN roles ... WHERE activo = TRUE
 *      buscarPorId            →  SELECT ... WHERE u.id = ?
 *      buscarPorNombreUsuario →  SELECT ... WHERE u.nombre_usuario = ?
 *      guardar                →  INSERT                                  (NUEVO)
 *      desactivar             →  UPDATE ... SET activo = FALSE           (NUEVO)
 *
 *  PENDIENTE (trabajo a futuro / ejercicio):
 *      actualizar             →  UPDATE ... SET nombre_completo, rol_id, contrasena_hash
 *    Ver el comentario al final de la sección U.
 * =========================================================================
 */
package ministore.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;           // NUEVO: por la constante RETURN_GENERATED_KEYS (guardar).
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
    // C: CREATE  →  guardar  (INSERT)                                 NUEVO
    // Guarda un usuario nuevo. MySQL le asigna el id (AUTO_INCREMENT) y
    // aquí se le pone ese id al objeto.
    // Se guarda el HASH de la contraseña, nunca la contraseña: el objeto
    // ya lo trae calculado (lo hizo el PRIMER constructor de Usuario).
    // =====================================================================

    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena_hash, nombre_completo, rol_id, activo) "
                   + "VALUES (?, ?, ?, ?, ?)";                  // Un ? por cada columna

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasenaHash());       // La HUELLA (64 caracteres)
            ps.setString(3, usuario.getNombreCompleto());
            ps.setInt(4, usuario.getRol().getId());             // El OBJETO Rol se guarda como su id (FK)
            ps.setBoolean(5, usuario.isActivo());               // true: todo usuario nuevo puede entrar
            ps.executeUpdate();

            try (ResultSet llaves = ps.getGeneratedKeys()) {    // El id que asignó MySQL
                if (llaves.next()) {
                    usuario.setId(llaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            // Por ejemplo: un nombre de usuario repetido (la columna es UNIQUE).
            throw new RuntimeException("Error al guardar el usuario: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // D: DELETE  →  desactivar  (UPDATE ... SET activo = FALSE)       NUEVO
    // Eliminación LÓGICA: el usuario no se borra, porque tiene ventas
    // registradas a su nombre (usuario_id en la tabla ventas). Solo deja
    // de poder entrar: el login rechaza a los desactivados.
    // Devuelve true si se desactivó, false si ese id no existe.
    // =====================================================================

    public boolean desactivar(int id) {
        String sql = "UPDATE usuarios SET activo = FALSE WHERE id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;                      // executeUpdate devuelve cuántas filas cambió
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar el usuario: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // U: UPDATE  →  actualizar                PENDIENTE (trabajo a futuro / ejercicio)
    // No se construyó: el mockup de VentanaUsuarios no tiene botón Actualizar.
    // Si se agrega, la idea es:
    //   - SQL: UPDATE usuarios SET nombre_completo = ?, rol_id = ?, contrasena_hash = ? WHERE id = ?
    //   - Usuario no tiene setRol ni setContrasenaHash (a propósito). Por eso el
    //     controlador arma un objeto NUEVO con el SEGUNDO constructor (el que
    //     recibe el hash) y le pasa este objeto a actualizar().
    //   - Si el campo de la contraseña queda vacío, se conserva el hash anterior.
    // =====================================================================

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