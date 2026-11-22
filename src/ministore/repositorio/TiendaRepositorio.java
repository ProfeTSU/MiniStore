/*
 * =========================================================================
 *  CLASE: TiendaRepositorio               PAQUETE: ministore.repositorio
 *  VERSIÓN: MySQL
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es la bodega de los DATOS DE LA TIENDA: nombre, NIT, dirección y
 *    teléfono, guardados en la tabla tienda de MySQL. Esos datos son el
 *    encabezado del comprobante de cada venta y se pueden cambiar desde
 *    VentanaConfiguracion (solo el administrador).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ConexionBD → para abrir la conexión con MySQL.
 *      - Tienda     → convierte la fila de la tabla en un objeto.
 *    La usan:
 *      - TiendaControlador → y, a través de él, VentanaConfiguracion.
 *      - VentaControlador  → para el encabezado del comprobante.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - ConexionBD (probada con PruebaConexion).
 *    - La tabla tienda, creada y llenada con los scripts 01 y 02.
 *
 *  ¿POR QUÉ SOLO DOS MÉTODOS, SI LAS OTRAS BODEGAS TIENEN SEIS?
 *    El MiniStore maneja UNA sola tienda: la tabla tiene UNA sola fila.
 *    No se crean ni se eliminan tiendas; solo se leen y se cambian sus datos.
 *    Por eso, del CRUD solo quedan la R y la U:
 *      obtener     →  SELECT ... LIMIT 1
 *      actualizar  →  UPDATE ... WHERE id = ?
 * =========================================================================
 */
package ministore.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ministore.modelo.Tienda;
import ministore.util.ConexionBD;

/**
 * La bodega de los datos de la tienda (una sola fila), guardada en MySQL.
 *
 * @author Mauricio
 */
public class TiendaRepositorio {

    // =====================================================================
    // CONSTRUCTOR
    // No recibe nada ni crea datos: los datos de la tienda los pone el
    // script 02. Se deja para poder escribir new TiendaRepositorio().
    // =====================================================================

    public TiendaRepositorio() {
    }

    // =====================================================================
    // R: READ  →  obtener  (SELECT)
    // Devuelve la tienda, o null si la tabla está vacía.
    // ORDER BY id LIMIT 1 = "la primera fila": aunque por error la tabla
    // tuviera más filas, siempre se lee la misma.
    // =====================================================================

    public Tienda obtener() {
        String sql = "SELECT id, nombre, nit, direccion, telefono FROM tienda ORDER BY id LIMIT 1";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);    // Sin ?: no recibe datos
             ResultSet rs = ps.executeQuery()) {                  // executeQuery: SELECT

            if (rs.next()) {                                     // if: se espera UNA fila o ninguna
                // Convierte la fila en un objeto Tienda (columna → atributo).
                Tienda t = new Tienda(rs.getString("nombre"), rs.getString("nit"),
                        rs.getString("direccion"), rs.getString("telefono"));
                t.setId(rs.getInt("id"));                        // El id lo necesita actualizar()
                return t;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer la tienda: " + e.getMessage(), e);
        }
        return null;                                             // La tabla está vacía
    }

    // =====================================================================
    // U: UPDATE  →  actualizar  (UPDATE)
    // Guarda en MySQL los datos que tenga el objeto tienda.
    // Devuelve true si se actualizó la fila, false si ese id no existe.
    // =====================================================================

    public boolean actualizar(Tienda tienda) {
        String sql = "UPDATE tienda SET nombre = ?, nit = ?, direccion = ?, telefono = ? " + "WHERE id = ?";  // El WHERE dice CUÁL fila cambiar

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tienda.getNombre());                 // Los ? se llenan en el MISMO
            ps.setString(2, tienda.getNit());                    // orden en que aparecen en el SQL
            ps.setString(3, tienda.getDireccion());
            ps.setString(4, tienda.getTelefono());
            ps.setInt(5, tienda.getId());                        // El ? número 5: el del WHERE
            return ps.executeUpdate() > 0;                       // executeUpdate devuelve cuántas filas cambió
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la tienda: " + e.getMessage(), e);
        }
    }
}