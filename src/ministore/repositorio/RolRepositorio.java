/*
 * =========================================================================
 *  CLASE: RolRepositorio                  PAQUETE: ministore.repositorio
 *  VERSIÓN: MySQL
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es la bodega de los ROLES (Administrador y Vendedor), guardados en la
 *    tabla roles de MySQL. VentanaUsuarios la necesita para llenar la lista
 *    desplegable cmbRol cuando el administrador crea un usuario (RF1).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ConexionBD → para abrir la conexión con MySQL.
 *      - Rol        → convierte cada fila de la tabla en un objeto.
 *    La usan:
 *      - UsuarioControlador → y, a través de él, VentanaUsuarios.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - ConexionBD (probada con PruebaConexion) y el modelo Rol.
 *    - La tabla roles con sus dos filas (scripts 01 y 02).
 *
 *  ¿POR QUÉ UN SOLO MÉTODO?
 *    Los roles son FIJOS: los crea el script 02 y el programa no crea,
 *    cambia ni elimina roles (por eso Rol no tiene set). Del CRUD solo
 *    queda la R:
 *      listar  →  SELECT id, nombre FROM roles ORDER BY id
 * =========================================================================
 */
package ministore.repositorio;

import java.sql.Connection;          // La conexión abierta con MySQL.
import java.sql.PreparedStatement;   // Una instrucción SQL.
import java.sql.ResultSet;           // Las filas que devuelve un SELECT.
import java.sql.SQLException;        // El error de la base de datos.
import java.util.ArrayList;
import ministore.modelo.Rol;
import ministore.util.ConexionBD;

/**
 * La bodega de los roles (fijos), guardada en MySQL.
 *
 * @author Mauricio
 */
public class RolRepositorio {

    // =====================================================================
    // CONSTRUCTOR
    // No recibe nada ni crea datos: los roles los pone el script 02.
    // Se deja para poder escribir new RolRepositorio().
    // =====================================================================

    public RolRepositorio() {
    }

    // =====================================================================
    // R: READ  →  listar  (SELECT)
    // Devuelve todos los roles, ordenados por id: 1 = Administrador,
    // 2 = Vendedor. Es lo que se mostrará en cmbRol, en ese orden.
    // No hay WHERE activo: la tabla roles no tiene esa columna, porque
    // los roles nunca se desactivan.
    // =====================================================================

    public ArrayList<Rol> listar() {
        ArrayList<Rol> roles = new ArrayList<>();
        String sql = "SELECT id, nombre FROM roles ORDER BY id";   // Sin ?: no recibe datos

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {          // executeQuery: SELECT (solo LEE)

            while (rs.next()) {                           // while: se esperan VARIAS filas
                // Cada FILA se convierte en un OBJETO Rol (columna → atributo).
                roles.add(new Rol(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los roles: " + e.getMessage(), e);
        }
        return roles;
    }
}