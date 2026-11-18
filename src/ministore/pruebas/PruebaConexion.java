package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import ministore.util.ConexionBD;

/**
 * Prueba de ConexionBD. Se ejecuta con Shift + F6.
 * Comprueba que Java se conecta a MySQL y que puede LEER los datos.
 * Si esta prueba falla, nada de lo que sigue va a funcionar.
 *
 * @author Mauricio
 */
public class PruebaConexion {

    public static void main(String[] args) {

        // try-with-resources: lo que se abre dentro de los paréntesis del try
        // (la conexión, la instrucción y el resultado) se CIERRA SOLO al
        // terminar, aunque ocurra un error. Así no quedan conexiones abiertas.
        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement instruccion = conexion.createStatement();
             ResultSet resultado = instruccion.executeQuery(
                     "SELECT COUNT(*) AS total FROM productos WHERE activo = TRUE")) {

            // 1. Si llegó aquí, la conexión se abrió.
            System.out.println("¡Conexión exitosa!");
            System.out.println("Base de datos: " + conexion.getCatalog());   // ministore

            // 2. Leer el resultado de la consulta.
            //    next() pasa a la primera fila; getInt("total") lee la columna "total".
            if (resultado.next()) {
                System.out.println("Productos activos en MySQL: " + resultado.getInt("total"));   // 16
            }

        } catch (SQLException e) {
            // Si algo falló, se muestra el mensaje de MySQL: dice exactamente qué pasó.
            System.out.println("No se pudo conectar a la base de datos.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}