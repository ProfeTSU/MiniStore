/*
 * =========================================================================
 *  CLASE: ReporteRepositorio              PAQUETE: ministore.repositorio
 *  VERSIÓN: MySQL
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Consulta en MySQL los datos de los REPORTES del administrador:
 *    las ventas de un periodo, el total vendido, la utilidad y los
 *    productos más vendidos. Cubre el RF7 (reportes semanal y mensual),
 *    el RF11 (utilidad) y el RF12 (estadísticas).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ConexionBD → para abrir la conexión con MySQL.
 *    La usan:
 *      - ReporteControlador → y, a través de él, VentanaReportes.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - Las tablas ventas, detalle_venta, productos, clientes y usuarios,
 *      con los datos del script 02 (las fechas se calculan desde HOY).
 *
 *  ¿POR QUÉ LAS CUENTAS LAS HACE MySQL Y NO UN CICLO EN JAVA?
 *    Para recorrer las ventas en Java habría que reconstruir cada Venta con
 *    su fecha, y Venta no tiene setFecha (está desactivado a propósito).
 *    MySQL ya sabe sumar, contar y agrupar: SUM, COUNT y GROUP BY. Son las
 *    mismas consultas 13 a 16 del script 03, ahora con un ? para los días.
 *
 *  ¿POR QUÉ DEVUELVE Object[] Y NO OBJETOS DEL MODELO?
 *    Un reporte no es una cosa de la tienda (como un Producto): es una
 *    FILA de resultados. Un Object[] es justo lo que recibe addRow() de la
 *    tabla, en el mismo orden de las columnas.
 *
 *  LOS MÉTODOS Y SU SQL (dias = 7 semanal, 30 mensual):
 *      ventasPorPeriodo      →  SELECT ... JOIN clientes ... JOIN usuarios ... WHERE fecha >= hace ? días
 *      totalVendido          →  SELECT SUM(total) ...
 *      utilidad              →  SELECT SUM(cantidad * (precio_unitario - costo_unitario)) ...
 *      productosMasVendidos  →  SELECT ... SUM(cantidad) ... GROUP BY ... LIMIT 5
 * =========================================================================
 */
package ministore.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;   // Para mostrar la fecha como "22/11/2026 09:45".
import java.util.ArrayList;
import ministore.util.ConexionBD;

/**
 * Las consultas de los reportes, en MySQL.
 *
 * @author Mauricio
 */
public class ReporteRepositorio {

    // "Desde hace ? días": DATE_SUB(NOW(), INTERVAL 7 DAY) = hace 7 días.
    // Se escribe una vez y la usan los cuatro métodos.
    private static final String DESDE = "v.fecha >= DATE_SUB(NOW(), INTERVAL ? DAY) AND v.confirmada = TRUE ";

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ReporteRepositorio() {
    }

    // =====================================================================
    // 1. LAS VENTAS DEL PERIODO  →  tblVentas            (consultas 13 y 14)
    // Cada fila: { número, fecha, cliente, vendedor, total }.
    // Los dos JOIN traen los NOMBRES del cliente y del vendedor (RF10).
    // ORDER BY fecha DESC: las más recientes primero.
    // =====================================================================

    public ArrayList<Object[]> ventasPorPeriodo(int dias) {
        ArrayList<Object[]> filas = new ArrayList<>();
        String sql = "SELECT v.id, v.fecha, c.nombre AS cliente, u.nombre_completo AS vendedor, v.total "
                   + "FROM ventas v "
                   + "JOIN clientes c ON c.id = v.cliente_id "
                   + "JOIN usuarios u ON u.id = v.usuario_id "
                   + "WHERE " + DESDE
                   + "ORDER BY v.fecha DESC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dias);                                      // 7 o 30
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {                                  // Una fila de la tabla por cada venta
                    filas.add(new Object[]{
                        rs.getInt("id"),
                        rs.getTimestamp("fecha").toLocalDateTime().format(FORMATO),   // DATETIME → texto
                        rs.getString("cliente"),
                        rs.getString("vendedor"),
                        rs.getDouble("total")
                    });
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar las ventas: " + e.getMessage(), e);
        }
        return filas;
    }

    // =====================================================================
    // 2. EL TOTAL VENDIDO  →  lblTotalVendido            (consultas 13 y 14)
    // SUM devuelve UNA sola fila. Si no hay ventas, SUM da NULL, y
    // getDouble convierte NULL en 0: el reporte muestra 0, no un error.
    // =====================================================================

    public double totalVendido(int dias) {
        String sql = "SELECT SUM(v.total) FROM ventas v WHERE " + DESDE;
        return consultarNumero(sql, dias, "el total vendido");
    }

    // =====================================================================
    // 3. LA UTILIDAD  →  lblUtilidad                     (consulta 15, RF11)
    // Por cada línea vendida: (precio - costo) x cantidad. Se usa el precio
    // y el costo GUARDADOS en detalle_venta: los del día de la venta.
    // =====================================================================

    public double utilidad(int dias) {
        String sql = "SELECT SUM(d.cantidad * (d.precio_unitario - d.costo_unitario)) "
                   + "FROM detalle_venta d "
                   + "JOIN ventas v ON v.id = d.venta_id "
                   + "WHERE " + DESDE;
        return consultarNumero(sql, dias, "la utilidad");
    }

    // =====================================================================
    // 4. LOS 5 PRODUCTOS MÁS VENDIDOS  →  tblMasVendidos (consulta 16, RF12)
    // GROUP BY junta todas las líneas de un mismo producto en UNA fila,
    // y SUM suma sus cantidades. Cada fila: { producto, unidades }.
    // =====================================================================

    public ArrayList<Object[]> productosMasVendidos(int dias) {
        ArrayList<Object[]> filas = new ArrayList<>();
        String sql = "SELECT p.nombre, SUM(d.cantidad) AS unidades "
                   + "FROM detalle_venta d "
                   + "JOIN ventas v ON v.id = d.venta_id "
                   + "JOIN productos p ON p.id = d.producto_id "
                   + "WHERE " + DESDE
                   + "GROUP BY p.id, p.nombre "                     // Una fila por producto
                   + "ORDER BY unidades DESC, p.nombre "            // Los que más se venden primero; si empatan, por nombre
                   + "LIMIT 5";                                     // Solo los 5 primeros

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dias);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new Object[]{rs.getString("nombre"), rs.getInt("unidades")});
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar los más vendidos: " + e.getMessage(), e);
        }
        return filas;
    }

    // =====================================================================
    // MÉTODO AYUDANTE (private)
    // Ejecuta un SELECT que devuelve UN solo número (SUM) para los días
    // dados. Lo usan totalVendido y utilidad: se escribe una sola vez.
    // =====================================================================

    private double consultarNumero(String sql, int dias, String que) {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dias);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();                                          // SUM siempre devuelve UNA fila
                return rs.getDouble(1);                             // La columna 1 (NULL → 0)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar " + que + ": " + e.getMessage(), e);
        }
    }
}