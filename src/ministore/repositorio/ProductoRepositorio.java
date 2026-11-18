/*
 * =========================================================================
 *  CLASE: ProductoRepositorio             PAQUETE: ministore.repositorio
 *  VERSIÓN: MySQL (sesión 12)
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es LA BODEGA: guarda TODOS los productos de la tienda y permite el
 *    CRUD. Ahora la bodega es la tabla productos de MySQL: los datos ya no
 *    se pierden al cerrar el programa. Cubre el RF3 (gestión de productos),
 *    el RF5 (inventario) y el RF6 (búsqueda).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ConexionBD   → para abrir la conexión con MySQL.
 *      - Producto y Categoria → convierte cada fila en un objeto.
 *    La usan:
 *      - ProductoControlador → y, a través de él, VentanaProductos.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - ConexionBD (probada) y CategoriaRepositorio en su versión MySQL.
 *    - Las tablas productos y categorias, con los scripts 01 y 02.
 *
 *  ¿QUÉ CAMBIÓ RESPECTO A LA VERSIÓN EN MEMORIA?
 *    Por FUERA, nada: los mismos constructores y los mismos métodos.
 *    Por DENTRO, cada método ejecuta SQL:
 *      guardar          →  INSERT
 *      listar           →  SELECT ... JOIN categorias ... WHERE activo = TRUE
 *      buscarPorId      →  SELECT ... WHERE p.id = ?
 *      buscarPorCodigo  →  SELECT ... WHERE p.codigo = ?
 *      buscarPorNombre  →  SELECT ... WHERE p.nombre LIKE ?
 *      actualizar       →  UPDATE
 *      desactivar       →  UPDATE ... SET activo = FALSE  (eliminación lógica)
 * =========================================================================
 */
package ministore.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import ministore.modelo.Categoria;
import ministore.modelo.Producto;
import ministore.util.ConexionBD;

/**
 * La bodega de productos, guardada en MySQL.
 *
 * @author Mauricio
 */
public class ProductoRepositorio {

    // =====================================================================
    // EL SELECT BASE
    // Todas las búsquedas leen las mismas columnas. Para no repetirlas en
    // cada método, se escriben UNA vez en esta constante, y cada método
    // le agrega su propio WHERE.
    // El JOIN trae, en la misma fila, el nombre de la categoría del producto.
    // c.nombre se renombra (AS categoria_nombre) porque p.nombre ya se llama "nombre".
    // =====================================================================
    private static final String SELECT_BASE =
            "SELECT p.id, p.codigo, p.nombre, p.descripcion, p.precio_compra, p.precio_venta, "
          + "p.stock, p.stock_minimo, p.ruta_imagen, p.activo, "
          + "c.id AS categoria_id, c.nombre AS categoria_nombre "
          + "FROM productos p "
          + "JOIN categorias c ON c.id = p.categoria_id ";

    // =====================================================================
    // CONSTRUCTORES (SOBRECARGA)
    // Se conservan los dos de la versión en memoria, para que el controlador
    // y el main de VentanaProductos sigan funcionando sin cambios.
    // Ya no cargan datos de prueba: los datos los pone el script 02.
    // =====================================================================

    // Constructor 1: sin parámetros.
    public ProductoRepositorio() {
    }

    // Constructor 2: recibe el repositorio de categorías.
    // En MySQL ya no hace falta, porque la categoría de cada producto llega
    // con el JOIN. Se conserva solo para no cambiar el resto del programa.
    public ProductoRepositorio(CategoriaRepositorio categorias) {
    }

    // =====================================================================
    // C: CREATE  →  guardar  (INSERT)
    // =====================================================================

    public void guardar(Producto producto) {
        String sql = "INSERT INTO productos (codigo, nombre, descripcion, precio_compra, precio_venta, "
                   + "stock, stock_minimo, ruta_imagen, activo, categoria_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";     // Un ? por cada columna

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            llenarDatos(ps, producto);       // Llena los 10 signos ? (ver el método ayudante)
            ps.executeUpdate();

            try (ResultSet llaves = ps.getGeneratedKeys()) {   // El id que asignó MySQL
                if (llaves.next()) {
                    producto.setId(llaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el producto: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // R: READ  →  listar, buscarPorId, buscarPorCodigo, buscarPorNombre
    // =====================================================================

    // Solo los productos activos (los "eliminados" no se muestran).
    public ArrayList<Producto> listar() {
        return consultarLista(SELECT_BASE + "WHERE p.activo = TRUE ORDER BY p.id", null);
    }

    // Busca por id. Devuelve el producto o null si no existe.
    public Producto buscarPorId(int id) {
        String sql = SELECT_BASE + "WHERE p.id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);                           // setInt: el ? es un número
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearProducto(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el producto: " + e.getMessage(), e);
        }
        return null;
    }

    // Busca por código ("P001"). La usa el controlador para no repetir códigos.
    // MySQL compara los textos SIN importar mayúsculas: "p001" encuentra "P001",
    // igual que hacía equalsIgnoreCase en la versión en memoria.
    public Producto buscarPorCodigo(String codigo) {
        String sql = SELECT_BASE + "WHERE p.codigo = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearProducto(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el producto: " + e.getMessage(), e);
        }
        return null;
    }

    // RF6: los productos activos cuyo nombre CONTIENE el texto.
    // LIKE '%texto%' = "que contenga el texto en cualquier parte".
    // Los % se agregan al VALOR, no al SQL, para seguir usando el ?.
    public ArrayList<Producto> buscarPorNombre(String texto) {
        return consultarLista(SELECT_BASE + "WHERE p.activo = TRUE AND p.nombre LIKE ? ORDER BY p.nombre",
                              "%" + texto + "%");
    }

    // =====================================================================
    // U: UPDATE  →  actualizar  (UPDATE)
    // Devuelve true si se actualizó una fila, false si ese id no existe.
    // =====================================================================

    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET codigo = ?, nombre = ?, descripcion = ?, precio_compra = ?, "
                   + "precio_venta = ?, stock = ?, stock_minimo = ?, ruta_imagen = ?, activo = ?, "
                   + "categoria_id = ? WHERE id = ?";       // El WHERE dice CUÁL producto cambiar

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            llenarDatos(ps, producto);                   // Los ? del 1 al 10
            ps.setInt(11, producto.getId());             // El ? número 11: el del WHERE
            return ps.executeUpdate() > 0;               // executeUpdate devuelve cuántas filas cambió
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // D: DELETE  →  desactivar  (ELIMINACIÓN LÓGICA con UPDATE)
    // No se usa DELETE: el producto puede aparecer en ventas anteriores
    // (y la clave foránea de detalle_venta ni siquiera lo permitiría).
    // =====================================================================

    public boolean desactivar(int id) {
        String sql = "UPDATE productos SET activo = FALSE WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;               // 0 filas = no existía ese id
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar el producto: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // MÉTODOS AYUDANTES (private): evitan repetir el mismo código
    // =====================================================================

    // Convierte UNA FILA del ResultSet en UN OBJETO Producto.
    // Es el "traductor" entre la tabla y la clase:
    //   columna precio_compra  →  atributo precioCompra, etc.
    private Producto crearProducto(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria(rs.getInt("categoria_id"), rs.getString("categoria_nombre"));

        Producto p = new Producto(rs.getString("codigo"), rs.getString("nombre"), categoria,
                rs.getDouble("precio_compra"), rs.getDouble("precio_venta"), rs.getInt("stock"));
        p.setId(rs.getInt("id"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        p.setRutaImagen(rs.getString("ruta_imagen"));
        p.setActivo(rs.getBoolean("activo"));            // TINYINT(1): 1 → true, 0 → false
        return p;
    }

    // Llena los 10 signos ? de guardar y actualizar, en el MISMO orden de las columnas.
    private void llenarDatos(PreparedStatement ps, Producto p) throws SQLException {
        ps.setString(1, p.getCodigo());
        ps.setString(2, p.getNombre());
        ps.setString(3, p.getDescripcion());
        ps.setDouble(4, p.getPrecioCompra());
        ps.setDouble(5, p.getPrecioVenta());
        ps.setInt(6, p.getStock());
        ps.setInt(7, p.getStockMinimo());
        ps.setString(8, p.getRutaImagen());
        ps.setBoolean(9, p.isActivo());
        ps.setInt(10, p.getCategoria().getId());         // El OBJETO categoría se guarda como su id (FK)
    }

    // Ejecuta un SELECT que devuelve VARIAS filas y las convierte en una lista.
    // valor: el texto para el único ? de la consulta, o null si no tiene ?.
    private ArrayList<Producto> consultarLista(String sql, String valor) {
        ArrayList<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (valor != null) {
                ps.setString(1, valor);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {                      // Una vuelta por cada fila
                    lista.add(crearProducto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar los productos: " + e.getMessage(), e);
        }
        return lista;
    }
}
