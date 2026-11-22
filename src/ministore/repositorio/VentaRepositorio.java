/*
 * =========================================================================
 *  CLASE: VentaRepositorio                PAQUETE: ministore.repositorio
 *  VERSIÓN: MySQL
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Guarda en MySQL una venta confirmada: la venta, cada línea del carrito
 *    (detalle_venta) y el descuento del stock de cada producto. Cubre el
 *    RF8 (registro de ventas) y el RF5 (el inventario baja al vender).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ConexionBD          → para abrir la conexión con MySQL.
 *      - Venta y DetalleVenta → los datos que se guardan.
 *    La usan:
 *      - VentaControlador → y, a través de él, VentanaVenta.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - Las tablas ventas, detalle_venta y productos (scripts 01 y 02).
 *    - Los modelos Venta y DetalleVenta.
 *
 *  LO NUEVO: LA TRANSACCIÓN ("TODO O NADA")
 *    Guardar una venta son VARIAS instrucciones SQL:
 *      1 INSERT en ventas
 *      + por cada línea del carrito: 1 UPDATE del stock y 1 INSERT en detalle_venta
 *    Si una falla a la mitad (por ejemplo, ya no hay stock de un producto),
 *    no puede quedar una venta a medias: la venta sin detalles, o el stock
 *    descontado de unos productos y de otros no.
 *    Una TRANSACCIÓN agrupa todas las instrucciones:
 *      setAutoCommit(false) → "no guarde nada todavía"
 *      commit()             → "todo salió bien: guarde todo"
 *      rollback()           → "algo falló: deshaga todo"
 *
 *  LOS MÉTODOS Y SU SQL:
 *      guardar       →  INSERT ventas + (UPDATE productos + INSERT detalle_venta) por línea
 *      contarVentas  →  SELECT COUNT(*) FROM ventas
 * =========================================================================
 */
package ministore.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import ministore.modelo.DetalleVenta;
import ministore.modelo.Venta;
import ministore.util.ConexionBD;

/**
 * La bodega de las ventas, guardada en MySQL.
 *
 * @author Mauricio
 */
public class VentaRepositorio {

    // =====================================================================
    // EL SQL DE LA VENTA
    // Se escriben una vez como constantes: guardar() los usa todos.
    // =====================================================================

    // 1. La venta: confirmada = TRUE porque solo se guardan ventas confirmadas.
    private static final String SQL_VENTA =
            "INSERT INTO ventas (fecha, total, confirmada, cliente_id, usuario_id) "
          + "VALUES (?, ?, TRUE, ?, ?)";

    // 2. El stock: AND stock >= ? es la protección. Si otro vendedor vendió
    //    el producto un momento antes y ya no alcanza, el UPDATE no cambia
    //    ninguna fila (devuelve 0) y la venta completa se deshace.
    private static final String SQL_STOCK =
            "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";

    // 3. Cada línea del carrito. El precio y el costo se COPIAN: si mañana
    //    el producto cambia de precio, esta venta conserva el de hoy (RF11).
    private static final String SQL_DETALLE =
            "INSERT INTO detalle_venta (cantidad, precio_unitario, costo_unitario, venta_id, producto_id) "
          + "VALUES (?, ?, ?, ?, ?)";

    public VentaRepositorio() {
    }

    // =====================================================================
    // C: CREATE  →  guardar  (UNA TRANSACCIÓN)
    // Devuelve true si se guardó todo, false si algún producto ya no tenía
    // stock (en ese caso se deshizo todo con rollback y MySQL quedó igual).
    // Si se guardó, la venta queda con el id que asignó MySQL: es el
    // número del comprobante.
    // =====================================================================

    public boolean guardar(Venta venta) {
        try (Connection con = ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);            // INICIO de la transacción: nada se guarda todavía.
            try {
                // ----- Paso 1: la venta (INSERT en ventas) -----
                int idVenta;
                try (PreparedStatement ps = con.prepareStatement(SQL_VENTA, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));   // LocalDateTime → DATETIME
                    ps.setDouble(2, venta.calcularTotal());
                    ps.setInt(3, venta.getCliente().getId());                  // Los OBJETOS se guardan
                    ps.setInt(4, venta.getVendedor().getId());                 // como su id (FK)
                    ps.executeUpdate();
                    try (ResultSet llaves = ps.getGeneratedKeys()) {           // El id que asignó MySQL
                        llaves.next();
                        idVenta = llaves.getInt(1);
                    }
                }

                // ----- Paso 2: por cada línea del carrito, descontar stock y guardar el detalle -----
                try (PreparedStatement psStock = con.prepareStatement(SQL_STOCK);
                     PreparedStatement psDetalle = con.prepareStatement(SQL_DETALLE, Statement.RETURN_GENERATED_KEYS)) {

                    for (DetalleVenta d : venta.getDetalles()) {               // "Para cada línea d del carrito"
                        // 2a. Descontar el stock (solo si alcanza).
                        psStock.setInt(1, d.getCantidad());
                        psStock.setInt(2, d.getProducto().getId());
                        psStock.setInt(3, d.getCantidad());
                        if (psStock.executeUpdate() == 0) {                    // 0 filas = ya no alcanzaba
                            con.rollback();                                    // DESHACE todo lo anterior
                            return false;
                        }

                        // 2b. Guardar la línea en detalle_venta.
                        psDetalle.setInt(1, d.getCantidad());
                        psDetalle.setDouble(2, d.getPrecioUnitario());
                        psDetalle.setDouble(3, d.getCostoUnitario());
                        psDetalle.setInt(4, idVenta);                          // La línea pertenece a ESTA venta
                        psDetalle.setInt(5, d.getProducto().getId());
                        psDetalle.executeUpdate();
                        try (ResultSet llaves = psDetalle.getGeneratedKeys()) {
                            if (llaves.next()) {
                                d.setId(llaves.getInt(1));
                            }
                        }
                    }
                }

                // ----- Paso 3: todo salió bien -----
                con.commit();                    // FIN de la transacción: ahora sí se guarda TODO.
                venta.setId(idVenta);            // La venta queda con su número de comprobante.
                return true;

            } catch (SQLException e) {
                con.rollback();                  // Cualquier error de SQL: se deshace TODO.
                throw e;                         // Y se avisa (lo atrapa el catch de afuera).
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la venta: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // R: READ  →  contarVentas  (SELECT COUNT)
    // Cuántas ventas hay en MySQL. Lo usa la prueba para comprobar que la
    // transacción no dejó una venta a medias.
    // =====================================================================

    public int contarVentas() {
        String sql = "SELECT COUNT(*) FROM ventas";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();                                   // COUNT siempre devuelve UNA fila
            return rs.getInt(1);                         // La columna 1: el número
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar las ventas: " + e.getMessage(), e);
        }
    }
}