/*
 * =========================================================================
 *  CLASE: ClienteRepositorio              PAQUETE: ministore.repositorio
 *  VERSIÓN: MySQL
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es la bodega de los clientes: guarda, consulta, actualiza y
 *    desactiva clientes en la tabla clientes de MySQL. Cubre el RF4
 *    (gestión de clientes) y la búsqueda por documento que usará la venta.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ConexionBD → para abrir la conexión con MySQL.
 *      - Cliente    → convierte cada fila de la tabla en un objeto.
 *    La usan:
 *      - ClienteControlador → y, a través de él, VentanaClientes.
 *      - Más adelante, VentaControlador (para buscar al cliente de la venta).
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - ConexionBD (probada con PruebaConexion).
 *    - La tabla clientes, creada y llenada con los scripts 01 y 02.
 *
 *  LOS MÉTODOS Y SU SQL (el mismo patrón de ProductoRepositorio):
 *      guardar             →  INSERT
 *      listar              →  SELECT ... WHERE activo = TRUE
 *      buscarPorId         →  SELECT ... WHERE id = ?
 *      buscarPorDocumento  →  SELECT ... WHERE documento = ?
 *      actualizar          →  UPDATE ... WHERE id = ?
 *      desactivar          →  UPDATE ... SET activo = FALSE  (eliminación lógica)
 * =========================================================================
 */
package ministore.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import ministore.modelo.Cliente;
import ministore.util.ConexionBD;

/**
 * La bodega de clientes, guardada en MySQL.
 *
 * @author Mauricio
 */
public class ClienteRepositorio {

    // Todas las búsquedas leen las mismas columnas: se escriben UNA vez.
    // Cada método le agrega su propio WHERE.
    private static final String SELECT_BASE =
            "SELECT id, documento, nombre, telefono, correo, activo FROM clientes ";

    // =====================================================================
    // CONSTRUCTOR
    // Ya no crea una lista ni datos de prueba: los datos los pone el script 02.
    // Se conserva para que new ClienteRepositorio() siga funcionando igual.
    // =====================================================================
    public ClienteRepositorio() {
    }

    // =====================================================================
    // C: CREATE  →  guardar  (INSERT)
    // MySQL asigna el id (AUTO_INCREMENT) y aquí se le pone al objeto.
    // =====================================================================
    public void guardar(Cliente cliente) {
        String sql = "INSERT INTO clientes (documento, nombre, telefono, correo, activo) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            llenarDatos(ps, cliente);                        // Los 5 signos ?
            ps.executeUpdate();

            try (ResultSet llaves = ps.getGeneratedKeys()) { // El id que asignó MySQL
                if (llaves.next()) {
                    cliente.setId(llaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el cliente: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // R: READ  →  listar, buscarPorId, buscarPorDocumento
    // =====================================================================

    // Solo los clientes activos, ordenados por id.
    public ArrayList<Cliente> listar() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE activo = TRUE ORDER BY id";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {                              // Una vuelta por cada fila
                lista.add(crearCliente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    // Busca por id. Devuelve el cliente o null si no existe.
    public Cliente buscarPorId(int id) {
        String sql = SELECT_BASE + "WHERE id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearCliente(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el cliente: " + e.getMessage(), e);
        }
        return null;
    }

    // Busca por documento ("1234567"). Devuelve el cliente o null.
    // Lo usan el controlador (para no repetir documentos) y la venta.
    // Devuelve también los desactivados: el controlador decide qué hacer con ellos.
    public Cliente buscarPorDocumento(String documento) {
        String sql = SELECT_BASE + "WHERE documento = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearCliente(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el cliente: " + e.getMessage(), e);
        }
        return null;
    }

    // =====================================================================
    // U: UPDATE  →  actualizar  (UPDATE)
    // Devuelve true si cambió una fila, false si ese id no existe.
    // =====================================================================
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET documento = ?, nombre = ?, telefono = ?, correo = ?, "
                   + "activo = ? WHERE id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            llenarDatos(ps, cliente);                        // Los ? del 1 al 5
            ps.setInt(6, cliente.getId());                   // El ? número 6: el del WHERE
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el cliente: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // D: DELETE  →  desactivar  (ELIMINACIÓN LÓGICA con UPDATE)
    // Un cliente con ventas no se puede borrar: la clave foránea de
    // ventas lo impide, y se perdería el historial.
    // =====================================================================
    public boolean desactivar(int id) {
        String sql = "UPDATE clientes SET activo = FALSE WHERE id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar el cliente: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // MÉTODOS AYUDANTES (private)
    // =====================================================================

    // Convierte UNA FILA en UN OBJETO Cliente.
    private Cliente crearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente(rs.getString("documento"), rs.getString("nombre"),
                rs.getString("telefono"), rs.getString("correo"));
        c.setId(rs.getInt("id"));
        c.setActivo(rs.getBoolean("activo"));                // TINYINT(1): 1 → true, 0 → false
        return c;
    }

    // Llena los 5 signos ? de guardar y actualizar, en el orden de las columnas.
    private void llenarDatos(PreparedStatement ps, Cliente c) throws SQLException {
        ps.setString(1, c.getDocumento());
        ps.setString(2, c.getNombre());
        ps.setString(3, c.getTelefono());
        ps.setString(4, c.getCorreo());
        ps.setBoolean(5, c.isActivo());
    }
}