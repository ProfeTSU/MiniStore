/*
 * =========================================================================
 *  CLASE: CategoriaRepositorio            PAQUETE: ministore.repositorio
 *  VERSIÓN: MySQL (sesión 12)
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es la "bodega" de las categorías (Granos, Lácteos...). Ahora la bodega
 *    no es un ArrayList: es la tabla categorias de MySQL. Los datos ya no
 *    se pierden al cerrar el programa. Cubre el RF6 (filtrar por categoría).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ConexionBD   → para abrir la conexión con MySQL.
 *      - Categoria    → convierte cada fila de la tabla en un objeto.
 *    La usan:
 *      - ProductoRepositorio y ProductoControlador (para cmbCategoria).
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - La clase ConexionBD (paquete util), probada con PruebaConexion.
 *    - La tabla categorias, creada con los scripts 01 y 02.
 *
 *  ¿QUÉ CAMBIÓ RESPECTO A LA VERSIÓN EN MEMORIA?
 *    Por FUERA, nada: los mismos métodos, con los mismos parámetros y lo
 *    mismo que devuelven. Por eso ProductoControlador y VentanaProductos
 *    no cambian ni una línea. Por DENTRO, cada método ejecuta SQL:
 *      guardar          →  INSERT
 *      listar           →  SELECT ... WHERE activo = TRUE
 *      buscarPorNombre  →  SELECT ... WHERE nombre = ?
 *    Ya no hay ArrayList categorias, ni siguienteId (lo hace el
 *    AUTO_INCREMENT), ni cargarDatosDePrueba (lo hace el script 02).
 * =========================================================================
 */
package ministore.repositorio;

import java.sql.Connection;          // La conexión abierta con MySQL.
import java.sql.PreparedStatement;   // Una instrucción SQL con signos ? para los valores.
import java.sql.ResultSet;           // Las filas que devuelve un SELECT.
import java.sql.SQLException;        // El error de la base de datos.
import java.sql.Statement;           // Se usa por la constante RETURN_GENERATED_KEYS.
import java.util.ArrayList;
import ministore.modelo.Categoria;
import ministore.util.ConexionBD;

/**
 * La bodega de categorías, guardada en MySQL.
 *
 * @author Mauricio
 */
public class CategoriaRepositorio {

    // =====================================================================
    // CONSTRUCTOR
    // Ya no crea una lista ni carga datos de prueba: los datos viven en
    // MySQL. Se conserva (vacío) para que el resto del programa, que hace
    // new CategoriaRepositorio(), siga funcionando igual.
    // =====================================================================
    public CategoriaRepositorio() {
    }

    // =====================================================================
    // C: CREATE  →  guardar  (INSERT)
    // =====================================================================

    // Guarda una categoría nueva. MySQL le asigna el id (AUTO_INCREMENT),
    // y aquí se le pone ese id al objeto, igual que antes hacía siguienteId.
    public void guardar(Categoria categoria) {
        // El ? es un "espacio" que se llena después con setString.
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        // try-with-resources: la conexión y la instrucción se cierran solas.
        // RETURN_GENERATED_KEYS: "después del INSERT, devuélveme el id que asignaste".
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNombre());   // Llena el ? número 1 con el nombre.
            ps.executeUpdate();                       // Ejecuta el INSERT (UPDATE: porque CAMBIA datos).

            try (ResultSet llaves = ps.getGeneratedKeys()) {   // El id que asignó MySQL.
                if (llaves.next()) {
                    categoria.setId(llaves.getInt(1));         // Se lo pone al objeto.
                }
            }
        } catch (SQLException e) {
            // Si algo falla (por ejemplo, una categoría repetida), se avisa con un error
            // que explica qué pasó. Ver la explicación al final de la clase.
            throw new RuntimeException("Error al guardar la categoría: " + e.getMessage(), e);
        }
    }

    // =====================================================================
    // R: READ  →  listar  (SELECT con WHERE activo = TRUE)
    // =====================================================================

    // Devuelve SOLO las categorías activas, ordenadas por id
    // (las que se muestran en cmbCategoria).
    public ArrayList<Categoria> listar() {
        ArrayList<Categoria> activas = new ArrayList<>();
        String sql = "SELECT id, nombre FROM categorias WHERE activo = TRUE ORDER BY id";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {          // executeQuery: para SELECT (solo LEE).

            // El ResultSet es como una tabla con un "cursor".
            // next() avanza a la siguiente fila y devuelve false cuando ya no hay más.
            while (rs.next()) {
                // Cada FILA se convierte en un OBJETO Categoria.
                activas.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las categorías: " + e.getMessage(), e);
        }
        return activas;
    }

    // =====================================================================
    // R: READ  →  buscarPorNombre  (SELECT con WHERE nombre = ?)
    // =====================================================================

    // Devuelve la categoría con ese nombre, o null si no existe.
    public Categoria buscarPorNombre(String nombre) {
        String sql = "SELECT id, nombre FROM categorias WHERE nombre = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);                      // Llena el ? con el nombre buscado.

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {                          // ¿Hay al menos una fila?
                    return new Categoria(rs.getInt("id"), rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la categoría: " + e.getMessage(), e);
        }
        return null;                                      // No había ninguna fila: no existe.
    }

    // =====================================================================
    // ¿POR QUÉ throw new RuntimeException(...) EN EL catch?
    //   Los métodos de JDBC lanzan SQLException. Si se dejara pasar con
    //   "throws SQLException", habría que cambiar la firma de los métodos,
    //   y el controlador y la ventana tendrían que cambiar también.
    //   RuntimeException no obliga a cambiar nada: si la base de datos falla,
    //   el programa se detiene con un mensaje claro de qué pasó, en lugar de
    //   seguir como si nada con una lista vacía.
    //
    // ¿POR QUÉ ? Y NO PEGAR EL VALOR EN EL TEXTO DEL SQL?
    //   "... WHERE nombre = '" + nombre + "'" falla si el nombre tiene una
    //   comilla (por ejemplo O'Brien) y permite la INYECCIÓN SQL: alguien
    //   podría escribir un texto que cambie la instrucción. Con ? y setString,
    //   el valor siempre se trata como un dato, nunca como parte del SQL.
    // =====================================================================
}